package puretech.rpgengine.display;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPush;


public class RainbowScreen extends AbstractScreenHandler {
    private boolean fullscreen;
    
    @Override
    protected void init() {
        super.init();
        if (fullscreen) {
            this.monitor = glfwGetPrimaryMonitor();
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stackMallocInt(1);
                IntBuffer pHeight = stackMallocInt(1);
        
                glfwGetMonitorWorkarea(monitor, null, null, pWidth, pHeight);
        
                this.dim = new int[] { pWidth.get(0), pHeight.get(0) };
            } finally {
                glfwSetWindowSize(this.window, this.dim[0], this.dim[1]);
                glfwSetWindowMonitor(this.window, this.monitor,  0, 0, this.dim[0], this.dim[1], GLFW_DONT_CARE);
            }
        }
    }
    
    @Override
    protected void setWindowHints() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }
    
    @Override
    protected void setKeyCallback() {
        glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });
    }
    
    @Override
    protected void setWindowPos() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
        
            glfwGetWindowSize(window, pWidth, pHeight);
        
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
            glfwSetWindowPos(window, ((vidMode.width() - pWidth.get(0)) / 2), (vidMode.height() - pHeight.get(0)) / 2);
        }
    }
    
    @Override
    void loopActions() {
        progressColor();
        glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
    }
    
    private void progressColor() { // Will only trigger if at least 1 value is 255.
        int r = this.clearColor.getRed();
        int g = this.clearColor.getGreen();
        int b = this.clearColor.getBlue();
        progress: {
            if (r == 255) {
                if (b != 0) { b--; break progress; }
                if (g != 255) { g++; break progress; }
            }
            if (g == 255) {
                if (r != 0) { r--; break progress; }
                b++; break progress;
            }
            if (b == 255) {
                if (g != 0) { g--; break progress; }
                r++;
            }
        }
        this.clearColor = new Color(r,g,b,this.clearColor.getAlpha());
    }
    
    
    /**
     * @param title Title of the window
     * @param dim Dimensions of the window. Will be ignored if fullscreen == true
     * @param clearColor The clearColor to start with. The gradient will only apply if at least 1 component of the RGB is 255.
     * @param fullscreen Wether or not to fullscreen onto {@link org.lwjgl.glfw.GLFW#glfwGetPrimaryMonitor()}
     */
    public RainbowScreen(CharSequence title, int[] dim, Color clearColor, boolean fullscreen) {
        super(title, dim, MemoryUtil.NULL, MemoryUtil.NULL, clearColor, 1);
        this.fullscreen = fullscreen;
    }
}
