package test;

import java.awt.geom.AffineTransform;

public class AffineTest {

	public static void main(String args[]){
		AffineTransform transform = new AffineTransform();
		//transform.rotate(Math.toRadians(angle), bounds.width / 2, bounds.height / 2);
		
		double x;
		double y;
		double angle = 0;
		double anchorX = 0;
		double anchorY = 0;
		double[] pt = {x, y};
		AffineTransform.getRotateInstance(Math.toRadians(angle), anchorX, anchorY)
		  .transform(pt, 0, pt, 0, 1); // specifying to use this double[] to hold coords
		double newX = pt[0];
		double newY = pt[1];
		Math.acos

	}
}
