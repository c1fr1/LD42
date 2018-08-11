package game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.VAO;
import org.joml.Vector2f;

public class Entity {
	public static VAO entityVAO;
	public static ShaderProgram entityProgram;
	public Vector2f position = new Vector2f();
	public Vector2f velocity = new Vector2f();
	public void updatePosition(float time) {
		velocity.y -= 40f*time;
		float t = -position.y/(velocity.y * time);
		if (t < 0 || t > 1) {
			position.add(velocity.mul(time, new Vector2f()));
		}else {
			position.x += velocity.x * time;
			position.y = -(position.y + velocity.y * time) * 0.75f;
			velocity.y *= -0.75f;
		}
	}
}
