package helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HudActor {

	private Image image;
	private Label label;

	public HudActor(String name, int count) {
		String item_png = String.join("", name.split("\\s+")).toLowerCase();
		this.image = new Image(new Texture(String.format("hud/%s.png", item_png)));
		this.image.setScale(2f);
		this.label = new Label(String.format("%d", count), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.label.scaleBy(2f);
	}

	public Image getImage() {
		return image;
	}

	public Label getLabel() {
		return label;
	}

}
