package puretech.rpgengine.util;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import puretech.rpgengine.display.screen.AbstractScreenHandler;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class ScreenUtil {
    /**
     * Typically should be used in {@link AbstractScreenHandler#setWindowPos()} to place the window in the center of the monitor
     * @param window the window to manipulate
     */
    public static void centerWindowPos(long window) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
        
            glfwGetWindowSize(window, pWidth, pHeight);
        
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
            glfwSetWindowPos(window, ((vidMode.width() - pWidth.get(0)) / 2), (vidMode.height() - pHeight.get(0)) / 2);
        }
    }
    
    /**
     * Makes a keyCallback that's only action is to close when esc is pressed. Only use if not defining other key callbacks.
     * @param win the window to apply the KeyCallback to.
     */
    public static void escCloseScreen(long win) {
        glfwSetKeyCallback(win, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });
    }
}
