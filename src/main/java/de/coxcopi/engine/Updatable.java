package de.coxcopi.engine;

public interface Updatable {
    /**
     * Called when an object is first instanced.
     */
    void init();

    /**
     * Called every frame.
     * @param delta The time (in seconds) elapsed since the last frame.
     */
    void update(double delta);
}
