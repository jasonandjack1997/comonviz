package database.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.uq.dke.comonviz.model.DatabaseModelListener;

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

import database.dao.OntologyClassDAO;
import database.model.ontology.OntologyClass;
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
public class OntologyClassService {

	OntologyClassDAO dao;
	List<DatabaseModelListener> listeners = new ArrayList<DatabaseModelListener>();
	public List<DatabaseModelListener> getListeners() {
		return listeners;
	}

	protected void fireClassAddedEvent(OntologyClass cls){
		for(DatabaseModelListener listener: listeners){
			listener.databaseClassAdded(cls);
		}
	}
	
	protected void fireClassUpdatdeEvent(OntologyClass cls){
		for(DatabaseModelListener listener: listeners){
			listener.databaseClassUpdated(cls);
		}
	}
	

	protected void fireClassRemovedEvent(OntologyClass cls){
		for(DatabaseModelListener listener: listeners){
			listener.databaseClassRemoved(cls);
		}
	}

	@Autowired
	public void setDao(OntologyClassDAO dao) {
		this.dao = dao;
	}

	public List<OntologyClass> getRootList(){
			List<OntologyClass> rootList = this.dao.search(new Search().addFilterEqual("level", 1));
			return rootList;
	}
	public void deleteAll() {
		List<OntologyClass> classList = dao.findAll();
		for(OntologyClass ontologyClass: classList){
			dao.remove(ontologyClass);
		}
	}
	
	public void delete(OntologyClass ontologyClass){
		dao.remove(ontologyClass);
		fireClassRemovedEvent(ontologyClass);
	}
	public void save(OntologyClass ontologyClass) {
		boolean isCreate = dao.save(ontologyClass);
		if (isCreate) {
			fireClassAddedEvent(ontologyClass);
		} else {
			fireClassUpdatdeEvent(ontologyClass);
		}
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
