package terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import model.RawModel;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Mathemagic;
import universe.Loader;

public class Terrain {
   private static final float SIZE = 800;
   private static final float MAX_HEIGHT = 40;
   private static final float MAX_PIXEL_COLOR = 256*256*256;
   
   private float x;
   private float z;
   private RawModel model;
   private TerrainTexturePack texturePack;
   private TerrainTexture blendMap;
   
   private float[][] heights;
   
   public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String s){
	   this.texturePack = texturePack;
	   x = gridX*SIZE;
	   z = gridZ*SIZE;
	   model = generateTerrain(loader, s);
	   this.blendMap = blendMap;

   }
   
   
   public float getX() {
	return x;
}


public float getZ() {
	return z;
}


public RawModel getModel() {
	return model;
}


public TerrainTexturePack getTexturePack() {
	return texturePack;
}


public TerrainTexture getBlendMap() {
	return blendMap;
}

public float getHeightOfTerrian(float worldX, float worldZ ){
	float terrainX = worldX - this.x;
	float terrainZ = worldZ - this.z;
	float gridSquareSize = SIZE / ((float)heights.length -1);	
	int gridX = (int)Math.floor(terrainX/gridSquareSize);
	int gridZ = (int)Math.floor(terrainZ/gridSquareSize);
if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX <0 || gridZ<0){
	return 0;
}
float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
float answer;
if(xCoord <= (1-zCoord)){
	answer = Mathemagic.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX+1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
}else{
	answer = Mathemagic.barryCentric(new Vector3f(1, heights[gridX+1][gridZ], 0), new Vector3f(1, heights[gridX+1][gridZ+1], 1), new Vector3f(0, heights[gridX][gridZ+1],1), new Vector2f(xCoord, zCoord));
}
return answer;
}

private RawModel generateTerrain(Loader loader, String filename){
	
	      BufferedImage image = null;
	      try {
			image = ImageIO.read(new File("resource/"+filename+".png"));
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	      int VERTEX_COUNT = image.getHeight();
	      heights = new float[VERTEX_COUNT][VERTEX_COUNT];
	      int count = VERTEX_COUNT * VERTEX_COUNT;
	      float[] vertices = new float[count*3];
	      float[] normals = new float[count*3];
	      float[] texCoords = new float[count*2];
	      int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
	      int vertexPointer = 0;
	      for(int i = 0; i<VERTEX_COUNT; i++){
	    	  for(int j=0; j<VERTEX_COUNT; j++){
	    		  vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT-1)*SIZE;
	    		  float height = getHeight(j,i,image);
	    		  heights[j][i] = height;
	    		  vertices[vertexPointer*3+1] = height;
	    		  vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT-1)*SIZE;
	    		  Vector3f normal = calculateNormal(j, i, image);
	    		  normals[vertexPointer*3] = normal.getX();
	    		  normals[vertexPointer*3+1] = normal.getY();
	    		  normals[vertexPointer*3+2] = normal.getZ();
	    		  texCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT -1 );
	    		  texCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT -1 );
	    		  vertexPointer++;	    		  
	    	  }
	      }
	      int pointer = 0;
	      for(int gz = 0; gz<VERTEX_COUNT-1; gz++){
	    	  for(int gx=0; gx<VERTEX_COUNT-1; gx++){
	    		  int topleft = (gz*VERTEX_COUNT)+gx;
	    		  int topright = topleft +1;
	    		  int bottomleft = ((gz+1)*VERTEX_COUNT)+gx;
	    		  int bottomright = bottomleft + 1;
	    		  indices[pointer++] = topleft;
	    		  indices[pointer++] = bottomleft;
	    		  indices[pointer++] = topright;
	    		  indices[pointer++] = topright;
	    		  indices[pointer++] = bottomleft;
	    		  indices[pointer++] = bottomright;
	    		  
	    	  }
	      }
	      return loader.loadVAO(vertices, texCoords, normals, indices);
   }

   private float getHeight(int x, int z, BufferedImage image){
	   if(x<0 || x>=image.getHeight() || z<0 || z>=image.getWidth()){
		   return 0;
	   }
	   float height = image.getRGB(x, z);
	   height += MAX_PIXEL_COLOR/2f;
	   height /= MAX_PIXEL_COLOR/2f;
	   height *= MAX_HEIGHT;
	   return height;
   }
   
   private Vector3f calculateNormal(int x, int z, BufferedImage image){
	   float heightL = getHeight(x-1, z, image);
	   float heightR = getHeight(x+1, z, image);
	   float heightU = getHeight(x, z+1, image);
	   float heightD = getHeight(x, z-1, image);
       Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
       normal.normalise();
       return normal;
   }
   
   
}
