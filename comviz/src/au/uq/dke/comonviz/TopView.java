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
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import au.uq.dke.comonviz.actions.CajunAction;
import au.uq.dke.comonviz.filter.FilterManager;
import au.uq.dke.comonviz.handler.tree.MyTreeExpansionListener;
import au.uq.dke.comonviz.handler.tree.MyTreeHirarchyChangeListener;
import au.uq.dke.comonviz.handler.tree.MyTreeItemSelectionListener;
import au.uq.dke.comonviz.ui.FilterPanel;
import au.uq.dke.comonviz.ui.OpenOntologyFileAction;
import au.uq.dke.comonviz.ui.StatusProgressBar;
import uk.ac.manchester.cs.bhig.util.MutableTree;
import ca.uvic.cs.chisel.cajun.graph.FlatGraph;
import ca.uvic.cs.chisel.cajun.graph.Graph;
import ca.uvic.cs.chisel.cajun.graph.GraphModelAdapter;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class TopView extends JPanel {

	private static final long serialVersionUID = -7720543969598323711L;

	private FlatGraph graph;

	private JToolBar toolbar;
	private JPanel mainPanel;
	private StatusProgressBar status;

	private JSplitPane rightPanel;
	private FilterPanel nodeFilterPanel;
	private FilterPanel arcFilterPanel;

	private JSplitPane horizontalSplitPane;

	private JSplitPane leftVerticalSplitPane;
	private JSplitPane topHorizontalSplitPane;
	public JSplitPane getTopHorizontalSplitPane() {
		return topHorizontalSplitPane;
	}

	private DefaultTreeModel treeModel;

	private NodeCollection selectedNodes;

	private JTree jTree;
	private JTextPane  jTextArea;

	public JTextPane  getjTextArea() {
		return jTextArea;
	}

	public void setjTextArea(JTextPane  jTextArea) {
		this.jTextArea = jTextArea;
	}

	public JTree getjTree() {
		return jTree;
	}

	public TopView() {
		super(new BorderLayout());
		this.graph = EntryPoint.getFlatGraph();
		this.selectedNodes = graph.getNodeSelection();
		initialize();
		EntryPoint.getjFrame().add(this);
	}

	public void initialize() {
		// this.ontologyTree = ontologyTree;

		this.add(getToolBar(), BorderLayout.NORTH);

		//this.add(getStatusBar(), BorderLayout.SOUTH);

		treeModel = new DefaultTreeModel(null);

		jTextArea = new JTextPane ();
		jTextArea.setContentType("text/html");
		jTextArea.setMinimumSize(new Dimension(200, 100));
		jTextArea.setText("");
		jTextArea.setEditable(true);
		//jTextArea.setLineWrap(true);
		//jTextArea.setWrapStyleWord(true);
		jTextArea.setMargin(new Insets(10,10,10,10));
		jTree = new JTree(treeModel);

		jTree.addTreeSelectionListener(new MyTreeItemSelectionListener(this,
				selectedNodes));
		jTree.addTreeExpansionListener(new MyTreeExpansionListener(
				selectedNodes));
		jTree.addHierarchyListener(new MyTreeHirarchyChangeListener());
		
		jTree.setSelectionRow(0);
		
		
		jTree.setCellRenderer(new DefaultTreeCellRenderer() {
		        /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
		        public Component getTreeCellRendererComponent(JTree tree,
		                Object value, boolean sel, boolean expanded, boolean leaf,
		                int row, boolean hasFocus) {
					
					this.setLeafIcon(null);
					this.setOpenIcon(null);
					this.setClosedIcon(null);


		            
//					StringBuffer html = new StringBuffer("<html><b style= \"color: #000000; background-color: #fff3ff\">T</b>  ");
//					StringBuffer html.append(value.toString());
//					html.append("</html>");
		            return super.getTreeCellRendererComponent(
		                    tree, value.toString(), sel, expanded, leaf, row, hasFocus);
		        }
		    });
		
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

		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.setMinimumSize(new Dimension(500, 500));

		topHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		topHorizontalSplitPane.add(leftVerticalSplitPane);
		topHorizontalSplitPane.add(horizontalSplitPane);
		topHorizontalSplitPane.setOneTouchExpandable(true);
		topHorizontalSplitPane.setDividerLocation(400);

		
		horizontalSplitPane.add(getMainPanel());
		horizontalSplitPane.add(getRightPanel());
		
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				horizontalSplitPane.setDividerLocation(1.0);
				TopView.this.removeComponentListener(this);
			}
		});

		// this.add(horizontalSplitPane, BorderLayout.CENTER);
		this.add(topHorizontalSplitPane, BorderLayout.CENTER);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (getRightPanel().getTopComponent() == null
						&& getRightPanel().getBottomComponent() == null) {
					horizontalSplitPane.setDividerLocation(1.0);
				}

				super.componentResized(e);
			}
		});

		// this.add(getRightPanel(), BorderLayout.EAST);

		initializeToolBar();
	}

	public void changeDividerLocation(){
		topHorizontalSplitPane.setDividerLocation(300);

	}
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	private void initializeToolBar() {

		addToolBarAction(new OpenOntologyFileAction());
		// addToolBarAction(new ClearOrphansAction(graph.getModel(), graph));
		// zoom
		//addToolBarAction(new ZoomInAction(graph.getCamera()));
		//addToolBarAction(new NoZoomAction(graph.getCamera()));
		//addToolBarAction(new ZoomOutAction(graph.getCamera()));

		//getToolBar().addSeparator();

		// node and arc filter actions
		// final JToggleButton nodesToggle = addToolBarToggleAction(new
		// ShowFilterPanelAction(getNodeFilterPanel()));
		final JToggleButton arcsToggle = addToolBarToggleAction(new ShowFilterPanelAction(
				getArcFilterPanel()));
		
		arcsToggle.setVisible(false);
		// listen for panel close events - keep the toggle buttons in sync
		/*
		 * getNodeFilterPanel().getCloseButton().addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) {
		 * nodesToggle.setSelected(false); } });
		 */
		getArcFilterPanel().getCloseButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						arcsToggle.setSelected(false);
					}
				});
	}

	/**
	 * Returns the main panel - this contains the {@link Graph} in the center
	 * position of the panel which is using a {@link BorderLayout}.
	 * 
	 * @return JPanel
	 */
	public JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(new PScrollPane(graph.getCanvas()),
					BorderLayout.CENTER);
		}
		return mainPanel;
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

	public JSplitPane getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // new
																	// GradientPanel();
			rightPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			rightPanel.addContainerListener(new ContainerListener() {
				public void componentAdded(ContainerEvent e) {
					refeshRightPanel();
				}

				public void componentRemoved(ContainerEvent e) {
					refeshRightPanel();
				}
			});
		}
		rightPanel.setVisible(false);
		return rightPanel;
	}

	/**
	 * Repaints the this panel so that the right panel will properly resize.
	 */
	private void refeshRightPanel() {
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();

		if (rightPanel.getTopComponent() == null
				&& rightPanel.getBottomComponent() == null) {
			horizontalSplitPane.setDividerLocation(1.0);
		}

		if (rightPanel.getTopComponent() == null
				|| rightPanel.getBottomComponent() == null) {
			rightPanel.setDividerSize(0);
		} else {
			rightPanel.setDividerSize(2);
			rightPanel.setDividerLocation(0.5);
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

	private FilterPanel getArcFilterPanel() {
		if (arcFilterPanel == null) {
			Icon icon = ResourceHandler.getIcon("icon_arc_filter.gif");
			final FilterManager filterManager = graph.getFilterManager();
			arcFilterPanel = new FilterPanel("Arc Types", icon,
					graph.getGraphArcStyle()) {
				private static final long serialVersionUID = -1656466039034202473L;

				public void setTypeVisibility(Object arcType, boolean visible) {
					filterManager.setArcTypeVisible(arcType, visible);
				}

				public Map<Object, Boolean> getTypes() {
					return filterManager.getArcTypesMap();
				}
			};
			graph.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (Graph.GRAPH_ARC_STYLE_PROPERTY.equals(evt
							.getPropertyName())) {
						arcFilterPanel.setStyle(graph.getGraphArcStyle());
					}
				}
			});
			filterManager.addFilterChangedListener(arcFilterPanel);
			graph.getModel().addGraphModelListener(new GraphModelAdapter() {
				@Override
				public void graphArcTypeAdded(Object arcType) {
					arcFilterPanel.reload();
				}
			});
		}
		return arcFilterPanel;
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

	private class ShowFilterPanelAction extends CajunAction {
		private static final long serialVersionUID = -3317243155479206347L;

		private FilterPanel filterPanel;

		public ShowFilterPanelAction(FilterPanel filterPanel) {
			super(filterPanel.getTitle(), filterPanel.getIcon());
			setTooltip(filterPanel.getTitle());
			this.filterPanel = filterPanel;
		}

		private boolean isShown() {
			for (int i = 0; i < getRightPanel().getComponentCount(); i++) {
				if (filterPanel == getRightPanel().getComponent(i)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void doAction() {
			if (isShown()) {
				getRightPanel().remove(filterPanel);
				// getRightPanel().getParent().repaint();

				// if (getRightPanel().getComponentCount() == 0) {
				// getRightPanel().setSize(0, 0);
				getRightPanel().invalidate();

				// horizontalSplitPane.setDividerLocation(1.0);
				// }
			} else {
				filterPanel.reload();
				getRightPanel().add(filterPanel);
				getRightPanel().invalidate();

				horizontalSplitPane.setDividerLocation(1.0);
			}
		}
	}

}
