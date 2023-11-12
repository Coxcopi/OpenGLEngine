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
out vec4 fragColor;
in vec3 POSITION;
in vec3 WORLD_POSITION;
in vec3 NORMAL;

uniform vec3 ambientColor;
uniform vec3 viewPosition;

void main() {

    vec3 view = vec3(viewPosition.x, viewPosition.y, -viewPosition.z);
    vec3 tempLightPos = vec3(0, 5, -5);
    vec3 normal = normalize(NORMAL);
    //vec3 lightDir = normalize(tempLightPos - WORLD_POSITION);

    vec3 lightDir = normalize(vec3(0, 5, -5));

    vec3 viewDir = normalize(view - WORLD_POSITION);
    vec3 reflectDir = reflect(-lightDir, normal);

    float specularStrength = 0.5;
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);

    //vec3 albedo = ((POSITION + vec3(1))) / 2; // temporary
    vec3 albedo = vec3(0.0, 0.5, 0.6);
    vec3 diffuse = vec3(max(dot(normal, lightDir), 0.0)); // assuming light color is white, multiply with light color to get the diffuse color with a non-white light
    vec3 specular = vec3(specularStrength * spec); // same

    //fragColor = vec4(viewMatrix * vec4(0.0, 1.0, 0.0, 1.0));
    //fragColor = vec4(1f);
    //fragColor = vec4((vec3(position.x, position.y, position.z) + vec3(1)) / 2, 1f);

    vec3 lightResult = (ambientColor + diffuse + specular) * albedo;

    fragColor = vec4(lightResult, 1.0);
}