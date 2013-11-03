package ca.uvic.cs.chisel.cajun.graph;

import org.protege.ontograf.common.GraphController;

import ca.uvic.cs.chisel.cajun.filter.FilterManager;
import ca.uvic.cs.chisel.cajun.graph.handlers.CameraDragPanHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.CameraKeyPanHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.FocusOnExtentsHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.HighlightHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.NodeDragHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.NodeSelectionHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.RotationHandler;
import ca.uvic.cs.chisel.cajun.graph.handlers.ZoomHandler;
import ca.uvic.cs.chisel.cajun.graph.util.AnimationHandler;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.util.PPaintContext;

public class FlatGraph extends AbstractGraph {
	private static final long serialVersionUID = 2134657503991199239L;
	
	private boolean showNodeTooltips;
	
	private AnimationHandler animationHandler;
	
	private GraphController graphController;
	
	public GraphController getGraphController() {
		return graphController;
	}

	public FlatGraph(GraphController graphController) {
		this(graphController, new DefaultGraphModel());
		
		this.showNodeTooltips = true;
	}
	
	public FlatGraph(GraphController graphController, GraphModel model) {
		super(model);
		this.graphController = graphController;
		
		setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

		this.setAnimationHandler(new AnimationHandler(this));

		PCamera camera = getCamera();

		// use our custom pan handler (when the canvas is dragged)
		setPanEventHandler(new CameraDragPanHandler());
		// this causes panning to happen when the arrow keys are pressed
		camera.addInputEventListener(new CameraKeyPanHandler(camera));
		
		// disable key zooming - we'll use our own handler
		setZoomEventHandler(null);
        // handles keyboard (+/-) and mouse wheel zoom events
        camera.addInputEventListener(new ZoomHandler(camera));

        // handles dragging of nodes
		camera.addInputEventListener(new NodeDragHandler());
		// handles node selections
        camera.addInputEventListener(new NodeSelectionHandler(getNodeSelection()));
        //  handles highlighting nodes and arcs
        camera.addInputEventListener(new HighlightHandler());
        
        // ensures that all nodes are displayed on the canvas
        camera.addInputEventListener(new FocusOnExtentsHandler(getAnimationHandler()));
        //rotate handler
        camera.addInputEventListener(new RotationHandler(camera));
	}

	public AnimationHandler getAnimationHandler() {
		return animationHandler;
	}

	public FilterManager getFilterManager() {
		return filterManager;
	}

	public boolean isShowNodeTooltips() {
		return showNodeTooltips;
	}

	public void setShowNodeTooltips(boolean showNodeTooltips) {
		this.showNodeTooltips = showNodeTooltips;
	}

	public void setAnimationHandler(AnimationHandler animationHandler) {
		this.animationHandler = animationHandler;
	}
}
