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
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

public class discordTokenHandler implements HttpHandler{
    private String code;
    private String redirect;
    private String clientID;
    private String clientSecret;
    private HttpClient httpClient;
    
    private JSONObject tokenJSON;
    private JSONObject userData;
    
    public discordTokenHandler(String clientID, String clientSecret, HttpClient client) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.httpClient = client;
        this.redirect = "http://localhost:8000/auth/discord/callback";
    }
    
    @Override
    public void handle(HttpExchange request) throws IOException {
        Map<String, String> parameters = parseURI(request.getRequestURI());
        this.code = parameters.get("code");
        
        //System.out.println(this.code);
        discordTokenRequest tokenReq = new discordTokenRequest(code, redirect, clientID, clientSecret, httpClient);
        this.tokenJSON = new JSONObject(tokenReq.httpRequest());
        String accessToken = tokenJSON.get("access_token").toString();
        String tokenType = tokenJSON.get("token_type").toString();
        
        System.out.println(accessToken);
        discordDataRequest dataReq = new discordDataRequest(accessToken, tokenType, httpClient);
        this.userData = new JSONObject(dataReq.httpRequest());
        
        Headers res = request.getResponseHeaders();
        
        res.add("Content-Type", "text/html");
        res.add("Date", new Date().toString());
        
        String response = this.buildResponse(userData);
        
        request.sendResponseHeaders(200, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    private Map<String, String> parseURI(URI reqURI) {
        Map<String, String> parameters = new HashMap<>();
        String[] uriSplit = reqURI.getQuery().split("&");
        
        for (String param : uriSplit) {
            String[] keyVal = param.split("=");
            parameters.put(keyVal[0], keyVal[1]);
        }
        
        return parameters;
    }
    
    private String buildResponse(JSONObject userData) {
        String response = "<html><head><title>Hello Oauth!</title></head><body>";
        response += "<h1>Oauth response:</h1>";
        response += "<img src='https://cdn.discordapp.com/avatars/" + userData.get("id") + "/" + userData.get("avatar") + "' alt='User Profile Picture'";
        response += "<p>Global Name: " + userData.get("global_name") + "</p>";
        response += "<p>UserID: " + userData.get("id") + "</p>";
        
        return response;
    }
}
