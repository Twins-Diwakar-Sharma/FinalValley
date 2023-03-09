package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
   private int texture;
   private Vector2f positon;
   private Vector2f scale;
public GuiTexture(int texture, Vector2f positon, Vector2f scale) {
	
	this.texture = texture;
	this.positon = positon;
	this.scale = scale;
}
public int getTexture() {
	return texture;
}
public Vector2f getPositon() {
	return positon;
}
public Vector2f getScale() {
	return scale;
}


   
}
