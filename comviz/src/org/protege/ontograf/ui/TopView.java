package org.protege.ontograf.ui;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import comonviz.EntryPoint;

import uk.ac.manchester.cs.bhig.util.MutableTree;
import ca.uvic.cs.chisel.cajun.actions.CajunAction;
import ca.uvic.cs.chisel.cajun.actions.LayoutAction;
import ca.uvic.cs.chisel.cajun.actions.NoZoomAction;
import ca.uvic.cs.chisel.cajun.actions.ZoomInAction;
import ca.uvic.cs.chisel.cajun.actions.ZoomOutAction;
import ca.uvic.cs.chisel.cajun.filter.FilterManager;
import ca.uvic.cs.chisel.cajun.graph.FlatGraph;
import ca.uvic.cs.chisel.cajun.graph.Graph;
import ca.uvic.cs.chisel.cajun.graph.GraphModelAdapter;
import ca.uvic.cs.chisel.cajun.graph.ui.FilterPanel;
import ca.uvic.cs.chisel.cajun.graph.ui.StatusProgressBar;
import ca.uvic.cs.chisel.cajun.resources.ResourceHandler;
import edu.umd.cs.piccolox.swing.PScrollPane;

public class TopView extends JPanel {
	
	private MutableTree ontologyTree;
	private static final long serialVersionUID = -7720543969598323711L;

	private FlatGraph graph;

	private JToolBar toolbar;
	private JPanel mainPanel;
	private StatusProgressBar status;

	private JSplitPane rightPanel;
	private FilterPanel nodeFilterPanel;
	private FilterPanel arcFilterPanel;
	
	private JSplitPane horizontalSplitPane;
	
	private JSplitPane topHorizontalSplitPane;

	private JSplitPane leftPanel;

	public TopView(FlatGraph graph) {
		super(new BorderLayout());
		this.graph = graph;

		initialize();
	}

	public void initialize() {
		//this.ontologyTree = ontologyTree;

		this.add(getToolBar(), BorderLayout.NORTH);
		
		this.add(getStatusBar(), BorderLayout.SOUTH);
		
		topHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.add(getMainPanel());
		horizontalSplitPane.add(getRightPanel());
		
		topHorizontalSplitPane.add(getLeftPanel());
		topHorizontalSplitPane.add(horizontalSplitPane);
		getLeftPanel().add(EntryPoint.ontologyTreeExplorer);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				horizontalSplitPane.setDividerLocation(1.0);
				TopView.this.removeComponentListener(this);
			}
		});
		
//		this.add(horizontalSplitPane, BorderLayout.CENTER);
		this.add(topHorizontalSplitPane, BorderLayout.CENTER);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if(getRightPanel().getTopComponent() == null && getRightPanel().getBottomComponent() == null) {
					horizontalSplitPane.setDividerLocation(1.0);
				}
				
				super.componentResized(e);
			}
		});
		
		//this.add(getRightPanel(), BorderLayout.EAST);

		initializeToolBar();
	}

	private void initializeToolBar() {
		// Home
		//addToolBarAction(new FocusOnHomeAction(graph.getAnimationHandler()));
		//addToolBarAction(new ClearOrphansAction(graph.getModel(), graph));

		//getToolBar().addSeparator();

		// Layouts
		for (LayoutAction action : graph.getLayouts()) {
			//addToolBarAction(action);
		}

		//getToolBar().addSeparator();
		
		//add BOTH Button & the ACTION

		// zoom
		addToolBarAction(new ZoomInAction(graph.getCamera()));
		addToolBarAction(new NoZoomAction(graph.getCamera()));
		addToolBarAction(new ZoomOutAction(graph.getCamera()));

		getToolBar().addSeparator();

		// node and arc filter actions
		//final JToggleButton nodesToggle = addToolBarToggleAction(new ShowFilterPanelAction(getNodeFilterPanel()));
		final JToggleButton arcsToggle = addToolBarToggleAction(new ShowFilterPanelAction(getArcFilterPanel()));
		// listen for panel close events - keep the toggle buttons in sync
/*		getNodeFilterPanel().getCloseButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodesToggle.setSelected(false);
			}
		});
*/		getArcFilterPanel().getCloseButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arcsToggle.setSelected(false);
			}
		});
	}

	/**
	 * Returns the main panel - this contains the {@link Graph} in the center position of the panel
	 * which is using a {@link BorderLayout}.
	 * 
	 * @return JPanel
	 */
	public JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.add(new PScrollPane(graph.getCanvas()), BorderLayout.CENTER);
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

	public JSplitPane getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT); //new GradientPanel();
			leftPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 40, 1));
			leftPanel.addContainerListener(new ContainerListener() {
				public void componentAdded(ContainerEvent e) {
					//refeshLeftPanel();
				}

				public void componentRemoved(ContainerEvent e) {
					//refeshLeftPanel();
				}
			});
		}
		return leftPanel;
	}
	
	public JSplitPane getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT); //new GradientPanel();
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

		if(rightPanel.getTopComponent() == null && rightPanel.getBottomComponent() == null) {
			horizontalSplitPane.setDividerLocation(1.0);
		}
		
		if (rightPanel.getTopComponent() == null || rightPanel.getBottomComponent() == null) {
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
			nodeFilterPanel = new FilterPanel("Node Types", icon, graph.getGraphNodeStyle()) {
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
					if (Graph.GRAPH_NODE_STYLE_PROPERTY.equals(evt.getPropertyName())) {
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
			arcFilterPanel = new FilterPanel("Arc Types", icon, graph.getGraphArcStyle()) {
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
					if (Graph.GRAPH_ARC_STYLE_PROPERTY.equals(evt.getPropertyName())) {
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
				//getRightPanel().getParent().repaint();

				//if (getRightPanel().getComponentCount() == 0) {
				//getRightPanel().setSize(0, 0);
				getRightPanel().invalidate();
				
				//horizontalSplitPane.setDividerLocation(1.0);
				//}
			} else {
				filterPanel.reload();
				getRightPanel().add(filterPanel);
				getRightPanel().invalidate();
				
				horizontalSplitPane.setDividerLocation(0.8);
			}
		}
	}

}
