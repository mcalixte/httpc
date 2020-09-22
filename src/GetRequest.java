import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class GetRequest implements iGetRequest {
    private String url;
    private int port;

    //Network objects
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;


    @Override
    public void sendRequest(String url, int port) {

        try{
            /**For the bonus questions with redirects, you should use the simple constructor
             * and dynamically bind the sockets to a url at runtime
             * 
            */
            socket = new Socket(url, port);

            printWriter = new PrintWriter(socket.getOutputStream());

            writeGetRequestHeader(printWriter, url);
            getGetRequestResponse(socket, new InputStreamReader(socket.getInputStream()));
        }catch(Exception e){
            System.out.println("MKC1: Error in creating network connection");
            e.printStackTrace();
        }
    }

    private void getGetRequestResponse(Socket socket, InputStreamReader inputStreamReader) {
        bufferedReader = new BufferedReader(inputStreamReader);
        String outputString;

        try {
            outputString = bufferedReader.readLine();
            while(outputString != null){
                System.out.println(outputString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeGetRequestHeader(PrintWriter printWriter, String url) {
        printWriter.print("GET /get?course=networking&assignment=1%27 HTTP/1.1\r\n");
        printWriter.print("Host: "+url+"\r\n\r\n");
        printWriter.print("");
        printWriter.flush();
    }
}
