package helper;

import java.util.HashMap;

public class Constants {

	public static HashMap<String, Float> FRAME_TIME = new HashMap<>();

	public static HashMap<String, Float[]> OFFSET = new HashMap<>();

	public static String[] ACTION = { "idle", "dead", "hit", "attack" };

	public static String[] ITEMS_STATE = { "idle", "loot" };

	public static String[] ITEMS = { "Silver Key", "Gold Key", "Crystal" };

	public static String[] DECOR = { "water", "bonfire", "torch", "regia", "edge", "waterplant", "greentree",
			"orangetree", "bluehouse", "orangehouse", "doublestonepillar", "leftwoodpillar", "rightwoodpillar",
			"shortstonepillar", "highstonepillar", "bluehouse" };

	public static String[] ITEMS_DROP = { "Crystal", "Health Potion" };

	public static String[] DIRECTIONS = { "down", "up", "left", "right" };

	public static void init() {
		initOffSet();
		FRAME_TIME.put("Chest", 1 / 4f);
		FRAME_TIME.put("Vase", 1 / 6f);
		FRAME_TIME.put("Silver Key", 1 / 8f);
		FRAME_TIME.put("Gold Key", 1 / 8f);
		FRAME_TIME.put("Crystal", 1 / 8f);
		FRAME_TIME.put("water", 1 / 6f);
		FRAME_TIME.put("bonfire", 1 / 8f);
		FRAME_TIME.put("torch", 1 / 8f);
		FRAME_TIME.put("regia", 1 / 4f);
		FRAME_TIME.put("edge", 1 / 4f);
		FRAME_TIME.put("waterplant", 1 / 6f);
		FRAME_TIME.put("door", 1 / 6f);
	}

	private static void initOffSet() {
		OFFSET.put("orangehouse", new Float[] { 0f, 16f });
		OFFSET.put("bluehouse", new Float[] { 0f, 16f });
		OFFSET.put("greentree", new Float[] { 0f, 12f });
		OFFSET.put("orangetree", new Float[] { 0f, 12f });
		OFFSET.put("highstonepillar", new Float[] { 0f, 10f });
		OFFSET.put("doublestonepillar", new Float[] { 0f, 18f });
		OFFSET.put("leftwoodpillar", new Float[] { 12f, 12f });
		OFFSET.put("rightwoodpillar", new Float[] { -12f, 12f });
		OFFSET.put("shortstonepillar", new Float[] { 0f, 10f });
		OFFSET.put("bonfire", new Float[] { 0f, 2f });
		OFFSET.put("torch", new Float[] { 0f, 2f });
		OFFSET.put("door", new Float[] { 18f, 0f });
	}

	public static float getFRAME_TIME(String key) {
		if (FRAME_TIME.isEmpty())
			Constants.init();
		if (FRAME_TIME.containsKey(key)) {
			return FRAME_TIME.get(key);
		}
		return 1f;
	}

	public static Float[] getOFFSET(String name) {
		if (OFFSET.containsKey(name))
			return OFFSET.get(name);
		return new Float[] { 0f, 0f };
	}

}
