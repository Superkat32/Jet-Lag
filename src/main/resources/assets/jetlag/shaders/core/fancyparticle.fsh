#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;
in vec2 uv;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;

    if (color.a < 0.1) {
        discard;
    }

    vec3 lightblue = vec3(0.3, 0.9, 1.0);
    float lightblueradius = 1.5;
    vec3 blue = vec3(0.3, 0.4, 1.0);
    float blueradius = 0.9;
    vec3 purple = vec3(0.5, 0.1, 1.0);
    float purpleradius = 0.95;
    vec3 pink = vec3(1.0, 0.4, 0.9);
    float pinkradius = 1.0;
    float remainingradius = 1.5;
    float finalradius = 2.0;

    float dist = length(uv);

    vec3 mixed = mix(lightblue, blue, smoothstep(0.0, lightblueradius, dist));
    mixed = mix(mixed, purple, smoothstep(blueradius, purpleradius, dist));
    mixed = mix(mixed, pink, smoothstep(purpleradius, pinkradius, dist));
    mixed = mix(mixed, lightblue, smoothstep(pinkradius, remainingradius, dist));
    mixed = mix(mixed, lightblue, smoothstep(remainingradius, finalradius, dist));

    //well that was pretty shrimple - who needs DRY anyways?
    color *= vec4(mixed, 1.0);

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
