/**
 * 
 */
package document.ui.server.mail;

import java.util.logging.Logger;

/**
 * @author azhuk
 * Creation date: Apr 29, 2015
 *
 */
public class Message {
	private static final Logger _logger = Logger.getLogger(Message.class
			.getName());
	
	 String 	_subject;	 
	 String 	_body;
	 String 	_from;
	 String[]  	_recipients;
	 
	 
	 
	 /**
	 * @return the _subgect
	 */
	public String getSubject() {
		return _subject;
	}


	/**
	 * @return the _body
	 */
	public String getBody() {
		return _body;
	}


	/**
	 * @return the _from
	 */
	public String getFrom() {
		return _from;
	}


	/**
	 * @return the _recipients
	 */
	public String[] getRecipients() {
		return _recipients;
	}


	/**
	 * @param _subject the _subgect to set
	 */
	public void setSubject(String value) {
		this._subject = value;
	}


	/**
	 * @param _body the _body to set
	 */
	public void setBody(String value) {
		this._body = value;
	}


	/**
	 * @param _from the _from to set
	 */
	public void setFrom(String value) {
		this._from = value;
	}


	/**
	 * @param _recipients the _recipients to set
	 */
	public void setRecipients(String[] value) {
		this._recipients = value;
	}


}
