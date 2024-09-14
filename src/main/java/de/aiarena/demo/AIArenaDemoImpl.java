package de.aiarena.demo;

import de.aiarena.demo.websocket.AIArenaWebSocketClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableScheduling
public class AIArenaDemoImpl {
    @Autowired
    private AIArenaWebSocketClient client;

    @PostConstruct
    public void onInit(){
        Runnable todo = this::todo;
        var thread = new Thread(todo);
        thread.start();
    }

    public void todo(){
        try {
            Thread.sleep(3000);
        }catch(Exception ex){

        }
        System.out.println("Authentication token required:");
        try {
            var token = "your token goes here";
            client.authenticate(token);
        }catch(Exception ex){
            System.out.println("Error reading token / connecting "+ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args){
        new SpringApplicationBuilder(AIArenaDemoImpl.class).build().run(args);
    }
}

