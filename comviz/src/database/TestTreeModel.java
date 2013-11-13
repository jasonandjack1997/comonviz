package database;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import au.uq.dke.comonviz.NewGraphModel;
import database.model.ontology.OntologyClass;

public class TestTreeModel extends JPanel{
	DefaultTreeModel treeModel ;
	JTree jTree;
	NewGraphModel newGraphModel = new NewGraphModel();
	
	public void start(){
		
		JFrame jFrame = new JFrame();
		DefaultMutableTreeNode treeRoot = newGraphModel.generateMutableTree();
		treeModel = new DefaultTreeModel(treeRoot);
		jTree = new JTree(treeModel);
		jTree.setSelectionRow(0);
		JScrollPane leftTopJScrollPane = new JScrollPane(jTree);
		this.add(leftTopJScrollPane);
		jFrame.add(this);
		jFrame.setMinimumSize(new Dimension(600, 600));
		jFrame.pack();
		jFrame.setVisible(true);
		OntologyClass rootCls = (OntologyClass)newGraphModel.findRoot().getUserObject();
		rootCls.setName("hehe3");
		
	}
	
	public static void main(String args[]){
		new TestTreeModel().start();
	}
}
