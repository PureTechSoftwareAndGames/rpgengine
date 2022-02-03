package puretech.rpgengine.display;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;

public class ScreenBooter {
    private long window;
    private int[] dim;
    private Color color;
    private final boolean fullscreen;
    
    public void disp() {
        init();
        loop();
        
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) throw new IllegalStateException("GLFW init failed.");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        long monitor = fullscreen ? glfwGetPrimaryMonitor() : MemoryUtil.NULL;
        if (fullscreen) {
            try (MemoryStack stack = stackPush()) {
                IntBuffer pWidth = stackMallocInt(1);
                IntBuffer pHeight = stackMallocInt(1);
                
                glfwGetMonitorWorkarea(monitor, null, null, pWidth, pHeight);
                
                this.dim = new int[] { pWidth.get(0), pHeight.get(0) };
            }
        }
        
        window = glfwCreateWindow(this.dim[0], this.dim[1], "TEST", monitor, MemoryUtil.NULL);
        
        if (window == MemoryUtil.NULL) throw new RuntimeException("GLFW window creation failed.");
        
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
           if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });
        
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            
            glfwGetWindowSize(window, pWidth, pHeight);
            
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            
            glfwSetWindowPos(window, ((vidMode.width() - pWidth.get(0)) / 2), (vidMode.height() - pHeight.get(0)) / 2);
        }
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        
        glfwShowWindow(window);
    }
    
    private void loop() {
        GL.createCapabilities();
        
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            progressColor();
            glClearColor(this.color.getRed() / 255f, this.color.getGreen() / 255f, this.color.getBlue() / 255f, this.color.getAlpha() / 255f);
            
            glfwSwapBuffers(window);
            
            glfwPollEvents();
        }
    }
    
    private void progressColor() { // Will only trigger if at least 1 value is 255.
        int r = this.color.getRed();
        int g = this.color.getGreen();
        int b = this.color.getBlue();
        progress: {
            if (r == 255) {
                if (b != 0) { b--; break progress; }
                if (g != 255) { g++; break progress; }
            }
            if (b == 255) {
                if (g != 0) { g--; break progress; }
                r++; break progress;
            }
            if (g == 255) {
                if (r != 0) { r--; break progress; }
                b++;
            }
        }
        this.color = new Color(r,g,b,this.color.getAlpha());
    }
    
    public static ScreenBooter makeScreenBooter() { return makeScreenBooter(new int[]{500,500}); }
    public static ScreenBooter makeScreenBooter(Color startColor) { return makeScreenBooter(new int[] {500,500}, startColor); }
    public static ScreenBooter makeScreenBooter(int[] dim) { return makeScreenBooter(dim, new Color(255,0,0)); }
    public static ScreenBooter makeScreenBooter(int[] dim, Color startColor) { return new ScreenBooter(dim, startColor, false); }
    public static ScreenBooter makeScreenBooter(boolean fullscreen) { return makeScreenBooter(fullscreen, new Color(255,0,0)); }
    public static ScreenBooter makeScreenBooter(boolean fullscreen, Color startColor) {
        if (!fullscreen) return makeScreenBooter(startColor);
        return new ScreenBooter(null, startColor, true);
    }
    
    private ScreenBooter(int[] dim, Color startColor, boolean fullscreen) {
        if (dim != null) {
            if (fullscreen) System.err.println("WARN (ScreenBooter(int[], Color, boolean)): Arg 'dim' will be overwritten as fullscreen == true");
            if (dim.length != 2) throw new IllegalArgumentException("Arg 'dim' must be of length 2");
            for (int i : dim) if (i < 0) throw new IllegalArgumentException("Arg 'dim' must have values > 0");
        } else if (!fullscreen) throw new IllegalArgumentException("Arg 'dim' must not be null when fullscreen == false");
        
        this.dim = dim;
        this.color = startColor;
        this.fullscreen = fullscreen;
    }
}