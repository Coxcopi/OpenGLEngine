package de.coxcopi.util.math;

import org.jetbrains.annotations.NotNull;

/**
 * 4x4 Matrix used for perspective rendering and transformation.
 * The matrix is defined as using a row-major order.
 */
public class Matrix4 {

    // TODO: Rewrite using Vector3s (x, y, z and origin) instead of double[][] (?)

    /**
     * The matrix's internal values defined by a 2-dimensional array.
     */
    private final double[][] matrix = new double[4][4];

    /**
     * Constructs an empty matrix (filled with value 0).
     */
    public Matrix4() {
        fill(0);
    }

    /**
     * Constructs a new matrix from a given matrix. Useful for
     * duplicating matrices. Returns an exact copy.
     * @param matrix The copied matrix.
     */
    public Matrix4(@NotNull Matrix4 matrix) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                this.matrix[x][y] = matrix.getValue(x, y);
            }
        }
    }

    /**
     * Translates (offsets / moves) the transformation matrix's origin.
     * @param vector Vector by which to translate the matrix.
     */
    public void translate(@NotNull Vector3 vector) {
        matrix[3][0] = matrix[3][0] + vector.x;
        matrix[3][1] = matrix[3][1] + vector.y;
        matrix[3][2] = matrix[3][2] + vector.z;
    }

    /**
     * Returns a translated (offset / moved) matrix.
     * @param vector Vector by which to translate the matrix.
     * @return The translated matrix.
     */
    public Matrix4 translated(@NotNull Vector3 vector) {
        Matrix4 matrix = new Matrix4(this);
        matrix.translate(vector);
        return matrix;
    }

    /**
     * Scales the transformation matrix.
     * @param vector Vector by which to multiply the matrix's scale.
     */
    public void scale(@NotNull Vector3 vector) {
        matrix[0][0] *= vector.x;
        matrix[1][1] *= vector.y;
        matrix[2][2] *= vector.z;
    }

    /**
     * Scales a transformation matrix.
     * @param s Value by which to multiply the matrix's scale.
     */
    public void scale(double s) {
        matrix[0][0] *= s;
        matrix[1][1] *= s;
        matrix[2][2] *= s;
    }

    /**
     * Returns a scaled transformation matrix.
     * @param vector Vector by which to multiply the matrix's scale.
     * @return The scaled matrix.
     */
    public Matrix4 scaled(@NotNull Vector3 vector) {
        Matrix4 matrix = new Matrix4(this);
        matrix.scale(vector);
        return matrix;
    }

    /**
     * Returns a scaled transformation matrix.
     * @param s Value by which to multiply the matrix's scale.
     * @return The scaled matrix.
     */
    public Matrix4 scaled(double s) {
        Matrix4 matrix = new Matrix4(this);
        matrix.scale(s);
        return matrix;
    }

    /**
     * Multiplies the matrix by another matrix. Note
     * that matrix multiplication is not commutative, so the
     * order in which two matrices are multiplied affects the result.
     * @param matrix The matrix which to multiply with.
     */
    public void multiply(@NotNull Matrix4 matrix) {
        Matrix4 matrixCopy = new Matrix4(this);
        fill(0);
        for (int row = 0; row < 4; row++) {
            for (int coloumn = 0; coloumn < 4; coloumn++) {
                for (int i = 0; i < 4; i++) {
                    this.matrix[coloumn][row] += matrixCopy.getValue(i, row) * matrix.getValue(coloumn, i);
                }
            }
        }
    }

    /**
     * Returns the matrix multiplied by another matrix. Note
     * that matrix multiplication is not commutative, so the
     * order in which two matrices are multiplied has an effect on the outcome.
     * @param matrix The matrix which to multiply with.
     * @return The multiplied matrix.
     */
    public Matrix4 multiplied(@NotNull Matrix4 matrix) {
        Matrix4 matrixMultiplied = new Matrix4(this);
        matrixMultiplied.multiply(matrix);
        return matrixMultiplied;
    }

    public Vector3 multiply(@NotNull Vector3 vector) {
        return new Vector3(
                vector.dot(new Vector3(matrix[0][0], matrix[1][0], matrix[2][0])),
                vector.dot(new Vector3(matrix[0][1], matrix[1][1], matrix[2][1])),
                vector.dot(new Vector3(matrix[0][2], matrix[1][2], matrix[2][2]))
        );
    }

    /**
     * Constructs a 1-dimensional 16-element float array to be used in OpenGL shader uniforms.
     * <p>
     * NOTE: The resulting array is defined using a column-major order, so transposing is not
     * necessary when parsing the array to OpenGL.
     * @return The matrix as a float array.
     */
    public float[] toUniformFloatArray() {
        float[] fm = new float[16];
        int index = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                fm[index] = (float) matrix[x][y];
                index++;
            }
        }
        return fm;
    }

    /**
     * Returns a projection matrix according to a set of properties.
     * @param fovy (Vertical) Field of view in degrees.
     * @param aspect Aspect ratio. Can be calculated by dividing the screen width by the screen height.
     * @param near Near plane z-value.
     * @param far Far plane z-value. Note that a high difference between near and far plane causes depth
     * precision issues such as z-fighting.
     * @return A projection matrix.
     */
    public static Matrix4 perspective(double fovy, double aspect, double near, double far) {
        fovy = MathUtils.degToRad(fovy);
        double top = java.lang.Math.tan(fovy / 2.0) * near;
        double bottom = -top;
        double right = top * aspect;
        double left = -right;
        return perspective(left, right, top, bottom, near, far);
    }

    /**
     * Returns a projection matrix for perspective rendering.
     * @param left Left edge of the projection plane.
     * @param right Right edge of the projection plane.
     * @param top Top edge of the projection plane.
     * @param bottom Bottom edge of the projection plane.
     * @param near Near plane z-value.
     * @param far Far plane z-value.
     * @return A projection matrix.
     */
    public static Matrix4 perspective(double left, double right, double top, double bottom, double near, double far) {
        Matrix4 matrix = new Matrix4();
        matrix.setValue(0, 0, (2 * near) / (right - left));
        matrix.setValue(1, 1, (2 * near) / (top - bottom));
        matrix.setValue(2, 0, (right + left) / (right - left));
        matrix.setValue(2, 1, (top + bottom) / (top - bottom));
        matrix.setValue(2, 2, -((far + near) / (far - near)));
        matrix.setValue(2, 3, -1);
        matrix.setValue(3, 2, -((2 * far * near) / (far - near)));
        return matrix;
    }

    /**
     * Creates a view (camera) matrix that looks at the given center position
     * from the given eye position.
     * @param eye The position of the camera;
     * @param center The center of view / point to look at.
     * @param up The global up vector, usually (0, 1, 0).
     * @see <a href="https://github.com/g-truc/glm/blob/master/glm/ext/matrix_transform.inl">glm implementation</a>.
     * @return A view matrix.
     */
    public static Matrix4 lookAt(@NotNull Vector3 eye, @NotNull Vector3 center, @NotNull Vector3 up) {
        Vector3 f = center.translated(eye.inverted()).normalized();
        Vector3 s = f.cross(up).normalized();
        Vector3 u = s.cross(f);
        Matrix4 matrix = new Matrix4();
        matrix.fillDiagonal(1);
        matrix.setValue(0, 0, s.x);
        matrix.setValue(1, 0, s.y);
        matrix.setValue(2, 0, s.z);
        matrix.setValue(0, 1, u.x);
        matrix.setValue(1, 1, u.y);
        matrix.setValue(2, 1, u.z);
        matrix.setValue(0, 2, -f.x);
        matrix.setValue(1, 2, -f.y);
        matrix.setValue(2, 2, -f.z);
        matrix.setValue(3, 0, -s.dot(eye));
        matrix.setValue(3, 1, -u.dot(eye));
        matrix.setValue(3, 2, f.dot(eye));
        return matrix;
    }

    /**
     * Creates a transform matrix for translation, rotation and scaling.
     * Default scale is (1, 1, 1) and default translation is (0, 0, 0);
     * @return A transformation matrix.
     */
    public static Matrix4 transform() {
        Matrix4 matrix4 = new Matrix4();
        matrix4.fillDiagonal(1.0);
        return matrix4;
    }

    /**
     * Rotates the translation matrix on the local x-Axis.
     * @param angle The angle (in radians) by which to rotate.
     */
    public void rotateX(double angle) {
        multiply(getRotatedX(angle));
    }

    /**
     * Rotates the translation matrix on the local y-Axis.
     * @param angle The angle (in radians) by which to rotate.
     */
    public void rotateY(double angle) {
        multiply(getRotatedY(angle));
    }

    /**
     * Rotates the translation matrix on the local z-Axis.
     * @param angle The angle (in radians) by which to rotate.
     */
    public void rotateZ(double angle) {
        multiply(getRotatedZ(angle));
    }

    /*

    public void rotate(Vector3 axis, double angle) {
        // TODO: Implement
    }

     */

    /**
     * Returns a rotation matrix for a specified rotation along the x-Axis.
     * Multiplying this matrix with a transformation matrix
     * (transformationMatrix * rotationMatrix) results in the transformation
     * matrix being rotated.
     * @param angle The rotation angle (in radians).
     * @return A rotation matrix.
     */
    public static Matrix4 getRotatedX(double angle) {
        Matrix4 matrix = new Matrix4();
        matrix.fillDiagonal(1);
        matrix.setValue(0, 0, 1.0);
        matrix.setValue(1, 1, java.lang.Math.cos(angle));
        matrix.setValue(1, 2, java.lang.Math.sin(angle));
        matrix.setValue(2, 1, -java.lang.Math.sin(angle));
        matrix.setValue(2, 2, java.lang.Math.cos(angle));
        return matrix;
    }

    /**
     * Returns a rotation matrix for a specified rotation along the y-Axis.
     * Multiplying this matrix with a transformation matrix
     * (transformationMatrix * rotationMatrix) results in the transformation
     * matrix being rotated.
     * @param angle The rotation angle (in radians).
     * @return A rotation matrix.
     */
    public static Matrix4 getRotatedY(double angle) {
        Matrix4 matrix = new Matrix4();
        matrix.fillDiagonal(1);
        matrix.setValue(0, 0, java.lang.Math.cos(angle));
        matrix.setValue(0, 2, -java.lang.Math.sin(angle));
        matrix.setValue(1, 1, 1.0);
        matrix.setValue(2, 0, java.lang.Math.sin(angle));
        matrix.setValue(2, 2, java.lang.Math.cos(angle));
        return matrix;
    }

    /**
     * Returns a rotation matrix for a specified rotation along the z-Axis.
     * Multiplying this matrix with a transformation matrix
     * (transformationMatrix * rotationMatrix) results in the transformation
     * matrix being rotated.
     * @param angle The rotation angle (in radians).
     * @return A rotation matrix.
     */
    public static Matrix4 getRotatedZ(double angle) {
        Matrix4 matrix = new Matrix4();
        matrix.fillDiagonal(1);
        matrix.setValue(0, 0, java.lang.Math.cos(angle));
        matrix.setValue(0, 1, java.lang.Math.sin(angle));
        matrix.setValue(1, 0, -java.lang.Math.sin(angle));
        matrix.setValue(1, 1, java.lang.Math.cos(angle));
        matrix.setValue(2, 2, 1.0);
        return matrix;
    }

    /**
     * Returns the matrix value at the given x and y coordinates.
     * Throws an error if either ix or iy is out of bounds.
     * @param ix The row index.
     * @param iy The column index.
     * @return The value at the given coordinates.
     */
    public double getValue(int ix, int iy) {
        if (ix > (matrix.length - 1) || iy > (matrix.length - 1)) {
            // TODO: Print index out of bounds error
            return 0;
        }
        return matrix[ix][iy];
    }

    /**
     * Sets the matrix value to the specified value at the given x and y coordinates.
     * Throws an error if either ix or iy is out of bounds.
     * @param ix The row index.
     * @param iy The column index.
     * @param value The value to set.
     */
    public void setValue(int ix, int iy, double value) {
        if (ix > (matrix.length - 1) || iy > (matrix.length - 1)) {
            // TODO: Print index out of bounds error
            return;
        }
        matrix[ix][iy] = value;
    }

    /**
     * Returns the first column of the matrix as a Vector3 (ignoring the 4th value).
     * For transformation matrices, this is the x direction.
     * @return The column as a Vector3.
     */
    public Vector3 getX() {
        return new Vector3(matrix[0][0], matrix[0][1], matrix[0][2]);
    }

    /**
     * Returns the second column of the matrix as a Vector3 (ignoring the 4th value).
     * For transformation matrices, this is the y direction.
     * @return The column as a Vector3.
     */
    public Vector3 getY() {
        return new Vector3(matrix[1][0], matrix[1][1], matrix[1][2]);
    }

    /**
     * Returns the third column of the matrix as a Vector3 (ignoring the 4th value).
     * For transformation matrices, this is the z direction.
     * @return The column as a Vector3.
     */
    public Vector3 getZ() {
        return new Vector3(matrix[2][0], matrix[2][1], matrix[2][2]);
    }

    /**
     * Returns the fourth column of the matrix as a Vector3 (ignoring the 4th value).
     * For transformation matrices, this is origin (or position/translation).
     * @return The column as a Vector3.
     */
    public Vector3 getOrigin() {
        return new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
    }

    public void setX(@NotNull Vector3 vector) {
        matrix[0][0] = vector.x;
        matrix[0][1] = vector.y;
        matrix[0][2] = vector.z;
    }

    public void setY(@NotNull Vector3 vector) {
        matrix[1][0] = vector.x;
        matrix[1][1] = vector.y;
        matrix[1][2] = vector.z;
    }

    public void setZ(@NotNull Vector3 vector) {
        matrix[2][0] = vector.x;
        matrix[2][1] = vector.y;
        matrix[2][2] = vector.z;
    }

    public void setOrigin(@NotNull Vector3 vector) {
        matrix[3][0] = vector.x;
        matrix[3][1] = vector.y;
        matrix[3][2] = vector.z;
    }

    /**
     * Fills the matrix diagonally with the specified value.
     * Used for constructing identity matrices.
     * @param value The value to set.
     */
    private void fillDiagonal(double value) {
        for (int d = 0; d < 4; d++) {
            matrix[d][d] = value;
        }
    }

    /**
     * Sets all the matrix's fields to 0.
     */
    private void fill() {
        fill(0);
    }

    /**
     * Sets all the matrix's fields to the specified value.
     * @param value The value to set.
     */
    private void fill(double value) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                matrix[x][y] = value;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------------\n");
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                stringBuilder.append(matrix[x][y]);
                stringBuilder.append("\t");
            }
            if (y < 3) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
