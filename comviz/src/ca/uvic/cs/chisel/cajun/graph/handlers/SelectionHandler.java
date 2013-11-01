package ca.uvic.cs.chisel.cajun.graph.handlers;

import java.awt.event.InputEvent;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.DefaultGraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import comonviz.EntryPoint;
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
public class SelectionHandler extends PBasicInputEventHandler {

	private NodeCollection selectedNodes;

	public SelectionHandler(NodeCollection selectedNodes) {
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
				// also select node in the tree explorer
				JTree jTree = EntryPoint.gc.getView().getjTree();
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) jTree
						.getModel().getRoot();
				Enumeration<?> enumeration = rootNode.breadthFirstEnumeration();
				while (enumeration.hasMoreElements()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
							.nextElement();
					GraphNode graphNode = (GraphNode) node.getUserObject();
					if (graphNode.getUserObject() == displayNode
							.getUserObject()) {
						TreePath treePath = new TreePath(
								((DefaultTreeModel) jTree.getModel())
										.getPathToRoot(node));
						jTree.scrollPathToVisible(treePath);
						jTree.setSelectionPath(treePath);
						// jTree.startEditingAtPath(treePath);
					}
				}

			}
		}
		if (nodeAdded) {
			RotationHandler.ANCHOR_X = ((DefaultGraphNode) displayNode)
					.getCenterX();
			RotationHandler.ANCHOR_Y = ((DefaultGraphNode) displayNode)
					.getCenterY();
		}
	}

}
