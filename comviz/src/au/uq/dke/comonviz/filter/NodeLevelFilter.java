package au.uq.dke.comonviz.filter;

import java.util.Map;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.GraphItem;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import database.model.ontology.OntologyClass;

public class NodeLevelFilter implements GraphFilter {

	private int visibleLevel = 3;

	public int getVisibleLevel() {
		return visibleLevel;
	}

	public void setVisibleLevel(int visibleLevel) {
		this.visibleLevel = visibleLevel;
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
		if (cls.getLevel() <= visibleLevel) {
			return true;
		}
		return false;
	}

}
