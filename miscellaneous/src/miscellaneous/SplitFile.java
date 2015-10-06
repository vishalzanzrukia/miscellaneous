package miscellaneous;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author vishal.zanzrukia
 * 
 */
public class SplitFile {
	public static final String INPUT_FILE = "";
	public static final int NUMBER_OF_OUTPUT_FILES = 10;
	public static final String FILE_SUFFIX = ".txt";
	private static final String OUTPUT_FILE_NAME = INPUT_FILE + "_Splits\\fullJoin" + FILE_SUFFIX;
	private static final String GENETATED_SPITTED_FILE_NAME = INPUT_FILE + "_Splits\\split.%d" + FILE_SUFFIX;
	
	/**
	 * split file
	 * 
	 * @throws Exception
	 */
	static void splitFile() throws Exception{

		File inputFile = new File(INPUT_FILE + "_Splits");
		inputFile.mkdir();

		RandomAccessFile raf = new RandomAccessFile(INPUT_FILE, "r");

		long sourceSize = raf.length();
		long bytesPerSplit = sourceSize / NUMBER_OF_OUTPUT_FILES;
		long remainingBytes = sourceSize % NUMBER_OF_OUTPUT_FILES;

		int maxReadBufferSize = 8 * 1024; // 8KB
		for (int destIx = 1; destIx <= NUMBER_OF_OUTPUT_FILES; destIx++) {
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(String.format(GENETATED_SPITTED_FILE_NAME, destIx)));
			if (bytesPerSplit > maxReadBufferSize) {
				long numReads = bytesPerSplit / maxReadBufferSize;
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWrite(raf, bw, maxReadBufferSize);
				}
				if (numRemainingRead > 0) {
					readWrite(raf, bw, numRemainingRead);
				}
			} else {
				readWrite(raf, bw, bytesPerSplit);
			}
			bw.close();
		}
		if (remainingBytes > 0) {
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split." + NUMBER_OF_OUTPUT_FILES + 1));
			readWrite(raf, bw, remainingBytes);
			bw.close();
		}
		raf.close();
	}
	
	
	public static void joinFiles(File[] files) throws Exception {
		joinFiles(files, OUTPUT_FILE_NAME);
	}
	
	/**
	 * join file
	 * 
	 * @throws Exception
	 */
	public static void joinFiles(File[] files, String outputFileName) throws Exception {
		
	    int maxReadBufferSize = 8 * 1024;

	    BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(outputFileName));

	    RandomAccessFile raf = null;
	    for (File file : files) {
	        raf = new RandomAccessFile(file, "r");
	        long numReads = raf.length() / maxReadBufferSize;
	        long numRemainingRead = raf.length() % maxReadBufferSize;
	        for (int i = 0; i < numReads; i++) {
	            readWrite(raf, bw, maxReadBufferSize);
	        }
	        if (numRemainingRead > 0) {
	            readWrite(raf, bw, numRemainingRead);
	        }
	        raf.close();

	    }
	    bw.close();
	}
	
	public static void joinFiles() throws Exception {

	    File[] files = new File[NUMBER_OF_OUTPUT_FILES];
	    for (int i = 1; i <= NUMBER_OF_OUTPUT_FILES; i++) {
	        files[i - 1] = new File(String.format(GENETATED_SPITTED_FILE_NAME, i));
	    }

	    joinFiles(files);
	}
 
	public static void main(String[] args) throws Exception {
		splitFile();
//		joinFiles();
	}

	static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
		byte[] buf = new byte[(int) numBytes];
		int val = raf.read(buf);
		if (val != -1) {
			bw.write(buf);
		}
	}
}
