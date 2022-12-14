package character;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import controller.AudioManager;
import model.Enemy;
import model.Entity;
import model.Items;
import screen.GameScreen;
import ui.Hud;

public class Player extends Entity {

	public static Player INSTANCE;
	public static final float MAX_HEALTH = 20f;
	private LinkedHashMap<String, HashSet<Enemy>> listEnemies;
	private LinkedHashMap<String, HashSet<Vase>> listInteractiveObjects;
	private LinkedHashMap<String, Integer> inventory;
	private float health;

	public Player(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;
		this.speed = 5f;
		this.listEnemies = new LinkedHashMap<>();
		this.listInteractiveObjects = new LinkedHashMap<>();
		this.inventory = new LinkedHashMap<>();
		this.health = 20;
		this.FRAME_TIME = 1 / 8f;
		this.damage = 2f;

		String[] directions = { "up", "down", "left", "right" };
		for (String i : directions) {
			this.listEnemies.put(i, new HashSet<>());
			this.listInteractiveObjects.put(i, new HashSet<>());
			this.animationHandler.add(1 / 20f, "players", "attack", i);
			this.animationHandler.add(1 / 8f, "players", "run", i);
			this.animationHandler.add(1 / 5f, "players", "idle", i);
			this.animationHandler.add(1 / 12f, "players", "hit", i);
		}

		this.animationHandler.add(FRAME_TIME, "players", "dead", "");

		this.animationHandler.setActionDirection("idle", "down", true);

		this.body.setLinearDamping(10f);

	}

	@Override
	public void update() {
		if (this.animationHandler.getAction().equals("dead")) {
			AudioManager.INSTANCE.stopSound("footstep");
			return;
		}

		super.update();

		checkUserInput();
	}

	@Override
	public void render(SpriteBatch batch) {
		update();

		if (this.animationHandler.getAction().equals("dead") && this.animationHandler.isAnimationFinished())
			return;

		TextureRegion currentFrame = animationHandler.getFrame();

		batch.draw(currentFrame, this.x - this.width / 2 - 16, this.y - this.height / 2 - 14,
				currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

	}

	private void checkUserInput() {

		String[] state = { "dead", "attack", "hit" };

		for (int i = 0; i < state.length; i++) {
			if (this.animationHandler.getAction().equals(state[i]) && !this.animationHandler.isAnimationFinished()) {
				return;
			}
		}

		boolean check = true;
		velX = 0;
		velY = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			AudioManager.INSTANCE.playSound("footstep", true);
			check = false;
			velX = 1;
			this.animationHandler.setDirection("right");
			animationHandler.setActionDirection("run", "right", true);
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			AudioManager.INSTANCE.playSound("footstep", true);
			check = false;
			velX = -1;
			this.animationHandler.setDirection("left");
			animationHandler.setActionDirection("run", "left", true);
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			AudioManager.INSTANCE.playSound("footstep", true);
			check = false;
			velY = 1;
			this.animationHandler.setDirection("up");
			animationHandler.setActionDirection("run", "up", true);
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			AudioManager.INSTANCE.playSound("footstep", true);
			check = false;
			velY = -1;
			this.animationHandler.setDirection("down");
			animationHandler.setActionDirection("run", "down", true);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			check = false;
			animationHandler.setAction("attack", false);

			this.attack();
		}

		this.body.setLinearVelocity(velX * speed, velY * speed);
		
		if (check) {
			AudioManager.INSTANCE.stopSound("footstep");
			this.animationHandler.setAction("idle", true);
		}

	}

	private void attack() {
		boolean whoosh = true;

		HashSet<Vase> temp = new HashSet<>();
		for (Vase i : listInteractiveObjects.get(this.animationHandler.getDirection())) {
			if (!i.getAnimationHandler().getAction().equals("loot")) {
				i.loot();
				whoosh = false;
			}

			else {
				temp.add(i);
			}
		}

		listInteractiveObjects.put(this.animationHandler.getDirection(), temp);

		for (Enemy i : listEnemies.get(this.animationHandler.getDirection())) {
			i.isHit(this);
			whoosh = false;
		}

		if (whoosh)
			AudioManager.INSTANCE.playSound("whoosh");
	}

	public void isHit(Entity entity) {

		if (this.animationHandler.getAction().equals("dead") || entity.getAnimationHandler().getAction().equals("dead"))
			return;

		AudioManager.INSTANCE.playSound("ishit");
		this.health -= entity.getDamage();

		Hud.INSTANCE.addMessage(String.format("You were hit %.0f damage", entity.getDamage()));

		if (this.health > 0) {
			this.animationHandler.setAction("hit", false);
		}

		else {
			this.animationHandler.setAction("dead", false);
			this.animationHandler.setDirection("");
		}
	}

	public void addInventory(Items item) {
		if (!item.isLooted()) {
			if (!this.inventory.containsKey(item.getName())) {
				this.inventory.put(item.getName(), 0);
			}
			this.inventory.put(item.getName(), this.inventory.get(item.getName()) + 1);
		}
	}

	public HashMap<String, Integer> getInventory() {
		return this.inventory;
	}

	public HashSet<Enemy> getListEnemies(String direction) {
		return this.listEnemies.get(direction);
	}

	public HashSet<Vase> getListInteractiveObjects(String direction) {
		return this.listInteractiveObjects.get(direction);
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void addSpeed(int add) {
		this.speed += add;
	}

	public void heal(float hp) {
		this.health += hp;
		this.health = this.getHealth();
	}

	public float getHealth() {
		if (this.health > MAX_HEALTH)
			return MAX_HEALTH;
		return health;
	}

	public boolean checkChest() {
		if (!this.inventory.containsKey("Silver Key") || this.inventory.get("Silver Key") < 1) {
			AudioManager.INSTANCE.playSound("cancel");
			return false;
		}

		this.inventory.put("Silver Key", this.inventory.get("Silver Key") - 1);
		return true;
	}
	
	public boolean checkDoor() {
		if (!this.inventory.containsKey("Gold Key") || this.inventory.get("Gold Key") < 2) {
			AudioManager.INSTANCE.playSound("cancel");
			return false;
		}

		this.inventory.put("Gold Key", this.inventory.get("Gold Key") - 2);
		return true;
	}
	
	public boolean checkGate() {
		if (!this.inventory.containsKey("Silver Key") || this.inventory.get("Silver Key") < 2) {
			AudioManager.INSTANCE.playSound("cancel");
			return false;
		}

		this.inventory.put("Silver Key", this.inventory.get("Silver Key") - 2);
		return true;
	}

//	public void revive() {
//		this.health = Player.MAX_HEALTH;
//		this.animationHandler.setAction("idle", true);
//	}
}
