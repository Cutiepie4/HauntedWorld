package helper;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HudObjectsHelper {

	protected Image image;
	protected int count;
	protected Label label;

	public HudObjectsHelper(Image image, Label label) {
		super();
		this.image = image;
		this.image.setScale(2f);
		this.count = 0;
		this.label = label;
	}

	public void setCount(int count) {
		this.count = count;
		this.label.setText(String.format("%d", count));
	}

	public Image getImage() {
		return image;
	}

	public int getCount() {
		return count;
	}

	public Label getLabel() {
		return label;
	}
	
	
}
