
public class Response {
	String statusLine;
	int statusCode;
	String headers;
	
	public Response() {
		statusLine = "";
		
	}
	
	public void setRequest(String version, int statCode) {
		String statusString = "";
		if(version != null) {
			statusLine = "version ";
		}
		statusString = getStatus(statCode);
		statusLine += statusString;
	}
	
	public void addHeaders(String headersFun) {
		headers = headersFun;
	}
	
	public String getResponse() {
		return statusLine + headers;
	}
	
	public String getStatus(int statCode) { 
		String status = "";
		switch(statCode) {
		case 0:
			status = "200 : OK";
			break;
		case 1:
			status = "404 : Not Found";
			break;
		case 2:
			status = "201 : Created";
			break;
		case 3:
			status = "400 : Bad Request";
			break;
		case 4:
			status = "403 : Forbidden";
			break;
		}
		status  += "\r\n";
		return status;
	}
}
