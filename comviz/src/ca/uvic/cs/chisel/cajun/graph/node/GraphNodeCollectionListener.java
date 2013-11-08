package ca.uvic.cs.chisel.cajun.graph.node;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNodeCollectionEvent;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;

/**
 * Notifies listeners that the {@link NodeCollection} changed.
 *
 * @author Chris
 * @since  20-Nov-07
 */
public interface GraphNodeCollectionListener {

	/**
	 * Notifies listeners that the {@link NodeCollection} changed.
	 * @param evt
	 */
	public void collectionChanged(GraphNodeCollectionEvent evt);
	
}
