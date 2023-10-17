package jhttp.oauth;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author delro
 */
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

//import classes from adjacent google folder.
import jhttp.oauth.google.googleAuthHandler;
import jhttp.oauth.google.googleTokenHandler;

public class server {
    private HttpServer server;
    private int port;
    
    //GOOGLE ID AND SECRET
    private String googleID;
    private String googleSecret;
    
    //DISCORD ID AND SECRET
    private String discordID;
    private String discordSecret;
    
    public server(int initPort) {
        this.port = initPort;
        this.server = null;
        
        //GOOGLE
        //Ideally, I would store these in some sort of env variable, but I'm
        //still learning and figuring out java and maven so I've been
        //sticking to this for now.
        this.googleID = "684021548761-aogqulml24qr2au9mv1hvo92do99b2mb.apps.googleusercontent.com";
        this.googleSecret = "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";
        
        //DISCORD
        this.discordID = null;
        this.discordSecret = null;
    }
    
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port),0);

        //GOOGLE
        this.createGoogleContext();
        
        //DISCORD
        this.createDiscordContext();
        
        server.setExecutor(null);
        server.start();
    }
    
    public void createGoogleContext() {
        server.createContext("/auth/google", new googleAuthHandler(googleID));
        server.createContext("/auth/google/callback", new googleTokenHandler(googleID, googleSecret));
    }
    
    public void createDiscordContext() {
        //server.createContext("/auth/discord", null);
        //server.createContext("/auth/discord/callback", null);
    }
}
