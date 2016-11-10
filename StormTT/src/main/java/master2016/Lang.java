package master2016;

/**
* 
* Object to Store all the languages to be controlled
*
* @authors	Alvaro Feal;
* 			Gorka Revilla
* @version 	0.1
* @since   	07-11-2016 
*/
public class Lang {

	private String id;
	private String window;

	/**
	 * Create a Language with Id and Window
	 * 
	 * @param theId of the Language
	 * @param theWindow used to start and stop the windows
	 */
	public Lang (String theId, String theWindow) {
		this.setId(theId);
		this.setWindow(theWindow);
	}
	
	
	@Override
	public String toString() {
		return "["+this.getId()+","+this.getWindow()+"]";
		
	}
	
	
	/*
	 * GETTERS AND SETTERS
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}
	
}
