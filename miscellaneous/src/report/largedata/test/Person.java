package report.largedata.test;

/**
 * @author vishal.zanzrukia
 */
public class Person {

	public Person() {

	}

	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}

	private int id;
	private String name;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Id:" + this.id;
	}
}
