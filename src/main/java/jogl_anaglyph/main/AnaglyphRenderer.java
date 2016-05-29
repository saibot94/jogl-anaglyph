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
        implements GLEventListener,
        KeyListener,
        MouseListener,
        MouseMotionListener {
    private Animator an;
    public final static int LEFT_KEY = 37;
    public final static int UP_KEY = 38;
    public final static int RIGHT_KEY = 39;
    public final static int DOWN_KEY = 40;

    // Define camera variables
    float cameraAzimuth = 0.0f, cameraSpeed = 0.0f, cameraElevation = 0.0f;

    // Set camera at (0, 0, -20)

    float cameraCoordsPosx = 0.0f, cameraCoordsPosy = 0.0f, cameraCoordsPosz = -20.0f;
    // Set camera orientation
    float cameraUpx = 0.0f, cameraUpy = 1.0f, cameraUpz = 0.0f;

    private final GLCanvas canvas;
    private StereoCamera stereoCamera;

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

        this.canvas = canvas;
        this.an = new Animator(canvas);
        initializeJogl();
    }

    public Animator getAn() {
        return an;
    }

    public void moveCamera() {
        float[] tmp = polarToCartesian(cameraAzimuth, cameraSpeed, cameraElevation);
        cameraCoordsPosx += tmp[0];
        cameraCoordsPosy += tmp[1];
        cameraCoordsPosz += tmp[2];
    }

    public void aimCamera(GL2 gl, GLU glu) {
        gl.glLoadIdentity();
        float[] tmp = polarToCartesian(cameraAzimuth, 100.0f, cameraElevation);
        float[] camUp = polarToCartesian(cameraAzimuth, 100.0f, cameraElevation + 90);

        cameraUpx += camUp[0];
        cameraUpx += camUp[1];
        cameraUpx += camUp[2];
        glu.gluLookAt(cameraCoordsPosx, cameraCoordsPosy, cameraCoordsPosz,
                cameraCoordsPosx + tmp[0], cameraCoordsPosy + tmp[1],
                cameraCoordsPosz + tmp[2], cameraUpx, cameraUpy, cameraUpz);
    }

    private float[] polarToCartesian(float azimuth, float length, float altitude) {
        float[] result = new float[3];
        float x, y, z;

        float theta = (float) Math.toRadians(90 - azimuth);
        float tantheta = (float) Math.tan(theta);
        float radianAlt = (float) Math.toRadians(altitude);
        float cospi = (float) Math.cos(radianAlt);

        x = (float) Math.sqrt((length * length) / (tantheta * tantheta + 1));
        z = tantheta * x;
        x = -x;
        if ((azimuth >= 180 && azimuth <= 360) || azimuth == 0) {
            x = -x;
            z = -z;
        }
        y = (float) (Math.sqrt(z * z + x * x) * Math.sin(radianAlt));
        if (length < 0) {
            x = -x;
            z = -z;
            y = -y;
        }

        x = x * cospi;
        z = z * cospi;

        result[0] = x;
        result[1] = y;
        result[2] = z;

        return result;
    }

    private void initializeJogl() {
        this.canvas.addKeyListener(this);
        this.canvas.addMouseMotionListener(this);
        this.canvas.addMouseListener(this);
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
        gl.glLoadIdentity();
        aimCamera(gl, glu);
        moveCamera();

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            cameraElevation-=100;
            System.out.println("Elevated camera..");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
