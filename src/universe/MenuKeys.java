package universe;

import org.lwjgl.input.Keyboard;

public class MenuKeys {
	public int state = 0;   // 0 = play, 1 = quit;
	private boolean released = false;
	
   public void checkMenuInputs(){
	   if( Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
		   state = 1;
	   }
	   else if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
		   state = 0;
	   }
	   else if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
		   if(state == 0){
			   GameLoop.inMenu = false;
		   }
		   else if(state == 1){
			   System.exit(0);
		   }
	   }
	  
   }
}
