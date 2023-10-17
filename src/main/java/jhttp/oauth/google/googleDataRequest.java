/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhttp.oauth.google;

/**
 *
 * @author delro
 */
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.io.IOException;
import java.time.Duration;

public class googleDataRequest {
    private String accessToken;
    private String tokenType;
    
    public googleDataRequest(String initToken, String initType) {
        this.accessToken = initToken;
        this.tokenType = initType;
    }
    
    public String httpRequest() {
        HttpRequest request = this.buildRequest();
        HttpClient client = this.buildClient();
        HttpResponse<String> response = null;
        
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException error) {
            System.out.println(error.getMessage());
        }
        
        if (response != null) {
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return response.body();
        }
        
        return "ERROR: Response from googleDataRequest is null!";
    }
    
    private HttpRequest buildRequest() {
        URI url = URI.create("https://www.googleapis.com/oauth2/v2/userinfo");
        String authString = " " + tokenType + " " + accessToken;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Authorization", authString)
                .GET()//default method, but I like to be explicit
                .build();
        
        return request;
    }
    
    private HttpClient buildClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }
}
