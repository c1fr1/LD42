package game;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static game.Main.*;

public class NumberRenderer {
	private static Texture[] textures = new Texture[10];
	private static VAO vao;
	private static ShaderProgram shader;
	private static float width;
	public float scale = 1;
	public float xPos = 0;
	public float yPos = 0;
	public Vector3f color = new Vector3f(0f, 0f, 0f);
	
	public void render(int num) {
		String stringRepresentation = "" + num;
		shader.enable();
		shader.shaders[2].uniforms[0].set(color);
		Matrix4f transformation = new Matrix4f().scale(scale).translate(xPos + 2 * width * stringRepresentation.length(), yPos, 0);
		vao.prepareRender();
		for (char c: (stringRepresentation).toCharArray()) {
			textures[Character.getNumericValue(c)].bind();
			shader.shaders[0].uniforms[0].set(transformation);
			transformation.translate(-2*width, 0, 0);
			vao.drawTriangles();
		}
		vao.unbind();
	}
	
	public static void genTextures() {
		width = -0.5f / window.getAspectRatio();
		vao = new VAO(-0.5f / window.getAspectRatio(), -1, 1/window.getAspectRatio(), 2);
		shader = new ShaderProgram("numberShaders");
		for (int i = 0; i < 10; ++i) {
			textures[i] = new Texture("res/numbers/" + i + ".png");
		}
	}
}
