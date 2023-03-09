package model;

import textures.TextureData;

public class TexturedModel {
    
	private RawModel model;
	private TextureData data;
	
	public TexturedModel(RawModel model, TextureData data){
		this.model = model;
		this.data = data;
	}
	
	public RawModel getModel(){
		return model;
	}
	
	public TextureData getData(){
		return data;
	}
}
