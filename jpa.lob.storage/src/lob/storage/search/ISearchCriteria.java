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
public interface ISearchCriteria {

	public void addCondition(String value);
	public List<String> getCondition();
	
}
