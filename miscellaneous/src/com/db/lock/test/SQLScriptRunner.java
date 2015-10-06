package com.db.lock.test;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import miscellaneous.SplitFile;

public class SQLScriptRunner {
	
	private static final String JDBC_USERNAME = "root";
	private static final String JDBC_PASSWORD = "root";
	private static final String JDBC_URL = "jdbc:mysql://localhost/test";
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String INPUT_FOLDER_PATH = "D:\\vishal\\repos\\git\\miscellaneous\\miscellaneous\\test";
	private static final String OUTPUT_FILE_PATH = INPUT_FOLDER_PATH + "\\all.sql";

	/**
	 * extract int number from String
	 * 
	 * @param s
	 * @return
	 */
	private static int extractInt(String s) {
		String num = s.replaceAll("\\D", "");
		return num.isEmpty() ? 0 : Integer.parseInt(num);
	}

	private static List<String> getTestStrings() {
		return Arrays.asList("room1", "roo12", "room10", "room1001", "room101");
	}

	/**
	 * get the connection
	 * 
	 * @return
	 */
	private static Connection getConnection() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();
			String url = JDBC_URL;
			return DriverManager.getConnection(url, JDBC_USERNAME, JDBC_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * get sorted by number full file names 
	 * 
	 * @return
	 */
	private static File[] getSortedFileFullNames(){
		File inputFolder = new File(INPUT_FOLDER_PATH);
		File[] files = inputFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.toLowerCase().endsWith(".sql");
			}
		});

		return Stream.of(files)
		.sorted((file1, file2) -> (extractInt(file1.getName()) - extractInt(file2.getName())))
		.toArray(File[]::new);
	}
	
	
	private static void test() throws Exception{
		SplitFile.joinFiles(getSortedFileFullNames(), OUTPUT_FILE_PATH);
	}

	public static void main(String[] args) throws Exception {
		test();
		/*String query = "SELECT * FROM employee";
	    try
	    {
	      Statement st = getConnection().createStatement();
	      ResultSet rs = st.executeQuery(query);
	      while (rs.next())
	      {
	        String name = rs.getString("name");
	        float id = rs.getInt("id");
	        System.out.println(id + "   " + name);
	      }
	    }
	    catch (SQLException ex)
	    {
	      System.err.println(ex.getMessage());
	    }*/
	}

}
