

public class Help {
	
	public void getHelp() {
		System.out.println("httpfs is a simple file server.\r\n" + 
				"usage: httpfs [-v] [-p PORT] [-d PATH-TO-DIR]\r\n" + 
				"-v Prints debugging messages.\r\n" + 
				"-p Specifies the port number that the server will listen and serve at.\r\n" + 
				"Default is 8080.\r\n" + 
				"Comp445 – Lab Assignment # 2 Page 4\r\n" + 
				"-d Specifies the directory that the server will use to read/write\r\n" + 
				"requested files. Default is the current directory when launching the\r\n" + 
				"application.\r\n" );
	}
}
