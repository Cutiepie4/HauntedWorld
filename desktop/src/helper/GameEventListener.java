package helper;

import com.badlogic.gdx.Gdx;
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
import things.Entity;
import things.Vase;
import ui.Hud;

public class GameEventListener implements ContactListener {

	private float timer = 10f;

	@Override
	public void beginContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null || fa.getUserData() == null || fb.getUserData() == null)
			return;

		if (fa.getUserData() instanceof String) {
			String nameFixtureA = (String) fa.getUserData();
			for (String direction : Constants.DIRECTIONS) {
				if (direction.equals(nameFixtureA)) {
					if (fb.getUserData() instanceof Enemy && !fb.isSensor()) {
						Player.INSTANCE.getListEnemies(direction).add((Enemy) fb.getUserData());
					}

					else if (fb.getUserData().equals("bossbody")) {
						Player.INSTANCE.getListEnemies(direction).add((Enemy) Boss.INSTANCE);
					}

					else if (fb.getUserData() instanceof Vase) {
						Player.INSTANCE.getListInteractiveObjects(direction).add((Vase) fb.getUserData());
					}
				}
			}

			if (nameFixtureA.equals("playerbody")) {
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

						if (check && !item.getName().equals("Vase"))
							item.loot();
					}
				}

				else if (fb.getUserData() instanceof Enemy) {
					if (fb.isSensor()) {
						Enemy enemy = (Enemy) fb.getUserData();
						enemy.detectPlayer();
					}
				}

				else if (fb.isSensor() && (fb.getUserData() instanceof Laser || fb.getUserData() instanceof Bullet)) {
					Player.INSTANCE.isHit((Entity) fb.getUserData());
				}

				else if (fb.getUserData().equals("bossvision")) {
					Boss.INSTANCE.detectPlayer();
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

		if (fa.getUserData() instanceof String) {
			for (String direction : Constants.DIRECTIONS) {
				if (direction.equals((String) fa.getUserData())) {
					if (fb.getUserData() instanceof Enemy && !fb.isSensor()) {
						Player.INSTANCE.getListEnemies(direction).remove((Enemy) fb.getUserData());
					}

					else if (fb.getUserData().equals("bossbody")) {
						Player.INSTANCE.getListEnemies(direction).remove((Enemy) Boss.INSTANCE);
					}

					else if (fb.getUserData() instanceof Vase) {
						Player.INSTANCE.getListInteractiveObjects(direction).remove((Vase) fb.getUserData());
					}
				}
			}

			if (fa.getUserData().equals("playerbody")) {
				if (fb.isSensor() && fb.getUserData() instanceof Enemy) {
					Enemy enemy = (Enemy) fb.getUserData();
					enemy.lostPlayer();
				}

				else if (fb.getUserData().equals("bossvision")) {
					Boss.INSTANCE.lostPlayer();
				}
			}

		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null || fa.getUserData() == null || fb.getUserData() == null)
			return;

		if (fa.getUserData().equals("playerbody")) {
			if (fb.getUserData() instanceof Enemy) {
				timer += Gdx.graphics.getDeltaTime();
				if (timer > 0.5f) {
					Player.INSTANCE.isHit((Enemy) fb.getUserData());
					timer = 0f;
				}
			}

			else if (fb.getUserData() instanceof Trap) {
				timer += Gdx.graphics.getDeltaTime();
				if (timer > 0.5f) {
					Player.INSTANCE.isHit((Entity) fb.getUserData());
					timer = 0f;
				}
			}

			else if (fb.getUserData().equals("bossbody")) {
				timer += Gdx.graphics.getDeltaTime();
				if (timer > 0.5f) {
					Player.INSTANCE.isHit(Boss.INSTANCE);
					timer = 0f;
				}
			}
		}

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
	}
};