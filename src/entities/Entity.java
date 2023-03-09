package entities;

import org.lwjgl.util.vector.Vector3f;


import model.TexturedModel;

public class Entity {
    

	private TexturedModel texModel;
	private Vector3f translation;
	private float rotX, rotY, rotZ;
	float scale;
	
	public Entity(TexturedModel texModel, Vector3f translation, float rotX, float rotY, float rotZ, float scale){
	     this.texModel = texModel;
	     this.translation = translation;
	     this.rotX = rotX;
	     this.rotY = rotY;
	     this.rotZ = rotZ;
	     this.scale = scale;
	}
	
	public void translate(float dx, float dy, float dz){
		translation.x += dx;
		translation.y += dy;
		translation.z += dz;
	}
	
	public void rotate(float dx, float dy, float dz){
		rotX += dx;
		rotY += dy;
		rotZ += dz;
	}

	public TexturedModel getTexModel() {
		return texModel;
	}

	public void setTexModel(TexturedModel texModel) {
		this.texModel = texModel;
	}

	public Vector3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
