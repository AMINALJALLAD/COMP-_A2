
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Request  {
	Response response;
	Get getRequest;
	Post postRequest;
	String requestType;
	String version;
	String urlPath;
	ArrayList<String> paths;
//	String header;
	ArrayList<String> headers;
	String absouteUrl;
	int indexOf;
	boolean found;
	boolean printDebugging;
	String bodyRequest; 
	int bodyChar;
//	String message;
//	String[] directories;
	
	
	public Request(Response res) {
		response =res;
		paths = new ArrayList<String>();
//		message = "";
		requestType = "";
		version = "";
		urlPath = "";
		absouteUrl = "";
		bodyRequest = "";
//		header="";
		headers = new ArrayList<String>();
	}
	
	
	public void clear(Response res) {  // after submission
		response =res;
		paths = new ArrayList<String>();
//		message = "";
		requestType = "";
		version = "";
		urlPath = "";
		absouteUrl = "";
		bodyRequest = "";
//		header="";
		headers = new ArrayList<String>();
	}
	
	public String getInteger(String s) {
		String strInt = "";
		for(int i=0 ; i< s.length(); i++) {
			if(Character.isDigit(s.charAt(i))) {
				strInt += s.charAt(i);
			}
		}
	//	System.out.println("from getInteger");
	//	System.out.println("strInt is " + strInt + " length " + strInt.length());
		return strInt;
	}
	
	public int toInteger(String s) {
		int num = 0;
		try {
			num = Integer.parseInt(s);
		}catch(Exception e) {
			System.out.println("It should be an integer value");
			e.printStackTrace();
		}
		return num;
	}
	
	public boolean isValidHeaders(String header) {
		boolean validHeaders = true;
//		if(requestType.equals("GET") && header.length()>11) {
//			String temp = header.substring(0, 12);
//			if( temp.equals("Content-Type") && (temp.equals("Content-Disp") ) ) {
//				
//		}else if (header.length()>2) {
//		//	System.out.println("header will b");
//			headers.add(header);
//		}
		if(header.length() > 2) {
			if(requestType.equals("POST")){
				headers.add(header);
			}else {
				int ind1 = header.indexOf("Content-Type");
				int ind2 = header.indexOf("Content-Disposition");
				if(  (ind1 < 0) && (ind2 <0) ) {
					System.out.println("header wil be added " + header);
					headers.add(header);
				}
			}
		}
		
		//System.out.println("From isValidHeaders " + "header is " + header);
		String key ="", value ="";
		StringTokenizer stHeaders = new StringTokenizer(header);
		if(stHeaders.hasMoreTokens()) {
		//	System.out.println("header length is " + header.length());
			key = stHeaders.nextToken(":");
			while(stHeaders.hasMoreTokens()) {
				value += stHeaders.nextToken();
			}
		}else {
			if(header.length() != 1) {
				validHeaders = false;
			}
		}
		if(printDebugging) {
		//	System.out.println("key is "+ key);
		//	System.out.println("value is "+ value);
		}
//		System.out.println("key length is "+ key.length());
//		System.out.println("value length is "+ value.length());
		if(requestType.equals("POST") && key.equalsIgnoreCase("Content-Length")) {
			value = getInteger(value);
		//	System.out.println("*************************************************");
			bodyChar = toInteger(value);
		}
	//	System.out.println("validHeaders is " + validHeaders);
		return validHeaders;
	}
	
//	public boolean finishInputStream(int num) {
//		boolean finish = false;
//		if(requestType.equals("GET")) {
//			if( (headers.length() >= 4)&& (headers.charAt(headers.length()-1) == '\n') && (headers.charAt(headers.length()-2) == '\r') && (headers.charAt(headers.length()-3) == '\n') && (headers.charAt(headers.length()-4) == '\r')) {
//				System.out.println("Yes it is from get");
//				finish = true;
//			}
//		}else {
//			if(num == 0) {
//				 finish = true;
//				 System.out.println("Yes it is from post");
//			}
//		}
//		System.out.println("from finishInputStream");
//		return finish;
//	}
	
	public  boolean isValidRequest(BufferedReader buffer) {
		boolean validLine = true, validHeaders = true, requestLineDone =false, finishedHeader = false, finishPost = false;
		String requestLine = "", header = ""; int charNum = 0;
		//System.out.println("from valid request ");
		try {
			charNum = buffer.read();
			while (charNum != -1) {
		//		System.out.println((char)charNum);
				if(!requestLineDone) {
					requestLine += (char)charNum;
					if((char)charNum == '\r') {
						charNum = buffer.read();
						if((char)charNum == '\n') {
							if(printDebugging){
								System.out.println("requestLine is " + requestLine);
							}
							validLine = isValidRequestLine(requestLine);
							requestLineDone = true;
						}else { //error
							validLine = false;
						}
					}
				}else {
					header += (char)charNum;
//					headers += (char)charNum;
//					System.out.println("header is " + header);
//					System.out.println("header length is " + header.length());
//					System.out.println("headers is " + headers);
//					System.out.println(headers.equals(""));
					
					if((char)charNum == '\r') {
						charNum = buffer.read();
						if((char)charNum == '\n') {
//							if(h)){
//								if(printDebugging){
//									System.out.println("header has been finished");
//								}
//								if((char)charNum == '\r') {		// end of headers
//									charNum = buffer.read();
//									if((char)charNum == '\n') {
//										
//									}else { //error
//										validHeaders = false;
//										break;
//									}
//								}else {
//									validHeaders = false;
//									break;
//								}
//								break;
//							}else {
								if(printDebugging){
									System.out.println("Debug: header is " + header);
								}
								boolean temp = isValidHeaders(header);
								if(header.length() <= 1
										) {
									break;
								}
								header = "";
								if(!temp) {
									validHeaders = false;
									break;
								}
//							}
						}else { //error
							validLine = false;
						}
					}
				}
				charNum = buffer.read();
			//	System.out.println("finish " + charNum);
//				System.out.println("validLine is " + validLine);
//				System.out.println("validHeaders is " + validHeaders);
//				System.out.println("requestLineDone is " + requestLineDone);
		//		finishedHeader = finishInputStream(10);
				
			}
			System.out.println("");
//			System.out.println("validLine is " + validLine);
//			System.out.println("validHeaders is " + validHeaders);
//			System.out.println("requestLineDone is " + requestLineDone);
//			System.out.println("headers before " + headers.length());
//			
//			headers = headers.substring(0, headers.length()-2);
//			System.out.println("**************************************************************");
//			System.out.println("headers after"
//					+ " " + headers.length());
			if(requestType.equals("POST")) { 
				if(printDebugging) {
					System.out.println("We need to scan the body from post request");
				}
				int readChar = bodyChar ;
				charNum = buffer.read();
//				System.out.println("readChar is " + readChar);
//				System.out.println("charNum is " + charNum);
				while( charNum != -1)  {
					bodyRequest +=  (char) charNum;
					charNum = buffer.read();
					//System.out.println("readChar is " + readChar);
					--readChar;
					if(readChar <= 1) {
						break;
					}
				}
				//System.out.println("bodyRequest is " +bodyRequest);
				finishPost = true;
				//System.out.println("done");
				if(printDebugging){
					System.out.println("The body content is " + bodyRequest);
				}
			}
			System.out.println("please close the buffer");
			buffer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}		
		return validLine && validHeaders;
	}
	
	
	public boolean isValidRequestLine(String requestLine) {
		boolean validRequestType = true, validPath = true; 
		StringTokenizer st = new StringTokenizer(requestLine);
		String nextToken = "";
		int iteration = 0;
		while(st.hasMoreTokens()) {
			nextToken = st.nextToken();
			switch(iteration) {
			case 0:
				requestType = nextToken;
				break;
			case 1:
				urlPath = nextToken;
				break;
			case 2:
				version = nextToken;
				break;
			}
			iteration++;
		}
		System.out.println("from requestline is" + printDebugging);
		if(printDebugging){
			System.out.println("requestType is " + requestType);
			System.out.println("urlPath is " + urlPath);
			System.out.println("version is " + version);
		}
		if(iteration > 2) {
			validRequestType = false;
		}
		validRequestType = isValidRequestType(requestType);
		validPath = isValidPath(urlPath);
		return validRequestType && validPath;
	}
	
	public boolean isValidRequestType(String requestType) {
		boolean validRequestType = true;
		if( (requestType == null) || (!  (requestType.equals("GET") || requestType.equals("POST") ) ) ) {
			validRequestType =  false;
		}
		if(!validRequestType) {
			System.out.println("The request type should be either GET or POST");
			System.out.println(requestType +" is not valid");
		}
		return validRequestType;
	}
	
	public boolean isValidPath(String urlPath) {
		boolean validPath = false;
		StringTokenizer st = new StringTokenizer(urlPath);
		String token = "";
		if(urlPath.charAt(0) == '/') {
			validPath = true;
		}
		//if(paths.size() > 1) {
			while(st.hasMoreTokens()) {
				if(!validPath) {  // first char is not '/'
					break;
				}
				
				if(urlPath.length() > 1) {
					token = st.nextToken("/");
				//	System.out.println("add to path");
					paths.add(token);
				}else {
				//	System.out.println("Don't tawfiq");
					token = st.nextToken();
				}
				
//				if(token.length() > 0) {
//					paths.add(token);
//				}
			}
			if(printDebugging) {
				System.out.println("The relative path that you specify");
				System.out.println(paths);
			}
		//}
		return validPath;
	}
	
	public void setAbsolutePath(String directry) {
		absouteUrl = directry;
	}
	
	public String getVersion() {
		return version;
	}
	
	public File findFileDirectory(File rootFile, ArrayList<String> target, int index) {
		File wanted = null;//System.out.println("New calling " + index);
		indexOf =index ; 
		if(index >0) { // to tell that there is at least one of the path found
			wanted = rootFile;
		}
		if(target.size() > 0) {
			for(File fileTemp:rootFile.listFiles()) {
		//		System.out.println("target.get(index) is " + target.get(index));System.out.println("fileTemp.getName() " + fileTemp.getName());
				if(fileTemp.getName().equals(target.get(index))) {
				//	System.out.println("Good we find");System.out.println(fileTemp.getName());
					if(fileTemp.isDirectory()) {
						//System.out.println("It is a directry, and index is " + index);System.out.println("It is a directry, and index is " + index);
						++index;
						if(target.size() > index) { // more directry
							wanted = findFileDirectory( fileTemp, target, index);
						}else {// no more path string
							found =true; 
							wanted = fileTemp;
							//System.out.println(" ---------------finally -----------");System.out.println("It is a directery");System.out.println(" ---------------finally -----------");
						}
						
					}else {
						wanted = fileTemp;
						found =true;
						//System.out.println(" ---------------finally -----------");System.out.println("It is a file, and index is " + index);System.out.println(" ---------------finally -----------");indexOf =index;
					}
					break;
				}else {
				//	System.out.println("************");
				}
				
			}
		}
		if(printDebugging) {
			System.out.println("Founf the file "+ wanted);
		}
		return wanted;
	}
	

	
	public void doAction() {
		File rootFile = null, wanted = null;
		String pointerDirectory = null;
		rootFile = new File(absouteUrl);
		wanted = findFileDirectory(rootFile,paths,0);
		int statusCode = -1;
		for(int i=0; i<headers.size();i++) {
			response.addHeaders(headers.get(i));
		}
		if(requestType.equals("GET")) {
			//getRequest = new Get(printDebugging, response);
			getRequest = new Get(printDebugging, response); //after submission
			statusCode = getRequest.doAction(wanted, found, rootFile, paths);
		}else {
			postRequest = new Post(printDebugging);
			statusCode = postRequest.doAction(wanted, found, rootFile, bodyRequest, indexOf, paths);
		}
		response.setRequest(version, statusCode);
	}
	

	
}


//
//public boolean isValidHeaders(StringTokenizer st) {
//	boolean validHeaders = true;
//	String header = "", key ="", value ="";
//	headers = st.nextToken("\r\n\r\n");
//	System.out.println("Headers are "+ headers);
//	headers += "\r\n";
//	StringTokenizer stHeaders = new StringTokenizer(headers), stKeyValue =null;
//	header = stHeaders.nextToken("\r\n");
//	while(stHeaders.hasMoreTokens() && (!header.equals("")) ) {
//		stKeyValue = new StringTokenizer(header);
//		key = stKeyValue.nextToken(":");
//		System.out.println("key is "+ key);
//		if(stKeyValue.hasMoreTokens()) {
//			value = stKeyValue.nextToken();
//			System.out.println("value is "+ value);
//		}else {
//			value = "";
//		}
//		if( (key == "") || (value == "") ) {
//			validHeaders = false;
//			break;
//		}
//	}
//	return validHeaders;
//}
//
//public boolean isValidRequest() {
//	boolean validLine = true, validHeaders = true;
//	String requestLine = "";
//	StringTokenizer st = new StringTokenizer(message);
//	requestLine = st.nextToken("\r\n");
//	validLine = isValidRequestLine(requestLine);
//	
//	return validLine && validHeaders;
//}
//
//public boolean isValidPath(String urlPath) {
//	boolean validPath = false;
//	StringTokenizer st = new StringTokenizer(urlPath);
//	String token = "";
//	if(urlPath.charAt(0) == '/') {
//		validPath = true;
//	}
//	while(st.hasMoreTokens()) {
//		if(!validPath) {
//			break;
//		}
//		token = st.nextToken("/");
//		if(token.length() > 0) {
//			paths.add(token);
//		}
//	}
////	String absouteUrl = "";
////	File file = new File(absouteUrl + urlPath);
////	if(file.exists()) {
////		if(file.isDirectory()) {
////			directories = file.list();
////		}else if(file.isFile()) {
////			
////		}
////	}else {
////		validPath = false;
////	}
//	return validPath;
//}
//
//public boolean isValidRequestLine(String requestLine) {
//	boolean validRequestType = true, validPath = true; 
//	StringTokenizer st = new StringTokenizer(message);
//	String nextToken = "";
//	int iteration = 0;
//	while(st.hasMoreTokens()) {
//		nextToken = st.nextToken();
//		switch(iteration) {
//		case 0:
//			requestType = nextToken;
//			break;
//		case 1:
//			urlPath = nextToken;
//			break;
//		case 2:
//			version = nextToken;
//			break;
//		}
//		iteration++;
//	}
//	if(iteration > 2) {
//		validRequestType = false;
//	}
//	validRequestType = isValidRequestType(requestType);
//	validPath = isValidPath(urlPath);
//	return validRequestType && validPath;
//}
//
//public boolean isValidRequestType(String requestType) {
//	boolean validRequestType = true;
//	if( (requestType == null) || (!  (requestType.equals("GET") || requestType.equals("POST") ) ) ) {
//		validRequestType =  false;
//	}
//	if(!validRequestType) {
//		System.out.println("The request type should be either GET or POST");
//		System.out.println(requestType +" is not valid");
//	}
//	return validRequestType;
//}
//
//
//public boolean isValidVersion(String requestType) {
//	return ( !  (requestType.equals("GET") || 
//			requestType.equals("POST") )  );
//}
//
////public String searchForFile(File pointer) {
////	String result = "";
////	if(!pointer.isDirectory()) {
////		
////	}
////	for(File folderItem: pointer.listFiles()) {
////		if(folderItem.isDirectory()) {
////			result = searchForFile(folderItem);
////			if(!result.equals(""))){
////				return result;
////			}else {
////				
////			}
////		}
////	}
////}
//
////public String isThereSuchFile(File pointer) {
////	fileFile(pointer);
////}


//curl localhost:8080/POST/hello.txt -d "data to be posted"
//curl localhost:8080/Test.txtget.txt
//curl -X POST localhost:8080/Comp339/New_folder/Lesson1/Private_file.txt -d 'to be posted'
//curl localhost:8080/tawfiq/amin/concordia/wite.txt -d "data to be posted"