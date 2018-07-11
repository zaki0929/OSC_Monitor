package osc.sender;

import javax.websocket.Session;
import java.net.*;
import java.util.Collection;
import java.util.ArrayList;
import java.io.*;
import com.illposed.osc.*;

public class  OSCSender{
  // OSC を送信するメソッド
  public void sendOSC(String message, Session session) {
    String[] data = message.split(", ");
    
    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), 9000);
    }catch(Exception e){
      e.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    chat.add(data[1]);

    OSCMessage msg = new OSCMessage(data[0], chat);
    try{
      sender.send(msg);
      for(Object ob : msg.getArguments()){
          System.out.println((String) ob);
        try{
          session.getBasicRemote().sendText("Send: " + msg.getAddress() + ": " + (String) ob);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      System.out.println("Sended!");
    }catch(Exception e){
      e.printStackTrace();
    }
    sender.close();
  }
}
