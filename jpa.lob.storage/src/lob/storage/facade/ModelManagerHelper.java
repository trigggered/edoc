/**
 * 
 */
package lob.storage.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * @author azhuk
 * Creation date: May 31, 2013
 *
 * @param <T>
 */
@Stateless
public class ModelManagerHelper {	

	@PersistenceContext(unitName = "jpa.lob.storage" , type=PersistenceContextType.TRANSACTION )
	private EntityManager _entityManager;
	
	
	/* (non-Javadoc)
	 * @see mdb.core.db.entity.AbstractEntityFacade#getEntityManager()
	 */
	protected EntityManager getEntityManager() {		
		return _entityManager;
	}		
	

}