package au.uq.dke.comonviz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;

import au.uq.dke.comonviz.actions.CajunAction;
import au.uq.dke.comonviz.actions.NoZoomAction;
import au.uq.dke.comonviz.actions.ZoomInAction;
import au.uq.dke.comonviz.actions.ZoomOutAction;
import au.uq.dke.comonviz.filter.FilterManager;
import au.uq.dke.comonviz.graph.node.DefaultGraphNode;
import au.uq.dke.comonviz.ui.ArcTypeFilterPanel;
import au.uq.dke.comonviz.ui.FilterPanel;
import au.uq.dke.comonviz.ui.NodeLevelFilterPanel;
import au.uq.dke.comonviz.ui.OpenOntologyFileAction;
import au.uq.dke.comonviz.ui.StatusProgressBar;
import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;
import ca.uvic.cs.chisel.cajun.graph.FlatGraph;
import ca.uvic.cs.chisel.cajun.graph.Graph;
import ca.uvic.cs.chisel.cajun.graph.GraphModelAdapter;
import ca.uvic.cs.chisel.cajun.graph.GraphModelListener;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import database.model.ontology.OntologyClass;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class TopView extends JPanel {

	private static final long serialVersionUID = -7720543969598323711L;

	private FlatGraph graph;

	private JToolBar toolbar;
	private JPanel centerGraphPanel;
	private StatusProgressBar status;

	private JSplitPane rightFilterSplitPane;
	private FilterPanel nodeFilterPanel;
	private FilterPanel arcTypeFilterPanel;
	private FilterPanel nodeLevelFilterPanel;

	private JSplitPane centerAndRightHorizontalSplitPane;

	private JSplitPane leftVerticalSplitPane;
	private JSplitPane topHorizontalSplitPane;

	public JSplitPane getTopHorizontalSplitPane() {
		return topHorizontalSplitPane;
	}

	private DefaultTreeModel treeModel;

	private NodeCollection selectedNodes;

	private JTree jTree;
	private JTextPane jTextArea;

	TreeSelectionListener textFieldtreeSelectionListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) ((JTree) e
					.getSource()).getLastSelectedPathComponent();
			GraphNode selectedGraphNode = (GraphNode) selectedTreeNode
					.getUserObject();
			OntologyClass ontologyClass = (OntologyClass) selectedGraphNode
					.getUserObject();
			String annotation = ontologyClass.getDiscription();
			//annotation = annotation.substring(1, annotation.length() - 1);
			annotation = EntryPoint.getAnnotationManager()
					.getStylizedAnnotation(annotation);
			TopView.this.getjTextArea().setText(annotation);
			TopView.this.getjTextArea().setCaretPosition(0);

		}

	};

	GraphModelListener graphModelListener = new GraphModelListener() {

		@Override
		public void graphCleared() {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphNodeAdded(GraphNode node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphNodeRemoved(GraphNode node) {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphArcAdded(GraphArc arc) {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphArcRemoved(GraphArc arc) {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphNodeTypeAdded(Object nodeType) {
			// TODO Auto-generated method stub

		}

		@Override
		public void graphArcTypeAdded(Object arcType) {
			// TODO Auto-generated method stub

		}

	};

	public JTextPane getjTextArea() {
		return jTextArea;
	}

	public void setjTextArea(JTextPane jTextArea) {
		this.jTextArea = jTextArea;
	}

	public JTree getjTree() {
		return jTree;
	}

	public TopView() {
		super(new BorderLayout());
		this.graph = EntryPoint.getFlatGraph();
		// this.selectedNodes = graph.getNodeSelection();
		// initialize();
	}

	public void initialize() {
		// this.ontologyTree = ontologyTree;

		EntryPoint.getjFrame().add(this);
		this.add(getToolBar(), BorderLayout.NORTH);

		DefaultMutableTreeNode root = EntryPoint.getGraphModel()
				.generateMutableTree();

		treeModel = new DefaultTreeModel(root);

		jTextArea = new JTextPane();
		jTextArea.setContentType("text/html");
		jTextArea.setMinimumSize(new Dimension(200, 100));
		// jTextArea.setText("hehe");
		jTextArea.setEditable(true);
		jTextArea.setMargin(new Insets(10, 10, 10, 10));
		jTree = new JTree(treeModel);
		jTree.setSelectionRow(0);
		jTree.setCellRenderer(treeCellRender);
		jTree.addTreeSelectionListener(textFieldtreeSelectionListener);
		JScrollPane leftTopJScrollPane = new JScrollPane(jTree);

		leftTopJScrollPane.setMinimumSize(new Dimension(200, 200));
		JScrollPane leftBottomJScrollPane = new JScrollPane(jTextArea);
		leftBottomJScrollPane.setMinimumSize(new Dimension(100, 200));
		leftVerticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftVerticalSplitPane.setMinimumSize(new Dimension(200, 200));
		leftVerticalSplitPane.add(leftTopJScrollPane);
		leftVerticalSplitPane.add(leftBottomJScrollPane);
		leftVerticalSplitPane.setOneTouchExpandable(true);
		leftVerticalSplitPane.setDividerLocation(0.7f);

		centerAndRightHorizontalSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT);
		centerAndRightHorizontalSplitPane
				.setMinimumSize(new Dimension(500, 500));

		topHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		topHorizontalSplitPane.add(leftVerticalSplitPane);
		topHorizontalSplitPane.add(centerAndRightHorizontalSplitPane);
		topHorizontalSplitPane.setOneTouchExpandable(true);
		topHorizontalSplitPane.setDividerLocation(400);

		this.add(topHorizontalSplitPane, BorderLayout.CENTER);
		initializeToolBar();

		// arcFilterPanel
		arcTypeFilterPanel = new ArcTypeFilterPanel("Arc Types", null,
				graph.getGraphArcStyle());
		nodeLevelFilterPanel = new NodeLevelFilterPanel("Node Levels", null,
				graph.getGraphArcStyle());

		// right panel
		centerGraphPanel = new JPanel(new BorderLayout());
		centerGraphPanel.add(new PScrollPane(graph.getCanvas()),
				BorderLayout.CENTER);
		centerGraphPanel.setMinimumSize(new Dimension(800, 600));

		rightFilterSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // new
		// rightFilterSplitPane.setBorder(BorderFactory.createEmptyBorder(1, 1,
		// 1, 1));
		rightFilterSplitPane.setVisible(true);
		rightFilterSplitPane.add(arcTypeFilterPanel);
		rightFilterSplitPane.add(nodeLevelFilterPanel);
		rightFilterSplitPane.setMaximumSize(new Dimension(300, 800));
		rightFilterSplitPane.setDividerLocation(0.5f);
		rightFilterSplitPane.setDividerSize(20);
		centerAndRightHorizontalSplitPane.add(centerGraphPanel);
		centerAndRightHorizontalSplitPane.add(rightFilterSplitPane);
		centerAndRightHorizontalSplitPane.setOneTouchExpandable(true);
		centerAndRightHorizontalSplitPane.setDividerLocation(900);

	}

	public FilterPanel getArcTypeFilterPanel() {
		return arcTypeFilterPanel;
	}

	public FilterPanel getNodeLevelFilterPanel() {
		return nodeLevelFilterPanel;
	}

	public void hideSubclassArcType() {
		String subClass = "has subclass";
		String subClassType = null;
		for (Object arcType : EntryPoint.getFilterManager().getArcTypeFilter()
				.getArcTypes()) {
			if (((String) arcType).equals(subClass)) {
				subClassType = (String) arcType;
			}
		}
		this.arcTypeFilterPanel.setTypeVisibility(subClassType, false);
	}

	public void addListeners() {
		PBasicInputEventHandler graphListener = new PBasicInputEventHandler() {
			@Override
			public void mousePressed(PInputEvent e) {
				PNode node = e.getPickedNode();
				if (node instanceof GraphNode) {
					node.moveToFront();
					nodePressed(e, (GraphNode) node);
				}
				if (e.isLeftMouseButton()) {
					if (e.getClickCount() == 2) {
						if (e.getPickedNode() instanceof GraphNode) {
							// expand this node in tree
						}
					}
				}
				super.mousePressed(e);
			}

			private void nodePressed(PInputEvent e, GraphNode displayNode) {
				// select node in the tree explorer
				JTree jTree = EntryPoint.getTopView().getjTree();
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) jTree
						.getModel().getRoot();
				Enumeration<?> enumeration = rootNode.breadthFirstEnumeration();
				while (enumeration.hasMoreElements()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
							.nextElement();
					GraphNode graphNode = (GraphNode) node.getUserObject();
					if (graphNode.getUserObject() == displayNode
							.getUserObject()) {
						TreePath treePath = new TreePath(
								((DefaultTreeModel) jTree.getModel())
										.getPathToRoot(node));
						jTree.scrollPathToVisible(treePath);
						jTree.setSelectionPath(treePath);
					}
				}

			}

		};

		EntryPoint.getFlatGraph().getCamera()
				.addInputEventListener(graphListener);
		graph.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (Graph.GRAPH_ARC_STYLE_PROPERTY.equals(evt.getPropertyName())) {
					arcTypeFilterPanel.setStyle(graph.getGraphArcStyle());
				}
			}
		});
		EntryPoint.getFilterManager().addFilterChangedListener(
				arcTypeFilterPanel);
		graph.getModel().addGraphModelListener(new GraphModelAdapter() {
			@Override
			public void graphArcTypeAdded(Object arcType) {
				arcTypeFilterPanel.reload();
			}
		});

		rightFilterSplitPane.addContainerListener(new ContainerListener() {
			public void componentAdded(ContainerEvent e) {
				refeshRightPanel();
			}

			public void componentRemoved(ContainerEvent e) {
				refeshRightPanel();
			}
		});

	}

	public void changeDividerLocation() {
		topHorizontalSplitPane.setDividerLocation(300);

	}

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	private void initializeToolBar() {

		addToolBarAction(new OpenOntologyFileAction());
		// addToolBarAction(new ClearOrphansAction(graph.getModel(), graph));
		// zoom
		addToolBarAction(new ZoomInAction(graph.getCamera()));
		addToolBarAction(new NoZoomAction(graph.getCamera()));
		addToolBarAction(new ZoomOutAction(graph.getCamera()));

		// getToolBar().addSeparator();

		// node and arc filter actions
		// final JToggleButton nodesToggle = addToolBarToggleAction(new
		// ShowFilterPanelAction(getNodeFilterPanel()));
		// final JToggleButton arcsToggle = addToolBarToggleAction(new
		// ShowFilterPanelAction(
		// getArcFilterPanel()));

		// arcsToggle.setVisible(true);
		// listen for panel close events - keep the toggle buttons in sync
		/*
		 * getNodeFilterPanel().getCloseButton().addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) {
		 * nodesToggle.setSelected(false); } });
		 */
		// getArcFilterPanel().getCloseButton().addActionListener(
		// new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// arcsToggle.setSelected(false);
		// }
		// });
	}

	public JToolBar getToolBar() {
		if (toolbar == null) {
			toolbar = new JToolBar(JToolBar.HORIZONTAL);
			toolbar.setFloatable(false);
			toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		return toolbar;
	}

	public StatusProgressBar getStatusBar() {
		if (status == null) {
			status = new StatusProgressBar();
		}
		return status;
	}

	/**
	 * Repaints the this panel so that the right panel will properly resize.
	 */
	private void refeshRightPanel() {
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();

		if (rightFilterSplitPane.getTopComponent() == null
				&& rightFilterSplitPane.getBottomComponent() == null) {
			centerAndRightHorizontalSplitPane.setDividerLocation(1.0);
		}

		if (rightFilterSplitPane.getTopComponent() == null
				|| rightFilterSplitPane.getBottomComponent() == null) {
			rightFilterSplitPane.setDividerSize(0);
		} else {
			rightFilterSplitPane.setDividerSize(2);
			rightFilterSplitPane.setDividerLocation(0.5);
		}
	}

	private FilterPanel getNodeFilterPanel() {
		if (nodeFilterPanel == null) {
			Icon icon = ResourceHandler.getIcon("icon_node_filter.gif");
			final FilterManager filterManager = graph.getFilterManager();
			nodeFilterPanel = new FilterPanel("Node Types", icon,
					graph.getGraphNodeStyle()) {
				private static final long serialVersionUID = -2445793622682539920L;

				public void setTypeVisibility(Object nodeType, boolean visible) {
					filterManager.setNodeTypeVisible(nodeType, visible);
				}

				public Map<Object, Boolean> getTypes() {
					return filterManager.getNodeTypesMap();
				}
			};
			graph.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (Graph.GRAPH_NODE_STYLE_PROPERTY.equals(evt
							.getPropertyName())) {
						nodeFilterPanel.setStyle(graph.getGraphNodeStyle());
					}
				}
			});
			filterManager.addFilterChangedListener(nodeFilterPanel);
			// TODO this doesn't handle when the graph model changes!
			graph.getModel().addGraphModelListener(new GraphModelAdapter() {
				@Override
				public void graphNodeTypeAdded(Object nodeType) {
					nodeFilterPanel.reload();
				}
			});
		}
		return nodeFilterPanel;
	}

	public JButton addToolBarAction(Action action) {
		JButton btn = getToolBar().add(action);
		btn.setToolTipText((String) action.getValue(Action.NAME));
		return btn;
	}

	public JToggleButton addToolBarToggleAction(Action action) {
		JToggleButton btn = new JToggleButton(action);
		btn.setText(null);
		btn.setToolTipText((String) action.getValue(Action.NAME));
		getToolBar().add(btn);
		return btn;
	}

	public void removeToolBarAction(Action action) {
		if (action != null) {
			Component found = null;
			for (Component c : getToolBar().getComponents()) {
				if (c instanceof AbstractButton) {
					AbstractButton btn = (AbstractButton) c;
					if (action.equals(btn.getAction())) {
						found = c;
						break;
					}
				}
			}
			if (found != null) {
				getToolBar().remove(found);
				getToolBar().revalidate();
				getToolBar().repaint();
			}
		}
	}

	public void addToolBarSeparator() {
		addToolBarComponent(null);
	}

	public void addToolBarComponent(Component component) {
		if (component == null) {
			getToolBar().addSeparator();
		} else {
			getToolBar().add(component);
		}
	}

	public void removeToolBarComponent(Component c) {
		if (c != null) {
			getToolBar().remove(c);
			getToolBar().revalidate();
			getToolBar().repaint();
		}
	}

	DefaultTreeCellRenderer treeCellRender = new DefaultTreeCellRenderer() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			this.setLeafIcon(null);
			this.setOpenIcon(null);
			this.setClosedIcon(null);

			// StringBuffer html = new
			// StringBuffer("<html><b style= \"color: #000000; background-color: #fff3ff\">T</b>  ");
			// StringBuffer html.append(value.toString());
			// html.append("</html>");
			return super.getTreeCellRendererComponent(tree, value.toString(),
					sel, expanded, leaf, row, hasFocus);
		}
	};

}
