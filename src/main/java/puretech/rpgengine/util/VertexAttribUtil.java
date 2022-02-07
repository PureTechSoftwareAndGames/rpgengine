package puretech.rpgengine.util;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public final class VertexAttribUtil {
    /**
     * Element array for a 2x2 square
     */
    public static int[] TWO_TWO_SQUARE_ELEMENT_ARRAY = {
            2, 1, 0, //tr
            0, 1, 3  //bl
    };
    /**
     * Creates a vertex array and returns the IDs. <br/>
     * @param widths An array featuring the lengths of the sections
     * @param vertexArray the vertexArray to use
     * @param elementArray the elementArray to use
     * @param bufferUsage the buffer usage method to use
     * @return an array consisting of [vaoID, vboID, eboID]
     */
    public static int[] floatVertexAttrib(int[] widths, float[] vertexArray, int[] elementArray, int bufferUsage, boolean normalized) {
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, bufferUsage);
    
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, bufferUsage);
    
        int vertexSizeBytes = Arrays.stream(widths).sum() * Float.BYTES;
        int off = 0;
        int index = 0;
        for (int i : widths) {
            glVertexAttribPointer(index,i,GL_FLOAT,normalized,vertexSizeBytes, (long) off * Float.BYTES);
            off += i;
            index++;
        }
        
        return new int[] { vaoID, vboID, eboID };
    }
}
