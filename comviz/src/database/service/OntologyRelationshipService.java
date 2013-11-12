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
import database.dao.OntologyRelationshipDAO;
import database.model.ontology.OntologyClass;
import database.model.ontology.OntologyRelationship;

/**
 * This is the implementation for our OntologyRelationship Service. The @Service
 * annotation allows Spring to automatically detect this as a component rather
 * than having to comfigure it in XML. The @Autowired annotation tells Spring to
 * inject our OntologyRelationship DAO using the setDao() method.
 * 
 * @author dwolverton
 * 
 */
@Service
@Transactional
public class OntologyRelationshipService {

	List<DatabaseModelListener> listeners = new ArrayList<DatabaseModelListener>();

	protected void fireRelationshipAddedEvent(OntologyRelationship relationship) {
		for (DatabaseModelListener listener : listeners) {
			listener.databaseRelationshipAdded(relationship);
		}
	}

	protected void fireRelationshipUpdatdeEvent(
			OntologyRelationship relationship) {
		for (DatabaseModelListener listener : listeners) {
			listener.databaseRelationshipUpdated(relationship);
		}
	}

	protected void fireRelationshipRemovedEvent(
			OntologyRelationship relationship) {
		for (DatabaseModelListener listener : listeners) {
			listener.databaseRelationshipRemoved(relationship);
		}
	}

	@Autowired
	OntologyClassService ontologyClassService;
	@Autowired
	OntologyAxiomService ontologyAxiomService;

	OntologyRelationshipDAO dao;

	@Autowired
	public void setDao(OntologyRelationshipDAO dao) {
		this.dao = dao;
	}

	// Tested
	public OntologyClass findSourceOntologyClass(
			OntologyRelationship ontologyRelationship) {

		return this.ontologyClassService.findById(ontologyRelationship
				.getSrcClassId());
	}

	// Tested
	public OntologyClass findDestinationOntologyClass(
			OntologyRelationship ontologyRelationship) {
		return this.ontologyClassService.findById(ontologyRelationship
				.getDstClassId());
	}

	// Tested
	public void deleteAll() {
		List<OntologyRelationship> relationshipList = dao.findAll();
		for (OntologyRelationship relationship : relationshipList) {
			dao.remove(relationship);
		}
	}

	// Tested
	public List<OntologyClass> findChildren(OntologyClass srcOntologyClass) {
		List<OntologyClass> childrenOntologyClass = new ArrayList<OntologyClass>();

		List<OntologyRelationship> subclassRelationships = this.dao
				.search(new Search().addFilterEqual("name", "has subclass")
						.addFilterEqual("srcClassId", srcOntologyClass.getId()));

		for (OntologyRelationship rel : subclassRelationships) {
			OntologyClass src = this.findSourceOntologyClass(rel);
			if (src.getId() == srcOntologyClass.getId()) {
				childrenOntologyClass.add(this
						.findDestinationOntologyClass(rel));
			}
		}

		return childrenOntologyClass;
	}

	// Tested
	public List<OntologyClass> findRelDestNeighbourClasses(
			OntologyClass ontologyClass) {

		List<OntologyClass> neighbourOntologyClass = new ArrayList<OntologyClass>();

		List<OntologyRelationship> subclassRelationships = this.dao
				.search(new Search().addFilterEqual("srcClassId",
						ontologyClass.getId()).addFilterNotEqual("name",
						"has subclass"));

		for (OntologyRelationship rel : subclassRelationships) {
			OntologyClass src = this.findSourceOntologyClass(rel);
			if (src.getId() == ontologyClass.getId()) {
				neighbourOntologyClass.add(this
						.findDestinationOntologyClass(rel));
			}
		}

		return neighbourOntologyClass;
	}

	// Tested
	public List<OntologyClass> findRelSrcNeighbourClasses(
			OntologyClass ontologyClass) {

		List<OntologyClass> neighbourOntologyClass = new ArrayList<OntologyClass>();

		List<OntologyRelationship> subclassRelationships = this.dao
				.search(new Search().addFilterEqual("dstClassId",
						ontologyClass.getId()).addFilterNotEqual("name",
						"has subclass"));

		for (OntologyRelationship rel : subclassRelationships) {
			OntologyClass dst = this.findDestinationOntologyClass(rel);
			if (dst.getId() == ontologyClass.getId()) {
				neighbourOntologyClass.add(this.findSourceOntologyClass(rel));
			}
		}

		return neighbourOntologyClass;
	}

	// Tested
	public List<OntologyClass> findDesendants(OntologyClass ontologyClass) {
		List<OntologyClass> desendants = new ArrayList<OntologyClass>();
		this.findDesendantsRecursively(desendants, ontologyClass);
		return desendants;
	}

	private void findDesendantsRecursively(List<OntologyClass> desendants,
			OntologyClass rootClass) {
		List<OntologyClass> children = this.findChildren(rootClass);
		if (children == null || children.size() == 0) {
			return;
		} else {
			desendants.addAll(children);
			for (OntologyClass subRoot : children) {
				findDesendantsRecursively(desendants, subRoot);
			}
		}
	}

	// Tested
	private void generateLevelInfo(List<OntologyClass> desendants,
			OntologyClass rootClass) {
		List<OntologyClass> children = this.findChildren(rootClass);
		if (children == null || children.size() == 0) {
			return;
		} else {
			desendants.addAll(children);
			for (OntologyClass subRoot : children) {
				subRoot.setLevel(rootClass.getLevel() + 1);
				generateLevelInfo(desendants, subRoot);
			}
		}
	}

	// Tested
	public void generateBranchRootInfo(OntologyClass rootClass) {

		rootClass.setBranchId(rootClass.getId());
		this.ontologyClassService.save(rootClass);
		List<OntologyClass> children = this.findChildren(rootClass);

		for (OntologyClass child : children) {
			if (child.getId() == null) {
				throw new NullPointerException();
			}
			child.setBranchId(child.getId());
			List<OntologyClass> desendants = this.findDesendants(child);
			for (OntologyClass desendant : desendants) {
				desendant.setBranchId(child.getId());
			}
		}

		return;

	}

	// Tested
	public void generateLevelInfo(OntologyClass rootClass) {
		rootClass.setLevel(1);
		this.ontologyClassService.save(rootClass);
		List<OntologyClass> desendants = new ArrayList<OntologyClass>();
		this.generateLevelInfo(desendants, rootClass);
	}

	public OntologyClass findRoot() {
		List<OntologyClass> rootClassList = this.ontologyClassService
				.getRootList();

		if (rootClassList == null || rootClassList.size() == 0) {
			return null;
		}
		return rootClassList.get(0);
	}

	public void delete(OntologyRelationship relationship) {
		dao.remove(relationship);
		fireRelationshipRemovedEvent(relationship);
	}

	public void save(OntologyRelationship ontologyRelationship) {
		boolean isCreate = dao.save(ontologyRelationship);
		if (isCreate) {
			fireRelationshipAddedEvent(ontologyRelationship);
		} else {
			fireRelationshipUpdatdeEvent(ontologyRelationship);
		}
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
