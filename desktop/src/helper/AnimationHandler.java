package helper;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHandler {

	private float stateTime;
	private String direction;
	private boolean loop;
	private String action;
	private HashMap<String, Animation<TextureRegion>> animations;

	public AnimationHandler() {
		this.direction = "";
		this.stateTime = 0f;
		this.loop = true;
		this.animations = new HashMap<>();
	}

	public void add(float FRAME_TIME, String object, String action, String direction) {

		TextureAtlas textureAtlas = new TextureAtlas(
				Gdx.files.internal(object + "/" + action + "/" + action + direction + ".atlas"));

		animations.put(action + direction, new Animation<TextureRegion>(FRAME_TIME, textureAtlas.findRegions("tile")));
	}

	public void setActionDirection(String action, String direction, boolean loop) {
		this.action = action;
		this.direction = direction;
		this.loop = loop;
		if (!loop)
			this.stateTime = 0f;
	}

	public void setAction(String action, boolean loop) {
		this.action = action;
		this.loop = loop;
		if (!loop)
			this.stateTime = 0f;
	}

	public String getActionDirection() {
		return this.action + this.direction;
	}

	public String getAction() {
		return this.action;
	}

	public boolean isAnimationFinished() {
		return animations.get(this.getActionDirection()).isAnimationFinished(stateTime);
	}

	public TextureRegion getFrame() {
		stateTime += Gdx.graphics.getDeltaTime();
//		System.out.println(this.getActionDirection());
		return animations.get(this.getActionDirection()).getKeyFrame(stateTime, loop);
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public float getStateTime() {
		return this.stateTime;
	}

}
