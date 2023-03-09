package universe;

import entities.Player;
import model.RawModel;
import model.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import textures.TextureData;

public class PlayerMesh {

   private TexturedModel[] tex = new TexturedModel[9];
   private int i;
   private int deltaTime;
   
   public PlayerMesh(Loader loader, String texture){
	   ModelData robot[] = new ModelData[9];
	   RawModel alien[] = new RawModel[9];
	   
	   for(int i =0; i<8; i++){		   
	    robot[i] = OBJFileLoader.loadOBJ("dummy"+i);
	    alien[i] = loader.loadVAO(robot[i].getVertices(), robot[i].getTextureCoords(), robot[i].getNormals(), robot[i].getIndices());
	    tex[i] = new TexturedModel(alien[i], new TextureData(loader.loadTexture(texture)));	    
	   }
	   
   }
   public void animate(Player player){
	   if(player.getSpeed() != 0){
		 player.setTexModel(tex[i]);
		 deltaTime++;
		 if(deltaTime > 9){
		   i++;
		   deltaTime = 0;
		 }
		 if(i == 7 ){
		   i = 0;
	     }
	   }
	   else
		   player.setTexModel(tex[7]);
   }
   
   
}
