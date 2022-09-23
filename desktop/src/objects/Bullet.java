package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import main.Boot;
import main.GameScreen;

public class Bullet extends Objects {

	private float speed;
	private Sprite sprite;
	private float x, y;
	public boolean canRemove, isDisposed;

	public Bullet(float x, float y) {
		super(x, y);

		this.canRemove = false;

		this.isDisposed = false;

		this.x = x;

		this.y = y;

		this.speed = 10;

		sprite = new Sprite(new Texture(Gdx.files.internal("hud/bullet.png")));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.position.set(x / Boot.PPM, y / Boot.PPM);

		this.body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);

		this.body.setBullet(true);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(2 / Boot.PPM, 4 / Boot.PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();

//		this.setTarget();
	}

	public void setTarget() {
		this.body.setLinearVelocity(new Vector2(this.x - Player.INSTANCE.getBody().getPosition().x ,
				this.y - Player.INSTANCE.getBody().getPosition().y).scl((float) 1 / 10 / Boot.PPM));
	}

	public void update(float delta) {

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		if (y > 50 * Boot.PPM || x > 50 * Boot.PPM || x < 0 || y < 0)
			this.canRemove = true;

		if (this.isDisposed) {
			this.x = 1000;
			this.y = 1000;
		}
		this.sprite.setPosition(x, y);
	}

	public void render(SpriteBatch batch) {

		update(speed);

		sprite.draw(batch);
	}

}
