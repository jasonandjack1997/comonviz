package au.uq.dke.comonviz.handler;

import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Handles input events on the graph.
 * 
 * @author seanf
 */
public class NodeExpandCollapseListener extends PBasicInputEventHandler {
	private static final int DOUBLE_CLICK = 2;

	public void mousePressed(PInputEvent event) {

		if (event.isLeftMouseButton()) {
			if (event.getClickCount() == DOUBLE_CLICK) {
				if (event.getPickedNode() instanceof GraphNode) {
					expandCollapseNode((GraphNode) event.getPickedNode());
				}
			}
		}
	}

	/**
	 * Expands a node if it is not already expanded, otherwise it collapses it.
	 * 
	 * @param graphNode
	 *            The node to expand or collapse.
	 */
	private void expandCollapseNode(GraphNode graphNode) {
		graphNode.setHighlighted(false);
		graphNode.moveToFront();

		List<GraphNode> children = EntryPoint.getGraphModel().getChildren(
				graphNode);

		boolean isExpanded = false;// if any one of the children is visible,
									// then it is expanded
		
		try{
		for (GraphNode child : children) {
			if (child.isVisible() == true) {
				isExpanded = true;
				break;
			}
		}
		if (isExpanded) {// hide all descendants
			for (GraphNode desendant : EntryPoint.getGraphModel()
					.getDesendants(graphNode)) {
				desendant.setVisible(false);
			}

		} else {// only show children
			for (GraphNode child : children) {
				child.setVisible(true);
			}
			//EntryPoint.getFilterManager().getNodeLevelFilter().setVisibleLevel(visibleLevel);
		}
		}catch(NullPointerException e){
			e.printStackTrace();
		}

		EntryPoint.getFlatGraph().performLayoutWithoutNodeFilters();
		return;
	}

}
