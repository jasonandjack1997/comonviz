package database;

import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.semanticweb.owlapi.model.OWLEntity;

import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.model.EntityTreeInfo;
import au.uq.dke.comonviz.model.TreeInfoManager;
import database.ontology.OntologyClass;

public class Test  extends TestCase{

	private Session session;
	
	@Override
	protected void setUp() throws Exception {
		session = HibernateUtil.currentSession();
	}

	@Override
	protected void tearDown() throws Exception {
		//session.close();
	}


	public void test1(){
		session.beginTransaction();
		OntologyClass ontologyClass = new OntologyClass();
		ontologyClass.setName("compliance management");
		session.save(ontologyClass);
		session.getTransaction().commit();
		
	}
	public void testOwlToDataBase(){
		EntryPoint.main(null);
		Map<Object, EntityTreeInfo> treeInfoMap = TreeInfoManager.getEntityTreeInfoMap();
		for(Map.Entry entry: treeInfoMap.entrySet()){
			session.beginTransaction();
			EntityTreeInfo entityTreeInfo = (EntityTreeInfo) entry.getValue();
			String name = ((OWLEntity)entry.getKey()).toStringID();
			name = name.substring(name.lastIndexOf("#") +1);
			String iri = ((OWLEntity)entry.getKey()).toStringID();
			
			OntologyClass ontologyClass = new OntologyClass();
			ontologyClass.setName(name);
			ontologyClass.setIri(iri);
			ontologyClass.setLevel(entityTreeInfo.getLevel());
			ontologyClass.setSiblingRank(entityTreeInfo.getSiblingNumber());
			ontologyClass.setBranchId(-1L);//目前找不到哇!
			session.save(ontologyClass);
			session.getTransaction().commit();
		}
	}
}
