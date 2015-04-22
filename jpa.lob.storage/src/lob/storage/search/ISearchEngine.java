/**
 * 
 */
package lob.storage.search;

import java.util.List;

/**
 * @author azhuk
 * Creation date: Sep 5, 2013
 *
 */
public interface ISearchEngine {
	
	public List<Double> search(ISearchCriteria searchCriteria ); 

}
