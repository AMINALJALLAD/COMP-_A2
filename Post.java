import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Post {
	boolean printDebuggin;
	Post(boolean debug){
		printDebuggin = debug;
	}
	
	
	public int createDiretery(File folder, int index, ArrayList<String> paths, String bodyContent) {
		int temp = index, statusCode = 0;
		String newFolders = "", path = "";
		boolean pathNewFolder = false;
		path = folder.getPath();
			for(int i = index; i < paths.size() -1 ; i++) { // new Folders missing path
				if(!pathNewFolder) {
					pathNewFolder = true;
				}
				newFolders +=  "\\" + paths.get(i);
			}
		if(pathNewFolder) {
			path +=   newFolders; // the path for the new folder / s
			if(printDebuggin) {
				System.out.println("path for folder/s is/are " + path);
			}
		}
		File tempFile = null;
		try {							//System.out.println("temp is " + temp);System.out.println("Size is " + paths.size());
			if(pathNewFolder) { // that means missing one or more folder/s
				if(printDebuggin) {
					System.out.println("We create new folders " );
				}
				tempFile = new File(path);
				tempFile.mkdirs(); 	
			}
			System.out.println(paths.size()-1);
			System.out.println(paths);
			StringTokenizer st  = new StringTokenizer(paths.get(paths.size()-1));
			st.nextToken(".");  // make sure that it is a valid file name
			if(st.hasMoreTokens()) { // a valid file name e.g. file.txt
				path += "//" + paths.get(paths.size()-1); // the path for the file
				if(printDebuggin) {
					System.out.println("path for the new file is " + path);
				}
				tempFile = new File(path);
				if(tempFile.createNewFile()) {
					postFileContents(tempFile, bodyContent);
					if(printDebuggin) {
						System.out.println("We post the contents ");
					}
				}else {
					statusCode = 4;
					if(printDebuggin) {
						System.out.println("We can't make this file ");
					}
				}
			}else {
				statusCode = 3;
				if(printDebuggin) {
					System.out.println("Not valid file name");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return statusCode;
	}
	
	public void postFileContents(File file, String contents) {
		//String body = new String("");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(file, true));
			pw.println(contents);
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		pw.close();
	}
	
	public int doAction(File wanted, boolean found, File rootFile, String bodyContent, int indexOf, ArrayList<String> paths) {
		int statusCode = -1;
		if(wanted != null) {
			if(found) {
				if(wanted.isFile()) {
					if(wanted.canWrite()) {
						if(printDebuggin) {
							System.out.println("We can write in this file");
						}
						postFileContents(wanted, bodyContent);
						if(printDebuggin) {
							System.out.println("The contents are posted successfully");
						}
						statusCode = 0;
					}else {
						if(printDebuggin) {
							statusCode = 4;
							System.out.println("We can't write in this file");
						}
					}
				}else {
					statusCode = 3;
					if(printDebuggin) {
						System.out.println("bad request not file to post");
					}
				}
			}else {
				if(printDebuggin) {
					System.out.println("One of their father are missing");
					System.out.println("We have to create from the last folder");
				}
					statusCode = createDiretery(wanted, indexOf, paths, bodyContent);
				}
		}else {
			statusCode = createDiretery(rootFile, indexOf, paths, bodyContent);
		}
		return statusCode;
	}
	
	


}
