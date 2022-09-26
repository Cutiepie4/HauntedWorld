package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

import main.Boot;
import main.GameScreen;

public class Laser extends Objects {

	private float speed;
	private float x, y;
	public boolean canRemove, isDisposed;
	private float angle;

	public Laser(float x_offset, float y_offset, float angle) {
		super();

		this.x = Boss.INSTANCE.x + x_offset;

		this.y = Boss.INSTANCE.x + y_offset;
		
		this.angle = angle;

		this.canRemove = false;

		this.speed = 5 / Boot.PPM;

		this.animationHandler.add(1 / 6f, "laser", "shoot", "");

		this.animationHandler.setAction("shoot", false);

		this.createLaserHitBox();

		this.createJoint();
		
		this.setTarget();

	}

	public void createLaserHitBox() {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(98 / Boot.PPM, 6 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) ((float) angle * Math.PI / 180));
	}

	public void createJoint() {

		WeldJointDef jointDef = new WeldJointDef();

		jointDef.bodyA = Boss.INSTANCE.body;

		jointDef.bodyB = this.body;

		jointDef.collideConnected = true;

		jointDef.localAnchorA.set(0, Boss.INSTANCE.height / 2 / Boot.PPM);

		jointDef.localAnchorB.set(-90f / Boot.PPM, 0);

		GameScreen.INSTANCE.getWorld().createJoint(jointDef);
		
	}

	public void setTarget() {
		this.body.setAngularVelocity((float) (Math.PI / 4));
	}

	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

	}

	public void render(SpriteBatch batch) {

		TextureRegion currentFrame = this.animationHandler.getFrame();

		batch.draw(currentFrame, Boss.INSTANCE.x - 22, Boss.INSTANCE.y - 8, 22, 22, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight(), 0.75f, 0.75f, (float) Math.toDegrees(this.body.getAngle()));

	}

}
