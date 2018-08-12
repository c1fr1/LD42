package game;

import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;

public class Audio {
	private Sound song;
	private Sound blast;
	private Sound endingChime;
	private Sound menuMusic;
	private SoundSource player;
	private SoundSource[] blastSources = new SoundSource[5];
	private Sound[] contacts;
	private int blastIndex = 0;
	public Audio() {
		contacts = new Sound[5];
		for (int i = 0; i < 5; ++i) {
			contacts[i] = new Sound("res/sfx/hit" + (i + 1) + ".wav");
		}
		song = new Sound("res/sfx/song.wav");
		blast = new Sound("res/sfx/blast.wav");
		endingChime = new Sound("res/sfx/end.wav");
		menuMusic = new Sound("res/sfx/menu.wav");
		player = new SoundSource();
		for (int i = 0; i < 5;++i) {
			blastSources[i] = new SoundSource();
		}
	}
	public void startSong() {
		player.stop();
		player.setLoop();
		player.playSound(song);
	}
	public void stopPlaying() {
		player.pause();
	}
	public void playBlast() {
		blastSources[blastIndex].setPitch((float) Math.random()* 0.2f + 0.9f);
		blastSources[blastIndex].playSound(blast);
		++blastIndex;
		if (blastIndex == 5) {
			blastIndex = 0;
		}
	}
	public void playContact() {
		blastSources[blastIndex].setPitch(2f);
		blastSources[blastIndex].playSound(contacts[(int)(Math.random() * 5)]);
		//blastSources[blastIndex].setPitch(1f);
		++blastIndex;
		if (blastIndex == 5) {
			blastIndex = 0;
		}
	}
	public void playEnd() {
		player.stop();
		player.setNoLoop();
		player.playSound(endingChime);
	}
	public void playMenu() {
		player.stop();
		player.setLoop();
		player.playSound(menuMusic);
	}
}
