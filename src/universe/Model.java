package universe;

import model.RawModel;
import model.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import textures.TextureData;

public class Model {
	
	private TexturedModel texModel;
   
	public Model( Loader loader, String modelFile, String textureFile, int shineDamper, int reflectivity){
		ModelData data = OBJFileLoader.loadOBJ(modelFile);
		RawModel model = loader.loadVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		texModel = new TexturedModel(model, new TextureData(loader.loadTexture(textureFile)));
		TextureData specularTexture = texModel.getData();
		specularTexture.setShineDamper(shineDamper);
		specularTexture.setReflectivity(reflectivity);
	}
	
	public TexturedModel getTexModel(){
		return texModel;
	}
}
