var stompClient = null;

var tunnelClient = (function(win) {
	var $connectd = null;
	var $dfd = null;
	function connect() {
		$dfd = $dfd || jQuery.Deferred();
		var socket = new SockJS('/offsite/stomp-tunnel');
		stompClient = Stomp.over(socket);
		stompClient.connect({
			user : "guest",
			token : guid()
		}, function(frame) {
			console.log('Connected: ', frame);
			stompClient.subscribe("/app/stomp/tunnel/meta" , function(greeting) {
				console.log("@SubscribeMapping",JSON.parse(greeting.body));
				$dfd.resolve(frame);
			});
		});
		return $dfd.promise();
	}
	function guid() {
		function s4() {
			return Math.floor((1 + Math.random()) * 0x10000).toString(16)
					.substring(1);
		}
		return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4()
				+ s4() + s4();
	}
	function onConnect() {
		if (!this.$connectd) {
			this.$connectd = connect();
		}
		return this.$connectd;
	}
	return {
		connect : function() {
			onConnect();
			return this;
		},
		on : function subscribe(topic, fun) {
			onConnect().then(function() {
				return stompClient.subscribe("/topic" + topic, function(greeting) {
						fun(JSON.parse(greeting.body), topic, greeting);
				});
			});
			return this;
		},
		disconnect : function disconnect() {
			if (stompClient !== null) {
				stompClient.disconnect();
			}
			setConnected(false);
			console.log("Disconnected");
			return this;
		},
		send : function send(topic, msg) {
			this.onConnect().then(function() {
				stompClient.send(topic, {}, JSON.stringify(msg));
			});
			return this;
		}
	};
})(this);