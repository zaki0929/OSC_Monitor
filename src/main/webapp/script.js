$(function(){
  var url = 'ws://' + location.hostname + ':8080/websocket/echo';
  var ws = new WebSocket(url);

  // WebSocket サーバからメッセージが届いたときに実行
  ws.onmessage = function(receive){
    $('#log').prepend(receive.data + "<br/>");
  };

//---- Sender ---------------------------------------------

  function ws_send_s(){
    var address = $('#address_s').val();
    var message = $('#message_s').val();
    var data = 's: ' + address + ', ' + message;
    ws.send(data);
  };

  // send ボタンをクリックしたときに実行
  $('#send').click(ws_send_s);

  // アドレス入力欄でエンターキーを押したときに実行
  $('#address_s').keypress(function(e){
    if(e.which == 13){
      ws_send_s();
    }
  });

  // メッセージ入力欄でエンターキーを押したときに実行
  $('#message_s').keypress(function(e){
    if(e.which == 13){
      ws_send_s();
    }
  });

//---- Receiver ---------------------------------------------

  function ws_send_r(){
    var address = $('#address_r').val();
    var data = 'r: ' + address;
    ws.send(data);
  };

  function ws_send_r_and_wait(){
    if(isWait == 0){
      ws_send_r(); 
      $('#receive').text("wait");
      isWait = 1;
      setTimeout(function(){
        $('#receive').text("receive");
        isWait = 0;
      },12000);
    }
  }

  var isWait = 0;

  // receive ボタンをクリックしたとき
  $('#receive').click(ws_send_r_and_wait);

  // アドレス入力欄でエンターキーを押したとき
  $('#address_r').keypress(function(e){
    if(e.which == 13){
      ws_send_r_and_wait();
    }
  });

});
