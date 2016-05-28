package jogl_anaglyph.main;

import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class AnaglyphRenderer implements GLEventListener {
    private StereoCamera stereoCamera;

    public AnaglyphRenderer() {
        super();
        stereoCamera = new StereoCamera(
                2000.0f,   // convergence
                35f,    // eye separation
                1.3333f, // aspect ratio
                45f,  // fov along y in degrees
                10f,   //near clipping
                20000f // far clipping
        );
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        final GLUT glut = new GLUT();
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        // Apply the left frustum
        stereoCamera.applyFrustum(gl, true);
        gl.glColorMask(true, false, false, false);
        placeSceneElements(gl, glut);

        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        stereoCamera.applyFrustum(gl, false);
        gl.glColorMask(false, true, true, false);
        placeSceneElements(gl, glut);

        gl.glColorMask(true, true, true, true);

    }

    private void placeSceneElements(GL2 gl, GLUT glut) {
        gl.glTranslated(-70, -160, -850);

        // rotate the scene for viewing
        gl.glRotatef(-60f, 1f, 0f, 0f);
        gl.glRotatef(-45f, 0f, 0f, 1f);

        gl.glPushMatrix();
        gl.glTranslated(140, 150, 240);
        gl.glColor3f(0.2f, 0.2f, 0.6f);
        glut.glutSolidTorus(40, 200, 20, 30);
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        glut.glutWireTorus(40, 200, 20, 30);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 0, 240);
        gl.glRotatef(90f, 1f, 0f, 0f);
        gl.glColor3f(0.2f, 0.2f, 0.6f);
        glut.glutSolidTorus(40, 200, 20, 30);
        gl.glColor3f(0.7f, 0.7f, 0.7f);
        glut.glutWireTorus(40, 200, 20, 30);
        gl.glPopMatrix();

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

        AnaglyphRenderer anaglyphRenderer = new AnaglyphRenderer();
        glcanvas.addGLEventListener(anaglyphRenderer);
        glcanvas.setSize(800, 800);
        // the window frame
        JFrame frame = new JFrame("Scene example");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
