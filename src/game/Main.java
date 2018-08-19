package game;

import engine.OpenGL.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Main {
	public static Main main;
	public static EnigWindow window;
	public static GameView gameView;
	public static Player player;
	public static Vector2f cameraPos = new Vector2f();
	public static VAO screenVAO;
	public static Audio audio;
	public static ShaderProgram fboShader;
	private static Matrix4f perspectiveMatrix;
	public static PauseView pauseView;
	
	public static Texture logoTexture;
	
	public static FBO mainFrameBuffer;
	
	public static void main(String[] args) {
		System.out.println("arguments:");
		for (String s: args) {
			System.out.println(s);
		}
		if (args.length == 0) {
			String os = System.getProperty("os.name");
			System.out.println("Operating System: " + os);
			System.out.println("in order to get a stack trace, run with\njava -jar TheLastWall.jar noReRun\nif you are on mac, you will need to run it with the additional argument\n-XstartOnFirstThread");
			if (os.indexOf("mac") >= 0) {
				try {
					Runtime.getRuntime().exec(new String[]{"java", "-XstartOnFirstThread", "-jar", "TheLastWall.jar", "noReRun"});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				try {
					Runtime.getRuntime().exec(new String[]{"java", "-jar", "TheLastWall.jar", "noReRun"});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if (args[0].equals("noReRun")) {
			window = new EnigWindow("The Last Wall");
			
			glDisable(GL_DEPTH_TEST);
			perspectiveMatrix = new Matrix4f().ortho(-50f * window.getAspectRatio(), 50f * window.getAspectRatio(), -50f, 50f, 0, 1);
			
			NumberRenderer.genTextures();
			Button.setStatics();
			Enemy.setFrames();
			Entity.entityProgram = new ShaderProgram("entityShaders");
			Entity.entityVAO = new VAO(-2.5f, -2.5f, 5f, 5f);
			player = new Player();
			audio = new Audio();
			
			Projectile.projectileTexture = new Texture("res/projectile.png");
			
			fboShader = new ShaderProgram("fboShader");
			
			screenVAO = new VAO(-1f, -1f, 2f, 2f);
			
			mainFrameBuffer = new FBO(new Texture(window.getWidth(), window.getHeight()));
			
			mainFrameBuffer.prepareForTexture();
			Entity.entityProgram.enable();
			Entity.entityProgram.shaders[0].uniforms[0].set(getPerspectiveMatrix().scale(50));
			logoTexture = new Texture("res/logo.png");
			logoTexture.bind();
			screenVAO.fullRender();
			audio.playMenu();
			
			pauseView = new PauseView();
			pauseView.runLoop(mainFrameBuffer.getBoundTexture());
			if (pauseView.buttonPressed != 1) {
				gameView = new GameView(window);
				gameView.runLoop();
			}
			window.terminate();
		}
	}
	public static Matrix4f getPerspectiveMatrix() {
		return new Matrix4f(perspectiveMatrix);
	}
	public static Matrix4f getptpm() {
		return getPerspectiveMatrix().translate(-cameraPos.x, -cameraPos.y, 0);
	}
}
