package entities.user;

import java.io.Serializable;

public class Librarian implements Serializable{
	private String id;
	private String name;

	/**
     * Default constructor
	 * @param id - the id of the librarian
	 * @param name - the name of the librarian
	 */
	public Librarian(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
     * get the id of the librarian
	 * @return - the id of the librarian
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * get the name of the librarian
	 * @return - the name of the librarian
	 */
	public String getName()
	{
		return name;
	}
}
