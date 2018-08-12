package game;

import engine.OpenGL.Texture;
import org.joml.Vector4f;

import static game.Main.*;

public class Enemy extends Entity {
	private static Texture[] frames = new Texture[2];
	boolean tagged = false;
	private float animFrameOffset;
	public Enemy() {
		position.x = player.position.x + 100;
		position.y = (float) Math.random() * 50f;
		velocity.x = 20;
		animFrameOffset = (float) Math.random();
	}
	public static void setFrames() {
		frames[0] = new Texture("res/enemy/frame1.png");
		frames[1] = new Texture("res/enemy/frame2.png");
	}
	
	@Override
	public void updatePosition(float time) {
		velocity.x += (20 - velocity.x) * 0.1f;
		float ydiff = player.position.y - position.y;
		if (ydiff > 0) {
			velocity.y += 60f * time;
		}
		for (int i = 0; i < player.projectiles.size(); ++i) {
			Projectile proj = player.projectiles.get(i);
			float xdist = proj.position.x - position.x;
			float ydist = proj.position.y - position.y;
			if (xdist * xdist + ydist * ydist < 20) {
				audio.playContact();
				tagged = true;
				velocity.add(proj.velocity);
				player.projectiles.remove(i);
			}
		}
		super.updatePosition(time);
	}
	
	public void render(float time) {
		frames[(int) (((time + animFrameOffset) % 0.2f) / 0.1f)].bind();
		entityProgram.shaders[0].uniforms[0].set(getptpm().translate(position.x, position.y, 0));
		entityVAO.drawTriangles();
	}
}
