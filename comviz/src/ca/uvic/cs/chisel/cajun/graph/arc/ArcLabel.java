package ca.uvic.cs.chisel.cajun.graph.arc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

public class ArcLabel extends PText {
	private static final long serialVersionUID = -871571524212274580L;
	
	private boolean ignoreInvalidatePaint = false;

	public ArcLabel(GraphArc graphArc, String text) {
		super();

		// make this node match the text size
		setText(text);
		setTextPaint(Color.black);

	}
	
	public void setText(String text){
		String sb = new String(text);
		sb = sb.substring(sb.lastIndexOf("#")+1).replaceAll(">", "").replaceAll("_", "");
		super.setText(sb);
	}

	@Override
	protected void paint(PPaintContext paintContext) {
		// update the text paint - the super paint method doesn't call our getTextPaint() method
		Paint p = getTextPaint();
		if (!p.equals(super.getTextPaint())) {
			ignoreInvalidatePaint = true;
			setTextPaint(getTextPaint());
			ignoreInvalidatePaint = false;
		}
		// the font is never set in the super paint class?		
		paintContext.getGraphics().setFont(getFont());
		Shape backgroundShape = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), 5f, 5f);		//super.paint(paintContext);
		Graphics2D g2 = paintContext.getGraphics();
		g2.setPaint(Color.white);
		g2.fill(backgroundShape);
		super.paintText(paintContext);

	}

	@Override
	public void invalidatePaint() {
		if (!ignoreInvalidatePaint) {
			super.invalidatePaint();
		}
	}
	
	public String toString(){
		return this.getText();
	}

}
