package au.uq.dke.comonviz.filter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.Graph;
import ca.uvic.cs.chisel.cajun.graph.GraphItem;
import ca.uvic.cs.chisel.cajun.graph.GraphModel;
import ca.uvic.cs.chisel.cajun.graph.GraphModelAdapter;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;

public class FilterManager {

	private Graph graph;
	private List<FilterChangedListener> listeners;
	private List<GraphFilter> filters;

	private NodeLevelFilter nodeLevelFilter = new NodeLevelFilter();
	public NodeLevelFilter getNodeLevelFilter() {
		return nodeLevelFilter;
	}

	private ArcTypeFilter arcTypeFilter = new ArcTypeFilter();

	public ArcTypeFilter getArcTypeFilter() {
		return arcTypeFilter;
	}

	// maps the node types to their visibilities
	protected static Map<Object, Boolean> nodeTypesVisibilityMap = new HashMap<Object, Boolean>();

	public FilterManager() {
		// this listens for when a new model is set

		this.listeners = new ArrayList<FilterChangedListener>();
		this.filters = new ArrayList<GraphFilter>();

		// this.nodeTypesVisibilityMap = new HashMap<Object, Boolean>();

		// populate the node and arc types map (all visible by default)
	}

	public void addListeners() {
		this.graph = EntryPoint.getFlatGraph();
		NodeAndArcTypeListener listener = new NodeAndArcTypeListener();
		this.graph.addPropertyChangeListener(listener);
		// this listens for changes to the model (node/arc types added etc)
		// by adding it to the graph it will always be attached to the current
		// model even when a new model is set
		this.graph.addGraphModelListener(listener);
		updateNodeAndArcTypes();

		// add the node and arc type filters
		filters.add(new NodeTypeFilter());
		filters.add(arcTypeFilter);
		filters.add(nodeLevelFilter);
		

	}

	public void addFilterChangedListener(FilterChangedListener listener) {
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	public void removeFilterChangedListener(FilterChangedListener listener) {
		this.listeners.remove(listener);
	}

	public void addFilter(GraphFilter filter) {
		if (!this.filters.contains(filter)) {
			this.filters.add(filter);
			fireFiltersChanged();
		}
	}

	public void removeFilter(GraphFilter filter) {
		if (this.filters.remove(filter)) {
			fireFiltersChanged();
		}
	}

	protected void fireFiltersChanged() {
		if (listeners.size() > 0) {
			ArrayList<FilterChangedListener> copy = new ArrayList<FilterChangedListener>(
					listeners);
			FilterChangedEvent fce = new FilterChangedEvent(this);
			for (FilterChangedListener listener : copy) {
				listener.filtersChanged(fce);
			}
		}
	}

	public int getFilterCount() {
		return filters.size();
	}

	public boolean hasFilters() {
		return (filters.size() > 0);
	}

	public void applyFilters(GraphModel model) {
		applyNodeFilters(model);
		applyArcFilters(model);
	}

	private void applyNodeFilters(GraphModel model) {
		Collection<GraphNode> nodes = model.getAllNodes();
		for (GraphNode node : nodes) {
			// boolean oldVisibility = node.isVisible();
			boolean newVisibility = true; // visible by default
			for (GraphFilter filter : filters) {
				// only apply this filter if it is a node filter
				if (filter.isNodeFilter()) {
					newVisibility = filter.isVisible(node);

					// we stop once one filter says this node should be hidden
					if (!newVisibility) {
						break;
					}
				}
			}
			// if (oldVisibility != newVisibility) {
			node.setVisible(newVisibility);
			// }
		}
	}

	private void applyArcFilters(GraphModel model) {
		Collection<GraphArc> arcs = model.getAllArcs();
		for (GraphArc arc : arcs) {
			boolean oldVisibility = arc.isVisible();
			boolean newVisibility = true; // visible by default
			for (GraphFilter filter : filters) {
				// only apply this filter if it is an arc filter
				if (filter.isArcFilter()) {
					newVisibility = filter.isVisible(arc);

					// we stop once one filter says this arc should be hidden
					if (!newVisibility) {
						break;
					}
				}
			}
			if (oldVisibility != newVisibility) {
				arc.setVisible(newVisibility);
			}
		}
	}

	public Collection<Object> getNodeTypes() {
		return new HashSet<Object>(nodeTypesVisibilityMap.keySet());
	}

	public Map<Object, Boolean> getNodeTypesMap() {
		return new HashMap<Object, Boolean>(nodeTypesVisibilityMap);
	}

	public boolean isNodeTypeVisible(Object nodeType) {
		if (nodeTypesVisibilityMap.containsKey(nodeType)) {
			return nodeTypesVisibilityMap.get(nodeType);
		}
		// visible by default
		return true;
	}

	public void setNodeTypeVisible(Object nodeType, boolean visible) {
		// visible by default
		boolean old = true;
		if (nodeTypesVisibilityMap.containsKey(nodeType)) {
			old = nodeTypesVisibilityMap.get(nodeType);
		}
		if (old != visible) {
			nodeTypesVisibilityMap.put(nodeType, visible);
			fireFiltersChanged();
		}
	}

	/**
	 * This gets called when either the graph gets a new model, or when the
	 * model changes (a node or arc is added). In this case the node and arc
	 * types might have been updated, so we need to update our mappings. This
	 * method preserves the current node and arc type visibilities.
	 */
	protected void updateNodeAndArcTypes() {
		updateNodeTypes();
		updateArcTypes();
	}

	private void updateArcTypes() {
		this.getArcTypeFilter().updateArcTypes();
	}

	protected void updateNodeTypes() {
		GraphModel model = graph.getModel();
		// ensure that the new node types match the old ones,
		// preserving the original node type visibilities
		Collection<Object> newNodeTypes = model.getNodeTypes();
		if (newNodeTypes.isEmpty()) {
			nodeTypesVisibilityMap.clear();
		} else {
			// first remove any node types that no longer exist
			for (Iterator<Object> iter = nodeTypesVisibilityMap.keySet()
					.iterator(); iter.hasNext();) {
				Object oldNodeType = iter.next();
				if (!newNodeTypes.contains(oldNodeType)) {
					iter.remove();
				}
			}
			// now add any that don't already exist (visible by default)
			for (Object nodeType : newNodeTypes) {
				if (!nodeTypesVisibilityMap.containsKey(nodeType)) {
					nodeTypesVisibilityMap.put(nodeType, true);
				}
			}
		}
	}

	/**
	 * Listens for {@link Graph} property change events, and for graph model
	 * events and updates the node and arc types visibility maps.
	 * 
	 * @author Chris
	 * @since 20-Dec-07
	 */
	private class NodeAndArcTypeListener extends GraphModelAdapter implements
			PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (Graph.GRAPH_MODEL_PROPERTY.equals(evt.getPropertyName())) {
				updateNodeAndArcTypes();
			}
		}

		@Override
		public void graphArcTypeAdded(Object arcType) {
			updateArcTypes();
		}

		@Override
		public void graphNodeTypeAdded(Object nodeType) {
			updateNodeTypes();
		}

		@Override
		public void graphNodeAdded(GraphNode node) {
			FilterManager.this.getNodeLevelFilter().addNodeLevel(node);
		}

		@Override
		public void graphNodeRemoved(GraphNode node) {
			FilterManager.this.getNodeLevelFilter().removeNodeLevel(node);
		}

		@Override
		public void graphCleared() {
			updateNodeAndArcTypes();
		}

	}

	private class NodeTypeFilter implements GraphFilter {

		public boolean isArcFilter() {
			return false;
		}

		public boolean isNodeFilter() {
			return true;
		}

		public boolean isVisible(GraphItem item) {
			return isNodeTypeVisible(item.getType());
		}
	}

}
