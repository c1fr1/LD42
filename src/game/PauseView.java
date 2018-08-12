package game;

import engine.EnigView;
import engine.OpenGL.FBO;

public class PauseView extends EnigView {
	public Button testButton;
	public PauseView() {
		super(Main.window);
		testButton = new Button("res/buttons/continueButton.png", 0);
	}
	@Override
	public boolean loop() {
		FBO.prepareDefaultRender();
		if (testButton.render(frameStartTime)) {
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
