/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhttp.oauth.discord;

/**
 *
 * @author delro
 */
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.net.URLEncoder;

public class discordAuthHandler implements HttpHandler {
    private String redirect;
    private String clientID;
    private String scope;
    
    public discordAuthHandler(String clientID) {
        this.redirect = URLEncoder.encode("http://localhost:8000/auth/discord/callback", StandardCharsets.UTF_8);
        this.scope = "identify";
        this.clientID = clientID;
    }
    
    @Override
    public void handle(HttpExchange request) throws IOException {
        Headers responseHead = request.getResponseHeaders();
        Date currentDate = new Date();
        
        String authRedirect = "https://discord.com/api/oauth2/authorize?client_id=" + clientID + "&redirect_uri=" + redirect + "&response_type=code&scope=" + scope;
        
        responseHead.add("Date", currentDate.toString());
        responseHead.add("Location", authRedirect);
        
        request.sendResponseHeaders(302, -1);
    }
}
