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
import java.net.http.HttpClient;
import java.time.Duration;

//import classes from adjacent google folder.
import jhttp.oauth.google.googleAuthHandler;
import jhttp.oauth.google.googleTokenHandler;

//discord classes
import jhttp.oauth.discord.discordAuthHandler;
import jhttp.oauth.discord.discordTokenHandler;

public class server {
    private HttpServer server;
    private HttpClient client;
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
        this.client = this.buildClient();
        
        //GOOGLE
        //Ideally, I would store these in some sort of env variable, but I'm
        //still learning and figuring out java and maven so I've been
        //sticking to this for now.
        this.googleID = "";
        this.googleSecret = "";
        
        //DISCORD
        this.discordID = "";
        this.discordSecret = "";
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
        server.createContext("/auth/google/callback", new googleTokenHandler(googleID, googleSecret, client));
    }
    
    public void createDiscordContext() {
        server.createContext("/auth/discord", new discordAuthHandler(discordID));
        server.createContext("/auth/discord/callback", new discordTokenHandler(discordID, discordSecret, client));
    }
    
    public void createGithubContext() {
    //    server.createContext("/auth/github", null);
    //    server.createContext("/auth/github/callback", null);
    }
    
    private HttpClient buildClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }
}
