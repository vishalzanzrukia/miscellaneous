package report.largedata.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vishal.zanzrukia
 * 
 */
public class DataProvider {

	/**
	 * @param offset
	 * @param size
	 * @return
	 */
	public static List<Person> getPersons(int offset, int size) {
		List<Person> returnPersons = new ArrayList<Person>(size);
		for (int i = offset; i < ((offset + size) > getTotalNumOfRecords() ? getTotalNumOfRecords() : (offset + size)); i++) {
			returnPersons.add(new Person(i, "Person-" + i));
		}
		return returnPersons;
	}

	public static int getTotalNumOfRecords() {
		return 5000000;
	}
}
