package character;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import helper.BodyHelperService;
import main.Boot;
import screen.GameScreen;
import things.Entity;

public class Laser extends Entity {

	private float angle;
	
	public Laser(float x_offset, float y_offset, float angle) {
		super();
		
		this.x = Boss.INSTANCE.getX() + x_offset;
		
		this.y = Boss.INSTANCE.getY() + y_offset;
		
		this.damage = 5f;
		
		this.angle = angle;
		
		this.speed = (float) (Math.PI / 2f);
		
		this.animationHandler.add(1 / 10f, "laser", "shoot", "");
		
		this.animationHandler.setAction("shoot", false);
		
		this.createLaserHitBox();
		
		this.createJoint();
	}

	private void createLaserHitBox() {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.position.set(this.x, this.y);
		
		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.setBullet(true);
		PolygonShape shape = new PolygonShape();
		// size 72 x 6
		shape.setAsBox(72 / Boot.PPM, 6 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		this.body.createFixture(fixtureDef).setUserData(this);
		this.body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) (angle * Math.PI / 180f));
		shape.dispose();
	}

	private void createJoint() {

		WeldJointDef jointDef = new WeldJointDef();

		jointDef.bodyA = Boss.INSTANCE.getBody();

		jointDef.bodyB = this.body;

		jointDef.collideConnected = true;

		jointDef.localAnchorA.set(0, Boss.INSTANCE.getHeight() / 2 / Boot.PPM);

		jointDef.localAnchorB.set(-64f / Boot.PPM, 0);

		GameScreen.INSTANCE.getWorld().createJoint(jointDef);
	}

	private void rotate() {
		if (this.body.getAngularVelocity() == 0) {
			int[] clockwise = new int[] { -1, 1 };
			int rnd = new Random().nextInt(2);
			this.body.setAngularVelocity(clockwise[rnd] * speed);
		}
	}

	public void update() {
		if (this.isDisposed())
			return;

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		if (this.animationHandler.isAnimationFinished()) {
			this.rotate();
		}

		if (this.animationHandler.getStateTime() >= 5.4f && !this.isDisposed()) {
			GameScreen.INSTANCE.addToRemove(this);
			this.setDisposed(true);
			Boss.INSTANCE.getAnimationHandler().setAction("idle", true);
			Boss.INSTANCE.isLasering = false;
		}

	}

	public void render(SpriteBatch batch) {

		update();

		if (this.isDisposed())
			return;

		TextureRegion currentFrame = this.animationHandler.getFrame();

		currentFrame.setRegionWidth(200);

		batch.draw(currentFrame, Boss.INSTANCE.getY() - 22, Boss.INSTANCE.getY() - 8, 22, 22,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), 0.75f, 0.75f,
				(float) Math.toDegrees(this.body.getAngle()));

	}

}
