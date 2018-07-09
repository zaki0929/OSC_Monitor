package websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

import com.illposed.osc.*;
import java.net.*;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;
import java.io.*;

@ServerEndpoint("/echo")
public class WebSocketServer {

  @OnMessage
  public String echo(String message, Session session) {
    System.out.println(message);

    String[] data = message.split(": ");

    if(data[0].equals("s")){
      sendOSC(data[1]);
    }
      
    if(data[0].equals("r")){
      receiveOSC(data[1], session);
    }
    return message;
  }

  public void sendOSC(String message) {
    String[] data = message.split(", ");
    
    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), 8000);
    }catch(Exception ex){
      ex.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    chat.add(data[1]);

    OSCMessage msg = new OSCMessage(data[0], chat);
    try{
      sender.send(msg);
      System.out.println("Sended!");
    }catch(Exception e){
      e.printStackTrace();
    }
    sender.close();
  }

  public void receiveOSC(String message, Session session){
    OSCPortIn receiver = null;
    try{
      receiver = new OSCPortIn(8000);
    }catch(SocketException e2){
      e2.printStackTrace();
    }

    OSCListener listener = new OSCListener(){
      public void acceptMessage(Date time, OSCMessage message){
        System.out.println(message.getAddress());
        for(Object ob : message.getArguments()){
          System.out.println((String) ob);
          session.getOpenSessions().forEach(s -> {
            s.getAsyncRemote().sendText((String) ob);
          });
        }
      }
    };

    receiver.addListener(message, listener);
    receiver.startListening();
    //receiver.close();
  }
}

