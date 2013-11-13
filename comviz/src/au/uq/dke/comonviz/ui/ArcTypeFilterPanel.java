package au.uq.dke.comonviz.ui;

import java.util.Map;

import javax.swing.Icon;

import au.uq.dke.comonviz.EntryPoint;
import ca.uvic.cs.chisel.cajun.graph.GraphItemStyle;

public class ArcTypeFilterPanel extends FilterPanel {

	private static final long serialVersionUID = -1656466039034202473L;

	public ArcTypeFilterPanel(String title, Icon icon, GraphItemStyle style) {
		super(title, icon, style);
		// TODO Auto-generated constructor stub
	}

	public void setTypeVisibility(Object arcType, boolean visible) {
		EntryPoint.getFilterManager().getArcTypeFilter()
				.setArcTypeVisible(arcType, visible);
		EntryPoint.getFlatGraph().performLayout();
	}

	public Map<Object, Boolean> getTypes() {
		return EntryPoint.getFilterManager().getArcTypeFilter()
				.getArcTypesMap();
	}

}
