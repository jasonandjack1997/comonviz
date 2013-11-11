package database.dao;

import org.springframework.stereotype.Repository;

import database.model.ontology.OntologyRelationship;

/**
 * <p>
 * This is the implementation of the Citizen DAO. You can see that we don't
 * actually have to implement anything, it is all inherited from GenericDAOImpl
 * through BaseDAO. We just specify the entity type (Citizen) and its identifier
 * type (Long).
 * 
 * <p>
 * The @Repository allows Spring to recognize this as a managed component (so we
 * don't need to specify it in XML) and also tells spring to do DAO exception
 * translation to the Spring exception hierarchy.
 * 
 * @author dwolverton
 * 
 */
@Repository
public class OntologyRelationshipDAOImpl extends BaseDAO<OntologyRelationship, Long> implements OntologyRelationshipDAO {

}
