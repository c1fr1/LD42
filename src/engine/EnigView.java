package engine;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public abstract class EnigView {
	public EnigWindow window;
	
	public long loopStartTime;
	
	public long deltaTime;
	
	public long frameStartTime;
	
	/**
	 * creates a new main view
	 */
	public EnigView(EnigWindow swindow) {
		window = swindow;
	}
	
	public void runLoop() {
		loopStartTime = System.nanoTime();
		frameStartTime = System.nanoTime();
		while (!glfwWindowShouldClose(window.id)) {
			window.update();
			long newTime = System.nanoTime();
			deltaTime = frameStartTime - newTime;
			frameStartTime = newTime;
			if (loop()) {
				cleanUp();
				break;
			}
			window.update();
			cleanUp();
		}
		setDown();
	}
	
	/**
	 * cleans up at the end of a frame
	 */
	public void cleanUp() {
		ShaderProgram.disable();
		window.resetOffsets();
		glfwSwapBuffers(window.id);
		glfwPollEvents();
		EnigWindow.checkGLError();
		//window.update();
	}
	
	/**
	 * loop that gets called every frame
	 * @return if the view should close after this frame ends
	 */
	public abstract boolean loop();
	
	/**
	 * does any neccisary cleanup
	 */
	public void setDown() {};
}
