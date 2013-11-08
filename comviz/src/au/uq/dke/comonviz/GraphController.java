/**
 * Copyright 1998-2007, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved.
 */
package au.uq.dke.comonviz;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalDirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.progress.ProgressEvent;
import org.eclipse.zest.layouts.progress.ProgressListener;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import au.uq.dke.comonviz.actions.CajunAction;
import au.uq.dke.comonviz.actions.LayoutAction;
import au.uq.dke.comonviz.common.util.IconConstants;
import au.uq.dke.comonviz.filter.FilterChangedEvent;
import au.uq.dke.comonviz.filter.FilterChangedListener;
import au.uq.dke.comonviz.graph.FlatGraph;
import au.uq.dke.comonviz.graph.ProtegeInputEventHandler;
import au.uq.dke.comonviz.graph.arc.DefaultGraphArcStyle;
import au.uq.dke.comonviz.graph.node.DefaultGraphNode;
import au.uq.dke.comonviz.graph.node.DefaultGraphNodeStyle;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;
import uk.ac.manchester.cs.bhig.util.MutableTree;
import edu.umd.cs.piccolo.util.PBounds;
import ca.uvic.cs.chisel.cajun.graph.Graph;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;

/**
 * Controller for the graph and model elements. This controller ties the appropriate graph model
 * representation (Protege OWL) to the graph.
 * 
 * @author seanf
 */
public class GraphController {
	/** duration to animate the zoom in and zoom out */
	private static final int ANIMATION_DURATION = 700;
	
	/** the graph object, performs layouts and renders the model */
	private FlatGraph graph;

	/** the model representation of the graph, nodes and edges */
	private  ComonvizGraphModel model;
	
	/** the panel that renders the graph view */
	private TopView view;

	private Action expandAction;
	private Action collapseAction;
	private Set<Action> expandBasedOnActions;
	public static final String REGEXP = "regexp";
	public static final String EXACT_MATCH = "exact match";
	public static final String ENDS_WITH = "ends with";
	public static final String STARTS_WITH = "starts with";
	public static final String CONTAINS = "contains";
	
	private OWLEntity owlClass;

	private MutableTree treeOntology;
	
	public MutableTree getTreeOntology() {
		return treeOntology;
	}

	public void setTreeOntology(MutableTree treeOntology) {
		this.treeOntology = treeOntology;
	}

	public GraphController() {
		this.view = EntryPoint.getTopView();
		this.model = EntryPoint.getGraphModel();
		this.graph = EntryPoint.getFlatGraph();
		this.graph.setShowNodeTooltips(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		this.graph.setBounds(0, 0, width, height);


		// show the plus icon for expandable nodes
		DefaultGraphNodeStyle nodeStyle = new DefaultGraphNodeStyle() {
			public Collection<Icon> getOverlayIcons(GraphNode graphNode) {
				if (model.isExpandable(graphNode) && !model.isExpanded(graphNode)) {
					Collection<Icon> icons = new ArrayList<Icon>();
					icons.add(IconConstants.ICON_PLUS);

					return icons;
				}

				return null;
			}
		};
		nodeStyle.setNodeTypes(model.getNodeTypes());
		this.graph.setGraphNodeStyle(nodeStyle);

		// color the arcs based on arc type
		DefaultGraphArcStyle arcStyle = new DefaultGraphArcStyle() {
			public Stroke getStroke(GraphArc arc) {
				if (arc.getType().toString().contains(ComonvizGraphModel.DIRECT_SUBCLASS_SLOT_TYPE) || arc.getType().toString().contains(ComonvizGraphModel.DIRECT_INDIVIDUAL_SLOT_TYPE)) {
					setDashed(false);
				} else {
					setDashed(true);
					if (arc.getType().toString().contains("Equivalent")) {
						setDashedCapSquare(BasicStroke.CAP_ROUND);
						setDashWidth(2f);
					}
					else {
						setDashedCapSquare(BasicStroke.CAP_SQUARE);
						setDashWidth(10f);
					}
				}

				return super.getStroke(arc);
			}
		};
		arcStyle.setArcTypes(model.getArcTypes());
		this.graph.setGraphArcStyle(arcStyle);

		initialize();
		
		this.graph.addLayoutListener(new ProgressListener() {
			public void progressEnded(ProgressEvent arg0) {
				// make sure the node is visible
				DefaultGraphNode node = (DefaultGraphNode)model.getNode(owlClass);
				panTo(node);
			}

			public void progressStarted(ProgressEvent arg0) {}
			public void progressUpdated(ProgressEvent arg0) {}
		});
		
		this.graph.getFilterManager().addFilterChangedListener(new FilterChangedListener() {
			public void filtersChanged(FilterChangedEvent fce) {
				// have to update the relationship counts when a filter is applied
				model.resetNodeToArcCount();
			}
		});
	}

	public ComonvizGraphModel getModel() {
		return model;
	}

	public Graph getGraph() {
		return graph;
	}

	public void refresh() {
		graph.repaint();
	}

	public void clear() {
		graph.clear();
		model.clear();
		model.restrictToArcType = "";
	}

	public void display(OWLClass cls) {
		model.showNeighborhood(cls, true);
		graph.performLayout();
	}

	public void displayAsSingleNode(OWLEntity entity) {
		model.show(entity, graph.getFilterManager());
		graph.performLayout();
	}
	
	public JToolBar getToolBar() {
		return view.getToolBar();
	}

	/**
	 * Performs a search on the ontology with the given string.
	 * 
	 * @param searchString The entered string to search on
	 * @param searchMode The search type mode that determines how to match the searchString
	 */
	public int search(String searchString, String searchMode) {
		searchString = prepareSearchString(searchString, searchMode);
		
		for(GraphNode node : model.getAllNodes()) node.setSelected(false);

		Collection<GraphNode> matchingNodes = new ArrayList<GraphNode>();
		Collection<? extends OWLEntity> searchResults = model.search(searchString, graph.getFilterManager());
		for (OWLEntity owlEntity : searchResults) {
			GraphNode node = model.getNode(owlEntity);
			if(node != null) {
				matchingNodes.add(model.getNode(owlEntity));
			}
		}

		graph.setSelectedNodes(matchingNodes);
		graph.setMatchingNodes(matchingNodes);
		graph.performLayout();
		
		//model.recalculateArcTypes();

		return searchResults.size();
	}
	
	/**
	 * Shows the class as a node and any arcs that exist between this new node and the
	 * existing nodes on the canvas.
	 * 
	 * @param owlClass
	 */
	public void showOWLClass(OWLClass owlClass) {
		// set all the current nodes to a fixed location
		for(GraphNode node : model.getAllNodes()) {
			node.setFixedLocation(true);
		}
		
		boolean panToNode = true;
		if(model.getNode(owlClass) == null) {
			panToNode = false;
		}
		
		model.restrictToArcType = "";
		model.show(owlClass, graph.getFilterManager());
		
		Collection<GraphNode> matchingNodes = new ArrayList<GraphNode>();
		matchingNodes.add(model.getNode(owlClass));
		
		graph.setMatchingNodes(matchingNodes);
		//graph.performLayout();
		
		// unset the fixed location on all nodes
		for(GraphNode node : model.getAllNodes()) {
			node.setFixedLocation(false);
		}
		
		this.owlClass = owlClass;
		
		if(panToNode) {
			// make sure the node is visible
			DefaultGraphNode node = (DefaultGraphNode)model.getNode(owlClass);
			panTo(node);
		}
	}
	
	private void panTo(DefaultGraphNode node) {
		if(node != null) {
			double x = node.getFullBoundsReference().getX();
			double y = node.getFullBoundsReference().getY();
			double w = node.getFullBoundsReference().getWidth();
			double h = node.getFullBoundsReference().getHeight();
			PBounds bounds = new PBounds(x - w * .01, y - h * .02, w + w * .02, h + h * .04);
			// only pan to the bounds if the node is not already visible
			if(!graph.getCamera().getViewBounds().contains(bounds.getBounds2D())) {
				graph.getRoot().getActivityScheduler().addActivity(
						graph.getCamera().animateViewToCenterBounds(bounds.getBounds2D(), false, ANIMATION_DURATION)
				);
			}
		}
	}

	/**
	 * Adds appropriate query parameters to the searchString based on the selected searchMode.
	 */
	private String prepareSearchString(String searchString, String searchMode) {
		if (searchMode.equals(CONTAINS)) {
			return searchString;
		} else if (searchMode.equals(STARTS_WITH)) {
			return "^" + searchString;
		} else if (searchMode.equals(ENDS_WITH)) {
			return searchString + "$";
		} else if(searchMode.equals(EXACT_MATCH)) {
			return "^" + searchString + "$";
		}

		return searchString;
	}

	private void initialize() {
		// setup the layouts
		List<Object> layoutRelTypes = new ArrayList<Object>();
		layoutRelTypes.add(ComonvizGraphModel.DIRECT_SUBCLASS_SLOT_TYPE);
		layoutRelTypes.add(ComonvizGraphModel.DIRECT_INDIVIDUAL_SLOT_TYPE);
		for (LayoutAction layoutAction : graph.getLayouts()) {
			if (layoutAction.getName().equals(LayoutConstants.LAYOUT_TREE_HORIZONTAL)) {
				layoutAction.setLayout(new HorizontalDirectedGraphLayoutAlgorithm());
				this.graph.setLastLayout(layoutAction);
				layoutAction.setLayoutRelTypes(layoutRelTypes);
			} else if (layoutAction.getName().equals(LayoutConstants.LAYOUT_TREE_VERTICAL)) {
				layoutAction.setLayout(new DirectedGraphLayoutAlgorithm(0));
				layoutAction.setLayoutRelTypes(layoutRelTypes);
			}
		}

		initNodeMenu(graph.getNodeContextMenu());
		graph.addInputEventListener(new ProtegeInputEventHandler(model, graph));
	}
	
	public TopView getView() {
		return view;
	}

	/**
	 * Initializes the menu for the right-click operation on a graph node.
	 * 
	 * @param graphMenu
	 */
	private void initNodeMenu(JPopupMenu graphMenu) {
		graphMenu.add(new CajunAction("Show neighborhood", "Show neighborhood") {
			private static final long serialVersionUID = 4234994986788687910L;

			public void actionPerformed(ActionEvent e) {
				owlClass = (OWLEntity) graph.getFirstSelectedNode().getUserObject();
				model.showNeighborhood(owlClass, true);
				graph.performLayout();
			}
		});

		graphMenu.add(new CajunAction("Set as focus", "Set as focus") {
			private static final long serialVersionUID = -68720636770886830L;

			public void actionPerformed(ActionEvent e) {
				owlClass = (OWLEntity)graph.getFirstSelectedNode().getUserObject();
				//model.hideAscendants(graph.getFirstSelectedNode());
				graph.performLayout();
			}
		});

		graphMenu.addSeparator();

		expandAction = new CajunAction("Expand", "Expand") {
			private static final long serialVersionUID = -8044342001264176639L;

			public void actionPerformed(ActionEvent e) {
				owlClass = (OWLEntity)graph.getFirstSelectedNode().getUserObject();
				model.expandNode(graph.getFirstSelectedNode());
				graph.performLayout();
			}
		};

		collapseAction = new CajunAction("Collapse", "Collapse") {
			private static final long serialVersionUID = 815274665395852446L;

			public void actionPerformed(ActionEvent e) {
				owlClass = (OWLEntity)graph.getFirstSelectedNode().getUserObject();
				model.collapseNode(graph.getFirstSelectedNode());
				graph.performLayout();
			}
		};

		graphMenu.add(expandAction);
		graphMenu.add(collapseAction);
		graphMenu.addSeparator();

		final JMenu expandBasedOnMenu = new JMenu("Expand on");
		graphMenu.add(expandBasedOnMenu);

		expandBasedOnActions = new HashSet<Action>();
		// initialize the actions with the defaults
//		for(Object o : model.getArcTypes()) {
//			Action action = getNodeExpansionAction(o.toString());
//			expandBasedOnActions.add(action);
//			expandBasedOnMenu.add(action);
//		}
		
		// add any new arc types
//		model.addGraphModelListener(new GraphModelAdapter() {
//			public void graphArcTypeAdded(Object arcType) {
//				Action action = getNodeExpansionAction(arcType.toString());
//				if(!expandBasedOnActions.contains(action)) {
//					expandBasedOnActions.add(action);
//					expandBasedOnMenu.add(action);
//				}
//			}
//		});

		graphMenu.add(expandBasedOnMenu);

		graphMenu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent e) {}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				GraphNode node = graph.getFirstSelectedNode();
				if (node != null) {
					prepareNodeSpecificActions(node, expandBasedOnMenu);
					initNodeActionConditions(node);
				}
			}

		});
	}
	
	/**
	 * Sets the expansion menu options enabled/disabled flag based on the current state of the 
	 * node. If the node has already been expanded, everything is disabled. Otherwise, all the 
	 * non-filtered arc types are enabled.
	 * @param node
	 */
	private void initNodeActionConditions(GraphNode node) {
		if (model.isExpanded(node)) {
			expandAction.setEnabled(false);
			for (Action action : expandBasedOnActions) {
				action.setEnabled(false);
			}
		} else {
			expandAction.setEnabled(true);
			for (Action action : expandBasedOnActions) {
				if(graph.getFilterManager().isArcTypeVisible(((CajunAction)action).getName())) {
					action.setEnabled(true);
				}
				else {
					action.setEnabled(false);
				}
			}
		}
	}
	
	/**
	 * Uses the arc cache to get all the unique arc types for the given node.
	 */
	private void prepareNodeSpecificActions(GraphNode node, JMenu expandBasedOnMenu) {
		expandBasedOnActions.clear();
		expandBasedOnMenu.removeAll();
		for(GraphArc arc : model.getCachedArcsForEntity((OWLEntity)node.getUserObject())) {
			Action action = getNodeExpansionAction(arc.getType().toString());
			if(!expandBasedOnActions.contains(action)) {
				expandBasedOnActions.add(action);
				expandBasedOnMenu.add(action);
			}
		}
	}
	
	private CajunAction getNodeExpansionAction(String arcType) {
		return new CajunAction(arcType, arcType) {
			private static final long serialVersionUID = -4658385618425759291L;

			public void actionPerformed(ActionEvent e) {
				model.expandNode(graph.getFirstSelectedNode(), e.getActionCommand());
				graph.performLayout();
			}
		};
	}
}
