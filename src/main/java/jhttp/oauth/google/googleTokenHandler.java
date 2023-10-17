/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhttp.oauth.google;

/**
 *
 * @author delro
 */
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;

public class googleTokenHandler implements HttpHandler {
    private String code;
    private String scope;
    private String redirect;
    private String clientID;
    private String clientSecret;
    
    private JSONObject tokenJSON;
    private JSONObject userData;
    
    public googleTokenHandler(String clientID, String clientSecret) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirect = "http://localhost:8000/auth/google/callback";
    }
    
    @Override
    public void handle(HttpExchange request) throws IOException {
        Map<String, String> parameters = parseURI(request.getRequestURI());
        this.code = parameters.get("code");
        this.scope = parameters.get("scope");
        
        googleTokenRequest tokenReq = new googleTokenRequest(code, redirect, clientID, clientSecret);
        this.tokenJSON = new JSONObject(tokenReq.httpRequest());
        
        String accessToken = tokenJSON.get("access_token").toString();
        String tokenType = tokenJSON.get("token_type").toString();
        
        googleDataRequest dataReq = new googleDataRequest(accessToken, tokenType);
        this.userData = new JSONObject(dataReq.httpRequest());
        
        //Below is completely optional and for display purposes only.
        //From here we can store the data we need and redirect user to proper page.
        String response = "<html><head><title>Hello Oauth!</title></head><body>";
        response += "<h1>Oauth response:</h1>";
        response += "<img src='" + userData.get("picture") + "' alt='" + userData.get("name") + " Profile Picture'/>";
        response += "<h4>Username: " + userData.get("name") + "</h4>";
        response += "<h4>UserID: " + userData.get("id") + "</h4>";
        response += "</body></html>";
        
        Headers res = request.getResponseHeaders();
        
        res.add("Content-Type", "text/html");
        res.add("Date", new Date().toString());
        
        request.sendResponseHeaders(200, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    public Map<String, String> parseURI(URI reqURI) {
        Map<String, String> parameters = new HashMap<>();
        String[] uriSplit = reqURI.getQuery().split("&");
        
        for (String param : uriSplit) {
            String[] keyVal = param.split("=");
            parameters.put(keyVal[0], keyVal[1]);
        }
        
        return parameters;
    }
    
}
