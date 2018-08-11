package game;

import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;

public class Audio {
	private Sound song;
	private SoundSource player;
	public Audio() {
		song = new Sound("res/ld42SongIGuess.wav");
		player = new SoundSource();
		player.setLoop();
	}
	public void startPlaying() {
		player.playSound(song);
	}
	public void stopPlaying() {
		player.pause();
	}
}
