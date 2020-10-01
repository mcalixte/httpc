import GetRequest.GetRequest;
import PostRequest.PostRequest;
import httpc.Httpc;
import picocli.CommandLine;

@CommandLine.Command(
        subcommands = {
                GetRequest.class,
                PostRequest.class
        }
)
public class Entry  {
    public static void main(String[] args){
        int exitCode = new CommandLine(new Httpc()).execute(args);
    }


}
