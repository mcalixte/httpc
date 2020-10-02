package PostRequest;

import Interfaces.iRequest;
import picocli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@CommandLine.Command(name = "POST")
public class PostRequest implements Runnable, iRequest {
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
	
	@CommandLine.Option(names = {"-d", "--data"}, description = "inline data to be added to the body of the request")
    String data;
	
	@CommandLine.Option(names = {"-f", "--file"}, description = "file data to be added to the body of the request")
    String file;
	
    @Override
    public void run() {
    	if (data != null && file != null) {
    		System.out.println("Error: cannot run httpc with both inline and file data");
    		System.exit(1);
    	}
    	
        System.out.println("Sending POST request ...");
        sendRequest();
    }

    @Override
    public void sendRequest() {
        try{
            parsedData = parseUrlInput(url);
            InetAddress inetAddress = InetAddress.getByName(parsedData.get("Host"));
            socket = new Socket(inetAddress, 80);
            printWriter = new PrintWriter(socket.getOutputStream());

            writeRequest(printWriter, parsedData.get("Host"), parsedData.get("Path"));     
            getRequestResponse(socket, socket.getInputStream());

        }
        catch (MalformedURLException e) {
            System.out.println("Error in forming the URL");
        }
        catch (Exception e) {
            System.out.println("Error in establishing socket connection");
        	System.out.println(e.getMessage());
        }
        finally {
                try {
                	socket.close();
                } 
                catch (IOException e) {
                	System.out.println("Error in making POST Request");
                }
                printWriter.flush();
        }
    }

    @Override
    public void getRequestResponse(Socket socket, InputStream stream) {
    	Scanner in = new Scanner(stream);
        if (verbose) {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        }
        else {
            response = new ArrayList<>();
            while(in.hasNextLine()){
                response.add(in.nextLine());
            }
            parseVerboseResponse(response);
        }
    }

    @Override
    public void writeRequest(PrintWriter printWriter, String host, String path) {
    	StringBuilder request = new StringBuilder();
    	request.append("POST " + path + " HTTP/1.0\r\n");
    	request.append("Host: " + host + "\r\n");
    	
    	if (headers != null) {
    		for (String header : headers) {
    			request.append(header + "\r\n");
    		}
    	}
    	
    	// --- Add data to the request ---// 
    	if (data != null) {
    		request.append("Content-Length:" + data.length() + "\r\n\r\n");
    		request.append(data);
    	}
    	else if (file != null) {
    		try {
    			StringBuilder fileData = new StringBuilder();
    			Scanner fileReader = new Scanner(new File(file));
    			while (fileReader.hasNextLine()) {
    				fileData.append(fileReader.nextLine());
    			}
    			fileReader.close();
    			
    			if (fileData.length() > 0) {
    	    		request.append("Content-Length:" + fileData.length() + "\r\n\r\n");
    	    		request.append(fileData);
    			}
    			else {
    				// handles the case of an empty file
    	    		request.append("\r\n\r\n");
    			}
    		}
    		catch (FileNotFoundException e) {
    			System.out.println(e.getMessage());
    			System.exit(1);
    		}
    	}
    	else {
    		request.append("\r\n\r\n");
    	}

		printWriter.print(request.toString());
		printWriter.flush();
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
    
    private void parseVerboseResponse(List<String> response) {
        List<String> parsedResponse = response.subList(response.indexOf("")+1, response.size());
        for(String string : parsedResponse){
            System.out.println(string);
        }
    }
}
