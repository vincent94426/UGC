package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.anim;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


public class MetroItemRotate3DAnimation extends Animation {

    private int halfWidth;
    
    private int halfHeight;
    
    private Camera camera = new Camera();
    
    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(500);
        setFillAfter(true);
        
        halfWidth = width / 2;
        halfHeight = height / 2;
        setInterpolator(new LinearInterpolator());
        
        
    }
    
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
            
        final Matrix matrix = t.getMatrix();
        
        camera.save();
        camera.translate(0.0f , 0.0f, (300 - 300.0f * interpolatedTime));
        camera.rotateY(360 * interpolatedTime);
        
        camera.getMatrix(matrix);
        
        matrix.preTranslate(-halfWidth, -halfHeight);
        matrix.postTranslate(halfWidth, halfHeight);
        
        camera.restore();
    }
}
