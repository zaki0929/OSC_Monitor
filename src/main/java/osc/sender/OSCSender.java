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

    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), Integer.parseInt(data[0]));
    }catch(Exception e){
      e.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    if(isNumber(data[2])){
      if(isInt(data[2])){
        // int
        System.out.println("int");
        chat.add(Integer.parseInt(data[2]));
      }else{
        // float
        System.out.println("float");
        chat.add(Float.parseFloat(data[2]));
      }
    }else{
      // String
      System.out.println("String");
      chat.add(data[2]);
    }

    OSCMessage msg = new OSCMessage(data[1], chat);
    try{
      sender.send(msg);
      for(Object ob : msg.getArguments()){
        try{
          //session.getBasicRemote().sendText("Send: " + msg.getAddress() + ": " + (String) ob);
          session.getBasicRemote().sendText("Send: " + msg.getAddress() + ": " + ob);
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
