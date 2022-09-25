package objects;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import helper.AnimationHandler;
import main.Boot;
import main.Hud;

public class Player extends Objects {

	public static Player INSTANCE;
	public static final int MAX_HEALTH = 10;
	private HashMap<String, HashSet<Enemy>> listEnemies;
	private HashMap<String, Integer> inventory;
	private int health;
	private int damage;

	public Player(float width, float height, Body body) {
		super(width, height, body);

		INSTANCE = this;
		this.speed = 4f;
		this.listEnemies = new HashMap<>();
		this.inventory = new HashMap<>();
		this.health = 9;
		this.damage = 1;
		this.FRAME_TIME = 1 / 8f;

		String[] directions = { "up", "down", "left", "right" };
		for (int i = 0; i < 4; i++) {
			this.listEnemies.put(directions[i], new HashSet<>());
			this.animationHandler.add(1 / 20f, "players", "attack", directions[i]);
			this.animationHandler.add(FRAME_TIME, "players", "run", directions[i]);
			this.animationHandler.add(1 / 5f, "players", "idle", directions[i]);
			this.animationHandler.add(1 / 12f, "players", "hit", directions[i]);
		}

		this.animationHandler.add(FRAME_TIME, "players", "dead", "");

		animationHandler.setActionDirection("idle", "down", true);

		this.body.setLinearDamping(10f);

	}

	@Override
	public void update() {
		if (this.animationHandler.getAction().equals("dead"))
			return;

		this.x = this.body.getPosition().x * Boot.PPM;
		this.y = this.body.getPosition().y * Boot.PPM;

		checkUserInput();
	}

	@Override
	public void render(SpriteBatch batch) {

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
			check = false;
			velX = 1;
			this.animationHandler.setDirection("right");
			animationHandler.setActionDirection("run", "right", true);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			check = false;
			velX = -1;
			this.animationHandler.setDirection("left");
			animationHandler.setActionDirection("run", "left", true);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			check = false;
			velY = 1;
			this.animationHandler.setDirection("up");
			animationHandler.setActionDirection("run", "up", true);

		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			check = false;
			velY = -1;
			this.animationHandler.setDirection("down");
			animationHandler.setActionDirection("run", "down", true);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			check = false;
			animationHandler.setAction("attack", false);

			this.attack();

			Boss.INSTANCE.shoot();
			
			
		}

		if (check) {
			this.animationHandler.setAction("idle", true);
		}

		this.body.setLinearVelocity(velX * speed, velY * speed);
	}

	public void attack() {

		for (Enemy i : listEnemies.get(this.animationHandler.getDirection())) {
			i.isHit(this);
		}
	}

	public void isHit(Enemy enemy) {

		if (this.animationHandler.getAction().equals("dead") || enemy.getAnimationHandler().getAction().equals("dead"))
			return;

		this.health -= enemy.getDamage();

		if (this.health > 0) {
			this.animationHandler.setAction("hit", false);
		}

		else {
			this.animationHandler.setAction("dead", false);
			this.animationHandler.setDirection("");
		}
	}

	public void addInventory(Items item) {
		if (!this.inventory.containsKey(item.name)) {
			this.inventory.put(item.name, 0);
		}
		this.inventory.put(item.name, this.inventory.get(item.name) + 1);
		Hud.INSTANCE.printMessage(String.format("You picked up a %s.", item.name));
	}

	public HashMap<String, Integer> getInventory() {
		return this.inventory;
	}

	public HashSet<Enemy> getListEnemies(String direction) {
		return this.listEnemies.get(direction);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setSpeed(int add) {
		this.speed += add;
		Hud.INSTANCE.printMessage("You got an Old Boots, you can move faster now.");
	}

	public void heal(int hp) {
		this.health += hp;
		Hud.INSTANCE.printMessage(String.format("You healed %d health point.", hp));
	}

	public int getHealth() {
		if (this.health > MAX_HEALTH)
			return MAX_HEALTH;
		return health;
	}

	public boolean checkChest(Chest chest) {
		if (!this.inventory.containsKey("Silver Key") || this.inventory.get("Silver Key") < 1) {
			Hud.INSTANCE.printMessage("You don't have a Silver Key");
			return false;
		}

		this.inventory.put("Silver Key", this.inventory.get("Silver Key") - 1);
		chest.loot();
		Hud.INSTANCE.printMessage("You opened the Chest");
		return true;
	}

	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}
}
