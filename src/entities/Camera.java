package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import terrain.Terrain;
import universe.DisplayManager;

public class Camera {

	private float distanceFromPlayer = 30;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,10,0);
	private float pitch = 40;
	private float yaw = 0;
	private float roll;
	private Player player;
	
	private float preDistance;
	private boolean lock = false;  
	
	public Camera(Player player){
	   this.player = player;
	}
	
	public void move(Terrain terrain){
	     calculateZoom();
		 calculatePitch(terrain);
	     calculateAngleAround();

	     float horizontalDistance = calculateHorizontalDistance();
	     float verticalDistance = calculateVerticalDistance();
	     calculatePosition(horizontalDistance, verticalDistance);
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel; 
		if(distanceFromPlayer <= 2){
			distanceFromPlayer = 2;
		}
		if(distanceFromPlayer > 30){
			distanceFromPlayer = 30;
		}
	}
	
	private void calculatePitch(Terrain terrain){
		if(Mouse.getY() >= 3*DisplayManager.getHeight()/4 || Mouse.getY()<=DisplayManager.getHeight()/4){
           Mouse.setGrabbed(false);
           Mouse.setCursorPosition(DisplayManager.getWidth()/2, DisplayManager.getHeight()/2);
           Mouse.setGrabbed(true);
		}
		  float pitchChange = Mouse.getDY() * 0.1f;
		  pitch -= pitchChange;
        
          
		  if(pitch <= 0 && distanceFromPlayer > 2){
			  if(!lock)
			     preDistance = distanceFromPlayer;
			  distanceFromPlayer = 5;
			  lock = true;
		  }
		  
		  if(pitch > 0 && lock){
			  distanceFromPlayer = preDistance;
			  lock = false;
		  }
		  
		  if(pitch < -45)
			  pitch = -45;

		  if(pitch >= 70){
			  pitch = 70;
		  }
		  
		  // terrain height calculation
		  float tHeight = terrain.getHeightOfTerrian(position.x, position.z);
		  if(position.y - 770 < tHeight){
			  position.y = tHeight + 768;
		  }
	}
	
	private void calculateAngleAround(){
		if(Mouse.isButtonDown(2)){
			player.setRotaion(false);
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
		else{
			player.setRotaion(true);
			angleAroundPlayer = 0;
		}
	}
	
	private float calculateHorizontalDistance(){
		return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculatePosition(float hDistance, float vDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float zOffset = (float)(hDistance * (Math.cos(Math.toRadians(theta))));
		float xOffset = (float)(hDistance * (Math.sin(Math.toRadians(theta))));
		position.x = player.getTranslation().x - xOffset;
		position.y = player.getTranslation().y + vDistance +3;
		position.z = player.getTranslation().z - zOffset;
		yaw = (180 - theta);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}

}
