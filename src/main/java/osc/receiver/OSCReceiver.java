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

  // ポートが使える状態か確認
  public boolean canUse(int port){
    OSCPortIn ss = null;
    boolean isOpen;
    try{
      ss = new OSCPortIn(port);
      isOpen = true;
    }catch(Exception e){
      isOpen = false;
    }finally{
      if(ss!=null)try{ss.close();}catch(Exception e){}
    }
    return isOpen;
  }

  // 受信を開始するためのメソッド
  public void startReceive(String message, Session session){
    int port = Integer.parseInt(message);
    if(canUse(port)){
      isRun = true;
      receiver = null;
      try{
        receiver = new OSCPortIn(port);
      }catch(SocketException e){
        e.printStackTrace();
      }

      OSCListener listener = new OSCListener(){
        public void acceptMessage(Date time, OSCMessage msg){
          String receiver_obs = " ";
          String receiver_address = " ";
          for(Object ob : msg.getArguments()){
            try{
              try{
                Thread.sleep(10);
              }catch (InterruptedException e){
                e.printStackTrace();
              }
              receiver_obs += " ";
              receiver_obs += ob;
              receiver_address = msg.getAddress();
            }catch(Exception e){
              e.printStackTrace();
            }
          }
          try{
            session.getBasicRemote().sendText("Receive: " + receiver_address + ":" + receiver_obs);
          }catch(Exception e){
            e.printStackTrace();
          }
          System.out.println("Received!");
        }
      };
      receiver.addListener("/*", listener);
      receiver.startListening();
      try{
        session.getBasicRemote().sendText("Started up receiver on " + message);
      }catch(Exception e){
        e.printStackTrace();
      }
    }else{
      try{
        session.getBasicRemote().sendText("<font color=\"#E64552\">Error: Since port " + message + " is in use, please use another port.</font>");
      }catch(Exception e){
        e.printStackTrace();
      }
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
