/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhttp.oauth.discord;

/**
 *
 * @author delro
 */
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class discordTokenRequest {
    private String code;
    private String redirect;
    private String clientID;
    private String clientSecret;
    private HttpClient client;
    
    public discordTokenRequest(String initCode, String initRedirect, String initID, String initSecret, HttpClient client) {
        this.code = initCode;
        this.redirect = URLEncoder.encode(initRedirect, StandardCharsets.UTF_8);
        this.clientID = initID;
        this.clientSecret = initSecret;
        this.client = client;
    }
    
    public String httpRequest() {
        HttpRequest request = this.buildRequest();
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
        
        return "ERROR: discordTokenRequest response is null!";
    }
    
    private HttpRequest buildRequest() {
        URI url = URI.create("https://discord.com/api/oauth2/token");
        String body = "code=" + code + "&redirect_uri=" + redirect + "&client_id=" + clientID + "&client_secret=" + clientSecret + "&scope=identify&grant_type=authorization_code";
        System.out.println(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("content-type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(body))
                .build();
        
        return request;
    }
}
