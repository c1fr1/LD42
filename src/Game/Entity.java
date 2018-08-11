package Game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.VAO;
import org.joml.Vector2f;

public class Entity {
	public static VAO entityVAO;
	public static ShaderProgram entityProgram;
	public Vector2f position = new Vector2f();
	public Vector2f velocity = new Vector2f();
	public void updatePosition(float time) {
		velocity.y -= 30f*time;
		position.add(velocity.mul(time, new Vector2f()));
	}
}
