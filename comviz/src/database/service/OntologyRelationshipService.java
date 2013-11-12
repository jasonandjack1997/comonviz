package database.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

import database.dao.OntologyRelationshipDAO;
import database.model.ontology.OntologyAxiom;
import database.model.ontology.OntologyRelationship;

/**
 * This is the implementation for our OntologyRelationship Service. The @Service annotation
 * allows Spring to automatically detect this as a component rather than having
 * to comfigure it in XML. The @Autowired annotation tells Spring to inject our
 * OntologyRelationship DAO using the setDao() method.
 * 
 * @author dwolverton
 * 
 */
@Service
@Transactional
public class OntologyRelationshipService {
	
	@Autowired
	OntologyClassService ontologyClassService;
	@Autowired
	OntologyAxiomService ontologyAxiomService;

	OntologyRelationshipDAO dao;

	@Autowired
	public void setDao(OntologyRelationshipDAO dao) {
		this.dao = dao;
	}

	public void deleteAll() {
		List<OntologyRelationship> relationshipList = dao.findAll();
		for(OntologyRelationship relationship: relationshipList){
			dao.remove(relationship);
		}
	}
	
	public void delete(OntologyRelationship relationship){
		dao.remove(relationship);
		
	}

	public void save(OntologyRelationship ontologyRelationship) {
		dao.save(ontologyRelationship);
	}

	public List<OntologyRelationship> findAll() {
		return dao.findAll();
	}

	public OntologyRelationship findByName(String name) {
		if (name == null)
			return null;
		return dao.searchUnique(new Search().addFilterEqual("name", name));
	}

	public void flush() {
		dao.flush();
	}

	public List<OntologyRelationship> search(ISearch search) {
		return dao.search(search);
	}

	public OntologyRelationship findById(Long id) {
		return dao.find(id);
	}

	public void delete(Long id) {
		dao.removeById(id);
	}

	public SearchResult<OntologyRelationship> searchAndCount(ISearch search) {
		return dao.searchAndCount(search);
	}
}
