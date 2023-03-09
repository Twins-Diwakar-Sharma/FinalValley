package universe;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioMaster;
import audio.Source;
import entities.Camera;
import entities.Entity;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import model.RawModel;
import model.TexturedModel;
import network.UDPServer;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.TextureData;

public class GameLoop {
   static boolean inMenu = true;
	
	public static void main(String[] args) throws Exception{
        
		
		MenuKeys menuKeys = new MenuKeys();
		DisplayManager.makeDisplay();
		Loader loader = new Loader();
        // gui
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture play0 = new GuiTexture(loader.loadTexture("Play0"), new Vector2f(0f,0.3f),new Vector2f(0.25f,0.25f));
		GuiTexture play1 = new GuiTexture(loader.loadTexture("Play1"), new Vector2f(0f,0.3f),new Vector2f(0.25f,0.25f));
		GuiTexture quit0 = new GuiTexture(loader.loadTexture("Quit0"), new Vector2f(0f,-0.3f),new Vector2f(0.25f,0.25f));
		GuiTexture quit1 = new GuiTexture(loader.loadTexture("Quit1"), new Vector2f(0f,-0.3f),new Vector2f(0.25f,0.25f));
		GuiTexture bgmenu = new GuiTexture(loader.loadTexture("blackmenu"), new Vector2f(0.5f,-0.4f),new Vector2f(1.5f,1.5f));
	          
		
		guis.add(bgmenu); guis.add(play1); guis.add(quit0); ;
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        
        
        List<GuiTexture> dialog = new ArrayList<GuiTexture>();
       // GuiTexture dialogTex = new GuiTexture(loader.loadTexture("Dialog.png"), new Vector2f(0f,-0.4f),new Vector2f(0.25f,0.25f));
        //dialog.add(dialogTex);
		//Texture Pack
	    TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("snow"));
	    TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("volcano"));
	    TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("snow"));
	    TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("volcanoo"));
	    
	    TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
	    TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blipmap2"));
	    TerrainTexture blendMap2 = new TerrainTexture(loader.loadTexture("blipmap2"));
	    
	
        Model alienAlpine = new Model(loader, "AlienAlpine", "ap", 10, 0);
        Model multiStem = new Model(loader, "multistem", "multistemtex", 10, 0);
        Model crystalTree = new Model(loader, "crystaltree", "crystal", 10, 2);

	    Terrain terrain = new Terrain(-1,-1, loader,texturePack, blendMap2, "heightmap");
        Terrain terrain2 = new Terrain(0,-1, loader,texturePack, blendMap2, "heightmap2");
        
        ObjectMap objectMap = new ObjectMap(alienAlpine, multiStem, crystalTree);
        objectMap.makeObjects(terrain, "objectmap");
        objectMap.makeObjects(terrain2, "objectmap2");
       
	    
	    //.........................The Player.....................................
	    ModelData robot = OBJFileLoader.loadOBJ("dummy0");
	    RawModel alien = loader.loadVAO(robot.getVertices(), robot.getTextureCoords(), robot.getNormals(), robot.getIndices());
	    
	    TexturedModel theAlien = new TexturedModel(alien, new TextureData(loader.loadTexture("meale")));
        Player player = new Player(theAlien, new Vector3f(-200, 0, -80), 0, 45, 0, 0.5f);
        PlayerMesh PMesh = new PlayerMesh(loader, "meale");
        
	    //.... ..............................................................................

        ModelData robot2 = OBJFileLoader.loadOBJ("dummy0");
	    RawModel alien2 = loader.loadVAO(robot2.getVertices(), robot2.getTextureCoords(), robot2.getNormals(), robot2.getIndices());
	    
	    TexturedModel theAlien2 = new TexturedModel(alien2, new TextureData(loader.loadTexture("meale2")));
        Player player2 = new Player(theAlien2, new Vector3f(-200, 0, -80), 0, 45, 0, 0.5f);
        PlayerMesh PMesh2 = new PlayerMesh(loader, "meale2");
        
	    //.... ..............................................................................

        UDPServer server = new UDPServer();
        server.start();
        
	    MasterRenderer render = new MasterRenderer(loader);
	    
	    Camera camera = new Camera(player2);	    
	    
	    AudioMaster.init();
	    AudioMaster.setListernerData(player2.getTranslation().x, player2.getTranslation().y, player2.getTranslation().z);
	    
	    int bg = AudioMaster.loadSound("audio/bgbg.wav");
	    int menu = AudioMaster.loadSound("audio/menu.wav");
	    
	    Source source = new Source();
	    source.play(menu);	  
	    
	    while(inMenu){
	    	Mouse.setGrabbed(true);
	    	
	    	menuKeys.checkMenuInputs();
	    	if(menuKeys.state==0){
	    		guis.clear();
	    		guis.add(bgmenu);
	    		guis.add(play1);
	    		guis.add(quit0);
	
	    	}
	    	else if(menuKeys.state==1){
	    		guis.clear();
	    		guis.add(bgmenu);
	    		guis.add(play0);
	    		guis.add(quit1);
	    		
	    	}
	    	
	    	guiRenderer.render(guis);
	    	
	    	DisplayManager.updateDisplay();
	    }
	     
	    source.stop();

	    
	    source.play(bg);
	    
		while(!Display.isCloseRequested()){
			
			PMesh.animate(player);
			PMesh2.animate(player2);
			if(player2.getTranslation().x >= 0){
			 player2.move(terrain2);
			 camera.move(terrain2);
			}			
			if(player2.getTranslation().x < 0){
				 player2.move(terrain);
				 camera.move(terrain);
		    }
			server.setOutput(player2.getTranslation(), player2.getRotX(), player2.getRotY(), player2.getRotZ(), player2.getSpeed());
			if(player.getTranslation().x >= 0){
				 player.move(terrain2,  server.inPosition, server.inRotX, server.inRotY, server.inRotZ, server.inSpeed);
			}
			if(player.getTranslation().x < 0){
				player.move(terrain,  server.inPosition, server.inRotX, server.inRotY, server.inRotZ, server.inSpeed);
			}
		    render.processTerrain(terrain);
		    render.processTerrain(terrain2);
			for(Entity entity : objectMap.getTrees()){
		        render.processEntity(entity);
			}
			render.processEntity(player);
			render.processEntity(player2);

			render.render(objectMap.getLights(), camera);
			//guiRenderer.render(guis);
			DisplayManager.updateDisplay();
	
		}
		server.shutdown();
		source.delete();
		AudioMaster.cleanUp();
		guiRenderer.cleanUp();
		render.cleanUp();
		loader.cleanUp();
		
		DisplayManager.deleteDisplay();
	}
	
	
}
