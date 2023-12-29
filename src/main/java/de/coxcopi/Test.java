package de.coxcopi;

import de.coxcopi.engine.Engine;
import de.coxcopi.engine.Model;
import de.coxcopi.material.Material;
import de.coxcopi.mesh.Mesh;
import de.coxcopi.mesh.MeshBuilder;
import de.coxcopi.mesh.MeshParser;
import de.coxcopi.util.math.Vector3;

public class Test {

    public void run() {
        Engine.init();
        renderTest();
        Engine.run();
    }

    private void renderTest() {

        //Mesh quad = MeshBuilder.createPrimitiveRect(1f, 1f);
        //quad.transform.translate(new Vector3(0, 0, 0));
        //quad.transform.rotateX(MathUtils.degToRad(90));
        //quad.transform.scale(2);

        Mesh cube = MeshBuilder.createPrimitiveCuboid(1f, 1f, 1f);
        cube.transform.translate(new Vector3(0, 0, 0));

        Material material = new Material();
        //quad.material = material;
        cube.material = material;

        Mesh mesh = MeshParser.parseOBJ("suzanne_smooth.obj");
        System.out.println(mesh.getElementCount());

        Mesh sphere = MeshBuilder.createPrimitiveSphere(8, 8, 1.0);

        mesh.material = new Material();

        mesh.transform.scale(2);

        //renderer.addToRenderQueue(quad);
        //renderer.addToRenderQueue(cube);

        //Mesh sphere = MeshBuilder.createPrimitiveSphere(12, 6, 1);
        //sphere.transform.scale(1.3);
        //Mesh otherSphere = MeshBuilder.createPrimitiveOtherSphere(0, 1);
        Engine.instance(new Model(mesh));
    }
}
