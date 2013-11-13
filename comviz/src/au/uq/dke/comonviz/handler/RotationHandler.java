package au.uq.dke.comonviz.handler;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.ComonvizGraphModel;
import au.uq.dke.comonviz.graph.node.DefaultGraphNode;
import au.uq.dke.comonviz.treeUtils.TreeInfoManager;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Handles zooming.
 * 
 * @author Chris
 * @since 8-Nov-07
 */
public class RotationHandler extends PBasicInputEventHandler {

	public static double ANCHOR_X = -1;
	public static double ANCHOR_Y = -1;

	private double currentX;
	private double currentY;
	private double lastX;
	private double lastY;
	private double deltaRadians;
	private double vecCurrentX;
	private double vecCurrentY;
	private double vecLastX;
	private double vecLastY;
	private GraphNode anchorGraphNode;

	public RotationHandler(PCamera camera) {
		super();
	}

	@Override
	public void mousePressed(PInputEvent event) {

		// TODO Auto-generated method stub
		// if (event.isMiddleMouseButton()) {
		// if (ANCHOR_X >= 0 && ANCHOR_Y >= 0) {
		// return;
		// }
		// this.ANCHOR_X = event.getPosition().getX();
		// this.ANCHOR_Y = event.getPosition().getY();
		// }
		super.mousePressed(event);
	}

	@Override
	public void mouseDragged(PInputEvent event) {
		// TODO Auto-generated method stub
		if (!event.isMiddleMouseButton()) {
			return;
		}

		GraphNode oldNodeInTreeManager = (GraphNode) TreeInfoManager
				.getTreeRoot().getUserObject();
		anchorGraphNode = EntryPoint.getGraphModel()
				.getNode(oldNodeInTreeManager.getUserObject());
		ANCHOR_X = ((DefaultGraphNode) anchorGraphNode).getCenterX();
		ANCHOR_Y = ((DefaultGraphNode) anchorGraphNode).getCenterY();

		if (this.ANCHOR_X == -1l && this.ANCHOR_Y == -1l) {

		}

		if (this.ANCHOR_X != -1l && this.ANCHOR_Y != -1l) {
			PNode node = event.getPickedNode();
			if (node instanceof GraphNode) {
				node.moveToFront();
			}
			currentX = event.getPosition().getX();
			currentY = event.getPosition().getY();
			lastX = currentX - event.getDelta().getWidth();
			lastY = currentY - event.getDelta().getHeight();

			vecCurrentX = (currentX - ANCHOR_X);
			vecCurrentY = (currentY - ANCHOR_Y);

			int sign = (vecCurrentX * vecLastY - vecLastX * vecCurrentY) > 0 ? -1
					: 1;

			vecLastX = lastX - ANCHOR_X;
			vecLastY = lastY - ANCHOR_Y;

			double currentLengthPower2 = Math.sqrt(vecCurrentX * vecCurrentX
					+ vecCurrentY * vecCurrentY);
			double lastLengthPower2 = Math.sqrt(vecLastX * vecLastX + vecLastY
					* vecLastY);

			if (currentLengthPower2 == 0 || lastLengthPower2 == 0) {
				return;
			}

			double theta = (vecCurrentX * vecLastX + vecCurrentY * vecLastY)
					/ (currentLengthPower2 * lastLengthPower2);
			deltaRadians = Math.acos(theta);

			AffineTransform rotateTransform = AffineTransform
					.getRotateInstance(sign * deltaRadians, ANCHOR_X, ANCHOR_Y);

			// rotate all visible nodes
			Collection<GraphNode> visibleNodes = EntryPoint.getGraphModel().getVisibleNodes();
			List<OWLEntity> desendantsNode = null;
//			List<OWLEntity> desendantsNode = EntryPoint.getGraphModel().getDesendantList(
//					(OWLEntity) anchorGraphNode.getUserObject(), false);

			for (OWLEntity nodeEntity : desendantsNode) {
				// if (graphNode.getText().contains("Compliance")) {
				{
					GraphNode graphNode = EntryPoint.getGraphModel().getNode(nodeEntity);
					if (graphNode == null) {
						continue;
					}
					Point2D lastLocation = new Point2D.Double(
							((DefaultGraphNode) graphNode).getCenterX(),
							((DefaultGraphNode) graphNode).getCenterY());
					Point2D newLocation = new Point2D.Double();
					if (!newLocation.equals(lastLocation)) {
						rotateTransform.transform(lastLocation, newLocation);
						graphNode.setLocation(
								newLocation.getX()
										- ((DefaultGraphNode) graphNode)
												.getWidth() / 2,
								newLocation.getY()
										- ((DefaultGraphNode) graphNode)
												.getHeight() / 2);
					}
				}
			}

		}
		super.mouseDragged(event);

	}

	@Override
	public void mouseReleased(PInputEvent event) {
		// TODO Auto-generated method stub
		// this.ANCHOR_X = -1l;
		// this.ANCHOR_Y = -1l;
		super.mouseReleased(event);
	}

	@Override
	public void mouseMoved(PInputEvent event) {
		// TODO Auto-generated method stub
		super.mouseMoved(event);
	}

}
