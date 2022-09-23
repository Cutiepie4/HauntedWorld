package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Boots extends Objects {

	public Boots(float width, float height, Body body) {
		super(width, height, body);
		this.sprite = new Sprite(new Texture("props/boots.png"));
		this.sprite.setBounds(x - width / 2, y - height / 2 , this.width, this.height);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {

		this.sprite.draw(batch);
	}
}
