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
import things.Chest;
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

		if (fa.getUserData() instanceof String && fb.getUserData() instanceof Enemy && !fb.isSensor()) {
			for (String i : Constants.DIRECTIONS) {
				if (i.equals((String) fa.getUserData())) {
					Player.INSTANCE.getListEnemies((String) fa.getUserData()).add((Enemy) fb.getUserData());
					break;
				}
			}
		}

		if (fa.getUserData().equals("playerbody")) {
			if (fb.getUserData() instanceof Items) {
				Items item = (Items) fb.getUserData();

				if (!item.isLooted()) {
					boolean check = true;

					switch (item.getName()) {

					case "Chest":
						if (!Player.INSTANCE.checkChest()) {
							Hud.INSTANCE.addMessage("Required a Silver Key");
							check = false;
						} else {
							Hud.INSTANCE.addMessage("The Chest is opened");
						}
						break;

					case "Health Potion":
						Player.INSTANCE.heal(3);
						Hud.INSTANCE.addMessage("Your healed 3 HP.");
						break;

					case "Crystal":
						Player.INSTANCE.addInventory(item);
						Hud.INSTANCE.addMessage("You picked up a Crystal.");
						break;

					case "Boot":
						Player.INSTANCE.addSpeed(2);
						Hud.INSTANCE.addMessage("You picked up an old boot, you move faster now.");
						break;

					case "Gold Key":
						Player.INSTANCE.addInventory(item);
						Hud.INSTANCE.addMessage("You got a Gold Key.");
						break;

					case "Silver Key":
						Player.INSTANCE.addInventory(item);
						Hud.INSTANCE.addMessage("You got a Silver Key.");
						break;
					}
					
					if (check)
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

		if (fa.getUserData() instanceof String && fb.getUserData() instanceof Enemy && !fb.isSensor()) {
			for (String i : Constants.DIRECTIONS) {
				if (i.equals((String) fa.getUserData())) {
					Player.INSTANCE.getListEnemies((String) fa.getUserData()).remove((Enemy) fb.getUserData());
					break;
				}
			}
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