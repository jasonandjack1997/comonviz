package au.uq.dke.comonviz.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.GraphItem;
import ca.uvic.cs.chisel.cajun.graph.GraphModel;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import database.model.ontology.OntologyClass;

public class NodeLevelFilter implements GraphFilter {

	private Map<Object, Boolean> nodeLevelsVisibilityMap = new HashMap<Object, Boolean>();
	private int initialMaxVisibleLevel = -1;

	public int getInitialMaxVisibleLevel() {
		return initialMaxVisibleLevel;
	}

	public void setInitialMaxVisibleLevel(int visibleLevel) {
		this.initialMaxVisibleLevel = visibleLevel;
	}

	@Override
	public boolean isNodeFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isArcFilter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible(GraphItem item) {
		OntologyClass cls = (OntologyClass) item.getUserObject();
		if (this.nodeLevelsVisibilityMap.containsKey(cls.getLevel())) {
			return nodeLevelsVisibilityMap.get(cls.getLevel());
		}
		return true;
	}

	public void setNodeLevelVisible(Object nodeLevel, boolean visible) {

		// visible by default
		boolean old = true;
		if (nodeLevelsVisibilityMap.containsKey(nodeLevel)) {
			old = nodeLevelsVisibilityMap.get(nodeLevel);
		}
		if (old != visible) {
			nodeLevelsVisibilityMap.put(nodeLevel, visible);
			EntryPoint.getFilterManager().fireFiltersChanged();
		}
	}

	public Collection<Object> getNodeLevels() {
		return new HashSet<Object>(nodeLevelsVisibilityMap.keySet());
	}

	public Map<Object, Boolean> getNodeLevelsMap() {
		return new HashMap<Object, Boolean>(nodeLevelsVisibilityMap);
	}

	public void addNodeLevel(GraphNode node) {
		Object nodeLevel = ((OntologyClass) node.getUserObject()).getLevel();
		if (!nodeLevelsVisibilityMap.containsKey(nodeLevel)) {
			nodeLevelsVisibilityMap.put(nodeLevel, true);
		}
	}

	public void removeNodeLevel(GraphNode removedNode) {
		Object nodeLevelToBeRemove = ((OntologyClass) removedNode
				.getUserObject()).getLevel();
		if (nodeLevelsVisibilityMap.containsKey(nodeLevelToBeRemove)) {

			for (GraphNode node : EntryPoint.getGraphModel().getAllNodes()) {
				Object existNodeLevel = ((OntologyClass) node.getUserObject())
						.getLevel();
				if (existNodeLevel.equals(nodeLevelToBeRemove)) {
					return;
				}
			}
			nodeLevelsVisibilityMap.remove(nodeLevelToBeRemove);
		}

	}

	public void updateNodeLevels() {
		Collection<GraphNode> graphNodes = EntryPoint.getGraphModel()
				.getAllNodes();
		this.nodeLevelsVisibilityMap.clear();
		for (GraphNode graphNode : graphNodes) {
			Object level = ((OntologyClass) graphNode.getUserObject())
					.getLevel();
			if (!this.nodeLevelsVisibilityMap
					.containsKey(level)){
				int l = (int) level;
				this.nodeLevelsVisibilityMap.put(level, (l <= initialMaxVisibleLevel || 0 > initialMaxVisibleLevel) ? true : false);
			}
		}
	}
}
