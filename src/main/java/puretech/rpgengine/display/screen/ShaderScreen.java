package puretech.rpgengine.display.screen;

import org.lwjgl.system.MemoryUtil;
import puretech.rpgengine.display.render.Shader;
import puretech.rpgengine.util.ScreenUtil;
import puretech.rpgengine.util.VertexAttribUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ShaderScreen extends AbstractScreenHandler {
    private final Shader shader;
    private int vaoID;
    
    private final float[] vertexArray = {
            //Pos                //color
             0.5f, -0.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,  // br 0
            -0.5f,  0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,  // tl 1
             0.5f,  0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f,  // tr 2
            -0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f   // bl 3
    };
    
    @Override
    protected void finishInit() {
        shader.compile();
        vaoID = VertexAttribUtil.floatVertexAttrib(new int[]{3,4},vertexArray,VertexAttribUtil.TWO_TWO_SQUARE_ELEMENT_ARRAY,GL_STATIC_DRAW,false)[0];
        super.finishInit();
    }
    
    @Override
    void setWindowPos() {
        ScreenUtil.centerWindowPos(window);
    }
    
    @Override
    protected void setKeyCallback() {
        ScreenUtil.escCloseScreen(window);
    }
    
    @Override
    protected void loopInit() {
        super.loopInit();
        shader.use();
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    
    @Override
    void loopActions() {
        glDrawElements(GL_TRIANGLES, VertexAttribUtil.TWO_TWO_SQUARE_ELEMENT_ARRAY.length, GL_UNSIGNED_INT, 0);
    }
    
    @Override
    protected void cleanup() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
        super.cleanup();
    }
    
    public ShaderScreen(int[] dim, Color clearColor, String shaderPath) {
        super("Shader", dim, MemoryUtil.NULL, MemoryUtil.NULL, clearColor, 1);
        this.shader = new Shader(shaderPath);
    }
}
