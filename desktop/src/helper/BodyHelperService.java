package helper;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import main.Boot;
import things.Entity;

public class BodyHelperService {

	public static Body createBody(float x, float y, float width, float height, boolean isStatic, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((float) x / Boot.PPM, (float) y / Boot.PPM);
		bodyDef.fixedRotation = true;

		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox((float) width / 2 / Boot.PPM, (float) height / 2 / Boot.PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.density = 1000;
		fixtureDef.isSensor = false;

		body.createFixture(fixtureDef);
		body.getFixtureList().first().setUserData("no name");
		shape.dispose();
		return body;
	}

	public static Body createPlayerBody(float x, float y, float width, float height, boolean isStatic, World world,
			Rectangle rectangle) {
		Body body = createBody(x, y, width, height, isStatic, world);

		body.getFixtureList().first().setUserData("playerbody");
		
		PolygonShape boxPoly = new PolygonShape();

		// LEFT
		boxPoly.setAsBox(0.32f, 0.5f, new Vector2(-0.8f, 0), 0);

		body.getFixtureList().add(body.createFixture(boxPoly, 1));
		body.getFixtureList().peek().setSensor(true);
		body.getFixtureList().peek().setUserData("left");

		// UP
		boxPoly.setAsBox(0.5f, 0.32f, new Vector2(0, 0.8f), 0);

		body.getFixtureList().add(body.createFixture(boxPoly, 1));
		body.getFixtureList().peek().setSensor(true);
		body.getFixtureList().peek().setUserData("up");

		// DOWN
		boxPoly.setAsBox(0.5f, 0.32f, new Vector2(0, -0.8f), 0);

		body.getFixtureList().add(body.createFixture(boxPoly, 1));
		body.getFixtureList().peek().setSensor(true);
		body.getFixtureList().peek().setUserData("down");

		// RIGHT
		boxPoly.setAsBox(0.32f, 0.5f, new Vector2(0.8f, 0), 0);

		body.getFixtureList().add(body.createFixture(boxPoly, 1));
		body.getFixtureList().peek().setSensor(true);
		body.getFixtureList().peek().setUserData("right");

		boxPoly.dispose();
		return body;
	}
	
	
}
