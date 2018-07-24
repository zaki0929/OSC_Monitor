package osc.sender;

import javax.websocket.Session;
import java.net.*;
import java.util.Collection;
import java.util.ArrayList;
import java.io.*;
import com.illposed.osc.*;

// OSC の送信用クラス
public class  OSCSender{

  // String の中身が数字か否かを判定
  public boolean isNumber(String num){
    try{
      Integer.parseInt(num);
      return true;
    }catch(NumberFormatException e){
      try{
        Float.parseFloat(num);
        return true;
      }catch(NumberFormatException e2){
        return false;
      }
    }
  }

  // 中身が数字である String の数字が int か否かを判定
  public boolean isInt(String num){
    try{
      Integer.parseInt(num);
      return true;
    }catch(NumberFormatException e){
      return false;
    }
  }
  
  // OSC を送信するメソッド
  public void sendOSC(String message, Session session){
    String[] data = message.split(", ");
    String[] msg_data = data[2].split(" ");

    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), Integer.parseInt(data[0]));
    }catch(Exception e){
      e.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();

    for(String one_msg : msg_data){
  
      if(isNumber(one_msg)){
        if(isInt(one_msg)){
          // int
          System.out.println("int");
          chat.add(Integer.parseInt(one_msg));
        }else{
          // float
          System.out.println("float");
          chat.add(Float.parseFloat(one_msg));
        }
      }else{
        // String
        System.out.println("String");
        chat.add(one_msg);
      }
    }
  
    OSCMessage msg = new OSCMessage(data[1], chat);
    String sender_obs = " ";
    String sender_address = " ";
    try{
      sender.send(msg);
      for(Object ob : msg.getArguments()){
        try{
          sender_obs += " ";
          sender_obs += ob;
          sender_address = msg.getAddress();
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      try{
        session.getBasicRemote().sendText("send: " + sender_address + ":" + sender_obs);
      }catch(Exception e){
        e.printStackTrace();
      }

      System.out.println("Sended!");
    }catch(Exception e){
      e.printStackTrace();
    }
    sender.close();
  }
}
