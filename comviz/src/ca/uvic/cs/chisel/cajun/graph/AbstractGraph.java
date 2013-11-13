package ca.uvic.cs.chisel.cajun.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalDirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.progress.ProgressListener;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.actions.LayoutAction;
import au.uq.dke.comonviz.filter.FilterChangedEvent;
import au.uq.dke.comonviz.filter.FilterChangedListener;
import au.uq.dke.comonviz.filter.FilterManager;
import au.uq.dke.comonviz.graph.arc.DefaultGraphArcStyle;
import au.uq.dke.comonviz.graph.node.DefaultGraphNode;
import au.uq.dke.comonviz.graph.node.DefaultGraphNodeStyle;
import au.uq.dke.comonviz.handler.graph.GraphPopupListener;
import au.uq.dke.comonviz.handler.graph.KeyHandlerDelegate;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArcStyle;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNodeCollectionEvent;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNodeCollectionListener;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNodeStyle;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import ca.uvic.cs.chisel.cajun.util.CustomToolTip;
import ca.uvic.cs.chisel.cajun.util.CustomToolTipManager;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEventListener;

public abstract class AbstractGraph extends PCanvas implements Graph {
	private static final long serialVersionUID = -2767059869604101888L;

	public static final int ARC_LAYER_INDEX = 0;
	public static final int NODE_LAYER_INDEX = 1;

	private static final Border FOCUS_LOST_BORDER = BorderFactory.createEmptyBorder(3, 3, 3, 3);
	private static final Border FOCUS_GAINED_BORDER = BorderFactory.createLineBorder(Color.GREEN.darker(), 3);

	protected GraphModel model;
	protected FilterManager filterManager;

	private NodeCollection selectedNodes;
	private NodeCollection matchingNodes;

	private GraphNodeStyle nodeStyle;
	private GraphArcStyle arcStyle;

	private Color ttTextColor = Color.black;
	private Color ttBackground = Color.white;
	private Font ttFont = null;

	private List<LayoutAction> layouts;
	private LayoutAction lastLayout;

	// we keep track of these listeners, and make sure they are always
	// added to the current model (and removed from the old models)
	private List<GraphModelListener> graphModelListeners;

	// contains the node, arc, and canvas JPopupMenus
	private GraphPopupListener graphPopupListener;

	private GraphModelListener modelListener = new GraphModelAdapter() {
		public void graphCleared() {
			clear();
		}
		public void graphNodeAdded(GraphNode node) {
			addGraphNode(node);
		}
		public void graphNodeRemoved(GraphNode node) {
			removeGraphNode(node);
		}
		public void graphArcAdded(GraphArc arc) {
			addGraphArc(arc);
		}
		public void graphArcRemoved(GraphArc arc) {
			removeGraphArc(arc);
		}
	};
	
	private TreeSelectionListener treeSelectionListener = new TreeSelectionListener(){

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			try {
				DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) ((JTree)e.getSource()).getLastSelectedPathComponent();
				GraphNode selectedGraphNode = (GraphNode) selectedTreeNode.getUserObject();
				Object userObject = selectedGraphNode.getUserObject();
				GraphNode realGraphNode = EntryPoint.getGraphModel().getNode(userObject);
				AbstractGraph.this.selectedNodes.setNode(realGraphNode);
				EntryPoint.getGraphController().panTo((DefaultGraphNode) realGraphNode);
			} catch(NullPointerException e2){
				e2.printStackTrace();
			}
			
		}
		
	};
	
	private TreeExpansionListener treeExpensionListener = new TreeExpansionListener(){

		@Override
		public void treeExpanded(TreeExpansionEvent e) {
			// TODO Auto-generated method stub
			//find all visible nodes in tree (expanded), and show them all, don't call expand method!
			JTree jTree = (JTree) e.getSource();
			int visibleNodesCount = jTree.getRowCount();
			for (int i = 0; i < visibleNodesCount; i++) {
				TreePath treePath = jTree.getPathForRow(i);
				DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) treePath
						.getLastPathComponent();
				GraphNode selectedGraphNode = (GraphNode) selectedTreeNode
						.getUserObject();
				try {
					Object userObject = selectedGraphNode.getUserObject();
					GraphNode realGraphNode = EntryPoint.getGraphModel().getNode(
							userObject);
					if (realGraphNode == null) {

						EntryPoint.getGraphModel().showWithExsistingNodes((OWLEntity) userObject,
								EntryPoint.getFlatGraph()
												.getFilterManager());
					} else {
					}
				} catch (NullPointerException e2) {
					e2.printStackTrace();
				}
			}

			EntryPoint.getFlatGraph().performLayout();
			
		}

		@Override
		public void treeCollapsed(TreeExpansionEvent e) {
			// TODO Auto-generated method stub
			JTree jTree = (JTree) e.getSource();
			int visibleNodesCount = jTree.getRowCount();
			Collection userObjectCollection = new LinkedList();
			for (int i = 0; i < visibleNodesCount; i++) {
				TreePath treePath = jTree.getPathForRow(i);
				DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) treePath
						.getLastPathComponent();
				GraphNode graphNode = (GraphNode) selectedTreeNode.getUserObject();
				Object userObject = graphNode.getUserObject();
				userObjectCollection.add(userObject);
			}

			for (GraphNode graphNodeToDelete : EntryPoint.getGraphModel().getVisibleNodes()) {
				if(!userObjectCollection.contains(graphNodeToDelete.getUserObject())){
					EntryPoint.getGraphModel().removeNode(graphNodeToDelete.getUserObject());
				}

			}

			EntryPoint.getFlatGraph().performLayout();
			
		}
		
	};

	private GraphNodeCollectionListener selectionListener = new GraphNodeCollectionListener() {
		public void collectionChanged(GraphNodeCollectionEvent evt) {
			for (GraphNode node : evt.getOldNodes()) {
				node.setSelected(false);
			}
			for (GraphNode node : evt.getNewNodes()) {
				node.setSelected(true);
			}
		}
	};

	private GraphNodeCollectionListener matchingListener = new GraphNodeCollectionListener() {
		public void collectionChanged(GraphNodeCollectionEvent evt) {
			// update the selected nodes
			for (GraphNode node : evt.getOldNodes()) {
				node.setMatching(false);
			}
			for (GraphNode node : evt.getNewNodes()) {
				node.setMatching(true);
			}
		}
	};

	private FocusListener focusListener = new FocusListener() {
		public void focusGained(FocusEvent e) {
			getCanvas().setBorder(FOCUS_GAINED_BORDER);
		}

		public void focusLost(FocusEvent e) {
			getCanvas().setBorder(FOCUS_LOST_BORDER);
		}
	};

	private FilterChangedListener filterListener = new FilterChangedListener() {
		public void filtersChanged(FilterChangedEvent fce) {
			filterManager.applyFilters(model);
			
			repaint();
		}
	};

	public AbstractGraph(GraphModel model) {
		this();

		// do this last after the layers and listeners have be created
		setModel(model);
	}

	public AbstractGraph() {
		super();
		this.model = new DefaultGraphModel();

		this.graphModelListeners = new ArrayList<GraphModelListener>();

		this.layouts = new ArrayList<LayoutAction>();
		addDefaultLayouts();

		this.graphPopupListener = new GraphPopupListener();
		getCamera().addInputEventListener(graphPopupListener);

		this.filterManager = new FilterManager(this);
		this.filterManager.addFilterChangedListener(filterListener);

		this.selectedNodes = new NodeCollection();
		selectedNodes.addCollectionListener(selectionListener);

		this.matchingNodes = new NodeCollection();
		matchingNodes.addCollectionListener(matchingListener);
		

		this.nodeStyle = new DefaultGraphNodeStyle();
		this.arcStyle = new DefaultGraphArcStyle();

		addFocusListener(focusListener);

		// register to use our custom tooltips
		CustomToolTipManager.sharedInstance().registerComponent(this);

		initializeLayers();

		// this is needed to handle keyboard events
		getRoot().getDefaultInputManager().setKeyboardFocus(new KeyHandlerDelegate(getCamera()));
	}

	public void addListeners(){
		EntryPoint.getTopView().getjTree().addTreeSelectionListener(treeSelectionListener);
		EntryPoint.getTopView().getjTree().addTreeExpansionListener(treeExpensionListener);

	}
	protected void initializeLayers() {
		// one layer is automatically created (layer 0), see the PCanvas constructor
		// we'll let that one be the arc layer

		// now create and add the node layer (layer 1)
		PLayer nodeLayer = new PLayer();
		getRoot().addChild(nodeLayer);
		getCamera().addLayer(NODE_LAYER_INDEX, nodeLayer);
	}

	protected PLayer getNodeLayer() {
		return getCamera().getLayer(NODE_LAYER_INDEX);
	}

	protected PLayer getArcLayer() {
		return getCamera().getLayer(ARC_LAYER_INDEX);
	}

	protected void addNode(PNode node) {
		getNodeLayer().addChild(node);
	}

	protected void removeNode(PNode node) {
		getNodeLayer().removeChild(node);
	}

	protected void addArc(PNode arc) {
		getArcLayer().addChild(arc);
	}

	protected void removeArc(PNode arc) {
		getArcLayer().removeChild(arc);
	}
	
	public FilterManager getFilterManager() {
		return filterManager;
	}

	public JComponent getGraphComponent() {
		return this;
	}
	
	public void addLayoutListener(ProgressListener listener) {
		for(LayoutAction layout : layouts) {
			layout.addProgressListener(listener);
		}
	}

	public PCanvas getCanvas() {
		return this;
	}

	public void addGraphModelListener(GraphModelListener gml) {
		graphModelListeners.add(gml);
		getModel().addGraphModelListener(gml);
	}

	public void removeGraphModelListener(GraphModelListener gml) {
		graphModelListeners.remove(gml);
		getModel().removeGraphModelListener(gml);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
	}

	@Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
	}

	public JPopupMenu getNodeContextMenu() {
		return graphPopupListener.getNodeMenu();
	}

	public JPopupMenu getArcContextMenu() {
		return graphPopupListener.getArcMenu();
	}

	public JPopupMenu getCanvasContextMenu() {
		return graphPopupListener.getCanvasMenu();
	}

	public void addGraphInputListener(PInputEventListener listener) {
		getCamera().addInputEventListener(listener);
	}

	public void removeGraphInputListener(PInputEventListener listener) {
		getCamera().removeInputEventListener(listener);
	}

	public void addNodeSelectionListener(GraphNodeCollectionListener listener) {
		getNodeSelection().addCollectionListener(listener);
	}

	public void removeNodeSelectionListener(GraphNodeCollectionListener listener) {
		getNodeSelection().removeCollectionListener(listener);
	}

	public void clear() {
		clearNodeSelection();

		for (int i = 0; i < getCamera().getLayerCount(); i++) {
			getCamera().getLayer(i).removeAllChildren();
		}
		getCamera().removeAllChildren();

		repaint();
	}

	public void clearNodeSelection() {
		selectedNodes.clear();
	}

	public Collection<GraphNode> getSelectedNodes() {
		return selectedNodes.getNodes();
	}

	public GraphNode getFirstSelectedNode() {
		return (selectedNodes.isEmpty() ? null : selectedNodes.getFirstNode());
	}

	public void setSelectedNodes(Collection<GraphNode> nodes) {
		selectedNodes.setNodes(nodes);
	}

	public NodeCollection getNodeSelection() {
		return selectedNodes;
	}

	public Collection<GraphNode> getMatchingNodes() {
		return matchingNodes.getNodes();
	}

	public void setMatchingNodes(Collection<GraphNode> nodes) {
		matchingNodes.setNodes(nodes);
	}

	public GraphModel getModel() {
		return model;
	}

	public void setModel(GraphModel model) {
		GraphModel oldModel = this.model;
		oldModel.removeGraphModelListener(modelListener);

		// now remove any of "our" graph model listeners
		for (GraphModelListener gml : graphModelListeners) {
			oldModel.removeGraphModelListener(gml);
		}

		// don't allow null models
		if (model == null) {
			model = new DefaultGraphModel();
		}

		this.model = model;
		this.model.addGraphModelListener(modelListener);

		// now add "our" graph model listeners back
		for (GraphModelListener gml : graphModelListeners) {
			this.model.addGraphModelListener(gml);
		}

		loadModel();

		firePropertyChange(GRAPH_MODEL_PROPERTY, oldModel, this.model);
	}

	public void setGraphArcStyle(GraphArcStyle style) {
		if ((style != null) && (this.arcStyle != style)) {
			GraphArcStyle oldStyle = this.arcStyle;
			this.arcStyle = style;

			// now update the styles on all arcs
			Collection<GraphArc> arcs = model.getAllArcs();
			for (GraphArc arc : arcs) {
				arc.setArcStyle(this.arcStyle);
			}

			firePropertyChange(GRAPH_ARC_STYLE_PROPERTY, oldStyle, this.arcStyle);
		}
	}

	public GraphArcStyle getGraphArcStyle() {
		return arcStyle;
	}

	public void setGraphNodeStyle(GraphNodeStyle style) {
		if ((style != null) && (this.nodeStyle != style)) {
			GraphNodeStyle oldStyle = this.nodeStyle;
			this.nodeStyle = style;

			// now update the styles on all nodes
			Collection<GraphNode> nodes = model.getAllNodes();
			for (GraphNode node : nodes) {
				node.setNodeStyle(this.nodeStyle);
			}

			firePropertyChange(GRAPH_NODE_STYLE_PROPERTY, oldStyle, this.nodeStyle);
		}
	}

	public GraphNodeStyle getGraphNodeStyle() {
		return nodeStyle;
	}

	/**
	 * Adds the nodes and arcs to this graph/canvas. Also sets the graph node and arc style on every
	 * node and arc.
	 */
	protected void loadModel() {
		clear();
		addGraphItems(model.getAllNodes(), model.getAllArcs());
	}

	protected void addGraphArc(GraphArc arc) {
		// copy the default arc style into the arc
		if ((getGraphArcStyle() != null) && !getGraphArcStyle().equals(arc.getArcStyle())) {
			arc.setArcStyle(getGraphArcStyle());
		}

		// check if this arc is filtered

		// add the arc to the canvas
		if (arc instanceof PNode) {
			addArc((PNode) arc);
		}
	}

	protected void addGraphNode(GraphNode node) {
		// copy the default node style into the node
		if ((getGraphNodeStyle() != null) && !getGraphNodeStyle().equals(node.getNodeStyle())) {
			node.setNodeStyle(getGraphNodeStyle());
		}

		// add the node to the canvas
		if (node instanceof PNode) {
			addNode((PNode) node);
		}
	}

	protected void removeGraphNode(GraphNode node) {
		if (node instanceof PNode) {
			removeNode((PNode) node);
		}
	}

	protected void removeGraphArc(GraphArc arc) {
		if (arc instanceof PNode) {
			removeArc((PNode) arc);
		}
	}

	protected void addGraphItems(Collection<GraphNode> nodesToAdd, Collection<GraphArc> arcsToAdd) {
		for (GraphNode node : nodesToAdd) {
			addGraphNode(node);
		}
		for (GraphArc arc : arcsToAdd) {
			addGraphArc(arc);
		}
	}

	protected void removeGraphItems(Collection<GraphNode> nodesToRemove, Collection<GraphArc> arcsToRemove) {
		for (GraphNode node : nodesToRemove) {
			removeGraphNode(node);
		}
		for (GraphArc arc : arcsToRemove) {
			removeGraphArc(arc);
		}
	}

	/**
	 * graph has a set of layouts
	 * every layout has its layout action
	 * so we create layout with different action
	 */
	protected void addDefaultLayouts() {
		int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_GRID_BY_ALPHA, ResourceHandler.getIcon("icon_grid_layout.gif"), new GridLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_RADIAL, ResourceHandler.getIcon("icon_radial_layout.gif"), new RadialLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_SPRING, ResourceHandler.getIcon("icon_spring_layout.gif"), new SpringLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_TREE_VERTICAL, ResourceHandler.getIcon("icon_tree_layout.gif"), new TreeLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_TREE_HORIZONTAL, ResourceHandler.getIcon("icon_tree_layout_horizontal.gif"), new HorizontalTreeLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_DIRECTED_VERTICAL, ResourceHandler.getIcon("icon_tree_layout.gif"), new DirectedGraphLayoutAlgorithm(style), this));
		addLayout(new LayoutAction(LayoutConstants.LAYOUT_DIRECTED_HORIZONTAL, ResourceHandler.getIcon("icon_tree_layout_horizontal.gif"), new HorizontalDirectedGraphLayoutAlgorithm(style), this));
		
		// important - set the last layout
		this.lastLayout = getLayout(LayoutConstants.LAYOUT_DIRECTED_VERTICAL);
	}

	public void addLayout(LayoutAction layoutAction) {
		if (!this.layouts.contains(layoutAction)) {
			this.layouts.add(layoutAction);
			if (lastLayout == null) {
				lastLayout = layoutAction;
			}
		}
	}
	
	public LayoutAction getLayout(String name) {
		for (LayoutAction layout : layouts) {
			if (layout.getName().equals(name)) {
				return layout;
			}
		}
		return null;
	}

	public void removeLayout(LayoutAction layout) {
		layouts.remove(layout);
	}

	public Collection<LayoutAction> getLayouts() {
		return layouts;
	}

	public void performLayout() {
		if (getLastLayout() != null) {
			filterManager.applyFilters(model);
			getLastLayout().runLayout();
		}
	}

	public void performLayout(LayoutAction layout) {
		if (layout != null) {
			layout.runLayout();
		}
	}

	public void setLastLayout(LayoutAction layout) {
		this.lastLayout = layout;
	}

	public LayoutAction getLastLayout() {
		return lastLayout;
	}

	/**
	 * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent e) {
		String tooltipText = "";
		if (e != null) {
			PNode pnode = getCamera().pick(e.getX(), e.getY(), 1).getPickedNode();
			if (pnode.getVisible()) {
				if (pnode instanceof GraphNode && isShowNodeTooltips()) {
					GraphNode node = (GraphNode) pnode;
					tooltipText = node.getTooltip();
					ttBackground = node.getNodeStyle().getTooltipBackgroundColor();
					ttTextColor = node.getNodeStyle().getTooltipTextColor();
					ttFont = node.getNodeStyle().getTooltipFont();
				} 
				
//				else if (pnode instanceof GraphArc) {
//					GraphArc arc = (GraphArc) pnode;
//					tooltipText = arc.getTooltip();
//					if (tooltipText == null || tooltipText.equals("")) {
//						tooltipText = arc.getSource().getText() + " ---" + arc.getType() + "---> " + arc.getDestination().getText();
//					}
//
//					ttBackground = arc.getArcStyle().getTooltipBackgroundColor();
//					ttTextColor = arc.getArcStyle().getTooltipTextColor();
//					ttFont = arc.getArcStyle().getTooltipFont();
//				}
			}
		}
		return (tooltipText.length() > 0 ? tooltipText : "");
	}

	/**
	 * @see javax.swing.JComponent#createToolTip()
	 */
	public JToolTip createToolTip() {
		return new CustomToolTip(ttTextColor, ttBackground, ttFont);
	}
}
