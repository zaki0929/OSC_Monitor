package osc.sender;
import com.illposed.osc.*;
import java.net.*;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

public class SenderMain{
  public static void main(String[] args){

    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), 9000);
    }catch(Exception ex){
      ex.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    chat.add("Hello!!");

    OSCMessage msg = new OSCMessage("/greet/hello", chat);
    try{
      sender.send(msg);
      System.out.println("Sended!");
    }catch(Exception e){
      e.printStackTrace();
    }
    
    sender.close();
  }
}
