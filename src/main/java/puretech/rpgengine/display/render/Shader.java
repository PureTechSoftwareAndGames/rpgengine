package puretech.rpgengine.display.render;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.readAllBytes;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    protected int programID;
    protected final String filePath;
    protected ArrayList<RawShaderData> rawShaderData;
    
    /**
     * Compiles and links shaders into a program
     */
    public void compile() {
        ArrayList<Integer> shaderIDs = new ArrayList<>();
        for (int i = 0; i < rawShaderData.size(); i++) {
            switch (rawShaderData.get(i).type()) {
                case "vertex" -> shaderIDs.add(compileShader(i, GL_VERTEX_SHADER, rawShaderData.get(i).src()));
                case "fragment" -> shaderIDs.add(compileShader(i, GL_FRAGMENT_SHADER, rawShaderData.get(i).src()));
                default -> throw new IllegalArgumentException("Unexpected type");
            }
        }
        int t = glCreateProgram();
        shaderIDs.forEach(i -> glAttachShader(t,i));
        glLinkProgram(t);
        if (glGetProgrami(t, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("PROGRAM INFO LOG:\n" + glGetProgramInfoLog(t));
            throw new IllegalStateException("Linking of shaders at '" + filePath + "' failed.");
        }
        this.programID = t;
    }
    
    protected int compileShader(int index, int type, String src) {
        int t = glCreateShader(type);
        glShaderSource(t, src);
        glCompileShader(t);
        if (glGetShaderi(t, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("SHADER INFO LOG:\n" + glGetShaderInfoLog(t));
            throw new IllegalStateException("Compilation of Shader #" + index + " in '" + filePath + "' failed.");
        }
        return t;
    }
    
    public void use() {
        glUseProgram(programID);
    }
    
    public void detach() {
        glUseProgram(0);
    }
    
    public Shader(String filePath) {
        this.filePath = filePath;
        this.rawShaderData = new ArrayList<>();
        try {
            String source = new String(readAllBytes(Paths.get(filePath)));
            String[] splitSource = source.split("#begin +[a-zA-Z]+ *");
            
            int index;
            int eol = 0;
            LinkedList<String> glslTypes = new LinkedList<>();
            while (true) {
                index = source.indexOf("#begin",eol);
                if (index == -1) break; else index += 7;
                eol = source.indexOf("\r\n", index);
                glslTypes.add(source.substring(index,eol).trim());
            }
            for (int i = 0; i < glslTypes.size() + 1; i++) {
                rawShaderData.add(new RawShaderData(i, glslTypes.pop(), splitSource[i + 1]));
            }
            if (rawShaderData.size() != 2) System.out.println("WARN: Raw data from shader at '" + filePath + "' does not have 2 shader types, errors may occur.");
        } catch (IOException e) {
            throw new IllegalStateException("Failure loading file '" + filePath + "'", e);
        }
    }
}
