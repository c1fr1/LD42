package game;

import engine.EnigView;
import engine.OpenGL.*;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

import static game.Main.*;

public class GameView extends EnigView {
	private VAO floorVAO;
	
	private ShaderProgram floorShader;
	private ShaderProgram skyShader;
	
	private Texture floorTexture;
	private Texture skyTexture;
	
	public ArrayList<Enemy> enemies = new ArrayList<>();
	
	public GameView(EnigWindow swindow) {
		super(swindow);
		floorVAO = new VAO(-1, -1, 2, 1f);
		floorShader = new ShaderProgram("floorShader");
		skyShader = new ShaderProgram("skyShader");
		floorTexture = new Texture("res/floor.png");
		skyTexture = new Texture("res/sky.png");
	}
	
	
	@Override
	public boolean loop() {
		//game here
		FBO.prepareDefaultRender();
		
		cameraPos.x += 0.1f * (player.position.x - cameraPos.x);
		cameraPos.y += 0.1f * (player.position.y - cameraPos.y - 25);
		
		if (cameraPos.y < 25) {
			cameraPos.y = 25;
		}
		
		skyShader.enable();
		skyShader.shaders[0].uniforms[0].set(cameraPos);
		skyShader.shaders[0].uniforms[1].set(2/window.getAspectRatio());
		
		skyTexture.bind();
		screenVAO.fullRender();
		
		Entity.entityVAO.prepareRender();
		Entity.entityProgram.enable();
		player.render(frameStartTime);
		player.updatePosition(deltaTime);
		
		for (Enemy e: enemies) {
			e.updatePosition(deltaTime);
			e.render(frameStartTime);
		}
		
		Entity.entityVAO.unbind();
		
		if (Math.random() < 0.002) {
			enemies.add(new Enemy());
		}
		
		floorShader.enable();
		floorTexture.bind();
		floorShader.shaders[0].uniforms[0].set(new Vector2f(cameraPos.x / (window.getAspectRatio() * 50f), cameraPos.y/(50f)));
		floorShader.shaders[0].uniforms[1].set(window.getAspectRatio());
		floorVAO.fullRender();
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
}
