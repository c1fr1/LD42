package Game;

import engine.EnigView;
import engine.OpenGL.EnigWindow;
import engine.OpenGL.FBO;

import static org.lwjgl.opengl.GL11.*;
import static Game.Main.*;

public class GameView extends EnigView {
	public GameView(EnigWindow swindow) {
		super(swindow);
	}
	
	
	@Override
	public boolean loop() {
		//game here
		FBO.prepareDefaultRender();
		Entity.entityVAO.prepareRender();
		player.render(frameStartTime);
		Entity.entityVAO.unbind();
		player.updatePosition(deltaTime);
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
}
