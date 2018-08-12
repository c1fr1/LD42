package game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import org.lwjgl.opengl.ARBTextureBorderClamp;
import org.lwjgl.opengl.ARBTextureCubeMap;
import org.lwjgl.opengl.GL11;

import static game.Main.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Enemy extends Entity {
	private static Texture[] frames = new Texture[4];
	public static ShaderProgram enemyShader;
	float taggedTimer = -1f;
	private float animFrameOffset;
	public Enemy() {
		position.x = player.position.x + 100;
		position.y = (float) Math.random() * 50f;
		velocity.x = 20;
		animFrameOffset = (float) Math.random();
	}
	public static void setFrames() {
		frames[0] = new Texture("res/enemy/frame1.png");
		frames[0].bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		frames[1] = new Texture("res/enemy/frame2.png");
		frames[1].bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		frames[2] = new Texture("res/enemy/frame3.png");
		frames[2].bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		frames[3] = new Texture("res/enemy/frame4.png");
		frames[3].bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		enemyShader = new ShaderProgram("enemyShaders");
	}
	
	@Override
	public void updatePosition(float time) {
		velocity.x += (20 - velocity.x) * 0.1f;
		float ydiff = player.position.y - position.y;
		if (ydiff > 0) {
			velocity.y += 60f * time;
		}
		if (taggedTimer > -0.5f) {
			taggedTimer -= time * 10;
		}
		for (int i = 0; i < player.projectiles.size(); ++i) {
			Projectile proj = player.projectiles.get(i);
			float xdist = proj.position.x - position.x;
			float ydist = proj.position.y - position.y;
			if (xdist * xdist + ydist * ydist < 20) {
				audio.playContact();
				gameView.score += 100;
				taggedTimer = 0;
				velocity.add(proj.velocity);
				player.projectiles.remove(i);
			}
		}
		super.updatePosition(time);
	}
	
	public void render(float time) {
		int animFrame = (int) (((time + animFrameOffset) % 0.2f) / 0.1f);
		if (taggedTimer > -0.5f) {
			animFrame += 2;
		}
		frames[animFrame].bind();
		enemyShader.shaders[0].uniforms[0].set(getptpm().translate(position.x, position.y, 0));
		if (taggedTimer > -0.5f) {
			enemyShader.shaders[2].uniforms[0].set(taggedTimer * taggedTimer);
		}else {
			enemyShader.shaders[2].uniforms[0].set(-1f);
		}
		entityVAO.drawTriangles();
	}
}
