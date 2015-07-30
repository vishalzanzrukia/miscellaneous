package report.largedata.test;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;

/**
 * @author vishal.zanzrukia
 * 
 */
public class JasperDemoApp {

	private static final String TASK_PDF = "pdf";

	private static final String TASK_XML = "xml";

	private static final String TASK_CSV = "csv";

	private static final String VITUALIZER_FILE = "file";

	private static final String VITUALIZER_SWAP_FILE = "swapFile";

	private static final String VITUALIZER_GZIP = "gZip";

	public static void main(String[] args) {
		String sourceFolderPath = "D:\\me\\projects\\fresca\\Tasks\\DUNNES-1966";
		String inFileName = sourceFolderPath + "\\test.jasper";
		String outFileName = sourceFolderPath + "\\testReport";
		String tmpFileName = "D:\\me\\projects\\fresca\\Tasks\\DUNNES-1966\\tmp";
		String taskName = "csv";
		String virtualizerType = "swapFile";

		try {

			System.out.println("creating report with : " + virtualizerType + " type");

			/** creating the data source */
			JRDataSource ds = new MyDataSource();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, false);
			JRAbstractLRUVirtualizer virtualizer = null;

			/** creating the virtualizer */
			if (VITUALIZER_FILE.equals(virtualizerType)) {
				virtualizer = new JRFileVirtualizer(2, tmpFileName);
				parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			} else if (VITUALIZER_SWAP_FILE.equals(virtualizerType)) {
				JRSwapFile swapFile = new JRSwapFile(tmpFileName, 4096, 1024);
				virtualizer = new JRSwapFileVirtualizer(2, swapFile, true);
				parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			} else if (VITUALIZER_GZIP.equals(virtualizerType)) {
				virtualizer = new JRGzipVirtualizer(2);
				parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			}

			/** filling the report */
			JasperPrint jasperPrint = fillReport(inFileName, ds, parameters);
			if (virtualizer != null) {
				virtualizer.setReadOnly(true);
			}

			if (TASK_PDF.equals(taskName)) {
				exportPDF(outFileName, jasperPrint);
			} else if (TASK_XML.equals(taskName)) {
				exportXML(outFileName, jasperPrint, false);
			} else if (TASK_CSV.equals(taskName)) {
				exportCSV(outFileName, jasperPrint);
			} else {
				System.err.println("Please provide valid parameters..!");
				System.exit(0);
			}

		} catch (JRException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @param outFileName
	 * @param jasperPrint
	 * @throws JRException
	 */
	private static void exportCSV(String outFileName, JasperPrint jasperPrint) throws JRException {
		long start = System.currentTimeMillis();
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
		exporter.exportReport();
		System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
	}

	/**
	 * @param outFileName
	 * @param jasperPrint
	 * @param embedded
	 * @throws JRException
	 */
	private static void exportXML(String outFileName, JasperPrint jasperPrint, boolean embedded) throws JRException {
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile(jasperPrint, outFileName, embedded);
		System.err.println("XML creation time : " + (System.currentTimeMillis() - start));
	}

	/**
	 * @param outFileName
	 * @param jasperPrint
	 * @throws JRException
	 */
	private static void exportPDF(String outFileName, JasperPrint jasperPrint) throws JRException {
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile(jasperPrint, outFileName);
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}

	/**
	 * @param fileName
	 * @param dataSource
	 * @param parameters
	 * @return
	 * @throws JRException
	 */
	private static JasperPrint fillReport(String fileName, JRDataSource dataSource, Map parameters) throws JRException {
		long start = System.currentTimeMillis();
		JasperPrint jasperPrint = JasperFillManager.fillReport(fileName, parameters, dataSource);
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
		return jasperPrint;
	}
}
