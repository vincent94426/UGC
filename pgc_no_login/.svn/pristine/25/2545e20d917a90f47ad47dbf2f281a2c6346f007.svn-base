package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation
{
  private Camera mCamera;
  private final float mCenterX;
  private final float mCenterY;
  private final float mFromDegrees;
  private final float mToDegrees;

  public Rotate3dAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.mFromDegrees = paramFloat1;
    this.mToDegrees = paramFloat2;
    this.mCenterX = paramFloat3;
    this.mCenterY = paramFloat4;
  }

  public static Animation GetFlyInAnimation()
  {
    Rotate3dAnimation localRotate3dAnimation = new Rotate3dAnimation(-90.0F, 0.0F, 0.0F, 65.0F);
    localRotate3dAnimation.setDuration(350L);
    localRotate3dAnimation.setInterpolator(new DecelerateInterpolator());
    localRotate3dAnimation.setStartOffset(0);
    return localRotate3dAnimation;
  }

  public static Animation GetFlyOutAnimation()
  {
    Rotate3dAnimation localRotate3dAnimation = new Rotate3dAnimation(0.0F, -90.0F, 0.0F, 65.0F);
    localRotate3dAnimation.setDuration(350L);
    localRotate3dAnimation.setInterpolator(new AccelerateInterpolator());
    localRotate3dAnimation.setFillAfter(true);
    return localRotate3dAnimation;
  }

  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f1 = this.mFromDegrees;
    float f2 = f1 + paramFloat * (this.mToDegrees - f1);
    f1 = this.mCenterX;
    float f3 = this.mCenterY;
    Camera localCamera = this.mCamera;
    Matrix localMatrix = paramTransformation.getMatrix();
    localCamera.save();
    localCamera.rotateY(f2);
    localCamera.getMatrix(localMatrix);
    localCamera.restore();
    localMatrix.preTranslate(-f1, -f3);
    localMatrix.postTranslate(f1, f3);
  }

  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mCamera = new Camera();
  }
}