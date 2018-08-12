package game;

import engine.OpenGL.Texture;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

import static engine.EnigUtils.clamp;
import static game.Main.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Player extends Entity {
	public ArrayList<Projectile> projectiles = new ArrayList<>();
	private Texture[] animFrames;
	float energy = 1;
	float hp = 1;
	public Player() {
		position = new Vector2f(0, 25f);
		animFrames = new Texture[2];
		animFrames[0] = new Texture("res/player/frame1.png");
		animFrames[1] = new Texture("res/player/frame2.png");
	}
	
	public void reset() {
		energy = 1;
		hp = 1;
		position.x = 0;
		position.y = 25f;
		velocity.x = 0;
		velocity.y = 0;
	}
	
	public void render(float time) {
		
		animFrames[(int) ((time % 0.2f) / 0.1f)].bind();
		entityProgram.shaders[0].uniforms[0].set(Main.getptpm().translate(position.x, position.y, 0));
		entityVAO.drawTriangles();
		
	}
	
	@Override
	public void updatePosition(float time) {
		//player.position.y *= 0.98;//dtn
		if (player.position.y > 50) {
			player.velocity.y -= 50f * time;
		}
		if (player.position.y > 80) {
			player.velocity.y = 0;
			player.position.y = 80;
		}
		if (UserControls.up(window)) {
			velocity.y += 80f * time;
		}
		if (UserControls.down(window)) {
			velocity.y -= 80f * time;
		}
		if (UserControls.left(window)) {
			velocity.x -= 120f * time;
		}
		if (UserControls.right(window)) {
			velocity.x += 120f * time;
		}
		if (velocity.x > 10) {
			velocity.x -= 10 * time;
		}
		if (velocity.y < 10) {
			velocity.y += 10 * -time;
		}
		if (player.velocity.x > 0) {
			player.velocity.x -= 20 * -time;
			if (player.velocity.x < 0) {
				player.velocity.x = 0;
			}
		}
		if (player.velocity.x < 0) {
			player.velocity.x += 20 * -time;
			if (player.velocity.x > 0) {
				player.velocity.x = 0;
			}
		}
		player.velocity.x = clamp(player.velocity.x, -50, 50);
		//velocity.x *= 0.96f;//dtn
		super.updatePosition(time);
		projectiles(time);
		player.hp += 0.05f * -time;
		if (player.hp > 1) {
			player.hp = 1;
		}
		if (player.position.x < gameView.wallPosition) {
			player.hp -= 0.2 * -time;
			player.energy += 0.4 * -time;
		}
	}
	
	public void projectiles(float time) {
		float xposEquivalent = window.cursorXFloat * 50f * window.getAspectRatio() + cameraPos.x;
		float yposEquavalent = window.cursorYFloat * 50f + cameraPos.y;
		//System.out.println(xposEquivalent);
		//System.out.println(yposEquavalent + "\n");
		energy += 0.25f * -time;
		if (energy > 1) {
			energy = 1;
		}
		if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
			if (energy > 0.3) {
				energy -= 0.3f;
				Vector2f impulse = new Vector2f(player.position.x - xposEquivalent, player.position.y - yposEquavalent);
				impulse.normalize();
				impulse.mul(100f);
				projectiles.add(new Projectile(position.x, position.y, impulse.x + velocity.x, impulse.y + velocity.y));
				velocity.y -= impulse.y / 4;
				velocity.x -= impulse.x / 2;
			}
		}
		for (int i = 0; i < projectiles.size();++i) {
			Projectile p = projectiles.get(i);
			if (p.position.y < 0 || p.position.y - position.y > 200 || p.position.x - position.x < -200 || p.position.x - position.x > 300) {
				projectiles.remove(i);
				continue;
			}
			p.updatePosition(time);
			p.render();
		}
	}
}
