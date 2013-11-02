package org.protege.ontograf.treeUtils;

import java.util.Collection;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.protege.ontograf.ui.TopView;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import comonviz.EntryPoint;

public class MyTreeSelectionListener implements TreeSelectionListener {
	private NodeCollection selectedNodes;
	private JTextArea annotationTextArea;
	
	public MyTreeSelectionListener(TopView topView, NodeCollection selectedNodes){
		this.selectedNodes = selectedNodes;
		this.annotationTextArea = topView.getjTextArea();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
		try {
			DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) ((JTree)e.getSource()).getLastSelectedPathComponent();
			GraphNode selectedGraphNode = (GraphNode) selectedTreeNode.getUserObject();
			Object userObject = selectedGraphNode.getUserObject();
			GraphNode realGraphNode = EntryPoint.gc.getModel().getNode(userObject);
			this.selectedNodes.setNode(realGraphNode);
			Collection <OWLAnnotation> owlAnnotationSet = ((OWLClass)userObject).getAnnotations(EntryPoint.ontology);
			if(owlAnnotationSet.size() != 0){
				String annotation = ((OWLAnnotation)owlAnnotationSet.toArray()[0]).getValue().toString();
				annotation = annotation.substring(1, annotation.length() -1);
				this.annotationTextArea.setText(annotation);
				this.annotationTextArea.setCaretPosition(0);
			}else{
				this.annotationTextArea.setText("");
			}
		} catch(NullPointerException e2){
			e2.printStackTrace();
		}
		
		
	}

}
