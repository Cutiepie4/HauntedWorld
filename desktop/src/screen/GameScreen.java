package screen;

import java.util.Comparator;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import character.Bullet;
import character.Player;
import helper.GameEventListener;
import helper.TileMapHelper;
import main.Boot;
import things.Entity;
import ui.Hud;

public class GameScreen extends ScreenAdapter {

	public static boolean isPause = false;
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
	}

	private void cameraUpdate() {
		Vector3 position = camera.position;
		position.x = Math.round(Player.INSTANCE.getBody().getPosition().x * Boot.PPM * 10) / 10f;
		position.y = Math.round(Player.INSTANCE.getBody().getPosition().y * Boot.PPM * 10) / 10f;
		float startX = camera.viewportWidth / 2;
		float startY = camera.viewportHeight / 2;
		float width = 1024f - startX;
		float height = 1024f - startY;
		boundCamera(camera, startX, startY, width, height);
		// camera.position.set(position);
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

		this.objectUpdate();

		cameraUpdate();

		hud.update(1 / 60f);

		batch.setProjectionMatrix(camera.combined);

		orthogonalTiledMapRenderer.setView(camera);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			this.dispose();
			Gdx.app.exit();
		}

		if (Player.INSTANCE.getAnimationHandler().getAction().equals("dead")
				&& Player.INSTANCE.getAnimationHandler().isAnimationFinished()) {
//			Player.INSTANCE.revive();
			Gdx.app.postRunnable(() -> {
				Player.INSTANCE.revive();
				Boot.INSTANCE.setScreen(new ReloadScreen(camera));
			});
		}

	}

	@Override
	public void render(float delta) {

		this.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		listObjects.sort(Comparator.comparing(Entity::getY).reversed());

		orthogonalTiledMapRenderer.render();

		batch.begin();

		this.objectsRender();

		batch.end();

		hud.draw(batch);

		box2dDebugRenderer.render(world, camera.combined.scl(Boot.PPM)); // debug hit box of object
	}

	private void objectsRender() {
		if (GameScreen.isPause)
			return;
		for (Entity i : listObjects) {
			if (i != null) {
				i.render(batch);
			}
		}
	}

	private void objectUpdate() {
		for (Entity i : toRemove) {
			if (i != null && i.getBody() != null && i.getAnimationHandler().isAnimationFinished()) {
				this.listObjects.removeValue(i, true);
//				i.getBody().setActive(false);
				this.world.destroyBody(i.getBody());
				i.setBody(null);
				i = null;
			}
		}

		for (Entity i : toAdd) {
			if (i != null && !this.listObjects.contains(i, true))
				this.listObjects.add(i);
		}
		toAdd.clear();
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

	public void addObjects(Entity object) {
		this.toAdd.add(object);
	}

}
