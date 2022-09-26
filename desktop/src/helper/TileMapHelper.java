package helper;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import main.Boot;
import main.GameScreen;
import objects.Bonfire;
import objects.Boots;
import objects.Boss;
import objects.Chest;
import objects.Crystal;
import objects.Door;
import objects.GoldKey;
import objects.HealthPotion;
import objects.Player;
import objects.SilverKey;
import objects.Spinner;
import objects.Trap;

public class TileMapHelper {

	private TiledMap tiledMap;
	private GameScreen gc;

	public TileMapHelper(GameScreen gc) {
		this.gc = gc;
	}

	public OrthogonalTiledMapRenderer setupMap() {
		tiledMap = new TmxMapLoader().load("maps/EscapeTheHaunt_16_64.tmx");
		parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
		return new OrthogonalTiledMapRenderer(tiledMap);
	}

	private void parseMapObjects(MapObjects mapObjects) {
		for (MapObject mapObject : mapObjects) {

			if (mapObject instanceof PolygonMapObject) {
				createBodyThings((PolygonMapObject) mapObject, true);
			}

			if (mapObject instanceof RectangleMapObject) {

				Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
				String rectangleName = mapObject.getName();

				if (rectangleName.equals("player")) {
					// main body
					Body body = BodyHelperService.createPlayerBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, (float) rectangle.getWidth() - 8,
							(float) rectangle.getHeight() / 2.5f, false, gc.getWorld(), rectangle);

					Player player = new Player(rectangle.getWidth(), rectangle.getHeight(), body);
					gc.addObjects(player);
				}

				if (rectangleName.equals("silverkey")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					SilverKey key = new SilverKey(rectangle.getWidth(), rectangle.getHeight(), body);

					key.getBody().getFixtureList().first().setUserData(key);
					key.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(key);
				}

				if (rectangleName.equals("goldkey")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					GoldKey key = new GoldKey(rectangle.getWidth(), rectangle.getHeight(), body);

					key.getBody().getFixtureList().first().setUserData(key);
					key.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(key);
				}

				if (rectangleName.equals("chest")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Chest chest = new Chest(rectangle.getWidth(), rectangle.getHeight(), body);
					chest.getBody().getFixtureList().first().setUserData(chest);
					gc.addObjects(chest);
				}

				if (rectangleName.equals("boots")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Boots boots = new Boots(rectangle.getWidth(), rectangle.getHeight(), body);
					boots.getBody().getFixtureList().first().setUserData(boots);
					boots.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(boots);
				}

				if (rectangleName.equals("spinner")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), false, gc.getWorld());

					CircleShape circle = new CircleShape();
					circle.setPosition(new Vector2(0, 0));

					circle.setRadius(3.2f);

					body.getFixtureList().add(body.createFixture(circle, 1));
					body.getFixtureList().peek().setSensor(true);

					Spinner spinner = new Spinner(rectangle.getWidth(), rectangle.getHeight(), body);

					spinner.getBody().getFixtureList().first().setUserData(spinner);
					spinner.getBody().getFixtureList().peek().setUserData(spinner);
					gc.addObjects(spinner);
				}

				if (rectangleName.equals("boss")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					CircleShape circle = new CircleShape();

					circle.setPosition(new Vector2((float) 0, (float) 0));
					circle.setRadius(6.5f);

					body.getFixtureList().add(body.createFixture(circle, 1));
					body.getFixtureList().peek().setSensor(true);

					Boss boss = new Boss(rectangle.getWidth(), rectangle.getHeight(), body);

					boss.getBody().getFixtureList().first().setUserData("bossbody");
					boss.getBody().getFixtureList().peek().setUserData("bossvision");
					gc.addObjects(boss);
				}

				if (rectangleName.equals("potion")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					HealthPotion potion = new HealthPotion(rectangle.getWidth(), rectangle.getHeight(), body);
					potion.getBody().getFixtureList().first().setUserData(potion);
					potion.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(potion);
				}

				if (rectangleName.equals("crystal")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Crystal crystal = new Crystal(rectangle.getWidth(), rectangle.getHeight(), body);

					crystal.getBody().getFixtureList().first().setUserData(crystal);
					crystal.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(crystal);
				}

				if (rectangleName.equals("bonfire")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Bonfire bonfire = new Bonfire(rectangle.getWidth(), rectangle.getHeight(), body);
					gc.addObjects(bonfire);
				}

				if (rectangleName.equals("door")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Door door = new Door(rectangle.getWidth(), rectangle.getHeight(), body);
					gc.addObjects(door);
				}

				if (rectangleName.equals("trap")) {

					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Trap trap = new Trap(rectangle.getWidth(), rectangle.getHeight(), body);

					trap.getBody().getFixtureList().first().setUserData(trap);
					trap.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(trap);
				}

			}
		}
	}

	private void createBodyThings(PolygonMapObject polygonMapObject, boolean isStatic) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
		Body body = gc.getWorld().createBody(bodyDef);
		Shape shape = createPolygonShape(polygonMapObject);
		body.createFixture(shape, 1000);
		body.setUserData(this);
		shape.dispose();
	}

	private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
		float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; i++) {
			Vector2 current = new Vector2(vertices[i * 2] / Boot.PPM, vertices[i * 2 + 1] / Boot.PPM);
			worldVertices[i] = current;
		}

		PolygonShape shape = new PolygonShape();
		shape.set(worldVertices);
		return shape;
	}
}
