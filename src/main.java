public class main {
    public static void main(String[] args) {
    	
    	CLIParser parser = new CLIParser(args); // May want to replace with a decorator pattern instead and pass into GetRequest
        GetRequest getRequest = new GetRequest();
        getRequest.sendRequest("www.httpbin.org", 80, parser);
    }
}
