package game;

import engine.EnigView;
import engine.OpenGL.FBO;
import engine.OpenGL.Texture;

import static game.Main.*;

public class PauseView extends EnigView {
	public Button continueButton;
	public Button quitButton;
	public Texture backgroundTexture;
	public int buttonPressed = 0;
	public PauseView() {
		super(Main.window);
		continueButton = new Button("res/buttons/playButton.png", 0.6f);
		quitButton = new Button("res/buttons/quitButton.png", -0.4f);
	}
	
	public void runLoop(Texture tex) {
		backgroundTexture = tex;
		continueButton.reset();
		quitButton.reset();
		super.runLoop();
	}
	
	@Override
	public boolean loop() {
		FBO.prepareDefaultRender();
		fboShader.enable();
		backgroundTexture.bind();
		screenVAO.fullRender();
		if (continueButton.render(frameStartTime)) {
			return true;
		}
		if (quitButton.render(frameStartTime)) {
			buttonPressed = 1;
			return true;
		}
		for (int i:UserControls.quit) {
			if (window.keys[i] == 1) {
				return true;
			}
		}
		return false;
	}
}
