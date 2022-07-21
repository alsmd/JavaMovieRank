package org.doc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DefaultApi {
    protected boolean success = false;
    protected URI address;
    protected HttpClient client;
    protected String errorMessage = "Something went Wrong!";
    public List<RankerData> getData(){return new ArrayList<RankerData>();}

    public String getErrorMessage() {
        return errorMessage;
    }

    protected void createClient(String url){
        this.address  = URI.create(url);
        this.client  = HttpClient.newHttpClient();
    }
    /**
     *
     * @return Return the body from a request to the configured URI
     *
     */
    protected String simleRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(address).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
