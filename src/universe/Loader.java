package universe;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import model.RawModel;
import textures.DataTexture;

public class Loader {
   private List<Integer> vaoList = new ArrayList<Integer>();
   private List<Integer> vboList = new ArrayList<Integer>();
   private List<Integer> textureList = new ArrayList<Integer>();
   
   
   public RawModel loadVAO(float[] positions, float[] texturesCoords, float[] normals, int[] indices){
	   int vaoId = createVAO();
	   bindIndicesBuffer(indices);
	   storeData(0,3,positions);
	   storeData(1,2,texturesCoords);
	   storeData(2,3,normals);
	   unbindVAO();
	   return new RawModel(vaoId,indices.length);
   }
   
   public RawModel loadVAO(float[] positions,int dimensions){
	   int vaoId = createVAO();
	   this.storeData(0, dimensions, positions);
	   unbindVAO();
	   return new RawModel(vaoId,positions.length/dimensions);
   }
   
   
   
   public int loadTexture(String filename){
	   Texture texture = null;
	   try{
	   texture = TextureLoader.getTexture("PNG", new FileInputStream("resource/"+filename+".png"));
	   GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	   GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
	   GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
   	   }catch(IOException ex){
   		   ex.printStackTrace();
   	   }
	   int textureId = texture.getTextureID();
	   textureList.add(textureId);
	   return textureId;
   }
   
   public void cleanUp(){
	   for(int vao:vaoList){
		   GL30.glDeleteVertexArrays(vao);
	   }
	   for(int vbo:vboList){
		   GL15.glDeleteBuffers(vbo);
	   }
	   for(int texture:textureList){
		   GL11.glDeleteTextures(texture);
	   }
   }
   
   private int createVAO(){
	   int vaoId = GL30.glGenVertexArrays();
	   GL30.glBindVertexArray(vaoId);
	   return vaoId;
   }
   
   private void storeData(int attno, int cordSize, float[] data){
	   int vboId = GL15.glGenBuffers();
	   vboList.add(vboId);
	   GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
	   FloatBuffer fBuffer = storeBuffer(data);
	   GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
	   GL20.glVertexAttribPointer(attno, cordSize, GL11.GL_FLOAT, false, 0,0);
	   GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
   }
   
   private void bindIndicesBuffer(int[] indices){
	   int vboId = GL15.glGenBuffers();
	   vboList.add(vboId);
	   GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboId);
	   IntBuffer ibuffer  = storeBufferInt(indices);
	   GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibuffer, GL15.GL_STATIC_DRAW);
   }
   
   private void unbindVAO(){
	   GL30.glBindVertexArray(0);
   }
   
   private FloatBuffer storeBuffer(float[] data){
	   FloatBuffer fbuffer = BufferUtils.createFloatBuffer(data.length);
	   fbuffer.put(data);
	   fbuffer.flip();
	   return fbuffer;
   }
   
   private IntBuffer storeBufferInt(int[] data){
	   IntBuffer ibuffer = BufferUtils.createIntBuffer(data.length);
	   ibuffer.put(data);
	   ibuffer.flip();
	   return ibuffer;
   }
   
   private DataTexture decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new DataTexture( width, height, buffer);
	}
   
   public int loadCubeMap(String[] textureFiles){
	   int texID = GL11.glGenTextures();
	   GL13.glActiveTexture(GL13.GL_TEXTURE0);
	   GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
	   for(int i=0; i<textureFiles.length; i++){
		   DataTexture data = this.decodeTextureFile("resource/"+textureFiles[i]+".png");
		   GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,data.getBuffer() );
	   }
	   GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	   GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	   GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_WRAP_S,GL12.GL_CLAMP_TO_EDGE);
	   GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	   
	   textureList.add(texID);
	   return texID;
   }
}
