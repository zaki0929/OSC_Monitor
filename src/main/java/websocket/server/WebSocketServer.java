package websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import com.illposed.osc.*;
import java.net.*;
import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

@ServerEndpoint("/echo")
public class WebSocketServer {

  @OnMessage
  public String echo(String message) {
    System.out.println(message);
    sendOSC(message);
    System.out.println(message);
    return message;
  }

  public void sendOSC(String message) {
    String[] data = message.split("-");
    
    // OSC Sender
    OSCPortOut sender = null;
    try{
      sender = new OSCPortOut(InetAddress.getLocalHost(), 8000);
    }catch(Exception ex){
      ex.printStackTrace();
    }

    ArrayList<Object> chat = new ArrayList<Object>();
    //chat.add(data[1]);
    chat.add(message);

    //OSCMessage msg = new OSCMessage(data[0], chat);
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

