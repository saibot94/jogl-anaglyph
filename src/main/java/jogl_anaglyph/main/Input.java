package jogl_anaglyph.main;

import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener, Runnable {
    private final Camera camera;
    private boolean keyBack;
    private boolean keyLeft;
    private boolean keyForward;
    private boolean keyRight;

    private Thread camAnimator;
    private boolean keyDown;
    private boolean keyUp;

    public Input(GLCanvas glCanvas, Camera camera) {
        glCanvas.addKeyListener(this);
        this.camera = camera;
        this.keyForward = false;
        this.keyLeft = false;
        this.keyRight = false;
        this.keyBack = false;
        this.keyUp = false;
        this.keyDown =false;

        this.camAnimator = new Thread(this);
        this.camAnimator.start();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            this.keyForward = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            this.keyLeft = true;
        }
        if(keyCode == KeyEvent.VK_D){
            this.keyRight = true;
        }
        if(keyCode == KeyEvent.VK_S){
            this.keyBack = true;
        }
        if(keyCode == KeyEvent.VK_R){
            this.keyUp = true;
        }
        if(keyCode == KeyEvent.VK_F){
            this.keyDown = true;
        }


        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            this.keyForward = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            this.keyLeft = false;
        }

        if(keyCode == KeyEvent.VK_D){
            this.keyRight = false;
        }

        if(keyCode == KeyEvent.VK_S){
            this.keyBack = false;
        }

        if(keyCode == KeyEvent.VK_R){
            this.keyUp = false;
        }
        if(keyCode == KeyEvent.VK_F){
            this.keyDown = false;
        }
    }

    @Override
    public void run() {
        //this.camera = Camera.getInstance()
        while (true) {
            if (this.keyForward) {
                this.camera.forward();
            }
            if (this.keyLeft) {
                this.camera.left();
            }
            if (this.keyRight){
                this.camera.right();
            }
            if(this.keyBack){
                this.camera.backward();
            }
            if(this.keyUp){
                this.camera.lookup();
            }
            if(this.keyDown){
                this.camera.lookdown();
            }

            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
