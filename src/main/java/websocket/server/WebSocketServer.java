package websocket.server;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

import java.util.Date;
import java.io.*;

import osc.receiver.OSCReceiver;
import osc.sender.OSCSender;

@ServerEndpoint("/echo")
public class WebSocketServer {
  OSCReceiver receiver;
  OSCSender sender;

  // 接続が確立したときに実行
  @OnOpen
  public void onOpen(){
    receiver = new OSCReceiver(); 
    sender = new OSCSender(); 
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
  public void echo(String message, Session session){
    // クライアントから届いた指示が, Sender か Receiver のどちらへ向けたものか調べる
    String[] data = message.split(": ");
    if(data[0].equals("s")){                      // Sender への指示だった場合
      sender.sendOSC(data[1], session);                  // OSC を送信するメソッドを実行
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
}
