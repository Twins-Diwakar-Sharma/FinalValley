package universe;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Light;
import model.TexturedModel;
import terrain.Terrain;

public class ObjectMap {
	private static float multiplier = 3.125f;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();
    private Model[] trees;
    private float gridX;
    private float gridZ;
    
	public ObjectMap(Model ... trees){		
		this.trees = trees;
		lights.add(new Light(new Vector3f(0,1000,0), new Vector3f(0.5f,0.5f,0.5f)));
	}
	
	public void makeObjects(Terrain terrain, String objectMap){
		gridX = (terrain.getX()/800) * multiplier;
		gridZ = (terrain.getZ()/800) * multiplier;
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("resource/"+objectMap+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int height = image.getHeight();
		
		for(int x =0; x<height; x++){
			for(int z=0; z<height; z++){
			   float y = terrain.getHeightOfTerrian(x*gridX,z*gridZ);
	
			   getColor(x,z,y,image);		   
			   
			}
		}
		
		
	
	}
	
	private void getColor(int x, int z, float y, BufferedImage image){
		
		   int pixel = image.getRGB(x, z);

		   int red = (pixel>>16) & 0xff;
		   int green = (pixel>>8) & 0xff;
		   int blue = (pixel) & 0xff;
		   
		   if(red==255 && green==0 && blue==0){
			   entities.add(new Entity(trees[0].getTexModel(), new Vector3f((float)x*gridX,y,(float)z*gridZ), 0,0,0,1));
		   }
		   else if(red==0 && green==255 && blue==0){
			   entities.add(new Entity(trees[1].getTexModel(), new Vector3f((float)x*gridX,y,(float)z*gridZ), 0,0,0,2));
		   }
		   else if(red==0 && green==0 && blue==255){
	    	  entities.add(new Entity(trees[2].getTexModel(), new Vector3f((float)x*gridX,y,(float)z*gridZ), 0,0,0,4));
		   }
		   else if(red==255 && green==255 && blue==0){
			   entities.add(new Entity(trees[0].getTexModel(), new Vector3f((float)x*gridX,y,(float)z*gridZ), 0,0,0,2));
			   lights.add(new Light(new Vector3f(x*gridX, y + 10, z*gridZ), new Vector3f(3, 3, 0), new Vector3f(1, 0.01f, 0.002f)));
		   }
		   else if(red==0 && green==255 && blue==255){
			   entities.add(new Entity(trees[0].getTexModel(), new Vector3f((float)x*gridX,y,(float)z*gridZ), 0,0,0,2));
			   lights.add(new Light(new Vector3f(x*gridX, y + 10, z*gridZ), new Vector3f(0, 3, 3), new Vector3f(1, 0.01f, 0.002f)));
		   }
		  
	}
	
	public List<Light> getLights(){
		return lights;
	}
	
	public List<Entity> getTrees(){
		return entities;
	}
	
}
