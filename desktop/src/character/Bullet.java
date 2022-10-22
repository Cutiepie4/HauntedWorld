package character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import main.Boot;
import screen.GameScreen;
import things.Entity;

public class Bullet extends Entity {

	public boolean canRemove = false;
	private float angle;

	public Bullet(float x_offset, float y_offset, float angle) {
		super();

		this.name = "Bullet";

		this.x = Boss.INSTANCE.getX() + x_offset;

		this.y = Boss.INSTANCE.getY() + y_offset;

		this.angle = angle;

		this.damage = 2;

		this.speed = 2f / Boot.PPM;

		this.animationHandler.add(1 / 8f, "bullet", "shoot", "");

		this.animationHandler.setAction("shoot", true);

		this.createBulletHitBox(angle);

	}

	private void createBulletHitBox(float angle) {

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.bullet = true;
		bodyDef.position.set(this.x, this.y);

		PolygonShape shape = new PolygonShape();
		// size 12 x 4
		shape.setAsBox(12 / Boot.PPM, 4 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.isSensor = true;
		fixtureDef.density = 1000;
		fixtureDef.shape = shape;
		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.createFixture(fixtureDef);
		this.body.setUserData(this);
		this.body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM),
				(float) ((float) angle * Math.PI / 180));
		shape.dispose();
		
	}

	@Override
	public void update() {

		super.update();

		this.body.setLinearVelocity(
				new Vector2(this.x - Boss.INSTANCE.getX(), this.y - Boss.INSTANCE.getY()).scl(speed));
	}

	@Override
	public void render(SpriteBatch batch) {
		if (this.canRemove)
			return;

		update();

		TextureRegion currentFrame = this.animationHandler.getFrame();

		float posX = this.x, posY = this.y;

		if (angle == 0) {
			posX -= 18;
			posY -= 9;
		}

		if (angle == 180) {
			posY -= 9;
		}

		if (angle == 90) {
			posX -= 9;
			posY -= 18;
		}

		if (angle == -90) {
			posX -= 9;
		}

		if (angle == 45) {
			posX -= 16;
			posY -= 16;
		}

		if (angle == -45) {
			posX -= 16;
			posY -= 2;
		}

		if (angle == 135) {
			posX -= 2;
			posY -= 16;
		}

		if (angle == -135) {
			posX -= 2;
			posY -= 2;
		}

		batch.draw(currentFrame, posX, posY, 9f, 9f, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
				0.8f, 0.8f, angle);

	}

}
