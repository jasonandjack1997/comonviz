package au.uq.dke.comonviz.ui.crud;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import au.uq.dke.comonviz.EntryPoint;


public class GraphNodeCRUD extends JPanel{

	public GraphNodeCRUD(){
		JFrame jFrame = new JFrame("hehe");
		JDialog jDialog = new JDialog(jFrame, "dialog1 ok!");
		jDialog.setSize(400,400);
		jFrame.add(this);
		//this.add(jDialog);
		jDialog.setVisible(true);
		Container jDialogContainer = jDialog.getContentPane();
		JButton btn = new JButton("hehe");
		jDialogContainer.add(btn);
		jFrame.setVisible(true);
		jFrame.pack();
	}
	
	private void initGUI(){
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

	}
	
	public  static void main(String args[]){
		new GraphNodeCRUD();
	}
}
