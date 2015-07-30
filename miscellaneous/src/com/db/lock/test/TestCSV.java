package com.db.lock.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.text.StrSubstitutor;


/**
 * @author Vishal.Zanzrukia
 *
 */
class CSVReportAdapter{
	
	enum CSV_REPORT {
		CUSTOMER_EMAIL_SUBSCRIPTION_REPORT("r80_Customer-and-prospect-export_10_Email-for-default-subscription-list_CSV");
		private String value;

		private CSV_REPORT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public boolean isCSVReport(String name){
			for(CSV_REPORT value : CSV_REPORT.values()){
				if(value.equals(name)){
					return true;
				}
			}
			return false;
		}
	}
	
	
	
	public CSVReportAdapter(CSV_REPORT report){
		
	}
	
	public File getFilledFile(CSV_REPORT report){
		
		switch (report) {
		case CUSTOMER_EMAIL_SUBSCRIPTION_REPORT:
			
			
			break;
		default:
			break;
		}
		return null;
	}
}

class NamedParamPreparedStatement {
	private final PreparedStatement prepStmt;
	private List<String> fields = new ArrayList<String>();

	public NamedParamPreparedStatement(Connection conn, String sql) throws SQLException {
		int pos;
		while ((pos = sql.indexOf(":")) != -1) {
			int end = sql.substring(pos).indexOf(" ");
			if (end == -1)
				end = sql.length();
			else
				end += pos;
			fields.add(sql.substring(pos + 1, end));
			sql = sql.substring(0, pos) + "?" + sql.substring(end);
		}
		prepStmt = conn.prepareStatement(sql);
	}

	public PreparedStatement getPreparedStatement() {
		return prepStmt;
	}

	public ResultSet executeQuery() throws SQLException {
		return prepStmt.executeQuery();
	}

	public void close() throws SQLException {
		prepStmt.close();
	}

	public void setInt(String name, int value) throws SQLException {
		prepStmt.setInt(getIndex(name), value);
	}
	
	public void setLong(String name, long value) throws SQLException {
		prepStmt.setLong(getIndex(name), value);
	}
	
	public void setDouble(String name, double value) throws SQLException {
		prepStmt.setDouble(getIndex(name), value);
	}
	
	public void setFloat(String name, float value) throws SQLException {
		prepStmt.setFloat(getIndex(name), value);
	}
	
	public void setString(String name, String value) throws SQLException {
		prepStmt.setString(getIndex(name), value);
	}

	private int getIndex(String name) {
		return fields.indexOf(name) + 1;
	}
}

public class TestCSV {

	private static void writeCSV(FileWriter fileWriter, ResultSet resultSet, List<String> columnNames, boolean isRequireToWriteColumns) throws IOException, SQLException {
		if (isRequireToWriteColumns) {
			for (String columnName : columnNames) {
				fileWriter.append("\"");
				fileWriter.append(columnName);
				fileWriter.append("\"");
				fileWriter.append(",");
			}
			fileWriter.append("\n");
		}

		while (resultSet.next()) {
			for (int i = 1; i <= columnNames.size(); i++) {
				fileWriter.append("\"");
				if (resultSet.getObject(i) != null) {
					fileWriter.append(resultSet.getObject(i).toString());
				}
				fileWriter.append("\"");
				fileWriter.append(",");
			}
			fileWriter.append("\n");
		}
	}

	public static void main(String[] args) {
//		getCustomerEmailSubscriptionData();
		System.out.println("vishal".substring("vishal".length()-1));
	}

	public static void getCustomerEmailSubscriptionData() {
		// List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		try {
			ArrayList<String> columns = new ArrayList<String>();
			// Session session = DBHelper.getHibernateSession();
			// Session session =

			Connection connection = MyConnectionPool.getInstance().getConnection();
			int limit = 100_000;
			// int inc_limit = limit;
			int offset = 0;

			Map<String, Object> valuesMap = new HashMap<String, Object>();

			StrSubstitutor sub = null;

			// String queryTemplate =
			// "SELECT comp.name AS companyName, COALESCE(fre_rec.frequency,0) as frequency, fre_rec.recency as recency, monetary.monetaryValue as monetaryValue, monetary.value AS Value, (RPAD(c.email, IF( (INSTR(c.email, '_SPQWsa1l23szA_' ) )>0,INSTR(c.email, '_SPQWsa1l23szA_')-1, CHARACTER_LENGTH(c.email) ) ,' ') ) as email, c.id as customerId, c.title as customerTitle, c.first_name as customerFirstName, c.last_name as customerLastName, ml.name as defaultList, mcp.opt_email as emailOptIn, mcp.opt_telephone_mobile as mobileOptIn, mcp.opt_address_contact as addressContactOptIn, mcp.opt_address_delivery as addressDeliveryOptIn, mcp.opt_telephone_home as homePhoneOptIn, mcp.opt_telephone_work as workPhoneOptIn, order_add.address1 as Address1, order_add.address2 as Address2, order_add.address3 as Address3, order_add.towncity as TownCity, order_add.county as county, order_add.postcode as postCode, order_add.home_telephone as homeTelephone, order_add.country as country, order_add.work_telephone as workTelephone, order_add.mobile_telephone as mobileTelephone FROM customer c JOIN marketing_customer_preference mcp on mcp.customer_id = c.id JOIN marketing_list ml on mcp.campaign_id = ml.id AND ml.id = (SELECT SUBSTRING_INDEX((SELECT SUBSTRING_INDEX((SELECT system_data.value FROM system_data WHERE system_data.name = 'DEFAULT_MARKETING_LIST'), '<', 2)), '>', -1)) JOIN ( SELECT co.name FROM company co ) comp left join ( select COUNT(DISTINCT orders.id) as frequency, MAX(DATE(orders.order_date)) as recency, orders.customer_id as customer_id FROM orders, channel, order_item oi where oi.order_id = orders.id AND not (oi.line_status = 'CANCELLED' OR oi.line_status = 'UNABLE_TO_FULFIL') AND orders.channel_id = channel.id AND orders.currency = 'GBP' AND channel.name LIKE('PWS') GROUP BY orders.customer_id ) fre_rec on fre_rec.customer_id = c.id left join ( select (sum(ot.quantity*ot.unit_gross_price) - COALESCE(sum(pai.value),0)) as monetaryValue, sum(ot.quantity*ot.unit_gross_price) as value, o.customer_id as customer_id from orders o join channel c on o.channel_id = c.id join order_item ot on ot.order_id = o.id left join promotion_applied_item pai on pai.order_item_id = ot.id WHERE ot.line_status <> 'CANCELLED' AND ot.line_status <> 'UNABLE_TO_FULFIL' AND o.currency = 'GBP' AND c.name LIKE('PWS') GROUP BY o.customer_id ) monetary on monetary.customer_id = c.id left join ( SELECT distinct oa.address1 as address1, oa.address2 as address2, oa.address3 as address3, oa.towncity as towncity, oa.county as county, oa.postcode as postcode, oa.country as country, oa.home_telephone as home_telephone, oa.work_telephone as work_telephone, oa.mobile_telephone as mobile_telephone, o.customer_id as customer_id FROM order_address oa JOIN order_tender ot on oa.id = ot.order_address JOIN orders o on o.id = ot.order_id JOIN channel c on o.channel_id = c.id inner join (select MAX(oa.id) id FROM order_address oa JOIN order_tender ot on oa.id = ot.order_address JOIN orders o on o.id = ot.order_id JOIN channel c on o.channel_id = c.id WHERE o.currency = 'GBP' AND c.name LIKE ('PWS') GROUP BY o.customer_id) temp3 on temp3.id = oa.id ) order_add on order_add.customer_id = c.id ORDER BY monetaryValue desc limit ? offset ?";
			String queryTemplate = "SELECT comp.name AS companyName, COALESCE(fre_rec.frequency,0) as frequency, fre_rec.recency as recency, monetary.monetaryValue as monetaryValue, monetary.value AS Value, (RPAD(c.email, IF( (INSTR(c.email, '_SPQWsa1l23szA_' ) )>0,INSTR(c.email, '_SPQWsa1l23szA_')-1, CHARACTER_LENGTH(c.email) ) ,' ') ) as email, c.id as customerId, c.title as customerTitle, c.first_name as customerFirstName, c.last_name as customerLastName, ml.name as defaultList, mcp.opt_email as emailOptIn, mcp.opt_telephone_mobile as mobileOptIn, mcp.opt_address_contact as addressContactOptIn, mcp.opt_address_delivery as addressDeliveryOptIn, mcp.opt_telephone_home as homePhoneOptIn, mcp.opt_telephone_work as workPhoneOptIn, order_add.address1 as Address1, order_add.address2 as Address2, order_add.address3 as Address3, order_add.towncity as TownCity, order_add.county as county, order_add.postcode as postCode, order_add.home_telephone as homeTelephone, order_add.country as country, order_add.work_telephone as workTelephone, order_add.mobile_telephone as mobileTelephone FROM customer c JOIN marketing_customer_preference mcp on mcp.customer_id = c.id JOIN marketing_list ml on mcp.campaign_id = ml.id AND ml.id = (SELECT SUBSTRING_INDEX((SELECT SUBSTRING_INDEX((SELECT system_data.value FROM system_data WHERE system_data.name = 'DEFAULT_MARKETING_LIST'), '<', 2)), '>', -1)) JOIN ( SELECT co.name FROM company co ) comp left join ( select COUNT(DISTINCT orders.id) as frequency, MAX(DATE(orders.order_date)) as recency, orders.customer_id as customer_id FROM orders, channel, order_item oi where oi.order_id = orders.id AND not (oi.line_status = 'CANCELLED' OR oi.line_status = 'UNABLE_TO_FULFIL') AND orders.channel_id = channel.id AND orders.currency = 'GBP' AND channel.name LIKE('PWS') GROUP BY orders.customer_id ) fre_rec on fre_rec.customer_id = c.id left join ( select (sum(ot.quantity*ot.unit_gross_price) - COALESCE(sum(pai.value),0)) as monetaryValue, sum(ot.quantity*ot.unit_gross_price) as value, o.customer_id as customer_id from orders o join channel c on o.channel_id = c.id join order_item ot on ot.order_id = o.id left join promotion_applied_item pai on pai.order_item_id = ot.id WHERE ot.line_status <> 'CANCELLED' AND ot.line_status <> 'UNABLE_TO_FULFIL' AND o.currency = 'GBP' AND c.name LIKE('PWS') GROUP BY o.customer_id ) monetary on monetary.customer_id = c.id left join ( SELECT distinct oa.address1 as address1, oa.address2 as address2, oa.address3 as address3, oa.towncity as towncity, oa.county as county, oa.postcode as postcode, oa.country as country, oa.home_telephone as home_telephone, oa.work_telephone as work_telephone, oa.mobile_telephone as mobile_telephone, o.customer_id as customer_id FROM order_address oa JOIN order_tender ot on oa.id = ot.order_address JOIN orders o on o.id = ot.order_id JOIN channel c on o.channel_id = c.id inner join (select MAX(oa.id) id FROM order_address oa JOIN order_tender ot on oa.id = ot.order_address JOIN orders o on o.id = ot.order_id JOIN channel c on o.channel_id = c.id WHERE o.currency = 'GBP' AND c.name LIKE ('PWS') GROUP BY o.customer_id) temp3 on temp3.id = oa.id ) order_add on order_add.customer_id = c.id ORDER BY monetaryValue desc limit :limit offset :offset";

			// CSVWriter writer = new CSVWriter(new FileWriter("D:\\me\\projects\\fresca\\Tasks\\DUNNES-1966\\db\\testFile.csv"), ',', CSVWriter.DEFAULT_QUOTE_CHARACTER);
			String finalQuery = null;

			// PreparedStatement statement = null;
			ResultSet resultSet = null;

			String filePath = "D:\\me\\projects\\fresca\\Tasks\\DUNNES-1966\\db\\testFile.csv";

			ResultSetMetaData metadata = null;
			boolean isColumnsEvaluated = false;
			boolean isRequireToWriteColumns;
			FileWriter fileWriter = null;

			while (true) {
				isRequireToWriteColumns = false;

				// statement = connection.prepareStatement(queryTemplate);

				NamedParamPreparedStatement statement = new NamedParamPreparedStatement(connection, queryTemplate);
				statement.setInt("limit", limit);
				statement.setInt("offset", offset);

				// statement.setInt(1, limit);
				// statement.setInt(2, offset);

				resultSet = statement.executeQuery();

				offset += limit;

				if (!resultSet.isBeforeFirst()) {
					break;
				}

				if (!isColumnsEvaluated) {
					metadata = resultSet.getMetaData();
					int columnCount = metadata.getColumnCount();
					String columnName = null;
					for (int i = 1; i <= columnCount; i++) {
						columnName = metadata.getColumnName(i);
						columns.add(columnName);
					}
					isColumnsEvaluated = true;
					isRequireToWriteColumns = true;
				}

				fileWriter = new FileWriter(filePath, true);
				writeCSV(fileWriter, resultSet, columns, isRequireToWriteColumns);

				fileWriter.close();
				resultSet.close();
				statement.close();
				// writer = new CSVWriter(new FileWriter(filePath, true), ',', CSVWriter.DEFAULT_QUOTE_CHARACTER);
				// writer.writeAll(resultSet, true);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
