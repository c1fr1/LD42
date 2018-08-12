package game;

import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static game.Main.*;

public class GameView extends EnigView {
	private VAO floorVAO;
	
	private ShaderProgram floorShader;
	private ShaderProgram skyShader;
	private ShaderProgram wallShader;
	private ShaderProgram statusShader;
	
	private Texture floorTexture;
	private Texture skyTexture;
	private Texture hpTexture;
	private Texture mpTexture;
	
	private VAO hpVAO;
	private VAO mpVAO;
	
	private NumberRenderer scoreRenderer;
	
	private float wallVelocity = 40f;
	public float wallPosition = -100f;
	
	public int score = 0;
	
	public ArrayList<Enemy> enemies = new ArrayList<>();
	
	public GameView(EnigWindow swindow) {
		super(swindow);
		floorVAO = new VAO(-1, -1, 2, 1f);
		floorShader = new ShaderProgram("floorShader");
		skyShader = new ShaderProgram("skyShader");
		wallShader = new ShaderProgram("wallShader");
		statusShader = new ShaderProgram("statusShader");
		
		floorTexture = new Texture("res/floor.png");
		skyTexture = new Texture("res/sky.png");
		hpTexture = new Texture("res/statusBars/hpBar.png");
		mpTexture = new Texture("res/statusBars/mpBar.png");
		
		hpVAO = new VAO(-1, 0.75f, 1/window.getAspectRatio(), 0.25f);
		mpVAO = new VAO(1 - 1/window.getAspectRatio(), 0.75f, 1/window.getAspectRatio(), 0.25f);
		
		scoreRenderer = new NumberRenderer();
		scoreRenderer.scale = 0.1f;
		scoreRenderer.xPos = 10;
		scoreRenderer.yPos = -9f;
		scoreRenderer.color = new Vector3f(1, 1, 1);
		
		audio.startSong();
	}
	
	
	@Override
	public boolean loop() {
		//game here
		mainFrameBuffer.prepareForTexture();
		System.out.println("palyerx: " + player.position.x);
		System.out.println("wallX: " + wallPosition);
		if (player.hp > 0) {
			updateCamera();
		}else {
			if (player.position.x > -25) {
				audio.playEnd();
			}
			player.position.x = -50;
			player.hp = -50;
		}
		
		renderBackground();
		updateEntities();
		
		renderScene();
		
		if (player.hp > 0) {
			renderStatus();
		}
		scoreRenderer.render(score);
		
		FBO.prepareDefaultRender();
		fboShader.enable();
		mainFrameBuffer.getBoundTexture().bind();
		screenVAO.fullRender();
		
		if (player.hp < 0) {
			if (wallPosition - cameraPos.x > 50 * window.getAspectRatio()) {
				pauseView.runLoop(mainFrameBuffer.getBoundTexture());
				if (pauseView.buttonPressed == 1) {
					return true;
				}
				reset();
				frameStartTime = System.nanoTime() / 1e9f;
			}
		}
		
		for (int i:UserControls.quit) {
			if (window.keys[i] == 1) {
				window.keys[i] = 2;
				pauseView.runLoop(mainFrameBuffer.getBoundTexture());
				if (pauseView.buttonPressed == 1) {
					return true;
				}
				frameStartTime = System.nanoTime() / 1e9f;
				break;
			}
		}
		return false;
	}
	
	public void reset() {
		enemies = new ArrayList<>();
		score = 0;
		wallVelocity = 40f;
		wallPosition = -100f;
		player.reset();
		audio.startSong();
	}
	
	public void renderStatus() {
		statusShader.enable();
		statusShader.shaders[2].uniforms[0].set(player.hp);
		hpTexture.bind();
		hpVAO.fullRender();
		statusShader.shaders[2].uniforms[0].set(player.energy);
		mpTexture.bind();
		mpVAO.fullRender();
	}
	
	public void updateCamera() {
		//cameraPos.x += 0.1f * (player.position.x - cameraPos.x);
		//cameraPos.y += 0.1f * (player.position.y - cameraPos.y - 25);//dtn
		cameraPos.x = player.position.x;
		if (cameraPos.y < 30) {
			cameraPos.y = 30;
		}
	}
	
	public void renderBackground() {
		skyShader.enable();
		skyShader.shaders[0].uniforms[0].set(cameraPos);
		skyShader.shaders[0].uniforms[1].set(2/window.getAspectRatio());
		
		skyTexture.bind();
		screenVAO.fullRender();
	}
	
	public void updateEntities() {
		if (player.position.x > wallPosition) {
			if (enemies.size() < 10) {
				double advantage = player.position.x - wallPosition;
				score += (int) (advantage * -deltaTime);
				if (advantage < 100) {
					double thresh = -deltaTime * 0.06 * (10 - (double) enemies.size());
					if (Math.random() < thresh) {
						enemies.add(new Enemy());
					}
				}else {
					double max = 5 + advantage/5;
					double thresh = -deltaTime * 0.05 * (max - (double) enemies.size());
					if (Math.random() < thresh) {
						enemies.add(new Enemy());
					}
				}
			}
		}
		Entity.entityVAO.prepareRender();
		Entity.entityProgram.enable();
		player.render(frameStartTime);
		player.updatePosition(deltaTime);
		Enemy.enemyShader.enable();
		for (int i = 0; i < enemies.size();++i) {
			Enemy e = enemies.get(i);
			e.updatePosition(deltaTime);
			e.render(frameStartTime);
			if (e.position.sub(player.position, new Vector2f()).lengthSquared() < 25) {
				if (player.velocity.x < 50f) {
					player.velocity.x = 50f;
				}
				player.hp -= 0.02;
			}
			if (e.position.x + 5 < wallPosition) {
				if (e.taggedTimer < -0.5f) {
					wallVelocity += 500f/wallVelocity;
				}
				enemies.remove(i);
			}
		}
		
		Entity.entityVAO.unbind();
	}
	
	public void renderScene() {
		floorShader.enable();
		floorTexture.bind();
		floorShader.shaders[0].uniforms[0].set(new Vector2f(cameraPos.x / 50f, cameraPos.y/50f));
		floorShader.shaders[0].uniforms[1].set(window.getAspectRatio());
		floorVAO.fullRender();
		
		wallPosition += wallVelocity * -deltaTime;
		if (wallVelocity > 40f) {
			wallVelocity -= 15 * -deltaTime;
		}
		//wallVelocity += (0.675f - wallVelocity) * 0.03f;//dtn
		
		wallShader.enable();
		wallShader.shaders[0].uniforms[0].set(cameraPos);
		wallShader.shaders[0].uniforms[1].set(window.getAspectRatio());
		wallShader.shaders[2].uniforms[0].set((cameraPos.x - wallPosition) / 100f);
		screenVAO.fullRender();
	}
}
