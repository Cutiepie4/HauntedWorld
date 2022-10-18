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

import character.Boss;
import character.Player;
import character.Spider;
import character.Spinner;
import character.Trap;
import main.Boot;
import screen.GameScreen;
import things.Chest;
import things.Decor;
import things.Items;

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

				for (String i : Constants.DECOR) {
					if (i.equals(rectangleName)) {
						Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
								rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
								rectangle.getHeight(), true, gc.getWorld());

						Decor decor = new Decor(rectangle.getWidth(), rectangle.getHeight(), body, rectangleName);
						gc.addObjects(decor);
						break;
					}
				}

				if (rectangleName.equals("player")) {
					Body body = BodyHelperService.createPlayerBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, (float) rectangle.getWidth() - 8,
							(float) rectangle.getHeight() / 2.5f, false, gc.getWorld(), rectangle);

					Player player = new Player(rectangle.getWidth(), rectangle.getHeight(), body);
					gc.addObjects(player);
				}

				else if (rectangleName.equals("spinner")) {
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

				else if (rectangleName.equals("spider")) {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), false, gc.getWorld());

					CircleShape circle = new CircleShape();
					circle.setPosition(new Vector2(0, 0));

					circle.setRadius(3.2f);

					body.getFixtureList().add(body.createFixture(circle, 1));
					body.getFixtureList().peek().setSensor(true);

					Spider spinner = new Spider(rectangle.getWidth(), rectangle.getHeight(), body);

					spinner.getBody().getFixtureList().first().setUserData(spinner);
					spinner.getBody().getFixtureList().peek().setUserData(spinner);
					gc.addObjects(spinner);
				}

				else if (rectangleName.equals("boss")) {
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

				else if (rectangleName.equals("trap")) {

					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Trap trap = new Trap(rectangle.getWidth(), rectangle.getHeight(), body);

					trap.getBody().getFixtureList().first().setUserData(trap);
					trap.getBody().getFixtureList().first().setSensor(true);
					gc.addObjects(trap);
				}

				else if (rectangleName.equals("chest")) {

					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					Chest chest = new Chest(rectangle.getWidth(), rectangle.getHeight(), body);
					chest.getBody().getFixtureList().first().setUserData(chest);
				}

				else {
					Body body = BodyHelperService.createBody(rectangle.getX() + (float) rectangle.getWidth() / 2,
							rectangle.getY() + (float) rectangle.getHeight() / 2, rectangle.getWidth(),
							rectangle.getHeight(), true, gc.getWorld());

					switch (rectangleName) {
					case "silverkey":
						Items item = new Items(rectangle.getWidth(), rectangle.getHeight(), body, "Silver Key");
						item.getBody().getFixtureList().first().setSensor(true);
						item.getBody().getFixtureList().first().setUserData(item);
						break;

					case "goldkey":
						item = new Items(rectangle.getWidth(), rectangle.getHeight(), body, "Gold Key");
						item.getBody().getFixtureList().first().setSensor(true);
						item.getBody().getFixtureList().first().setUserData(item);
						break;

					case "crystal":
						item = new Items(rectangle.getWidth(), rectangle.getHeight(), body, "Crystal");
						item.getBody().getFixtureList().first().setSensor(true);
						item.getBody().getFixtureList().first().setUserData(item);
						break;

					case "boot":
						item = new Items(rectangle.getWidth(), rectangle.getHeight(), body, "Boot");
						item.getBody().getFixtureList().first().setUserData(item);
						item.getBody().getFixtureList().first().setSensor(true);
						break;

					case "healthpotion":
						item = new Items(rectangle.getWidth(), rectangle.getHeight(), body, "Health Potion");
						item.getBody().getFixtureList().first().setUserData(item);
						item.getBody().getFixtureList().first().setSensor(true);
						break;
					}
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
