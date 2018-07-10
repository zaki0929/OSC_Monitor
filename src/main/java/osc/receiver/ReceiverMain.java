package osc.receiver;
import com.illposed.osc.*;
import java.net.SocketException;
import java.util.Date;

public class ReceiverMain{
  public static void main(String[] args){

    OSCPortIn receiver = null;
    try{
      receiver = new OSCPortIn(9000);
    }catch(SocketException e2){
      e2.printStackTrace();
    }

    OSCListener helloListener = new OSCListener(){
      public void acceptMessage(Date time, OSCMessage message){
        System.out.println("hello received!");
        System.out.println(message.getAddress());
        for(Object ob : message.getArguments()){
          System.out.println((String) ob);
        }
      }
    };

    receiver.addListener("/greet/hello", helloListener);
    
    while(true){
      receiver.startListening();
      try{
        Thread.sleep(1000);
      }catch (InterruptedException e){
      }
    }
  }
}
