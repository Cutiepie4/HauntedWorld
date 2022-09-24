package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import main.Boot;

public class HealthPotion extends Items {

	public HealthPotion(float width, float height, Body body) {
		super(width, height, body);

		this.name = "Health Potion";

		this.sprite = new Sprite(new Texture("props/potion.png"));

		this.sprite.setBounds(this.x - this.width / 2, this.y - this.height / 2, this.width, this.height);
	}

	@Override
	public void update() {

	}

	public void render(SpriteBatch batch) {
		this.sprite.draw(batch);
	}

}
