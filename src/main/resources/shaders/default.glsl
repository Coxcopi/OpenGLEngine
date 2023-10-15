#shader vertex
#version 430 core
layout(location = 0) in vec3 vertexPos;
layout(location = 1) uniform mat4 modelMatrix;
layout(location = 2) uniform mat4 viewMatrix;
layout(location = 3) uniform mat4 projectionMatrix;
out vec4 position;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertexPos, 1.0);
    position = vec4(vertexPos, 1.0);
}

#shader fragment
#version 330 core
out vec4 fragColor;
in vec4 position;

void main() {
    fragColor = vec4((vec3(position.x, position.y, position.z) + vec3(1)) / 2, 1f);
    //fragColor = vec4(viewMatrix * vec4(0.0, 1.0, 0.0, 1.0));
    //fragColor = vec4(1f);
}