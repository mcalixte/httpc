package GetRequest;

import Interfaces.iRequest;
import PostRequest.PostRequest;
import picocli.CommandLine;

import java.io.FileWriter;
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
    
    @CommandLine.Option(names = {"-o", "--output"}, description = "output file to write the body of the response to")
    String output;


    public void sendRequest() {

        try{
            parsedData = parseUrlInput(url);
            InetAddress inetAddress = InetAddress.getByName(parsedData.get("Host"));
            socket = new Socket(inetAddress, 80);
            printWriter = new PrintWriter(socket.getOutputStream());

            writeRequest(printWriter, parsedData.get("Host"), parsedData.get("Path"));
            getRequestResponse(socket, socket.getInputStream());

        }catch(MalformedURLException e){
            System.out.println("Error in forming the URL");
        }
        catch(Exception e){
            System.out.println("Error in establishing socket connection");
        }
        finally {
                try {
                    socket.close();
                } catch (IOException e) {
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
        return parsedMap;
    }

    public void getRequestResponse(Socket socket, InputStream stream) {
    	Scanner in = new Scanner(stream);
    	
    	List<String> response = new ArrayList<>();
    	while(in.hasNextLine()){
    		response.add(in.nextLine());
    	}
    	List<String> responseBody	 = response.subList(response.indexOf("") + 1, response.size());
    	
    	if (verbose) {
    		for (String responseLine : response) {
    			System.out.println(responseLine);
    		}
    	}
    	else {
    		for (String responseLine : responseBody) {
    			System.out.println(responseLine);
    		}
    	}
    	
    	if (output != null) {
    		try {
    			FileWriter outputFileWriter = new FileWriter(output);
    			for (String responseLine : responseBody) {
    				outputFileWriter.write(responseLine + "\n");
    			}
    			outputFileWriter.close();
    		}
    		catch (IOException e) {
    			System.out.println(e.getMessage());
    		}
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
        printWriter.print("Host: "+host+"\r\n");
        if(headers != null){
            for(String header : headers){
                printWriter.print(header+"\r\n");
            }
        }
        printWriter.print("\r\n");
        printWriter.print("");
        printWriter.flush();
    }

    @Override
    public void run() {
        sendRequest();
        System.exit(0);
    }
}
