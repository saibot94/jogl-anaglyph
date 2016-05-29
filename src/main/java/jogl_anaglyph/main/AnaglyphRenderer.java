package jogl_anaglyph.main;

import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.awt.event.*;

public class AnaglyphRenderer extends JFrame
        implements GLEventListener {
    private Animator an;
    public final static int LEFT_KEY = 37;
    public final static int UP_KEY = 38;
    public final static int RIGHT_KEY = 39;
    public final static int DOWN_KEY = 40;

    private StereoCamera stereoCamera;
    private Input input;
    private Camera camera;

    public AnaglyphRenderer(GLCanvas canvas) {
        super();
        stereoCamera = new StereoCamera(
                2000.0f,   // convergence
                35f,    // eye separation
                1.3333f, // aspect ratio
                45f,  // fov along y in degrees
                10f,   //near clipping
                20000f // far clipping
        );
        this.an = new Animator(canvas);
        initializeJogl(canvas);
    }

    public Animator getAn() {
        return an;
    }


    private void initializeJogl(GLCanvas canvas) {
        this.camera = new Camera();
        this.input = new Input(canvas, camera);

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        final GLUT glut = new GLUT();
        final GLU glu = new GLU();
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);


        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //gl.glLoadIdentity();

        // Compute rotation and translation angles

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
        handleCamera(gl);

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

    private void handleCamera(GL2 gl) {

        float[] trans = camera.getTrans();
        float look = camera.getLookUpDown();
        float sceneroty = 360.0f - camera.getYrot();

        // Perform operations on the scene
        gl.glRotatef(look, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(sceneroty, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(trans[0], trans[1], trans[2]);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {

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

        AnaglyphRenderer anaglyphRenderer = new AnaglyphRenderer(glcanvas);
        glcanvas.addGLEventListener(anaglyphRenderer);
        glcanvas.setFocusable(true);
        glcanvas.setSize(800, 800);
        // the window frame

        anaglyphRenderer.getContentPane().add(glcanvas);
        anaglyphRenderer.setSize(anaglyphRenderer.getContentPane().getPreferredSize());
        anaglyphRenderer.setResizable(false);
        anaglyphRenderer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        anaglyphRenderer.setVisible(true);

        glcanvas.requestFocus();
        anaglyphRenderer.getAn().start();
    }

}
