package org.protege.ontograf.tree;

import java.util.List;

import org.semanticweb.owlapi.model.OWLEntity;

import uk.ac.manchester.cs.bhig.util.Tree;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;

public class EntityTreeInfo {
	
	private OWLEntity owlEntity;
	
	private Tree tree;
	/**
	 * refer to the tree branch it belongs, so as to get the color
	 */
	private Object branchObject;
	/**
	 * the order of this node among its siblings to arrange the order on the graph
	 */
	private int siblingNumber = 0;
	
	/**
	 * the level of it in the tree it belongs. used to determine the size of the node and the arc
	 */
	private int level = 0;
	
	private GraphNode graphNode;
	
	public OWLEntity getOwlEntity() {
		return owlEntity;
	}
	public void setOwlEntity(OWLEntity owlEntity) {
		this.owlEntity = owlEntity;
	}
	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public Object getBranchObject() {
		return branchObject;
	}
	public void setBranchObject(Object branchObject) {
		this.branchObject = branchObject;
	}
	public int getSiblingNumber() {
		return siblingNumber;
	}
	public void setSiblingNumber(int siblingNumber) {
		this.siblingNumber = siblingNumber;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public GraphNode getGraphNode() {
		return graphNode;
	}
	public void setGraphNode(GraphNode graphNode) {
		this.graphNode = graphNode;
	}
	public EntityTreeInfo(OWLEntity owlEntity, Tree tree, Object branchObject, int level, int siblingNumber){
		this.owlEntity = owlEntity;
		this.tree = tree;
		this.branchObject = branchObject;
		this.level = level;
		this.siblingNumber = siblingNumber;
	}
	

}
