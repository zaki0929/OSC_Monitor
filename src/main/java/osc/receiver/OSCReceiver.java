package osc.receiver;

import javax.websocket.Session;
import java.net.*;
import java.util.Date;
import java.io.*;
import com.illposed.osc.*;

// OSC の受信用クラス
public class OSCReceiver{
  OSCPortIn receiver;
  public boolean isRun;

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
              Thread.sleep(10);
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
