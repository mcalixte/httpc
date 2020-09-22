public class main {
    public static void main(String[] args) {
        GetRequest getRequest = new GetRequest();
        getRequest.sendRequest("www.httpbin.org/", 80);


    }
}
