package helper;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	protected HashMap<String, Sound> sound;
	protected HashMap<String, Music> music;
	protected float soundVolume;
	protected float musicVolume;
	protected boolean muted;
	public boolean isSoundLooping = false;
	protected long currentID;

	private ArrayList<String> listMusic, listSound;

	public AudioManager() {
		sound = new HashMap<String, Sound>();
		music = new HashMap<String, Music>();
		listSound = new ArrayList<String>();
		listMusic = new ArrayList<String>();
		soundVolume = 1f;
		musicVolume = 1f;
		muted = false;
		currentID = -1;
	}

	public void mute() {
		muted = true;
	}

	public void setSfxVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
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
			currentID = s.loop();
		}
	}

	public void playSound(String name, float pitch) {
		if (!muted) {
			sound.get(name).play(soundVolume, pitch, 0f);
		}
	}

	/**
	 * Plays a song and stops any other currently playing, if this is the current
	 * song playing it will just continue
	 * 
	 * @param name
	 * @param loop
	 */
	public void playMusic(String name, boolean loop) {
		if (!muted) {
			stopAllSongs();
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

	public void stopAllSongs() {
		for (Music song : music.values()) {
			if (song.isPlaying()) {
				song.stop();
			}
		}
	}

	public void dispose() {

	}
}