package GetRequest;

import Interfaces.iRequest;
import PostRequest.PostRequest;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

@CommandLine.Command(name = "GET")
public class GetRequest implements Runnable, iRequest {
    //Network objects
    private Socket socket;
    private PrintWriter printWriter;

    //Command Line related objects
    @CommandLine.Parameters
    private String url;

    @CommandLine.Option(names = {"-h", "--header"}, description = "headers to be added to the request")
    String[] headers;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "verbose mode will include the response header")
    boolean verbose;


    public GetRequest(){
    }


    public void sendRequest() {

        try{
            InetAddress inetAddress = InetAddress.getByName(url);
            socket = new Socket(inetAddress, 80);
            printWriter = new PrintWriter(socket.getOutputStream());

            writeRequest(printWriter, url, "");
            getRequestResponse(socket, socket.getInputStream());

        }catch(Exception e){
            System.out.println("Error in creating network connection");
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

    public void getRequestResponse(Socket socket, InputStream stream) {
        Scanner in = new Scanner(stream);
        while(in.hasNextLine()){
            System.out.println(in.nextLine());
        }
    }

    public void writeRequest(PrintWriter printWriter, String url, String ending) {
        printWriter.print("GET / HTTP/1.1\r\n");
        printWriter.print("Host: "+url+"\r\n\r\n");
        if(headers != null){
            for(String header : headers){
                printWriter.print(header);
            }
        }
        printWriter.print("");
        printWriter.flush();
    }

    @Override
    public void run() {
        System.out.println("Sending GET request ...\r\n\r\n");
        sendRequest();
    }
}
