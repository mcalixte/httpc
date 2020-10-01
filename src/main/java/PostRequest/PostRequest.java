package PostRequest;

import Interfaces.iRequest;
import picocli.CommandLine;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

@CommandLine.Command(name = "POST")
public class PostRequest implements Runnable, iRequest {
    @Override
    public void run() {
        System.out.println("Sending POST request ...");
    }

    @Override
    public void sendRequest() {

    }

    @Override
    public void getRequestResponse(Socket socket, InputStream stream) {

    }

    @Override
    public void writeRequest(PrintWriter printWriter, String url, String urlPath) {

    }
}
