package org.protege.ontograf.treeUtils;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;

public class MyTreeWillExpandListener implements TreeWillExpandListener {

	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {

		int a = 1;
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		// TODO Auto-generated method stub
		
	}

}
