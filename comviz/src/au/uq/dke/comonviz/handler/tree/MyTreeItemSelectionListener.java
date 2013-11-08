package au.uq.dke.comonviz.handler.tree;

import java.util.Collection;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.NodeCollection;
import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.TopView;

public class MyTreeItemSelectionListener implements TreeSelectionListener {
	private NodeCollection selectedNodes;
	private JTextPane  annotationTextArea;
	
	public MyTreeItemSelectionListener(TopView topView, NodeCollection selectedNodes){
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
			GraphNode realGraphNode = EntryPoint.getGraphModel().getNode(userObject);
			this.selectedNodes.setNode(realGraphNode);
			Collection <OWLAnnotation> owlAnnotationSet = ((OWLClass)userObject).getAnnotations(EntryPoint.ontology);
			if(owlAnnotationSet.size() != 0){
				String annotation = ((OWLAnnotation)owlAnnotationSet.toArray()[0]).getValue().toString();
				annotation = annotation.substring(1, annotation.length() -1);
				annotation = EntryPoint.getAnnotationManager().getStylizedAnnotation(annotation);
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
