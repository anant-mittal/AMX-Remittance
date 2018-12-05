var stompClient = null;

var tunnelClient = (function(win) {

	function connect() {
		var dfd = jQuery.Deferred();
		var socket = new SockJS('/offsite/gs-guide-websocket');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			dfd.resolve(frame);
		});
		return dfd.promise();
	}

	return {
		$connectd : null,
		onConnect : function() {
			if (!this.$connectd) {
				this.$connectd = connect();
			}
			return this.$connectd;
		},
		on : function subscribe(topic, fun) {
			return this.onConnect().then(function() {
				return stompClient.subscribe(topic, function(greeting) {
					fun(JSON.parse(greeting.body), topic, greeting);
				});
			});
		},
		disconnect : function disconnect() {
			if (stompClient !== null) {
				stompClient.disconnect();
			}
			setConnected(false);
			console.log("Disconnected");
		},
		send : function send(topic, msg) {
			this.onConnect().then(function() {
				stompClient.send("topic/" + topic, {}, JSON.stringify(msg));
			});
		}
	};
})(this);