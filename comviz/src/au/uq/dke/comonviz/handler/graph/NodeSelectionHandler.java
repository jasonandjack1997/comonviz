package au.uq.dke.comonviz.handler.graph;

import java.awt.event.InputEvent;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.graph.node.DefaultGraphNode;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;

/**
 * Handles node selection - listens for mouse pressed events on the canvas and
 * updates the selection accordingly.
 * 
 * @author Chris
 * @since 8-Nov-07
 */
public class NodeSelectionHandler extends PBasicInputEventHandler {

	private NodeCollection selectedNodes;

	public NodeSelectionHandler(NodeCollection selectedNodes) {
		super();
		this.selectedNodes = selectedNodes;
		PInputEventFilter filter = new PInputEventFilter();
		filter.rejectAllEventTypes();
		filter.setOrMask(InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK);
		filter.setAcceptsMousePressed(true);
		setEventFilter(filter);
	}

	// OVERRIDES

	@Override
	public void mousePressed(PInputEvent e) {
		PNode node = e.getPickedNode();
		if (node instanceof GraphNode) {
			node.moveToFront();
			nodePressed(e, (GraphNode) node);
		} else if (node instanceof GraphArc) {
			node.moveToFront();
			arcPressed(e, (GraphArc) node);
		} else if (node instanceof PCamera) {
			cameraPressed(e, (PCamera) node);
		}

		super.mousePressed(e);
	}

	private void arcPressed(PInputEvent e, GraphArc arc) {

	}

	private void cameraPressed(PInputEvent e, PCamera camera) {
		// clear selection
		selectedNodes.clear();
		RotationHandler.ANCHOR_X = -1;
		RotationHandler.ANCHOR_Y = -1;
	}

	private void nodePressed(PInputEvent e, GraphNode displayNode) {
		boolean nodeAdded = true;
		if (e.isControlDown()) {
			nodeAdded = selectedNodes.addOrRemoveNode2(displayNode);
		} else if (e.isShiftDown()) {
			selectedNodes.addNode(displayNode);
		} else {
			if (e.isRightMouseButton()) {
				// right click - only set if the node isn't already selected
				if (!selectedNodes.containsNode(displayNode)) {
					selectedNodes.setNode(displayNode);
				}
			} else {
				// left click - always select just this node
				selectedNodes.setNode(displayNode);
			}
		}
		if (nodeAdded) {
//			RotationHandler.ANCHOR_X = ((DefaultGraphNode) displayNode)
//					.getCenterX();
//			RotationHandler.ANCHOR_Y = ((DefaultGraphNode) displayNode)
//					.getCenterY();
		}
	}

}
