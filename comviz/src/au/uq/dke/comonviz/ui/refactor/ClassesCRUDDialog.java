package au.uq.dke.comonviz.ui.refactor;

import static org.metawidget.inspector.InspectionResultConstants.HIDDEN;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessorConfig;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import database.model.ontology.OntologyClass;

public class ClassesCRUDDialog extends JDialog {

	private SwingMetawidget ontologyClassSwingMetawidget;
	private JTree jTree;
	private DefaultTreeModel treeModel;
	private boolean isEditing = true;

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	public ClassesCRUDDialog() {
		ontologyClassSwingMetawidget = new SwingMetawidget();
		ontologyClassSwingMetawidget.addWidgetProcessor( new BeansBindingProcessor( new BeansBindingProcessorConfig()
		));
		ontologyClassSwingMetawidget.setToInspect(this);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root1");
		// DefaultMutableTreeNode root = EntryPoint.getGraphModel()
		// .generateMutableTree();

		treeModel = new DefaultTreeModel(root);
		jTree = new JTree(treeModel);
		jTree.setSelectionRow(0);
		jTree.setCellRenderer(treeCellRender);
		jTree.addTreeSelectionListener(treeSelectionListener);
		jTree.setPreferredSize(new Dimension(400, 200));
		JScrollPane jScrollPane = new JScrollPane(jTree);
		jScrollPane.setPreferredSize(new Dimension(300, 300));
		Dimension d = jTree.getPreferredScrollableViewportSize();
		this.add(jScrollPane, BorderLayout.NORTH);
		this.add(ontologyClassSwingMetawidget, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	public boolean isReadOnly2() {
		return false;
	}

	@UiAction
	@UiAttribute(name = HIDDEN, value = "${this.isEditing")
	public void delete() {

	}

	@UiAction
	public void add() {
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	@UiAction
	@UiAttribute(name = HIDDEN, value = "${!this.readOnly2}")
	public void edit() {

	}

	@UiAction
	public void save() {

	}

	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
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
			return super.getTreeCellRendererComponent(tree, value.toString(),
					sel, expanded, leaf, row, hasFocus);
		}
	};

	TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) ((JTree) e
					.getSource()).getLastSelectedPathComponent();
			GraphNode selectedGraphNode = (GraphNode) selectedTreeNode
					.getUserObject();
			OntologyClass ontologyClass = (OntologyClass) selectedGraphNode
					.getUserObject();

		}

	};

	public static void main(String args[]) {
		new ClassesCRUDDialog();
	}

}
