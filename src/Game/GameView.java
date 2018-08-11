package Game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;

import static org.lwjgl.opengl.GL11.*;

public class GameView extends EnigView {
	
	public GameView(EnigWindow swindow) {
		super(swindow);
		glDisable(GL_DEPTH_TEST);
	}
	
	@Override
	public boolean loop() {
		//game here
		FBO.prepareDefaultRender();
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
}
