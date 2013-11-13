package au.uq.dke.comonviz.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.GraphItem;
import ca.uvic.cs.chisel.cajun.graph.GraphModel;

public class ArcTypeFilter implements GraphFilter {

	// maps the arc types to their visibilities
	private Map<Object, Boolean> arcTypesVisibilityMap = new HashMap<Object, Boolean>();


	public boolean isArcFilter() {
		return true;
	}

	public boolean isNodeFilter() {
		return false;
	}

	public boolean isVisible(GraphItem item) {
		return isArcTypeVisible(item.getType());
	}

	public boolean isArcTypeVisible(Object arcType) {
		if (arcTypesVisibilityMap.containsKey(arcType)) {
			return arcTypesVisibilityMap.get(arcType);
		}
		// visible by default
		return true;
	}

	public void setArcTypeVisible(Object arcType, boolean visible) {
		// visible by default
		boolean old = true;
		if (arcTypesVisibilityMap.containsKey(arcType)) {
			old = arcTypesVisibilityMap.get(arcType);
		}
		if (old != visible) {
			arcTypesVisibilityMap.put(arcType, visible);
			EntryPoint.getFilterManager().fireFiltersChanged();
		}
	}

	public void updateArcTypes() {
		GraphModel model = EntryPoint.getGraphModel();
		Collection<Object> newArcTypes = model.getArcTypes();
		if (newArcTypes.isEmpty()) {
			arcTypesVisibilityMap.clear();
		} else {
			// first remove any node types that no longer exist
			for (Iterator<Object> iter = arcTypesVisibilityMap.keySet()
					.iterator(); iter.hasNext();) {
				Object oldArcType = iter.next();
				if (!newArcTypes.contains(oldArcType)) {
					iter.remove();
				}
			}
			// now add any that don't already exist (visible by default)
			for (Object arcType : newArcTypes) {
				if (!arcTypesVisibilityMap.containsKey(arcType)) {
					arcTypesVisibilityMap.put(arcType, true);
				}
			}
		}
	}

	public Collection<Object> getArcTypes() {
		return new HashSet<Object>(arcTypesVisibilityMap.keySet());
	}

	public Map<Object, Boolean> getArcTypesMap() {
		return new HashMap<Object, Boolean>(arcTypesVisibilityMap);
	}

}
