import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class Get {
	boolean printDebuggin;
	String bodyContentFile;
	Get(boolean debug){
		printDebuggin = debug;
		bodyContentFile = "";
	}
	
	public int doAction(File wanted, boolean found, File rootFile) {
		int statusCode = -1;
		if(wanted != null) {
			if(found) {
				if(wanted.isDirectory()) {
					listFiles(wanted);
					statusCode = 0;
				}else {
					if(wanted.canRead()) {
						if(printDebuggin) {
							System.out.println("It is possible to read from this file");
						}
						statusCode = 0;
						System.out.println("Print contents");
						bodyContentFile = getFileContents(wanted.getPath());
				//		System.out.println(body);
					}else {
						statusCode = 4;
						if(printDebuggin) {
							System.out.println("You can't read from this file");
						}
					}
				}
			}else {
					if(printDebuggin) {
						System.out.println("We don't have such file");
					}
					statusCode = 1;
				}
		}else {
			listFiles(rootFile);
			statusCode = 0;
		}
		return statusCode; 
	}
	
	public void listFiles(File folder) {
		boolean empty = true, firstTime = true;
		for(File fiTemp : folder.listFiles()) {
			if(firstTime) {
				empty = false;
				firstTime = false;
				System.out.println("List of current files inside the direcerty you specify");
			}
			if(fiTemp.isFile()) {
				System.out.println(fiTemp.getName());
			}				
		}
		if(empty) {
			System.out.println("There is no any file here");
		}
	}
	
	public String getFileContents(String directory) {
		String body = new String("");
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream(directory));
			while(input.hasNext()) {
				body += input.nextLine() + "\r\n";
			}
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		input.close();
		return body;
	}
	
	
	
}
