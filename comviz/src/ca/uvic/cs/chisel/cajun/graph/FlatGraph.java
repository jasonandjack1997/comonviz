package ca.uvic.cs.chisel.cajun.graph;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.GraphController;
import au.uq.dke.comonviz.filter.FilterManager;
import au.uq.dke.comonviz.handler.graph.CameraDragPanHandler;
import au.uq.dke.comonviz.handler.graph.CameraKeyPanHandler;
import au.uq.dke.comonviz.handler.graph.FocusOnExtentsHandler;
import au.uq.dke.comonviz.handler.graph.HighlightHandler;
import au.uq.dke.comonviz.handler.graph.NodeDragHandler;
import au.uq.dke.comonviz.handler.graph.NodeSelectionHandler;
import au.uq.dke.comonviz.handler.graph.RotationHandler;
import au.uq.dke.comonviz.handler.graph.ZoomHandler;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.util.AnimationHandler;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

public class FlatGraph extends AbstractGraph {
	private static final long serialVersionUID = 2134657503991199239L;

	private boolean showNodeTooltips;

	private AnimationHandler animationHandler;

	public FlatGraph() {
		super(EntryPoint.getGraphModel());

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
		camera.addInputEventListener(new NodeSelectionHandler(
				getNodeSelection()));
		// handles highlighting nodes and arcs
		camera.addInputEventListener(new HighlightHandler());

		// ensures that all nodes are displayed on the canvas
		camera.addInputEventListener(new FocusOnExtentsHandler(
				getAnimationHandler()));
		// rotate handler
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

	public void addListeners() {
		// TODO Auto-generated method stub
		
	}
}
