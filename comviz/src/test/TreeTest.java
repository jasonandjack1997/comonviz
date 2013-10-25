package test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.manchester.cs.bhig.util.MutableTree;

public class TreeTest <T extends MutableTree> {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String args[]){
		MutableTree root1 = new MutableTree("root1");
		MutableTree child1_1 = new MutableTree("child1_1");
		MutableTree child1_2 = new MutableTree("child_1_2");
		
		root1.addChild(child1_1);
		child1_1.addChild(child1_2);
		
		List l = new ArrayList();
		l = child1_2.getPathToRoot();
		return;
	}

}
