var stompClient = null;

function setConnected(connected) {
	
}

function connect() {
	var socket = new SockJS('/offsite/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/greetings', function(greeting) {
			showGreeting(JSON.parse(greeting.body));
		});
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	stompClient.send("/app/hello", {}, JSON.stringify({
		'name' : "LT"
	}));
}

function showGreeting(reply) {
	console.log("Reply===", reply);
}