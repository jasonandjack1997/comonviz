package au.uq.dke.comonviz.handler.tree;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.semanticweb.owlapi.model.OWLEntity;

import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.ProtegeGraphModel;
import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;

public class MyTreeExpansionListener implements TreeExpansionListener {

	private NodeCollection selectedNodes;

	public MyTreeExpansionListener(NodeCollection selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	@Override
	public void treeExpanded(TreeExpansionEvent e) {
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
				GraphNode realGraphNode = EntryPoint.gc.getModel().getNode(
						userObject);
				if (realGraphNode == null) {

					((ProtegeGraphModel) EntryPoint.gc.getGraph().getModel())
							.showWithExsistingNodes((OWLEntity) userObject,
									((AbstractGraph) EntryPoint.graph)
											.getFilterManager());
					// ((ProtegeGraphModel)
					// EntryPoint.gc.getGraph().getModel()).(realGraphNode);
				} else {
				}
			} catch (NullPointerException e2) {
				e2.printStackTrace();
			}
		}

		(EntryPoint.gc.getGraph()).performLayout();

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent e) {
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

		for (GraphNode graphNodeToDelete : ((ProtegeGraphModel) EntryPoint.gc
				.getGraph().getModel()).getVisibleNodes()) {
			if(!userObjectCollection.contains(graphNodeToDelete.getUserObject())){
				((ProtegeGraphModel) EntryPoint.gc
						.getGraph().getModel()).removeNode(graphNodeToDelete.getUserObject());
			}

		}

		(EntryPoint.gc.getGraph()).performLayout();

	}

	// @Override
	// public void treeWillExpand(TreeExpansionEvent e)
	// throws ExpandVetoException {
	//
	// DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode)
	// ((JTree)e.getSource()).getLastSelectedPathComponent();
	// GraphNode selectedGraphNode = (GraphNode)
	// selectedTreeNode.getUserObject();
	// Object userObject = selectedGraphNode.getUserObject();
	// GraphNode realGraphNode = EntryPoint.gc.getModel().getNode(userObject);
	// //this.selectedNodes.setNode(realGraphNode);
	// ((ProtegeGraphModel)
	// EntryPoint.gc.getGraph().getModel()).expandNode(realGraphNode);
	// (EntryPoint.gc.getGraph()).performLayout();
	// }
	//
	// @Override
	// public void treeWillCollapse(TreeExpansionEvent e)
	// throws ExpandVetoException {
	// DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode)
	// ((JTree)e.getSource()).getLastSelectedPathComponent();
	// GraphNode selectedGraphNode = (GraphNode)
	// selectedTreeNode.getUserObject();
	// Object userObject = selectedGraphNode.getUserObject();
	// ((ProtegeGraphModel)
	// EntryPoint.gc.getGraph().getModel()).collapseNode2((OWLEntity)
	// userObject);
	// (EntryPoint.gc.getGraph()).performLayout();
	//
	// }

}
