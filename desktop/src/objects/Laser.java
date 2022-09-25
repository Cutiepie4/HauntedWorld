package objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

import main.Boot;
import main.GameScreen;

public class Laser extends Objects {

	private float speed, cnt = 0;
	private float x, y;
	public boolean canRemove, isDisposed;

	public Laser(float x_offset, float y_offset, float angle) {
		super();

		this.x = Boss.INSTANCE.x + x_offset;

		this.y = Boss.INSTANCE.x + y_offset;

		this.canRemove = false;

		this.speed = 5 / Boot.PPM;

		this.animationHandler.add(1 / 4f, "laser", "shoot", "");

		this.animationHandler.setAction("shoot", false);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(60 / Boot.PPM, 8 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) ((float) angle * Math.PI / 180));

		WheelJointDef wjd = new WheelJointDef();

		wjd.bodyA = Boss.INSTANCE.body;

		wjd.bodyB = this.body;

		wjd.collideConnected = false;

		wjd.localAnchorA.set(0, Boss.INSTANCE.height / 2 / Boot.PPM);

		wjd.localAnchorB.set(-52f / Boot.PPM, 0);

		GameScreen.INSTANCE.getWorld().createJoint(wjd);

		this.setTarget();

	}

	public void setTarget() {
//		this.body.setAngularVelocity((float) (Math.PI / 2));
	}

	public void update() {
		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		cnt += 1;
	}

	public void render(SpriteBatch batch) {
		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

//		batch.draw(currentFrame, this.x - this.width / 2, this.y - this.height / 2, currentFrame.getRegionWidth() * 0.75f,
//				currentFrame.getRegionHeight() * 0.75f);

//		batch.draw(currentFrame, this.x, this.y, 50, 0,
//				currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), 0.75f, 0.75f,
//				(float) Math.toDegrees(this.body.getAngle()));

		
		batch.draw(currentFrame, this.x, this.y, 0, -22 / Boot.PPM, currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight(), 0.75f, 0.75f, cnt);

	}

}
