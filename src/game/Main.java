package game;

import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Main {
	public static Main main;
	public static EnigWindow window;
	public static GameView gameView;
	public static Player player;
	public static Vector2f cameraPos = new Vector2f();
	public static VAO screenVAO;
	public static Audio song;
	private static Matrix4f perspectiveMatrix;
	
	public static void main(String[] args) {
		window = new EnigWindow("Ludum Dare 42 game by C1FR1");
		
		glDisable(GL_DEPTH_TEST);
		perspectiveMatrix = new Matrix4f().ortho(-50f*window.getAspectRatio(), 50f*window.getAspectRatio(), -50f, 50f, 0, 1);
		
		Enemy.setFrames();
		Entity.entityProgram = new ShaderProgram("entityShaders");
		Entity.entityVAO = new VAO(-2.5f, -2.5f, 5f, 5f);
		player = new Player();
		song = new Audio();
		
		Projectile.projectileTexture = new Texture("res/projectile.png");
		
		screenVAO = new VAO(-1f, -1f, 2f, 2f);
		
		gameView = new GameView(window);
		gameView.runLoop();
		window.terminate();
	}
	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix);
	}
	public static Matrix4f getptpm() {
		return getPerspectiveMatrix().translate(-cameraPos.x, -cameraPos.y, 0);
	}
}
