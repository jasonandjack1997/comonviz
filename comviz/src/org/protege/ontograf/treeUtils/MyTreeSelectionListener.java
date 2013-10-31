package org.protege.ontograf.treeUtils;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.semanticweb.owlapi.model.OWLEntity;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;

import comonviz.EntryPoint;

public class MyTreeSelectionListener implements TreeSelectionListener {
	private NodeCollection selectedNodes;
	
	public MyTreeSelectionListener(NodeCollection selectedNodes){
		this.selectedNodes = selectedNodes;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
		DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) ((JTree)e.getSource()).getLastSelectedPathComponent();
		GraphNode selectedGraphNode = (GraphNode) selectedTreeNode.getUserObject();
		Object userObject = selectedGraphNode.getUserObject();
		GraphNode realGraphNode = EntryPoint.gc.getModel().getNode(userObject);
		this.selectedNodes.setNode(realGraphNode);
		
	}

}
