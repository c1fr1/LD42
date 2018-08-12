package game;

import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector2f;

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
	
	private float wallVelocity = 0.7f;
	public float wallPosition = -200f;
	
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
		
		audio.startSong();
	}
	
	
	@Override
	public boolean loop() {
		//game here
		FBO.prepareDefaultRender();
		
		updateCamera();
		
		renderBackground();
		
		updateEntities();
		
		renderScene();
		
		renderStatus();
		
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
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
		cameraPos.x += 0.1f * (player.position.x - cameraPos.x);
		cameraPos.y += 0.1f * (player.position.y - cameraPos.y - 25);
		
		if (cameraPos.y < 25) {
			cameraPos.y = 25;
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
		if (enemies.size() < 10) {
			double thresh = 0.0008 * (10-(double)enemies.size());
			if (Math.random() < thresh) {
				enemies.add(new Enemy());
			}
		}
		Entity.entityVAO.prepareRender();
		Entity.entityProgram.enable();
		player.render(frameStartTime);
		player.updatePosition(deltaTime);
		for (int i = 0; i < enemies.size();++i) {
			Enemy e = enemies.get(i);
			e.updatePosition(deltaTime);
			e.render(frameStartTime);
			if (e.position.sub(player.position, new Vector2f()).lengthSquared() < 25) {
				if (player.velocity.x < 50f) {
					player.velocity.x = 50f;
				}
				player.velocity.x += 200f * deltaTime;
				player.hp -= 0.02;
			}
			if (e.position.x + 5 < wallPosition) {
				if (!e.tagged) {
					wallVelocity += 1.25f;
				}
				enemies.remove(i);
			}
		}
		
		Entity.entityVAO.unbind();
	}
	
	public void renderScene() {
		floorShader.enable();
		floorTexture.bind();
		floorShader.shaders[0].uniforms[0].set(new Vector2f(cameraPos.x / (window.getAspectRatio() * 50f), cameraPos.y/(50f)));
		floorShader.shaders[0].uniforms[1].set(window.getAspectRatio());
		floorVAO.fullRender();
		
		wallPosition += wallVelocity;
		wallVelocity += (0.675f - wallVelocity) * 0.03f;
		
		wallShader.enable();
		wallShader.shaders[0].uniforms[0].set(cameraPos);
		wallShader.shaders[0].uniforms[1].set(window.getAspectRatio());
		wallShader.shaders[2].uniforms[0].set((cameraPos.x - wallPosition) / (100 * window.getAspectRatio()));
		screenVAO.fullRender();
	}
}
