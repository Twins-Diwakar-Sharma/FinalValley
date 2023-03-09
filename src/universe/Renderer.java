package universe;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import model.RawModel;
import model.TexturedModel;
import shaders.StaticShader;
import textures.TextureData;
import toolbox.TransformationMaths;

public class Renderer {
   
	private StaticShader shader;
	
	public Renderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	   
   public void render(Map<TexturedModel, List<Entity>> entities){
	   for(TexturedModel model:entities.keySet()){
		   prepareTexturedModel(model);
		   List<Entity> batch = entities.get(model);
		   for(Entity entity:batch){
			   prepareInstance(entity);
			   GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT,0);

		   }
		   unbindTexturedModel();
	   }
   }
   
   private void prepareTexturedModel(TexturedModel texmodel){
	   RawModel model = texmodel.getModel();
	   GL30.glBindVertexArray(model.getVaoID());
	   GL20.glEnableVertexAttribArray(0);
	   GL20.glEnableVertexAttribArray(1);
	   GL20.glEnableVertexAttribArray(2);
	   TextureData tex  = texmodel.getData();
	   if(tex.isHasTransparency()){
		   MasterRenderer.disableCulling();
	   }
	   shader.loadFakeLighting(tex.isUseFakeLighting());
	   shader.loadShine(tex.getShineDamper(), tex.getReflectivity());
	   GL13.glActiveTexture(GL13.GL_TEXTURE0);
	   GL11.glBindTexture(GL11.GL_TEXTURE_2D, texmodel.getData().getTextureId());
   }
   
   private void unbindTexturedModel(){
	   MasterRenderer.enableCulling();
	   GL20.glDisableVertexAttribArray(0);
	   GL20.glDisableVertexAttribArray(1);
	   GL20.glDisableVertexAttribArray(2);
	   GL30.glBindVertexArray(0);
   }
   
   private void prepareInstance(Entity entity){
	   Matrix4f transformationMatrix = TransformationMaths.createTransformationMatrix(entity.getTranslation(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
	   shader.loadTransformationMatrix(transformationMatrix);
   }
      
}
