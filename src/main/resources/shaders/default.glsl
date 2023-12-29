#shader vertex
#version 430 core
layout(location = 0) in vec3 vertexPos;
layout(location = 1) in vec3 faceNormal;
layout(location = 2) uniform mat4 modelMatrix;
layout(location = 3) uniform mat4 viewMatrix;
layout(location = 4) uniform mat4 projectionMatrix;
out vec3 POSITION;
out vec3 WORLD_POSITION;
out vec3 NORMAL;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertexPos, 1.0);
    POSITION = vertexPos;
    WORLD_POSITION = vec3(modelMatrix * vec4(POSITION, 1.0));
    mat3 normal_Matrix = mat3(transpose(inverse(modelMatrix)));
    NORMAL = faceNormal * normal_Matrix;
}

#shader fragment
#version 430 core

struct Environment {
    vec3 ambient;
};

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

out vec4 fragColor;
in vec3 POSITION;
in vec3 WORLD_POSITION;
in vec3 NORMAL;

uniform Environment environment;
uniform Material material;

uniform vec3 viewPosition;

void main() {

    // ambient
    vec3 ambient = material.ambient + environment.ambient;

    vec3 tempLightPos = vec3(1.3, 4, 2.5);

    // diffuse
    vec3 normal = normalize(NORMAL);
    vec3 lightDir = normalize(tempLightPos - WORLD_POSITION);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diff * material.diffuse;

    // specular
    vec3 viewDir = normalize(viewPosition - WORLD_POSITION);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = spec * material.specular;

    vec3 result = ambient + diffuse + specular;
    fragColor = vec4(result, 1.0);
}