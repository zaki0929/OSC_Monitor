package osc.sender;

import javax.websocket.Session;
import java.net.*;
import java.util.Collection;
import java.util.ArrayList;
import java.io.*;
import com.illposed.osc.*;

// OSC の送信用クラス
public class  OSCSender{
  // OSC を送信するメソッド
  public void sendOSC(String message, Session session){
    String[] data = message.split(", ");
    
   // port = Integer.parseInt(data[0])
   // address = data[1]
   // data = data[2]


    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), Integer.parseInt(data[0]));
    }catch(Exception e){
      e.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    chat.add(data[2]);

    OSCMessage msg = new OSCMessage(data[1], chat);
    try{
      sender.send(msg);
      for(Object ob : msg.getArguments()){
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
