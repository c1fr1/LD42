package game;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Texture;
import engine.OpenGL.VAO;

import static game.Main.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Button {
	private static ShaderProgram shader;
	private static VAO vao;
	private Texture texture;
	private float height;
	private float hoverTimer;
	
	public Button(String path, float height) {
		texture = new Texture(path);
		this.height = height;
	}
	
	public void reset() {
		hoverTimer = 0;
	}
	
	public static void setStatics() {
		shader = new ShaderProgram("buttonShader");
		vao = new VAO(-0.5f, -0.125f, 1, 0.25f);
	}
	
	public boolean render(float frameStart) {
		boolean ret = false;
		if (window.cursorYFloat - height > -0.125) {
			if (window.cursorYFloat - height < 0.125) {
				if (window.cursorXFloat * window.getAspectRatio() > -0.5f) {
					if (window.cursorXFloat * window.getAspectRatio() < 0.5f) {
						hoverTimer += 0.75;
						if (window.mouseButtons[GLFW_MOUSE_BUTTON_LEFT] == 1) {
							ret = true;
						}
					}
				}
			}
		}
		hoverTimer -= 0.375;
		if (hoverTimer < 0) {
			hoverTimer = 0;
		}
		if (hoverTimer > 11) {
			hoverTimer = 11f;
		}
		shader.enable();
		shader.shaders[0].uniforms[0].set(height);
		shader.shaders[0].uniforms[1].set(1/window.getAspectRatio());
		shader.shaders[2].uniforms[0].set(hoverTimer);
		float theBrit = Math.abs(0.25f - (frameStart % 5f)/10f);
		shader.shaders[2].uniforms[1].set(theBrit);
		texture.bind();
		vao.fullRender();
		return ret;
	}
}
