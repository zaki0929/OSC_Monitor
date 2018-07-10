package websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
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
  OSCReceiver receiver;

  // 接続が確立したときに実行
  @OnOpen
  public void onOpen(){
    receiver = new OSCReceiver(); 
  }

  // 接続が切断されたときに実行
  @OnClose
  public void onClose(Session session){
    if(receiver.isRun){
      receiver.stopReceive(session);
    }
  }

  // メッセージを受信したときに実行
  @OnMessage
  public void echo(String message, Session session) {
    // クライアントから届いた指示が, Sender か Receiver のどちらへ向けたものか調べる
    String[] data = message.split(": ");
    if(data[0].equals("s")){                      // Sender への指示だった場合
      sendOSC(data[1], session);                  // OSC を送信するメソッドを実行
    }
    if(data[0].equals("r")){                      // Receiver への指示だった場合
      if(!receiver.isRun){
        receiver.startReceive(data[1], session);  // OSC を受信するメソッドを実行
      }else{
        receiver.stopReceive(session);
        receiver.startReceive(data[1], session);
      }
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
}

// OSC を受信するためのクラス 
class OSCReceiver{
  OSCPortIn receiver;
  boolean isRun;

  public OSCReceiver(){
    isRun = false;
  }

  // 受信を開始するためのメソッド
  public void startReceive(String message, Session session){
    isRun = true;
    receiver = null;
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
    try{
      session.getBasicRemote().sendText("Started up receiver: " + message);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  // 受信を終了するためのメソッド
  public void stopReceive(Session session){
    receiver.stopListening();
    receiver.close();
    try{
      session.getBasicRemote().sendText("Shut down receiver");
    }catch(Exception e){
      e.printStackTrace();
    }
    isRun = false;
  }
}
