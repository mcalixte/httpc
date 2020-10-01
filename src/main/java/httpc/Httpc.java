package httpc;

import GetRequest.GetRequest;
import PostRequest.PostRequest;
import picocli.CommandLine;

@CommandLine.Command(name = "httpc", subcommands = { GetRequest.class, PostRequest.class, CommandLine.HelpCommand.class })
public class Httpc {
}
