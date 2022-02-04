package puretech.rpgengine.display;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class AbstractScreenHandler {
    protected long window;
    protected long monitor;
    protected long share;
    protected CharSequence title;
    protected int[] dim;
    protected Color clearColor;
    protected int vsync;
    
    /**
     * Main function that should be called to start the screen. <br/>
     * Should initialize the window, enter its display loop, and cleanup once the loop has ended.
     */
    public void disp() {
        this.init();
        this.loop();
    
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);
    
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    /**
     * Initializes the screen by defining and showing it.
     * @throws IllegalStateException If GLFW initialization fails
     * @throws RuntimeException If GLFW window creation fails
     */
     protected void init() {
        this.setGFLWerrCallback();
    
        if (!glfwInit()) throw new IllegalStateException("GLFW init failed.");
    
        this.setWindowHints();
    
        this.window = glfwCreateWindow(this.dim[0], this.dim[1], this.title, this.monitor, this.share);
        if (this.window == MemoryUtil.NULL) throw new RuntimeException("GLFW window creation failed.");
        
        this.setKeyCallback();
        
        this.setWindowPos();
    
        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(this.vsync);
        
        glfwShowWindow(window);
    }
    
    protected void setGFLWerrCallback() { glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err)); }
    protected void setWindowHints() {}
    protected void setKeyCallback() {}
    
    abstract void setWindowPos();
    
    /**
     * Brief init and render loop.
     */
    protected void loop() {
        GL.createCapabilities();
    
        glClearColor(this.clearColor.getRed() / 255f, this.clearColor.getGreen() / 255f, this.clearColor.getBlue() / 255f, this.clearColor.getAlpha() / 255f);
        
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            this.loopActions();
            
            glfwSwapBuffers(window);
            
            glfwPollEvents();
        }
    }
    
    abstract void loopActions();
    
    /**
     * Returns a window. Can be opened using {@link AbstractScreenHandler#disp()}
     * @param title The title of the window to open
     * @param dim Dimensions of the window to open
     * @param monitor Monitor to display on. Use {@link MemoryUtil#NULL} for non-fullscreen, or pass in a monitor for fullscreen
     * @param share Share value. See the <a target="_blank" href="http://www.glfw.org/docs/latest/context.html#context_sharing">relevant GLFW documentation</a>.
     * @param clearColor Color for {@link GL11#glClearColor(float, float, float, float)} to be set to initially
     * @param vsync Value for {@link org.lwjgl.glfw.GLFW#glfwSwapInterval(int)} to be set to intially
     */
    public AbstractScreenHandler(CharSequence title, int[] dim, long monitor, long share, Color clearColor, int vsync) {
        if (dim.length != 2) throw new IllegalArgumentException("'dim' must be of length 2");
        for (int i : dim) if (i < 0) throw new IllegalArgumentException("'dim' must have values > 0");
        this.title = title;
        this.dim = dim;
        this.monitor = monitor;
        this.share = share;
        this.clearColor = clearColor;
        this.vsync = vsync;
    }
}
