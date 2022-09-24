package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import main.Boot;
import main.GameScreen;

public class Bullet extends Objects {

	private Vector2 center;
	private float speed;
	private Sprite sprite;
	private float x, y;
	public boolean canRemove;

	public Bullet(float x_offset, float y_offset, float angle) {
		super();

		this.x = Boss.INSTANCE.x + x_offset;

		this.y = Boss.INSTANCE.x + y_offset;

		this.canRemove = false;

		this.speed = 5 / Boot.PPM;

		sprite = new Sprite(new Texture(Gdx.files.internal("hud/bullet.png")));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
		this.body.setBullet(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(12 / Boot.PPM, 3 / Boot.PPM);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

		body.setTransform(new Vector2(this.x / Boot.PPM, this.y / Boot.PPM), (float) ((float) angle * Math.PI / 180));

		this.setTarget();
		sprite.rotate(angle);
		sprite.setScale(0.8f);
	}

	public void setTarget() {
		System.out.println(this.x + " " + this.y);
		System.out.println(Boss.INSTANCE.x + " " + Boss.INSTANCE.y);
		this.body.setLinearVelocity(new Vector2(this.x - Boss.INSTANCE.x, this.y - Boss.INSTANCE.y).scl(speed));
	}

	public void update() {

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

//		System.out.println(x + " " + y);

//		if (y > 50 * Boot.PPM || x > 50 * Boot.PPM || x < 0 || y < 0)
//			this.canRemove = true;

//		if (this.isDisposed) {
//			this.x = 1000;
//			this.y = 1000;
//		}

		this.sprite.setPosition(x, y);
	}

	public void render(SpriteBatch batch) {

		update();

		sprite.draw(batch);
	}

}
