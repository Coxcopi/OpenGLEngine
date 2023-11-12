package de.coxcopi.engine;

import de.coxcopi.mesh.Mesh;
import de.coxcopi.util.math.Matrix4;

public class Model implements Updatable {
    /**
     * The mesh associated with the model. The Mesh's transform
     * is determined by the Models transform.
     */
    public Mesh mesh = null;
    /**
     * The models global transform.
     */
    public Matrix4 transform = new Matrix4();
    /**
     * If true, the models does not receive calls to the update() method.
     */
    public boolean disabled = false;

    public Model() {
    }

    public Model(Mesh mesh) {
        this.mesh = mesh;
    }

    public Model(Mesh mesh, Matrix4 transform) {
        this.mesh = mesh;
        this.transform = transform;
    }

    @Override
    public void init() {}

    @Override
    public void update(double delta) {}
}
