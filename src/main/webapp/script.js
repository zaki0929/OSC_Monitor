$(function(){
  var url = 'ws://' + location.hostname + ':8080/websocket/echo';
  var ws = new WebSocket(url);

  // WebSocket サーバからメッセージが届いたときに実行
  ws.onmessage = function(receive){
    $('#log').prepend(receive.data + "<br/>");
  };

  // 要素が空か否かを調べるプラグイン
  (function($){
    $.isBlank = function(obj){
      return(!obj || $.trim(obj) === "");
    };
  })(jQuery);

//---- Sender ---------------------------------------------

  // OSC の Sender への命令を WebSocket サーバに送信する関数
  function ws_send_s(){
    if(!$.isBlank($('#address_s').val()) && !$.isBlank($('#message_s').val())){
      var address = $('#address_s').val();
      var message = $('#message_s').val();
      var data = 's: ' + address + ', ' + message;
      ws.send(data);
    }
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

  // OSC の Receiver への命令を WebSocket サーバに送信する関数
  function ws_send_r(){
    if(!$.isBlank($('#address_r').val())){
      var address = $('#address_r').val();
      var data = 'r: ' + address;
      ws.send(data);
    }
  };

  // OSC の Receiver への命令を WebSocket サーバに送信する関数に
　// 待機処理を加えた関数
  function ws_send_r_and_wait(){
    if(!isWait && !$.isBlank($('#address_r').val())){
      ws_send_r(); 
      $('#receive').text("wait");
      isWait = true;
      setTimeout(function(){
        $('#receive').text("receive");
        isWait = false;
      },14000);
    }
  }

  // 待機状態か否かを明示する変数
  var isWait = false;

  // receive ボタンをクリックしたときに実行
  $('#receive').click(ws_send_r_and_wait);

  // アドレス入力欄でエンターキーを押したときに実行
  $('#address_r').keypress(function(e){
    if(e.which == 13){
      ws_send_r_and_wait();
    }
  });

});
