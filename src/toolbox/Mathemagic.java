package toolbox;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Mathemagic {

    public static float computeW(Vector3f vector){
    	float t = 1.0f - (vector.x * vector.x) - (vector.y * vector.y) - (vector.z * vector.z);
    	if( t < 0.01){
    		return 0.0f;
    	}
    	else{
    		return (float) -Math.sqrt(t);
    	}  		
    }
    
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos){
    	float det = (p2.z - p3.z)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.z - p3.z);
    	float l1 = ((p2.z - p3.z)*(pos.x - p3.x) + (p3.x - p2.x)*(pos.y-p3.z))/det;
    	float l2 = ((p3.z - p1.z)*(pos.x - p3.x) + (p1.x - p3.x)*(pos.y - p3.z))/det;
        float l3 = 1.0f - l1 - l2;
        return l1*p1.y + l2*p2.y + l3*p3.y;
    }
}
