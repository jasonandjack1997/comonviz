package au.uq.dke.comonviz.actions;

import java.awt.event.ActionEvent;

import au.uq.dke.comonviz.handler.PNormalZoomHandler;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import edu.umd.cs.piccolo.PCamera;

public class NoZoomAction extends CajunAction {
	private static final long serialVersionUID = 1L;
	private PNormalZoomHandler zoom;

	public NoZoomAction(PCamera camera) {
		super("No Zoom", ResourceHandler.getIcon("icon_no_zoom.gif"));
		this.zoom = new PNormalZoomHandler(camera);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		zoom.noZoom();
	}
	
}
