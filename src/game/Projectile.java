package game;

import engine.OpenGL.Texture;
import org.joml.Vector2f;

import static engine.OpenGL.EnigWindow.checkGLError;
import static game.Main.audio;

public class Projectile extends Entity {
	public static Texture projectileTexture;
	public Projectile(float x, float y, float xvel, float yvel) {
		position.x = x;
		position.y = y;
		velocity.x = xvel;
		velocity.y = yvel;
		audio.playBlast();
	}
	
	@Override
	public void updatePosition(float time) {
		position.add(velocity.mul(time, new Vector2f()));
	}
	public void render() {
		projectileTexture.bind();;
		entityProgram.shaders[0].uniforms[0].set(Main.getptpm().translate(position.x, position.y, 0));
		entityVAO.draw();
	}
}
