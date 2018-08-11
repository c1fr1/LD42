package Game;

import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static Game.Main.*;

public class Player extends Entity {
	private Texture[] animFrames;
	public Player() {
		position = new Vector2f(0, 25f);
		animFrames = new Texture[2];
		animFrames[0] = new Texture("res/player/frame1.png");
		animFrames[1] = new Texture("res/player/frame2.png");
	}
	public void render(float time) {
		entityProgram.enable();
		animFrames[(int) ((time % 0.2f)/0.1f)].bind();
		entityProgram.shaders[0].uniforms[0].set(Main.getptpm().translate(2*position.x, position.y, 0));
		entityVAO.draw();
	}
	
	@Override
	public void updatePosition(float time) {
		if (UserControls.up(window)) {
			velocity.y += 60f * time;
		}
		if (UserControls.down(window)) {
			velocity.y -= 60f * time;
		}
		if (UserControls.left(window)) {
			velocity.x -= 30f * time;
		}
		if (UserControls.right(window)) {
			velocity.x += 30f * time;
		}
		velocity.x *= 0.9f;
		super.updatePosition(time);
	}
}
