package Interfaces;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface iRequest {
    public void sendRequest();
    public void getRequestResponse(Socket socket, InputStream stream);
    public void writeRequest(PrintWriter printWriter, String url, String urlPath);
}
