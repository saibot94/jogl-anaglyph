package jogl_anaglyph.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {


    public float xpos, yrot, zpos, heading;
    private float walkbias = 0.0f;
    private float walkbiasangle = 0.0f;
    private float lookupdown = 0.0f;





    public float[] getTrans(){
        float[] trans = new float[3];
        trans[0]= -xpos;
        trans[1]= -walkbias - 0.43f;
        trans[2]= -zpos;

        return trans;
    }

    public float getLookUpDown(){
        return lookupdown;
    }

    public float getYrot(){
        return yrot;
    }


    public void forward(){
        xpos -= (float)Math.sin(Math.toRadians(heading)) * 3f;
        zpos -= (float)Math.cos(Math.toRadians(heading)) * 3f;
        if (walkbiasangle >= 359.0f)
            walkbiasangle = 0.0f;
        else
            walkbiasangle += 10.0f;
        walkbias = (float)Math.sin(Math.toRadians(walkbiasangle))/20.0f;
     }

    public void backward(){
        xpos += (float)Math.sin(Math.toRadians(heading)) * 3f;
        zpos += (float)Math.cos(Math.toRadians(heading)) * 3f;
        if (walkbiasangle >= 359.0f)
            walkbiasangle = 0.0f;
        else
            walkbiasangle += 10.0f;
        walkbias = (float)Math.sin(Math.toRadians(walkbiasangle))/20.0f;
    }

    public void left() {
        heading += 2.0f;
        yrot = heading;
    }

    public void right(){
        heading -= 2.0f;
        yrot = heading;
    }

}
