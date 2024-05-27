#version 150

//#moj_import "rainbow_local_import.glsl"

in vec3 Position;
in vec2 UV0;
uniform vec2 OutSize;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

out vec2 texCoord0;
out vec2 uv;

void main() {
//    gl_Position = applyMatrix(ProjMat, applyMatrix(ModelViewMat, vec4(Position, 1.0)));
//    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    texCoord0 = UV0;
    uv = outPos.xy;
}