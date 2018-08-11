package game;

import engine.OpenGL.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

import static game.Main.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Player extends Entity {
	public ArrayList<Projectile> projectiles = new ArrayList<>();
	private Texture[] animFrames;
	float energy = 1;
	public Player() {
		position = new Vector2f(0, 25f);
		animFrames = new Texture[2];
		animFrames[0] = new Texture("res/player/frame1.png");
		animFrames[1] = new Texture("res/player/frame2.png");
	}
	public void render(float time) {
		
		animFrames[(int) ((time % 0.2f) / 0.1f)].bind();
		entityProgram.shaders[0].uniforms[0].set(Main.getptpm().translate(position.x, position.y, 0));
		entityVAO.drawTriangles();
		
	}
	
	@Override
	public void updatePosition(float time) {
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
		velocity.x *= 0.96f;
		super.updatePosition(time);
		projectiles(time);
	}
	
	public void projectiles(float time) {
		float xposEquivalent = window.cursorXFloat * 50f * window.getAspectRatio() + cameraPos.x;
		float yposEquavalent = window.cursorYFloat * 50f + cameraPos.y;
		//System.out.println(xposEquivalent);
		//System.out.println(yposEquavalent + "\n");
		if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
			Vector2f impulse = new Vector2f(player.position.x - xposEquivalent, player.position.y - yposEquavalent);
			impulse.normalize();
			impulse.mul(50f);
			projectiles.add(new Projectile(position.x, position.y, impulse.x + velocity.x, impulse.y + velocity.y));
			velocity.y -= impulse.y / 2;
			velocity.x -= impulse.x;
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
