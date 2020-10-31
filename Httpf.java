import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Httpf {
	Request requestServer;
	Response response;
	Help help;
	String directry;
	int portNumberServer;
	boolean debug;
//	boolean [] status; // 0: OK, 1: not found, 2 : created, 3 : bad request, 4: forbidden
	
	Httpf(){
		response = new Response();
		requestServer = new Request(response);
//		status = new boolean[5];
		help = new Help();
		portNumberServer = 8080;
		directry = "C:\\Users\\aalja\\OneDrive\\Desktop\\COMPP445\\A2_comp445\\LocalHost";		
	}
	
	public static String getFileContents(String directory) {
		String body = new String("");
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream(directory));
			while(input.hasNext()) {
				
				body += input.nextLine() + "\n";
			}
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		input.close();
		return body;
	}
	
	public boolean isNumber(String input) {
		boolean isNum = true;
		for(int i=0; i <input.length(); i++) {
			if(!Character.isDigit(input.charAt(i))){
				isNum = false;
				break;
			}
		}
		return isNum;
	}
	
	public boolean validatePortNumber(long num) {
		boolean valid = false;
		if( (num > 1023) && num <= 65535 ) {
			valid = true;
		}
		return valid;
	}
	
	public void error() {
		System.out.println("Sory. It is in vaild input");
	}
	
	public boolean validation(String input) {
		boolean valid = true;
		StringTokenizer st = new StringTokenizer(input);
		String token= "";
		if (st.hasMoreTokens()) {
			token = st.nextToken();
			if ( token.equalsIgnoreCase("httpf"))  {
				while(st.hasMoreTokens()) {
					token = st.nextToken();
					char firstChar = token.charAt(0);
					char secondChar = ' ';
					if(token.length() > 1) {
						secondChar = token.charAt(1);
					}
					if(firstChar == '-' ) {
						switch(secondChar) {
							case 'p':
								if((st.hasMoreTokens())) {
									token = st.nextToken();
									if(isNumber(token)) {
										portNumberServer = Integer.parseInt(token);
									}else {
										System.out.println("You should enter integer");
										valid = false;
									}
								}else {
									valid = false;
								}
								break;
							case 'v':
								debug = true;
								requestServer.printDebugging =true;
								break;
							case 'd':
								if((st.hasMoreTokens())) {
									token = st.nextToken();
									directry = token;
								}else {
									valid = false;
								}
							default:
								valid = false;
						}
					}else {
						valid = false;
					}
				}
			}else {
				valid = false;
				}
		}else {
			valid = false;
		}
		if(!valid) {
			error();
		}
		return valid;
	}
	
	
	public static void main(String[] args) {
		Httpf httpf = new Httpf();
		Post postServer = null;
		Post getServer = null; 
		PrintWriter outStream = null;
		Scanner key = new Scanner(System.in);
		String input = "";
		char needMore = 'y';
		ServerSocket server = null;
		Socket client = null;
		BufferedReader buffer = null;
		while(needMore == 'y') {
			try {
				input = key.nextLine();
				httpf.requestServer.setAbsolutePath(httpf.directry);
				if(!httpf.validation(input)) {
					continue;
				} else if(!httpf.validatePortNumber(httpf.portNumberServer)) {
					System.out.println("Sorry. You can't use this portNumber " + httpf.portNumberServer);
					continue;
				}
				System.out.println("here"
						+ "");
				System.out.println("portNumber " + httpf.portNumberServer);
				server = new ServerSocket(httpf.portNumberServer, 0, InetAddress.getLoopbackAddress());
				System.out.println("here2");
				client = server.accept();
				System.out.println("here");
				buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
				outStream = new PrintWriter(client.getOutputStream());
				boolean valid = httpf.requestServer.isValidRequest(buffer);
				if(valid) {
					System.out.println("Do action now");
					httpf.requestServer.doAction();
					System.out.println("Do action hs finished");
					System.out.println("\nthe response is \n" + httpf.response.getResponse());
					outStream.print(httpf.response.getResponse());
					server.close();
				}else {
					System.out.println("Sorry no action");
					httpf.response.setRequest(httpf.requestServer.version, 3);
					server.close();
				}
				System.out.println(" is valid " + valid);
				System.out.println("Do you need more : ['y'] for yes. Otherwise, it will stop the program");
				needMore = key.nextLine().charAt(0);
				if(needMore != 'y') {
					break;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
}
