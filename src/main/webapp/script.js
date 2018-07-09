$(function(){
  var url = 'ws://' + location.hostname + ':8080/websocket/echo';
  var ws = new WebSocket(url);

  ws.onmessage = function(receive){
    $('#message').text(receive.data);
  };

  ws.onopen = function(){
    ws.send('Hello WebSocket');
  }

  $(function(){
    $('#send').click(function(){
      var address = $('#address').val();
      var message = $('#message').val();
      var data = address + '-' + message;
      ws.send(data);
    });

    $('#message').keypress(function(e){
      if ( e.which == 13 ) {
        var address = $('#address').val();
        var message = $('#message').val();
        var data = address + '-' + message;
        ws.send(data);
        return false;
      }
    });
  });

});
