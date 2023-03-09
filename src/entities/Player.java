package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import audio.Source;
import model.TexturedModel;
import terrain.Terrain;
import universe.DisplayManager;

public class Player extends Entity{

	private static final float RUN_SPEED = 20;
	private static final float STRAFE_SPEED = 10;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 25;
	private static final float TERRAIN_HEIGHT = 0;
	
	private float strafeSpeed = 0;
	private float currentSpeed = 0;
	private float upwardSpeed = 0;

	
	private boolean inAir = false;
	private boolean rotate = true;
	
	public Player(TexturedModel texModel, Vector3f translation, float rotX, float rotY, float rotZ, float scale) {
		super(texModel, translation, rotX, rotY, rotZ, scale);
	}
    
	public void move(Terrain terrain){
		checkInputs();
		calculateRotation();
		calculateTranslation(terrain);
		
	}
	
	public void move(Terrain terrain, Vector3f position, float rotx, float roty, float rotz, float speed){
		super.setRotX(rotx);
		super.setRotY(roty);
		super.setRotZ(rotz);
		super.setTranslation(position);
		this.currentSpeed = speed;
	       upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
	        super.translate(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
	        float terrainHeight = terrain.getHeightOfTerrian(super.getTranslation().x, super.getTranslation().z);
	        if(super.getTranslation().y < terrainHeight){
	        	upwardSpeed = 0;
	        	inAir = false;
	        	super.getTranslation().y = terrainHeight;
	        }
	}
	
	private void calculateRotation(){
		if(rotate){
		if(Mouse.getX() >= 3*DisplayManager.getWidth()/4 || Mouse.getX() <= DisplayManager.getHeight()/4){
			Mouse.setGrabbed(false);
			Mouse.setCursorPosition(DisplayManager.getWidth()/2, DisplayManager.getHeight()/2);
			Mouse.setGrabbed(true);
		}
		float angle = Mouse.getDX()*0.1f;
		rotate(0,-angle, 0);
		}
	}
	
	private void calculateTranslation(Terrain terrain){
		//.....................X Z CALCULATION ..............................
        float forwardDistance = currentSpeed * DisplayManager.getFrameTimeSeconds();  
        float forwarddx = (float)(forwardDistance * Math.sin( Math.toRadians(super.getRotY() )));
        float forwarddz = (float)(forwardDistance * Math.cos( Math.toRadians(super.getRotY() )));
        
        float sideDistance = strafeSpeed * DisplayManager.getFrameTimeSeconds();
        float sidedx = (float)(sideDistance * Math.cos( Math.toRadians(super.getRotY() )));
        float sidedz = -(float)(sideDistance * Math.sin( Math.toRadians(super.getRotY() )));
        
        float dx = forwarddx + sidedx;
        float dz = forwarddz + sidedz;
        
        super.translate(dx, 0, dz);
        
        //...................Y CALCULATION....................................
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.translate(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrian(super.getTranslation().x, super.getTranslation().z);
        if(super.getTranslation().y < terrainHeight){
        	upwardSpeed = 0;
        	inAir = false;
        	super.getTranslation().y = terrainHeight;
        }
	}
	
	private void jump(){
		if(!inAir){
		  this.upwardSpeed = JUMP_POWER;
		  inAir = true;
		}
	}
	
	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
//			source.continuePlaying();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				this.currentSpeed = RUN_SPEED + 100;
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = - RUN_SPEED;
		}
		else{
			this.currentSpeed = 0;

		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.strafeSpeed = STRAFE_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.strafeSpeed = -STRAFE_SPEED;
		}
		else
			this.strafeSpeed = 0;		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump();
		}
	}
	
	
	public boolean isRotation(){
		return rotate;
	}
	
	public void setRotaion(boolean r){
		rotate = r;
	}
	
	public float getSpeed(){
		return currentSpeed;
	}
	
}
