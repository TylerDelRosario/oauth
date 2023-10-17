/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhttp.oauth.google;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.net.URLEncoder;
/**
 *
 * @author delro
 */
public class googleAuthHandler implements HttpHandler {
    private String redirect;
    private String clientID;
    private String scope;
    
    public googleAuthHandler (String clientID) {
        this.redirect = URLEncoder.encode("http://localhost:8000/auth/google/callback", StandardCharsets.UTF_8);
        this.scope = URLEncoder.encode("https://www.googleapis.com/auth/userinfo.profile", StandardCharsets.UTF_8);
        this.clientID = clientID;
    }
    
    @Override
    public void handle(HttpExchange request) throws IOException {
        Headers responseHead = request.getResponseHeaders();
        Date currentDate = new Date();
        
        String authRedirect ="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=" + redirect + "&prompt=consent&response_type=code&client_id=" + clientID + "&scope=" + scope + "&access_type=offline";
        
        responseHead.add("Date", currentDate.toString());
        responseHead.add("Location", authRedirect);
        request.sendResponseHeaders(302, -1);
    }
}
