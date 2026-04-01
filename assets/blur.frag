#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    float offset = 1.0 / 300.0;

    vec4 color = vec4(0.0);

    color += texture2D(u_texture, v_texCoords + vec2(-offset, 0.0)) * 0.25;
    color += texture2D(u_texture, v_texCoords) * 0.5;
    color += texture2D(u_texture, v_texCoords + vec2(offset, 0.0)) * 0.25;

    gl_FragColor = color;
}