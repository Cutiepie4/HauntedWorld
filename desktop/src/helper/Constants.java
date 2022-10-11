package helper;

import java.util.HashMap;

public class Constants {

	public static HashMap<String, Float> FRAME_TIME = new HashMap<>();

	public static String[] ITEMS_STATE = { "idle", "loot" };

	public static String[] ITEMS = { "Silver Key", "Gold Key", "Crystal" };

	public static String[] DECOR = { "water", "bonfire", "torch", "regia", "edge", "waterplant", "greentree",
			"orangetree", "bluehouse", "orangehouse", "doublestonepillar", "leftwoodpillar", "rightwoodpillar",
			"shortstonepillar", "highstonepillar", "door" };

	public static String[] ITEMS_DROP = { "Crystal", "Health Potion" };

	public static void init() {
		FRAME_TIME.put("Chest", 1 / 4f);
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

	public static float getFRAME_TIME(String key) {
		if (FRAME_TIME.containsKey(key)) {
			return FRAME_TIME.get(key);
		}
		return 1f;
	}
}
