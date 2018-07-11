$(function(){
  var url = 'ws://' + location.hostname + ':8080/websocket/echo';
  var ws = new WebSocket(url);

  ws.onmessage = function(receive){
    $('#log').prepend(receive.data + "<br/>");
  };

  // Sender
  $('#send').click(function(){
    var address = $('#address_s').val();
    var message = $('#message_s').val();
    var data = 's: ' + address + ', ' + message;
    ws.send(data);
  });

  $('#address_s').keypress(function(e){
    if(e.which == 13){
      var address = $('#address_s').val();
      var message = $('#message_s').val();
      var data = 's: ' + address + ', ' + message;
      ws.send(data);
    }
  });

  $('#message_s').keypress(function(e){
    if(e.which == 13){
      var address = $('#address_s').val();
      var message = $('#message_s').val();
      var data = 's: ' + address + ', ' + message;
      ws.send(data);
    }
  });

  // Receiver
  $('#receive').click(function(){
    var address = $('#address_r').val();
    var data = 'r: ' + address;
    ws.send(data);
  });

  $('#address_r').keypress(function(e){
    if(e.which == 13){
      var address = $('#address_r').val();
      var data = 'r: ' + address;
      ws.send(data);
    }
  });

});
