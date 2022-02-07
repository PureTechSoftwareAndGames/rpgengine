package puretech.rpgengine.display.render;

public record RawShaderData(int index, String type, String src) {
    public RawShaderData {
        if (!type.matches("vertex|fragment")) throw new IllegalArgumentException("Invalid shader type");
    }
}
