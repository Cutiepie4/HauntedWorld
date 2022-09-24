package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import helper.ListenerClass;
import helper.TileMapHelper;
import objects.Boss;
import objects.Objects;
import objects.Player;

public class GameScreen extends ScreenAdapter {

	public static GameScreen INSTANCE;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private World world;
	private Box2DDebugRenderer box2dDebugRenderer;
	private Hud hud;

	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private TileMapHelper tileMapHelper;

	private ArrayList<Objects> listObjects = new ArrayList<>();

	private ArrayList<Objects> toRemove = new ArrayList<>();

	public GameScreen(OrthographicCamera camera) {
		INSTANCE = this;

		this.camera = camera;
		this.batch = new SpriteBatch();
		this.world = new World(new Vector2(0, 0), true);
		this.box2dDebugRenderer = new Box2DDebugRenderer();
		this.hud = new Hud(batch);

		world.setContactListener(new ListenerClass()); // contact Collision

		this.tileMapHelper = new TileMapHelper(this);

		this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

	}

	private void update() {

		world.step(1 / 60f, 6, 2);

		cameraUpdate();

		hud.update(1 / 60f);

		batch.setProjectionMatrix(camera.combined);

		Player.INSTANCE.update();

		this.objectUpdate();

		orthogonalTiledMapRenderer.setView(camera);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			this.dispose();
			Gdx.app.exit();
		}
	}

	private void cameraUpdate() {

		Vector3 position = camera.position;
		position.x = Math.round(Player.INSTANCE.getBody().getPosition().x * 16.0f * 10) / 10f;
		position.y = Math.round(Player.INSTANCE.getBody().getPosition().y * 16.0f * 10) / 10f;
		float startX = camera.viewportWidth/2;
		float startY = camera.viewportHeight/2;
		float width = 1024f - startX;
		float height = 1024f - startY;
		boundCamera(camera,startX, startY, width, height);
		//camera.position.set(position);
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

	@Override
	public void render(float delta) {

		this.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Collections.sort(listObjects, Comparator.comparing(Objects::getY).reversed());

		orthogonalTiledMapRenderer.render();

		batch.begin();

		this.objectsRenderer();
		
		Boss.INSTANCE.render(batch);

		batch.end();

		batch.setProjectionMatrix(hud.stage.getCamera().combined);

		hud.stage.draw();

		box2dDebugRenderer.render(world, camera.combined.scl(Boot.PPM)); // debug size of object
	}

	public World getWorld() {
		return world;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public void addObjects(Objects object) {
		this.listObjects.add(object);
	}

	private void objectsRenderer() {
		for (Objects i : listObjects) {
			if (i != null) {
				i.render(batch);
			}
		}
	}

	public void objectUpdate() {
		for (Objects i : toRemove) {
			if (this.listObjects.contains(i))
				this.listObjects.remove(i);
			this.world.destroyBody(i.getBody());
		}
		toRemove.clear();
	}

	public void addToRemove(Objects object) {
		this.toRemove.add(object);
	}

}
