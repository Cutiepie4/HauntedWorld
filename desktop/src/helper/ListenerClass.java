package helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import main.GameScreen;
import objects.Boots;
import objects.Bullet;
import objects.Chest;
import objects.Enemy;
import objects.HealthPotion;
import objects.Items;
import objects.Laser;
import objects.Objects;
import objects.Player;
import objects.Spinner;
import objects.Trap;

public class ListenerClass implements ContactListener {

	private GameScreen gc = GameScreen.INSTANCE;

	@Override
	public void beginContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null || fa.getUserData() == null || fb.getUserData() == null)
			return;

		if (fa.getUserData() instanceof String && !fa.getUserData().equals("playerbody")
				&& fb.getUserData() instanceof Enemy && !fb.isSensor()) {
			Player.INSTANCE.getListEnemies((String) fa.getUserData()).add((Enemy) fb.getUserData());
		}

		if (fa.getUserData().equals("playerbody")) {
			if (fb.isSensor() && fb.getUserData() instanceof Items) {
				Items item = (Items) fb.getUserData();
				item.loot();
				Player.INSTANCE.addInventory(item);
			}

			if (fb.getUserData() instanceof Chest) {
				Player.INSTANCE.checkChest((Chest) fb.getUserData());
			}

			if (fb.getUserData() instanceof Boots) {
				Player.INSTANCE.setSpeed(2);
				gc.addToRemove((Objects) fb.getUserData());
			}

			if (fb.getUserData() instanceof HealthPotion) {
				Player.INSTANCE.heal(3);
				gc.addToRemove((Objects) fb.getUserData());
			}

			if (fb.getUserData() instanceof Spinner) {
				Enemy enemy = (Enemy) fb.getUserData();
				if (fb.isSensor()) {
					enemy.setTarget(Player.INSTANCE);
				} else {
					Player.INSTANCE.isHit(enemy);
				}
			}

			if (fb.getUserData() instanceof Laser || fb.getUserData() instanceof Bullet
					|| (fb.getUserData() instanceof Trap && !fb.isSensor())) {
				Player.INSTANCE.isHit(((Objects) fb.getUserData()).getDamage());
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
				enemy.setTarget(null);
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