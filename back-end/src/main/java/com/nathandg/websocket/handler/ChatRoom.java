package com.nathandg.websocket.handler;

import com.google.gson.Gson;
import com.nathandg.websocket.models.PADOLabsMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChatRoom extends AbstractWebSocketHandler {

    public final static List<WebSocketSession> sessionList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("Connection established...");
        sessionList.add(session);

        session.sendMessage(new TextMessage("{\"user\":\"" + session.getAttributes().get("user") +"\", \"message\": \""+ getRandomMessage() +"\"}"));
        System.out.println("Message sent...");
        System.out.println("{\"user\":\"" + session.getAttributes().get("user") +"\", \"message\": \""+ getRandomMessage() +"\"}");

//      session.sendMessage(new TextMessage("Welcome to the chat room! " + "seu usuário é: " + session.getAttributes().get("user").toString()));

        //TODO CHECK THIS
        super.afterConnectionEstablished(session);
    }

    private String getRandomMessage()
    {
        String[] messages = {"Cai de paraquedas no servidor!", "To na area", "É hora do jogo!"};
        Random random = new Random();
        return messages[random.nextInt(messages.length)];
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Gson gson = new Gson();
        PADOLabsMessage padoLabsMessage = gson.fromJson(message.getPayload(), PADOLabsMessage.class);

        System.out.println("\nMessage received...");
        System.out.println(padoLabsMessage);

        if(padoLabsMessage.getUser().equalsIgnoreCase(""))
        {
            System.out.println("User is empty");
            for(WebSocketSession webSocketSession : sessionList)
            {
                webSocketSession.sendMessage(new TextMessage("{\"user\":\"" + session.getAttributes().get("user") + "[ALL]" +"\", \"message\": \""+ padoLabsMessage.getMessage() +"\"}"));
                System.out.println("Message sent...");
                System.out.println("{\"user\":\"" + session.getAttributes().get("user") +"\", \"message\": \""+ padoLabsMessage +"\"} \n");
            }
        } else {

            Boolean notFound = true;

            for (WebSocketSession webSocketSession : sessionList) {

                if (webSocketSession.getAttributes().get("user").toString().equalsIgnoreCase(padoLabsMessage.getUser())) {
                    System.out.println("Sending message to: " + webSocketSession.getAttributes().get("user").toString());
                    session.sendMessage(new TextMessage("{\"user\":\"" + session.getAttributes().get("user") + "[PRIV]" +"\", \"message\": \""+ padoLabsMessage.getMessage() +"\"}"));
                    webSocketSession.sendMessage(new TextMessage("{\"user\":\"" + session.getAttributes().get("user") + "[PRIV]" + "\", \"message\": \""+ padoLabsMessage.getMessage() +"\"}"));
                    notFound = false;
                }
            }

            if(notFound)session.sendMessage(new TextMessage("{\"user\":\" User Not Found \", \"message\": \" unsent message \"}"));

        }

        System.out.println("Message ...\n");

        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        System.out.println("Connection closed..........");
        sessionList.forEach(
                webSocketSession -> {
                    System.out.println(webSocketSession.getAttributes().get("user").toString());
                }
        );
        System.out.println("Connection closed..........");
        super.afterConnectionClosed(session, status);
    }


}
