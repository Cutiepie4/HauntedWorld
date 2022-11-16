package screen;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import character.Player;
import controller.AudioManager;
import controller.GameEventListener;
import controller.TileMapHelper;
import main.Boot;
import model.Dropable;
import model.Entity;
import ui.Hud;

public class GameScreen extends ScreenAdapter {

	public static boolean isWin = false;
	public static int WORLD_WIDTH = 16 * 96, WORLD_HEIGHT = 16 * 96;
	public static GameScreen INSTANCE;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer box2dDebugRenderer;
	private Hud hud;

	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private TileMapHelper tileMapHelper;

	private Array<Entity> listObjects = new Array<>();

	private Array<Entity> toRemove = new Array<>();

	private Array<Entity> toAdd = new Array<>();

	public GameScreen(OrthographicCamera camera) {
		INSTANCE = this;

		this.camera = camera;
		this.batch = new SpriteBatch();
		this.world = new World(new Vector2(0, 0), true);
		this.box2dDebugRenderer = new Box2DDebugRenderer();
		this.hud = new Hud(batch);

		world.setContactListener(new GameEventListener()); // contact Collision

		this.tileMapHelper = new TileMapHelper(this);

		this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

		AudioManager.INSTANCE.playMusic("overworld", true);
	}

	private void cameraUpdate() {
		Vector3 position = camera.position;
		position.x = Math.round(Player.INSTANCE.getBody().getPosition().x * Boot.PPM * 10) / 10f;
		position.y = Math.round(Player.INSTANCE.getBody().getPosition().y * Boot.PPM * 10) / 10f;
		float startX = camera.viewportWidth / 2;
		float startY = camera.viewportHeight / 2;
		float width = GameScreen.WORLD_WIDTH - startX;
		float height = GameScreen.WORLD_HEIGHT - startY;
		boundCamera(camera, startX, startY, width, height);
		camera.update();
	}

	private void boundCamera(OrthographicCamera camera, float startX, float startY, float endX, float endY) {
		Vector3 position = camera.position;
		if (position.x < startX) {
			position.x = startX;
		}

		if (position.y < startY) {
			position.y = startY;
		}

		if (position.x > endX) {
			position.x = endX;
		}

		if (position.y > endY) {
			position.y = endY;
		}

		camera.position.set(position);
		camera.update();
	}

	public void update() {
		world.step(1 / 60f, 6, 2);

		this.listObjectUpdate();

		cameraUpdate();

		hud.update(1 / 60f);

		batch.setProjectionMatrix(camera.combined);

		orthogonalTiledMapRenderer.setView(camera);

		this.switchScreen();

		this.adjustVolume();
	}

	private void switchScreen() {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			this.dispose();
			Gdx.app.exit();
		}

		if (Player.INSTANCE.getAnimationHandler().getAction().equals("dead")
				&& Player.INSTANCE.getAnimationHandler().isAnimationFinished()) {
			this.dispose();
			Boot.INSTANCE.setScreen(new ReloadScreen(camera));
			return;
		}

		if (GameScreen.isWin) {
			AudioManager.INSTANCE.stopSound("footstep");
			this.dispose();
			Boot.INSTANCE.setScreen(new EndGameScreen(camera));
			return;
		}
	}

	@Override
	public void render(float delta) {

		this.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		listObjects.sort(Comparator.comparing(Entity::getY));

		orthogonalTiledMapRenderer.render();

		batch.begin();

		this.objectsRender();

		batch.end();

		hud.draw(batch);

		box2dDebugRenderer.render(world, camera.combined.scl(Boot.PPM)); // debug hit box of object
	}

	private void objectsRender() {
		for (int i = this.listObjects.size - 1; i >= 0; i--) {
			if (this.listObjects.get(i).shouldDraw()) {
				this.listObjects.get(i).render(batch);
			}
		}
	}

	private void listObjectUpdate() {
		final Array<Entity> temp = new Array<>(toRemove);
		for (Entity i : temp) {
			if (i.getAnimationHandler().isAnimationFinished() && !this.world.isLocked()) {
				this.removeObject(i);
			}
		}

		for (Entity i : toAdd) {
			if (!this.listObjects.contains(i, true))
				this.listObjects.add(i);
		}
		toAdd.clear();
	}

	private void removeObject(Entity i) {
		this.listObjects.removeValue(i, true);
		toRemove.removeValue(i, true);
		this.removeBody(i.getBody());
		if (i instanceof Dropable && !i.isDropped) {
			((Dropable) i).dropItem();
			i.isDropped = true;
		}
	}

	private void removeBody(Body body) {
		Array<Fixture> fixtures = body.getFixtureList();
		for (Fixture fixture : fixtures) {
			body.destroyFixture(fixture);
		}
		this.world.destroyBody(body);
	}

	public void addToRemove(Entity object) {
		this.toRemove.add(object);
	}

	public World getWorld() {
		return world;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public void addObject(Entity object) {
		this.toAdd.add(object);
	}

	private void adjustVolume() {
		int musicVolume = AudioManager.INSTANCE.getMusicVolume();
		int soundVolume = AudioManager.INSTANCE.getSoundVolume();

		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			musicVolume -= 10;
		}

		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			musicVolume += 10;
		}

		if (Gdx.input.isKeyJustPressed(Keys.F3)) {
			soundVolume -= 10;
		}

		if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			soundVolume += 10;
		}

		if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			AudioManager.INSTANCE.mute();
		}

		if (musicVolume < 0)
			musicVolume = 0;
		if (musicVolume > 100)
			musicVolume = 100;
		if (soundVolume < 0)
			soundVolume = 0;
		if (soundVolume > 100)
			soundVolume = 100;

		AudioManager.INSTANCE.setSfxVolume(soundVolume);
		AudioManager.INSTANCE.setMusicVolume(musicVolume);
	}
}
