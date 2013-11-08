package au.uq.dke.comonviz.actions;

import java.awt.event.ActionEvent;

import au.uq.dke.comonviz.handler.graph.PNormalZoomHandler;
import ca.uvic.cs.chisel.cajun.actions.CajunAction;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import edu.umd.cs.piccolo.PCamera;

public class ZoomInAction extends CajunAction {
	private static final long serialVersionUID = 6976775789684876270L;
	private PNormalZoomHandler zoom;

	public ZoomInAction(PCamera camera) {
		super("Zoom In", ResourceHandler.getIcon("icon_zoom_in.gif"));
		this.zoom = new PNormalZoomHandler(camera);
		this.zoom.setMagnificationScale(1.1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// zoom one step
		zoom.zoomInOneStep();
	}
	
}
