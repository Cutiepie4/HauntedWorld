package helper;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	public static AudioManager INSTANCE;
	protected HashMap<String, Sound> sound;
	protected HashMap<String, Music> music;
	protected float soundVolume;
	protected float musicVolume;
	protected boolean muted;
	public boolean isSoundLooping = false;
	protected long currentID;
	protected String currentSong;

	private ArrayList<String> listMusic, listSound;

	public AudioManager() {
		INSTANCE = this;
		sound = new HashMap<String, Sound>();
		music = new HashMap<String, Music>();
		listSound = new ArrayList<String>();
		listMusic = new ArrayList<String>();
		soundVolume = 0.5f;
		musicVolume = 1f;
		muted = false;
		currentID = -1;
		init();
	}

	private void init() {
		// MUSIC
		this.addMusic("audio/music/MainScreenMusic.ogg");
		this.addMusic("audio/music/overworld.mp3");
		this.addMusic("audio/music/dungeon.mp3");

		// SOUND
		this.addSound("audio/sound/player/footstep.ogg");
		this.addSound("audio/sound/player/slash.wav");
		this.addSound("audio/sound/player/whoosh.wav");
		this.addSound("audio/sound/player/slashboss.wav");
		this.addSound("audio/sound/player/ishit.mp3");

		this.addSound("audio/sound/boss/missle.wav");
		this.addSound("audio/sound/boss/deadboss.mp3");
		this.addSound("audio/sound/boss/laser.mp3");
		this.addSound("audio/sound/boss/spiketrap.ogg");
		this.addSound("audio/sound/boss/trap.wav");
		this.addSound("audio/sound/items/click.wav");
		this.addSound("audio/sound/items/chestopen.wav");

		this.addSound("audio/sound/items/loot.wav");

		this.addSound("audio/sound/items/break.mp3");

		this.addSound("audio/sound/door/open.ogg");

		this.load();
	}

	public void mute() {
		muted = !muted;
		if (muted) {
			this.pauseMusic(currentSong);
		} else {
			this.resumeMusic(currentSong);
		}

	}

	public void setSfxVolume(int soundVolume) {
		this.soundVolume = soundVolume / 100f;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume / 100f;
		for (Music i : music.values()) {
			if (i.isPlaying()) {
				i.setVolume(this.musicVolume);
				break;
			}
		}
	}

	public void addSound(String name) {
		listSound.add(name);
	}

	public void addMusic(String name) {
		listMusic.add(name);
	}

	private String getNameWithoutPath(String path) {
		String nameWithoutPath = path.substring(0, path.indexOf("."));
		nameWithoutPath = nameWithoutPath.substring(nameWithoutPath.lastIndexOf("/") + 1);
		return nameWithoutPath;
	}

	public void load() {
		for (String name : listSound) {
			sound.put(getNameWithoutPath(name), Gdx.audio.newSound(Gdx.files.internal(name)));
		}
		for (String name : listMusic) {
			music.put(getNameWithoutPath(name), Gdx.audio.newMusic(Gdx.files.internal(name)));
		}
	}

	// SOUND
	public void playSound(String name) {
		if (!muted) {
			sound.get(name).play(soundVolume);
		}
	}

	public void stopSound(String name) {
		if (isSoundLooping) {
			Sound s = sound.get(name);
			s.stop(currentID);
			isSoundLooping = false;
		}
	}

	public void playSound(String name, boolean loop) {
		if (!muted && !isSoundLooping) {
			Sound s = sound.get(name);
			isSoundLooping = true;
			currentID = s.loop(soundVolume);
		}
	}

	public void playSound(String name, float pitch) {
		if (!muted) {
			sound.get(name).play(soundVolume, pitch, 0f);
		}
	}

	public void playMusic(String name, boolean loop) {
		if (!muted) {
			this.currentSong = name;
			this.stopMusic();
			music.get(name).setVolume(musicVolume);
			music.get(name).setLooping(loop);
			music.get(name).play();
		}
	}

	public void pauseMusic(String name) {
		music.get(name).pause();
	}

	public void stopMusic(String name) {
		music.get(name).stop();
	}

	public void stopMusic() {
		for (Music song : music.values()) {
			if (song.isPlaying()) {
				song.stop();
			}
		}
	}

	public void resumeMusic(String name) {
		music.get("overworld").play();
	}

	public int getMusicVolume() {
		return (int) (musicVolume * 100);
	}

	public int getSoundVolume() {
		return (int) (soundVolume * 100);
	}
}