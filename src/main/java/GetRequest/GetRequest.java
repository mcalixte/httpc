package GetRequest;

import Interfaces.iRequest;
import PostRequest.PostRequest;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@CommandLine.Command(name = "GET")
public class GetRequest implements Runnable, iRequest {
    //Network objects
    private Socket socket;
    private PrintWriter printWriter;
    private List<String> response;
    private HashMap<String, String> parsedData;

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
            parsedData = parseUrlInput(url);
            InetAddress inetAddress = InetAddress.getByName(parsedData.get("Host"));
            socket = new Socket(inetAddress, 80);
            printWriter = new PrintWriter(socket.getOutputStream());

            writeRequest(printWriter, parsedData.get("Host"), parsedData.get("Path"));
            getRequestResponse(socket, socket.getInputStream());

        }catch(Exception e){
            System.out.println("Error in creating network connection");
            e.printStackTrace();
        }
        finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Remove stack traces
                    e.printStackTrace();
                   System.out.println("Error in making GET Request");
                }
                printWriter.flush();
        }
    }

    private HashMap<String, String> parseUrlInput(String url) throws MalformedURLException {
        HashMap<String, String> parsedMap = new HashMap<>();
        URL aURL = new URL(url);
        parsedMap.put("Host", aURL.getHost());
        parsedMap.put("Path", aURL.getPath());

        System.out.println("Fetching from Host: "+parsedMap.get("Host")+" ...");
        System.out.println("specified path: "+parsedMap.get("Path")+" ...");

        return parsedMap;
    }

    public void getRequestResponse(Socket socket, InputStream stream) {
        Scanner in = new Scanner(stream);
        if(verbose){
            while(in.hasNextLine()){
                System.out.println(in.nextLine());
            }
        }
        else{
            response = new ArrayList<>();
            while(in.hasNextLine()){
                response.add(in.nextLine());
            }
            parseVerboseResponse(response);
        }

    }

    private void parseVerboseResponse(List<String> response) {
        List<String> parsedResponse = response.subList(response.indexOf("")+1, response.size());
        for(String string : parsedResponse){
            System.out.println(string);
        }
    }

    public void writeRequest(PrintWriter printWriter, String host, String path) {
        printWriter.print("GET "+path+" HTTP/1.1\r\n");
        printWriter.print("Host: "+host+"\r\n\r\n");
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
