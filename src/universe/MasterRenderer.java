package universe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import model.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrain.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float 	NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	private static final float RED = 0.3f;
	private static final float GREEN = 0.3f;
	private static final float BLUE = 0.35f;
	
	private Matrix4f projectionMatrix;
	private StaticShader shader = new StaticShader();
	private Renderer renderer ;
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer ;
	
	private SkyboxRenderer skyboxRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer(Loader loader){
		createProjectionMatrix();
		renderer = new Renderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader,projectionMatrix);
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);		
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);		
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
	
		skyboxRenderer.render(camera,RED, GREEN, BLUE);
        terrains.clear();
		entities.clear();
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getTexModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}
		else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	   public void prepare(){
		   GL11.glEnable(GL11.GL_DEPTH_TEST);
		   GL11.glClearColor(RED, GREEN, BLUE, 1);
		   GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	   }
	   
	   private void createProjectionMatrix(){
		   float aspectRatio = (float) Display.getWidth()/(float)Display.getHeight();
		   float yScale = (float)((1f)/Math.tan(Math.toRadians(FOV/2f))*aspectRatio);
		   float xScale = yScale/aspectRatio;
		   float frustumLength = FAR_PLANE - NEAR_PLANE;
		   projectionMatrix = new Matrix4f();
		   projectionMatrix.m00 = xScale;
		   projectionMatrix.m11 = yScale;
		   projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE)/ frustumLength);
		   projectionMatrix.m23 = -1;
		   projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE / frustumLength));
		   projectionMatrix.m33 = 0;
	   }
}
