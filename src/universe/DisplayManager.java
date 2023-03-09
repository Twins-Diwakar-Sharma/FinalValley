package universe;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	static int width=1366;
	static int height=768;
	static int fps=120;
    static long lastFrameTime;
    static float delta;
	 
	 public static void makeDisplay(){
		
		 ContextAttribs conattrib = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		 try {
		    System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setFullscreen(true);
			Display.create(new PixelFormat(), conattrib);
		    
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		 
		 GL11.glViewport(0, 0, width, height);
		 lastFrameTime = getCurrentTime();
	 }
	 
	 public static void updateDisplay(){
		 Display.sync(fps);
		 Display.update();
		 long currentFrameTime = getCurrentTime();
		 delta = (currentFrameTime - lastFrameTime) / 1000f;
		 lastFrameTime = currentFrameTime;
	 }
	 
	 public static float getFrameTimeSeconds(){
		 return delta;
	 }

	 public static void deleteDisplay(){
		 Display.destroy();
	 }
	 
	 private static long getCurrentTime(){
		 return Sys.getTime()*1000 / Sys.getTimerResolution();
	 }
	 
	 public static int getWidth(){
		 return width;
	 }
	 
	 public static int getHeight(){
		 return height;
	 }
	 
}
