package ca.uvic.cs.chisel.cajun.graph.node;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;

import org.protege.ontograf.common.GraphController;
import org.protege.ontograf.tree.TreeInfoManager;

import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

public class ChildrenCountIcon extends PText {
	private static final long serialVersionUID = -871571524212274580L;

	private boolean ignoreInvalidatePaint = false;
	private GraphNode graphNode;

	public ChildrenCountIcon(GraphNode GraphNode, String text) {
		super();
		this.graphNode = GraphNode;

		// make this node match the text size
		setText(text);
		setTextPaint(Color.white);

	}

	@Override
	protected void paint(PPaintContext paintContext) {
		// update the text paint - the super paint method doesn't call our
		// getTextPaint() method
		Paint p = getTextPaint();
		if (!p.equals(super.getTextPaint())) {
			ignoreInvalidatePaint = true;
			setTextPaint(getTextPaint());
			ignoreInvalidatePaint = false;
		}
		// the font is never set in the super paint class?
		paintContext.getGraphics().setFont(getFont());

		Ellipse2D circle = new Ellipse2D.Double();

		double d = Math.max(this.getWidth(), this.getHeight());
		circle.setFrame(
				this.getX() - Math.abs(this.getWidth() - this.getHeight()) / 2l,
				this.getY(), d, d);

		Graphics2D g2 = paintContext.getGraphics();

		if (!GraphController.model.isExpanded(this.graphNode)) {
			TreeInfoManager treeInforManager = TreeInfoManager.getTreeManager();
			String childrenCount = String.valueOf(treeInforManager
					.getChildrenCount(graphNode.getUserObject()));
			this.setText(childrenCount);

			g2.setPaint(Color.red);

			g2.fill(circle);
			super.paintText(paintContext);
		}

	}

	@Override
	public void invalidatePaint() {
		if (!ignoreInvalidatePaint) {
			super.invalidatePaint();
		}
	}

	public String toString() {
		return this.getText();
	}

}
