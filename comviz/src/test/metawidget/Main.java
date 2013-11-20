package metawidget;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.metawidget.swing.SwingMetawidget;

public class Main {

	public static void main( String[] args ) {
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);

		JTree jTree = new JTree(treeModel);

		
		Person person = new Person();
		
		SwingMetawidget metawidget = new SwingMetawidget();
//		metawidget.setToInspect( jTree );
		metawidget.setToInspect( person );
		
		JFrame frame = new JFrame( "Metawidget Tutorial" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 250 );
		frame.setVisible( true );
	}
}