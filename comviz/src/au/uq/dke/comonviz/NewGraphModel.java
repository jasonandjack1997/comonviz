package au.uq.dke.comonviz;

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
	private void createNodes(){
		
		List<OntologyClass> ontologyClassList = this.ontologyClassService.findAll();
		for(OntologyClass ontologyClass: ontologyClassList){
			super.addNode(ontologyClass, ontologyClass.getName(),null);
		}
	}
	
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

	private GraphNode findGraphNode(OntologyClass ontologyClass){
		for(Map.Entry<Object, GraphNode> entry: nodes.entrySet()){
			if(((OntologyClass)entry.getKey()).getId() == ontologyClass.getId()){
				return entry.getValue();
			}
			
		}
		
		return null;
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
