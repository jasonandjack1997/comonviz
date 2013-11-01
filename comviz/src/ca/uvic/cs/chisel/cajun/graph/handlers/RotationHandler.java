package ca.uvic.cs.chisel.cajun.graph.handlers;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;

import ca.uvic.cs.chisel.cajun.graph.node.DefaultGraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import comonviz.EntryPoint;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Handles zooming.
 * 
 * @author Chris
 * @since 8-Nov-07
 */
public class RotationHandler extends PBasicInputEventHandler {

	private double anchorX = -1;
	private double anchorY = -1;

	private double currentX;
	private double currentY;
	private double lastX;
	private double lastY;
	private double deltaRadians;
	private double vecCurrentX;
	private double vecCurrentY;
	private double vecLastX;
	private double vecLastY;

	public RotationHandler(PCamera camera) {
		super();
	}

	@Override
	public void mousePressed(PInputEvent event) {
		// TODO Auto-generated method stub
		if (event.isMiddleMouseButton()) {
			if (anchorX >= 0 && anchorY >= 0) {
				return;
			}
			this.anchorX = event.getPosition().getX();
			this.anchorY = event.getPosition().getY();
		}
		super.mousePressed(event);
	}

	@Override
	public void mouseDragged(PInputEvent event) {
		// TODO Auto-generated method stub
		if (!event.isMiddleMouseButton()) {
			return;
		}
		if (this.anchorX != -1l && this.anchorY != -1l) {
			currentX = event.getPosition().getX();
			currentY = event.getPosition().getY();
			lastX = currentX - event.getDelta().getWidth();
			lastY = currentY - event.getDelta().getHeight();

			// rotate all visible nodes
			Collection<GraphNode> visibleNodes = EntryPoint.getGc().getModel()
					.getVisibleNodes();
			for (GraphNode graphNode : visibleNodes) {
				//if (graphNode.getText().contains("Compliance")) {
				{	

					vecCurrentX = (currentX - anchorX);
					vecCurrentY = (currentY - anchorY);

					vecLastX = lastX - anchorX;
					vecLastY = lastY - anchorY;

					double currentLengthPower2 = Math.sqrt(vecCurrentX
							* vecCurrentX + vecCurrentY * vecCurrentY);
					double lastLengthPower2 = Math.sqrt(vecLastX * vecLastX
							+ vecLastY * vecLastY);

					if (currentLengthPower2 == 0 || lastLengthPower2 == 0) {
						return;
					}

					
					double theta = (vecCurrentX * vecLastX + vecCurrentY
							* vecLastY)
							/ (currentLengthPower2 * lastLengthPower2);
					deltaRadians = Math.acos(theta);

					double degree = Math.toDegrees(deltaRadians);
					if (Math.abs(deltaRadians)>=0.5) {
						int a = 1;
					}

					//deltaRadians = 0.1;

					AffineTransform rotateTransform = AffineTransform
							.getRotateInstance(deltaRadians, anchorX, anchorY);
					Point2D lastLocation = new Point2D.Double(
							((DefaultGraphNode) graphNode).getX(),
							((DefaultGraphNode) graphNode).getY());
					Point2D newLocation = new Point2D.Double();
					if (!newLocation.equals(lastLocation)) {
						rotateTransform.transform(lastLocation, newLocation);
						graphNode.setLocation(newLocation.getX(),
								newLocation.getY());
					}
				}
			}

		}
		super.mouseDragged(event);

	}

	@Override
	public void mouseReleased(PInputEvent event) {
		// TODO Auto-generated method stub
		this.anchorX = -1l;
		this.anchorY = -1l;
		super.mouseReleased(event);
	}

	@Override
	public void mouseMoved(PInputEvent event) {
		// TODO Auto-generated method stub
		super.mouseMoved(event);
	}

}
