package client;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Args {
    @Parameter(names = {"-t"}, description = "Type of Request")
    private String request;

    @Parameter(names = {"-k"}, description = "key")
    private String key;

    @Parameter(names = {"-v"}, description = "value of request")
    private String value;

    @Parameter(names = {"-in"}, description = "filename")
    private String filename = "";

    public String getRequest() {
        return request;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFilename() {
        return filename;
    }
}
