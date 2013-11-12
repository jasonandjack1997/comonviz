package au.uq.dke.comonviz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uvic.cs.chisel.cajun.graph.DefaultGraphModel;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import database.model.ontology.OntologyClass;
import database.model.ontology.OntologyRelationship;
import database.service.OntologyAxiomService;
import database.service.OntologyClassService;
import database.service.OntologyRelationshipService;

public class NewGraphModel extends DefaultGraphModel {
	EntryPoint entryPoint = new EntryPoint();
	private static OntologyClassService  ontologyClassService = EntryPoint.getOntologyClassService();
	private static OntologyAxiomService  ontologyAxiomService = EntryPoint.getOntologyAxiomService();
	private static OntologyRelationshipService  ontologyRelationshipService = EntryPoint.getOntologyRelationshipService();

	private Map<Object, GraphNode> nodes;

	private Map<Object, GraphArc> arcs;
	
	public NewGraphModel(){
		this.nodes = super.getNodes();
		this.arcs = super.getArcs();
	}
	//tested
	private void createNodes(){
		
		List<OntologyClass> ontologyClassList = this.ontologyClassService.findAll();
		for(OntologyClass ontologyClass: ontologyClassList){
			super.addNode(ontologyClass, ontologyClass.getName(),null);
		}
	}
	//tested
	private void createArcs(){
		
		List<OntologyRelationship> ontologyRelationshipList = this.ontologyRelationshipService.findAll();
		for(OntologyRelationship ontologyRelationship: ontologyRelationshipList){
			OntologyClass sourceOntologyClass = this.ontologyRelationshipService.findSourceOntologyClass(ontologyRelationship);
			OntologyClass destinationOntologyClass = this.ontologyRelationshipService.findDestinationOntologyClass(ontologyRelationship);
			GraphNode sourceNode = findGraphNode(sourceOntologyClass);
			GraphNode destinationNode = findGraphNode(destinationOntologyClass);
			//String t
			
			super.addArc(ontologyRelationship, sourceNode, destinationNode, null);
		}
		
	}
	//tested
	private GraphNode findGraphNode(OntologyClass ontologyClass){
		for(Map.Entry<Object, GraphNode> entry: nodes.entrySet()){
			if(((OntologyClass)entry.getKey()).getId() == ontologyClass.getId()){
				return entry.getValue();
			}
			
		}
		
		return null;
	}
	
	private List<GraphNode> getChildren(GraphNode graphNode){
		List<OntologyClass> relationSrcClassList = this.ontologyRelationshipService.findChildren((OntologyClass) graphNode.getUserObject());
		List<GraphNode> graphNodeList = getGraphNodesFromClasses(relationSrcClassList);
		
		return graphNodeList;
	}
	
	private List<GraphNode> getRelationSrcNodes(GraphNode graphNode){
		List<OntologyClass> relationSrcClassList = this.ontologyRelationshipService.findRelSrcNeighbourClasses((OntologyClass) graphNode.getUserObject());
		List<GraphNode> graphNodeList = getGraphNodesFromClasses(relationSrcClassList);
		return graphNodeList;
	}
	
	private List<GraphNode> getRelationDestNodes(GraphNode graphNode){
		List<OntologyClass> relationDestClassList = this.ontologyRelationshipService.findRelDestNeighbourClasses((OntologyClass) graphNode.getUserObject());
		List<GraphNode> graphNodeList = getGraphNodesFromClasses(relationDestClassList);
		return graphNodeList;
	}
	
	private List<GraphNode> getDesendants(GraphNode graphNode){
		List<OntologyClass> desendantsClassList = this.ontologyRelationshipService.findDesendants((OntologyClass) graphNode.getUserObject());
		List<GraphNode> graphNodeList = getGraphNodesFromClasses(desendantsClassList);
		return graphNodeList;
	}
	
	private List<GraphNode> getGraphNodesFromClasses(List<OntologyClass> classList){
		List<GraphNode> graphNodeList = new ArrayList<GraphNode>();
		for(OntologyClass cls: classList){
			GraphNode node = this.findGraphNode(cls);
			graphNodeList.add(node);
		}
		return graphNodeList;
		
	}
	

	public void test(){
		createNodes();
		createArcs();
		return;
	}
	
	public static void main(String args[]){
		NewGraphModel ngm = new NewGraphModel();
		ngm.test();
		
	}

}
