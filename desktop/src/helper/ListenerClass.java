package helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import character.Boss;
import character.Bullet;
import character.Enemy;
import character.Laser;
import character.Player;
import character.Spider;
import character.Spinner;
import character.Trap;
import screen.GameScreen;
import things.Items;
import things.Objects;
import ui.Hud;

public class ListenerClass implements ContactListener {

	private GameScreen gc = GameScreen.INSTANCE;

	@Override
	public void beginContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null || fa.getUserData() == null || fb.getUserData() == null)
			return;

		if ((fa.getUserData().equals("up") || fa.getUserData().equals("down") || fa.getUserData().equals("left")
				|| fa.getUserData().equals("right")) && fb.getUserData() instanceof Enemy && !fb.isSensor()) {
			Player.INSTANCE.getListEnemies((String) fa.getUserData()).add((Enemy) fb.getUserData());
			return;
		}

		if (fa.getUserData().equals("playerbody")) {
			if (fb.getUserData() instanceof Items) {
				Items item = (Items) fb.getUserData();
				boolean check = true;

				switch (item.getName()) {
				case "Chest":
					if (!Player.INSTANCE.checkChest()) {
						Hud.INSTANCE.printMessage("You don't have a Silver Key");
						check = false;
					} else {
						Hud.INSTANCE.printMessage("You opened the Chest");
					}
					break;

				case "Health Potion":
					Player.INSTANCE.heal(3);
					Hud.INSTANCE.printMessage("Your healed 3 HP.");
					break;

				case "Crystal":
					Player.INSTANCE.addInventory(item);
					Hud.INSTANCE.printMessage("You picked up a Crystal.");
					break;

				case "Boot":
					Player.INSTANCE.addSpeed(2);
					Hud.INSTANCE.printMessage("You picked up an old boot, you move faster now.");
					break;

				case "Gold Key":
					Player.INSTANCE.addInventory(item);
					Hud.INSTANCE.printMessage("You got a Gold Key.");
					break;

				case "Silver Key":
					Player.INSTANCE.addInventory(item);
					Hud.INSTANCE.printMessage("You got a Silver Key.");
					break;
				}

				if (check) {
					item.loot();
				}

			}

			if (fb.getUserData() instanceof Spinner || fb.getUserData() instanceof Spider) {
				Enemy enemy = (Enemy) fb.getUserData();
				if (fb.isSensor()) {
					enemy.detectPlayer();

				} else {
					Player.INSTANCE.isHit(enemy);
				}
			}

			if (fb.getUserData() instanceof Laser || fb.getUserData() instanceof Bullet
					|| (fb.getUserData() instanceof Trap && !fb.isSensor())) {
				Player.INSTANCE.isHit(((Objects) fb.getUserData()).getDamage());
				return;
			}

			if (fb.getUserData() instanceof String) {
				switch ((String) fb.getUserData()) {
				case "bossvision":
					Boss.INSTANCE.detectPlayer();
					break;
				case "bossbody":
					Player.INSTANCE.isHit(5);
					break;
				}
			}

		}

	}

	@Override
	public void endContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null || fa.getUserData() == null || fb.getUserData() == null)
			return;

		if (fa.getUserData() instanceof String && !fa.getUserData().equals("playerbody")
				&& fb.getUserData() instanceof Enemy && !fb.isSensor()) {
			Player.INSTANCE.getListEnemies((String) fa.getUserData()).remove((Enemy) fb.getUserData());
		}

		if (fa.getUserData().equals("playerbody")) {
			if (fb.isSensor() && fb.getUserData() instanceof Enemy) {
				Enemy enemy = (Enemy) fb.getUserData();
				enemy.lostPlayer();
			}

			if (fb.getUserData().equals("bossvision")) {
				Boss.INSTANCE.lostPlayer();
			}
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
	}
};