package Game;

import engine.*;
import engine.OpenGL.*;

import static org.lwjgl.opengl.GL11.*;

public class Main {
	public static Main main;
	public static EnigWindow window;
	public static GameView gameView;
	
	public static void main(String[] args) {
		window = new EnigWindow("Ludum Dare 42 game by C1FR1");
		gameView = new GameView(window);
		gameView.runLoop();
	}
}
