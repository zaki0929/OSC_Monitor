package websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.OnClose;
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
  public void echo(String message, Session session) {
    System.out.println(message);

    // クライアントから届いた指示が, Sender か Receiver のどちらへ向けたものか調べる
    String[] data = message.split(": ");
    if(data[0].equals("s")){         // Sender への指示だった場合
      sendOSC(data[1], session);              // OSC を送信するメソッドを実行
    }
    if(data[0].equals("r")){         // Receiver への指示だった場合
      receiveOSC(data[1], session);  // OSC を受信するメソッドの実行
    }
  }

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

  // OSC を受信するメソッド
  public void receiveOSC(String message, Session session){
    OSCPortIn receiver = null;
    try{
      receiver = new OSCPortIn(9000);
    }catch(SocketException e){
      e.printStackTrace();
    }

    OSCListener listener = new OSCListener(){
      public void acceptMessage(Date time, OSCMessage msg){
        for(Object ob : msg.getArguments()){
	  try{
            try{
              Thread.sleep(100);
            }catch (InterruptedException e){
              e.printStackTrace();
            }
            session.getBasicRemote().sendText("Receive: " + msg.getAddress() + ": " + (String) ob);
	  }catch(Exception e){
            e.printStackTrace();
	  }
        }
        System.out.println("Received!");
      }
    };

    receiver.addListener(message, listener);
    receiver.startListening();
    //receiver.stopListening();
    //receiver.close();
  }
}
