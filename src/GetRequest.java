import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class GetRequest implements iGetRequest {

    //Network objects
    private Socket socket;
    private PrintWriter printWriter;
    private CLIParser parser;

    @Override
    public void sendRequest(String url, int port, CLIParser parser) {

        try{
            /**For the bonus questions with redirects, you should use the simple constructor
             * and dynamically bind the sockets to a url at runtime
            */
        	InetAddress ipAddress = InetAddress.getByName("www.httpbin.org");
            socket = new Socket(ipAddress, port);
            printWriter = new PrintWriter(socket.getOutputStream());

            writeGetRequestHeader(printWriter, ipAddress.getHostName());
            getGetRequestResponse(socket, new InputStreamReader(socket.getInputStream()), socket.getInputStream());
            
        }catch(Exception e){
            System.out.println("MKC1: Error in creating network connection");
            e.printStackTrace();
        }
        finally {
        	try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	printWriter.flush();
        }
    }

    private void getGetRequestResponse(Socket socket, InputStreamReader inputStreamReader, InputStream stream) {
    	Scanner in = new Scanner(stream);
      
        while(in.hasNextLine()){
            System.out.println(in.nextLine());
        }
    }

    private void writeGetRequestHeader(PrintWriter printWriter, String url) {

        printWriter.print("GET /get?course=networking&assignment=1%27 HTTP/1.1\r\n");
        printWriter.print("Host: "+url+"\r\n\r\n");
        printWriter.print("");
        printWriter.flush();
    }

}
