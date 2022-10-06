package helper;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static Assets INSTANCE;

	public Assets() {
		INSTANCE = this;
	}
	private AssetManager assetManager = new AssetManager();
	public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<>("background/uiskin.json", Skin.class, new SkinLoader.SkinParameter("background/uiskin.atlas"));
	
	public void loadALL() {
		assetManager.load(SKIN);
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
}
