package jogl_anaglyph.main;

import com.jogamp.opengl.GL2;

public class StereoCamera {
    private float farClippingDistance;
    private float nearClippingDistance;
    private float fov;
    private float convergence;
    private float eyeSeparation;
    private float aspectRatio;

    public StereoCamera(float convergence, float eyeSeparation, float aspectRatio,
                        float fov, float nearClippingDistance, float farClippingDistance) {
        this.convergence = convergence;
        this.eyeSeparation = eyeSeparation;
        this.aspectRatio = aspectRatio;
        this.fov = fov;
        this.nearClippingDistance = nearClippingDistance;
        this.farClippingDistance = farClippingDistance;
    }

    public void applyFrustum(GL2 gl, boolean isLeftFrustum) {
        float top, bottom, left, right;
        top = (float) (nearClippingDistance * Math.tan(fov / 2));
        bottom = -top;
        float a = aspectRatio * (float) (Math.tan(fov / 2) * convergence);

        float b = a - eyeSeparation / 2;
        float c = a + eyeSeparation / 2;

        if (isLeftFrustum) {

            left = -b * nearClippingDistance / convergence;
            right = c * nearClippingDistance / convergence;
            //Set projection matrix
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustum(left, right, bottom, top,
                    nearClippingDistance, farClippingDistance);

            // Displace world to the right
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(eyeSeparation / 2, 0.0f, 0.0f);

        } else {
            left    =  -c * nearClippingDistance/convergence;
            right   =   b * nearClippingDistance/convergence;

            // Set the Projection Matrix
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustum(left, right, bottom, top,
                    nearClippingDistance, farClippingDistance);
            // Displace the world to left
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(-eyeSeparation/2, 0.0f, 0.0f);
        }

    }

}
