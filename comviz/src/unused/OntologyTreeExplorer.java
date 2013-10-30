package unused;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import uk.ac.manchester.cs.bhig.util.MutableTree;
import comonviz.EntryPoint;
import comonviz.WindowUtilities;

public class OntologyTreeExplorer extends JPanel {

	JTree tree;

	public OntologyTreeExplorer() {
		WindowUtilities.setNativeLookAndFeel();
		Container content = this;
		setVisible(true);

	}

	public void updateTree() {

		DefaultMutableTreeNode root = null;
		if (EntryPoint.ontologyTree != null) {
			root = convertFromManchesterToUITreeNode(EntryPoint.ontologyTree);
		}
		tree = new JTree(root);
		JScrollPane jScrollpane = new JScrollPane(tree);
		jScrollpane.setPreferredSize(this.getParent().getSize());
		this.add(jScrollpane, BorderLayout.CENTER);
		// this.setSize(this.getParent().getSize());
		// setVisible(true);
		// tree.setSize(1000,2000);

		tree.setCellRenderer(new DefaultTreeCellRenderer() {
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

				/*
				 * String search = "javax"; String text = value.toString();
				 * 
				 * StringBuffer html = new StringBuffer("<html>"); Matcher m =
				 * Pattern.compile(Pattern.quote(search)).matcher(text); while
				 * (m.find()) m.appendReplacement(html, "<b>" + m.group() +
				 * "</b>"); m.appendTail(html).append("</html>");
				 */

				StringBuffer html = new StringBuffer(
						"<html><b style= \"color: #000000; background-color: #fff3ff\">T</b>  ");
				html.append(value.toString());
				html.append("</html>");
				return super.getTreeCellRendererComponent(tree,
						html.toString(), sel, expanded, leaf, row, hasFocus);
			}
		});
	}

	/**
	 * Small routine that will make node out of the first entry in the array,
	 * then make nodes out of subsequent entries and make them child nodes of
	 * the first one. The process is repeated recursively for entries that are
	 * arrays.
	 */

	private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
		DefaultMutableTreeNode child;
		for (int i = 1; i < hierarchy.length; i++) {
			Object nodeSpecifier = hierarchy[i];
			if (nodeSpecifier instanceof Object[]) // Ie node with children
				child = processHierarchy((Object[]) nodeSpecifier);
			else
				child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
			node.add(child);
		}
		return (node);
	}

	private DefaultMutableTreeNode convertFromManchesterToUITreeNode(
			MutableTree manchesterMutableTree) {

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				manchesterMutableTree.getUserObject());
		DefaultMutableTreeNode child;

		java.util.List<MutableTree> childrenList = manchesterMutableTree
				.getChildren();
		for (int i = 0; i < childrenList.size(); i++) {
			MutableTree nodeSpecifier = childrenList.get(i);
			if (nodeSpecifier.getChildCount() != 0) // Ie node with children
				child = convertFromManchesterToUITreeNode(nodeSpecifier);
			else
				child = new DefaultMutableTreeNode(
						nodeSpecifier.getUserObject()); // Ie Leaf
			node.add(child);
		}
		return (node);
	}

}