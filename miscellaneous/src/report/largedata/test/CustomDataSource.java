package report.largedata.test;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CustomDataSource implements JRDataSource {

	/**
	 * Variable to store how much records were read
	 */
	private int globalCounter = 0;

	/**
	 * define the page size
	 */
	private static final int PAGE_SIZE = 20000;

	/**
	 * chunk offset
	 */
	private int chunkOffset = 0;

	/**
	 * current chunk data
	 */
	private List<Map<String, Object>> chunkData;

	/**
	 * it indicates whether we need to load chunk or not.
	 */
	private boolean needsToLoadChunk = true;

	/**
	 * total number of records
	 */
	private int totalNumOfRecords = -1;

	/**
	 * track the current chunk of data
	 */
	private int currentChunkCounter;

	public CustomDataSource(int totalNumOfRecords) {
		this.totalNumOfRecords = totalNumOfRecords;
	}

	@Override
	public boolean next() throws JRException {

		boolean isNext = false;

		if (globalCounter < totalNumOfRecords) {
			globalCounter++;
			isNext = true;
		}

		if (isNext && needsToLoadChunk) {

			/** setting flag for loading current chunk */
			this.needsToLoadChunk = false;

			/** setting current Chunk Counter to zero */
			this.currentChunkCounter = 0;

			/** loading current chunk data */
//			chunkData = DataProvider.getPersons(getChunkOffset(), PAGE_SIZE);

			/** setting correct offset */
			setChunkOffset(getChunkOffset() + PAGE_SIZE);

		} else {
			this.currentChunkCounter++;
		}

		if (isNext && this.currentChunkCounter == (PAGE_SIZE - 1)) {
			this.needsToLoadChunk = true;
		}

		return isNext;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		if (jrField.getName().equals("id")) {
			return chunkData.get(currentChunkCounter).getId();
		} else if (jrField.getName().equals("name")) {
			return chunkData.get(currentChunkCounter).getName();
		}
		return "";
	}

	/**
	 * Return an instance of the class that implements the custom data adapter.
	 */
	public static JRDataSource getDataSource() {
		return new CustomDataSource(1000);
	}

	/**
	 * @return the chunkOffset
	 */
	public int getChunkOffset() {
		return chunkOffset;
	}

	/**
	 * @param chunkOffset the chunkOffset to set
	 */
	public void setChunkOffset(int chunkOffset) {
		this.chunkOffset = chunkOffset;
	}

	/**
	 * @return the totalNumOfRecords
	 */
	public int getTotalNumOfRecords() {
		return totalNumOfRecords;
	}

	/**
	 * @param totalNumOfRecords the totalNumOfRecords to set
	 */
	public void setTotalNumOfRecords(int totalNumOfRecords) {
		this.totalNumOfRecords = totalNumOfRecords;
	}
}