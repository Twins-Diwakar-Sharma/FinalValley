package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.TransformationMaths;

public class StaticShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 3;
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
    
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightColor[];
    private int location_lightPosition[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;
    private int location_attenuation[];
    
    public StaticShader(){
    	super(VERTEX_FILE,FRAGMENT_FILE	);
    }
    
    protected void bindAttributes(){
    	super.bindAttributes(0, "positon");
    	super.bindAttributes(1,"texturecoords");
    	super.bindAttributes(2, "normals");
    }

	protected void getAllUniformLocations() {
	  location_transformationMatrix = super.getUniformLocation("transformationMatrix");	
	  location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	  location_viewMatrix = super.getUniformLocation("viewMatrix");
	  location_shineDamper = super.getUniformLocation("shineDamper");
	  location_reflectivity = super.getUniformLocation("reflectivity");
	  location_useFakeLighting = super.getUniformLocation("useFakeLighting");
	  location_skyColour = super.getUniformLocation("skyColour");
	  
	  location_lightPosition = new int[MAX_LIGHTS];
	  location_lightColor = new int[MAX_LIGHTS];
	  location_attenuation = new int[MAX_LIGHTS];
	  
	  for(int i=0; i<MAX_LIGHTS; i++){
		  location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
		  location_lightColor[i] = super.getUniformLocation("lightColor["+i+"]");
		  location_attenuation[i] = super.getUniformLocation("attenuation["+i+"]");	  
	  }
	}
	
	public void loadTransformationMatrix(Matrix4f tmatrix){
		super.loadMatrix(location_transformationMatrix, tmatrix);
	}
	
	public void loadFakeLighting(boolean useFake){
	   	super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	public void loadProjectionMatrix(Matrix4f pmatrix){
		super.loadMatrix(location_projectionMatrix, pmatrix);
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f vmatrix = TransformationMaths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, vmatrix);
	}

	public void loadLights(List<Light> lights){
		for(int i=0; i<MAX_LIGHTS; i++){
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}
			else{
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
				super.loadVector(location_attenuation[i], new Vector3f(1,0,0));
			}
		}
	}
	
    public void loadShine(float damper,float reflectivity){
    	super.loadFloat(location_shineDamper, damper);
    	super.loadFloat(location_reflectivity, reflectivity);
    }
}
