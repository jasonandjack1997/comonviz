package au.uq.dke.comonviz.ui.refactor;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.junit.Before;
import org.junit.Test;

public class ClassesCRUDDialogTest {
	ClassesCRUDDialog dialog = new ClassesCRUDDialog();

	@Before
	public void setUp() throws Exception {
		dialog.setPreferredSize(new Dimension(400, 400));
		dialog.pack();
		
	}

	@Test
	public void test() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root2");
//		dialog.add(new JButton("button"));
		dialog.setTreeModel(new DefaultTreeModel(root));
		dialog.setVisible(true);
		return;
	}

}
