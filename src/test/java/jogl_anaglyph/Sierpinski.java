package jogl_anaglyph;
import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Sierpinski implements GLEventListener {

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        //triangle(gl);
        sierpinski(gl, 5);
    }

    void triangle(GL2 gl) {
        gl.glBegin(GL2.GL_LINE_LOOP);
        {
            gl.glVertex3f(0.0f, 1f, 0.0f);
            gl.glVertex3f(-1f, -1.0f, 0.0f);
            gl.glVertex3f(1f, -1.0f, 0.0f);
        }
        gl.glEnd();
    }

    void sierpinski(GL2 gl, int k) {
        if (k == 0) {
            triangle(gl);
        } else {
            gl.glPushMatrix();
            gl.glTranslated(0.0, 0.5 / Math.sqrt(3.0), 0.0);
            gl.glScaled(0.5, 0.5, 0.5);
            sierpinski(gl, k-1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslated(0.25, -0.25 / Math.sqrt(3.0), 0.0);
            gl.glScaled(0.5, 0.5, 0.5);
            sierpinski(gl, k-1);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glTranslated(-0.25, -0.25 / Math.sqrt(3.0), 0.0);
            gl.glScaled(0.5, 0.5, 0.5);
            sierpinski(gl, k-1);
            gl.glPopMatrix();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {

        // getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);

        Sierpinski sierpinski = new Sierpinski();
        glcanvas.addGLEventListener(sierpinski);
        glcanvas.setSize(600, 600);
        // the window frame
        JFrame frame = new JFrame("Scene example");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}