package database;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.treeUtils.EntityTreeInfo;
import au.uq.dke.comonviz.treeUtils.TreeInfoManager;
import database.model.ontology.OntologyClass;
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

		//importOWLToDatabase();
		testServices();

	}
	
	public void testServices(){
		
		List ol = ontologyClassService.findAll();
		OntologyClass oc1 = ontologyClassService.findByName("Compliance Management");
		
		ontologyClassService.findByIRI(oc1.getIri());
		
		return;
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
					iri.length()).replace("_"," "));

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
