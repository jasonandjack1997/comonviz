package database.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

import database.dao.OntologyClassDAO;
import database.model.ontology.OntologyAxiom;
import database.model.ontology.OntologyClass;

/**
 * This is the implementation for our OntologyClass Service. The @Service annotation
 * allows Spring to automatically detect this as a component rather than having
 * to comfigure it in XML. The @Autowired annotation tells Spring to inject our
 * OntologyClass DAO using the setDao() method.
 * 
 * @author dwolverton
 * 
 */
@Service
@Transactional
public class OntologyClassService{

	OntologyClassDAO dao;

	@Autowired
	public void setDao(OntologyClassDAO dao) {
		this.dao = dao;
	}

	public void deleteAll() {
		List<OntologyClass> classList = dao.findAll();
		for(OntologyClass ontologyClass: classList){
			dao.remove(ontologyClass);
		}
	}
	
	public void delete(OntologyClass ontologyClass){
		dao.remove(ontologyClass);
		
	}
	public void save(OntologyClass ontologyClass) {
		dao.save(ontologyClass);
	}

	public List<OntologyClass> findAll() {
		return dao.findAll();
	}

	public OntologyClass findByName(String name) {
		if (name == null)
			return null;
		return dao.searchUnique(new Search().addFilterEqual("name", name));
	}

	public OntologyClass findByIRI(String iri) {
		if (iri == null)
			return null;
		return dao.searchUnique(new Search().addFilterEqual("iri", iri));
	}

	public void flush() {
		dao.flush();
	}

	public List<OntologyClass> search(ISearch search) {
		return dao.search(search);
	}

	public OntologyClass findById(Long id) {
		return dao.find(id);
	}

	public void delete(Long id) {
		dao.removeById(id);
	}

	public SearchResult<OntologyClass> searchAndCount(ISearch search) {
		return dao.searchAndCount(search);
	}
}
