package database;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.uq.dke.comonviz.ComonvizGraphModel;
import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.treeUtils.EntityTreeInfo;
import au.uq.dke.comonviz.treeUtils.TreeInfoManager;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import database.model.ontology.OntologyAxiom;
import database.model.ontology.OntologyClass;
import database.model.ontology.OntologyRelationship;
import database.service.OntologyAxiomService;
import database.service.OntologyClassService;
import database.service.OntologyRelationshipService;

/**
 * Hello world!
 * 
 */
public class App {
	OntologyAxiomService ontologyAxiomService;
	OntologyClassService ontologyClassService;
	OntologyRelationshipService ontologyRelationshipService;

	static ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");

	public static void main(String[] args) {
		new App().start();

		ctx.close();
	}

	public void start() {

		ontologyAxiomService = (OntologyAxiomService) ctx
				.getBean("ontologyAxiomService");

		ontologyClassService = (OntologyClassService) ctx
				.getBean("ontologyClassService");

		ontologyRelationshipService = (OntologyRelationshipService) ctx
				.getBean("ontologyRelationshipService");

		// importOWLToDatabase();
		// testServices();
		
		

		ontologyAxiomService.deleteAll();
		ontologyRelationshipService.deleteAll();
		importAxiomToDatabase();
		List classes = ontologyClassService.findAll();
		List axioms = ontologyAxiomService.findAll();
		List relationships = ontologyRelationshipService.findAll();
		return;

	}

	public void testServices() {

		List ol = ontologyClassService.findAll();
		OntologyClass oc1 = ontologyClassService
				.findByName("Compliance Management");

		ontologyClassService.findByIRI(oc1.getIri());

	}

	public void importAxiomToDatabase() {
		EntryPoint entryPoint = new EntryPoint();
		entryPoint.start();

		ComonvizGraphModel model = EntryPoint.getGraphModel();
		Map<Object, GraphArc> arcs = model.getArcs();

		for (Map.Entry entry : arcs.entrySet()) {
			GraphArc graphArc = (GraphArc) entry.getValue();
			String key = graphArc.getUserObject().toString();

			String sourceIRI = graphArc.getSource().getUserObject().toString();
			String destinationIRI = graphArc.getDestination().getUserObject()
					.toString();

			sourceIRI = sourceIRI.substring(1, sourceIRI.length() - 1);
			destinationIRI = destinationIRI.substring(1,
					destinationIRI.length() - 1);

			List<OntologyClass> classes = ontologyClassService.findAll();
			OntologyClass sourceClass = ontologyClassService
					.findByIRI(sourceIRI);
			OntologyClass destinationClass = ontologyClassService
					.findByIRI(destinationIRI);

			String iri = graphArc.getType().toString();
			if(iri.contains("subclass")){//recover to normal type
				iri = "<http://www.semanticweb.org/uqwwan10/ontologies/2013/9/untitled-ontology-10#" + iri + ">";
			}
			iri = iri.substring(1, iri.length() - 1);

			String name = iri.substring(iri.lastIndexOf("#") + 1, iri.length()).replace("_", " ");

			OntologyRelationship ontologyRelationship = new OntologyRelationship();
			ontologyRelationship.setKey(key);
			ontologyRelationship.setSrcClassId(sourceClass.getId());
			ontologyRelationship.setDstClassId(destinationClass.getId());
			ontologyRelationship.setName(iri);
			ontologyRelationship.setName(name);

			OntologyAxiom newAxiom = new OntologyAxiom();
			newAxiom.setIri(iri);
			newAxiom.setName(name);

			OntologyAxiom ontologyAxiom = ontologyAxiomService.findByName(name);
			if (ontologyAxiom == null) {
				ontologyAxiomService.save(newAxiom);
				//ontologyAxiomService.delete(ontologyAxiom.getId());
			}

			ontologyRelationshipService.save(ontologyRelationship);
		}
	}

	public void importOWLToDatabase() {
		EntryPoint entryPoint = new EntryPoint();
		entryPoint.start();

		TreeInfoManager treeInfoManager = TreeInfoManager.getTreeManager();
		Map<Object, EntityTreeInfo> infoMap = TreeInfoManager
				.getEntityTreeInfoMap();

		for (Map.Entry entry : infoMap.entrySet()) {
			EntityTreeInfo treeInfo = (EntityTreeInfo) entry.getValue();
			OntologyClass ontologyClass = new OntologyClass();
			String iri = treeInfo.getOwlEntity().toStringID();
			ontologyClass.setIri(iri);

			ontologyClass.setName(iri.substring(iri.lastIndexOf("#") + 1,
					iri.length()).replace("_", " "));

			String annotation = "";
			Collection<OWLAnnotation> owlAnnotationSet = (treeInfo
					.getOwlEntity()).getAnnotations(EntryPoint.ontology);
			if (owlAnnotationSet.size() != 0) {
				annotation = ((OWLAnnotation) owlAnnotationSet.toArray()[0])
						.getValue().toString();
				annotation = annotation.substring(1, annotation.length() - 1);
			}

			ontologyClass.setDiscription(annotation);
			ontologyClassService.save(ontologyClass);
		}
	}

}
