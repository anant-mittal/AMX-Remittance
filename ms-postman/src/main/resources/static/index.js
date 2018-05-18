async function init() {

  const registration = await navigator.serviceWorker.register('/sw.js');
	
  firebase.initializeApp({
    messagingSenderId: '770916390748'
  });
  const messaging = firebase.messaging();
  
  messaging.useServiceWorker(registration);	
  
  try {
    await messaging.requestPermission();
    $(function() {
    		$(".subscribe-notifications-btn")
    		.attr("disabled", "disabled")
    		.html("Notifications enabled for browser");
    })
  } catch (e) {
    console.log('Unable to get permission', e);
    return;
  }

  navigator.serviceWorker.addEventListener('message', event => {
	  console.log("message=====",event);
    if (event.data === 'newData') {
      console.log("new Data");
    }
  });

  const currentToken = await messaging.getToken();
   fetch('/postman/subscribe/kwt-all?token='+currentToken, { method: 'post'});
  

  messaging.onTokenRefresh(async () => {
    console.log('token refreshed');
    const newToken = await messaging.getToken();
    fetch('/postman/subscribe/kwt-all?token='+newToken, { method: 'post'});
  });
  
  messaging.onMessage(function(payload) {
	console.log('Message received. ', payload);
  });
}


function sendNotification(title, message){
	title = title || "Notification";
	message = message || "Default message"
	fetch('/postman/push/notify', {
		method: 'post',
		headers: {
	        'Accept': 'application/json, text/plain, */*',
	        'Content-Type': 'application/json'
	    },
		body: JSON.stringify({
		  "image": "string",
		  "lang": "AB",
		  "lines": [
		    message
		  ],
		  "message": message,
		  "model": {},
		  "result": {},
		  "subject": title,
		  "template": "CONTACT_US",
		  "to": [
		    "/topics/kwt-all"
		  ]
		})
	})
}


$( function() {
	fetch("/postman/list/nations",{
		method: 'post'
	}).then(function(resp){
		resp.json().then(function(countries){
			console.log(countries);
			var countryOpts = countries.map(function(country){
				return {
					id: country,
					text: country
				}
			});
			
			console.log(countryOpts);
			$('.country-select').select2({data: countryOpts}).val("ALL_COUNTRIES").trigger("change");
		})
	})
	
	
	fetch("/postman/list/tenant",{
		method: 'post'
	}).then(function(resp){
		resp.json().then(function(tenants){
			var tenantOpts = tenants.map(function(tenant){
				return {
					id: tenant,
					text: tenant
				}
			});
			$('.env-select').select2({
				data: [{
					id: "KWT",
					text: "KWT"
				}, {
					id:"BHR",
					text: "BHR"
				}]
			});
		})
	})
	
	$('.send-notification-btn').on('click', function() {
//		sendNotification(null, $(".notif-msg").val());
		var nationality = $(".country-select").val();
		var tenant = $(".env-select").val();
		var title = $(".notif-title").val() || "Default Title";
		var message = $(".notif-msg").val() || "Default Message";
		fetch(`/postman/notify/nationality?tenant=${tenant}&nationality=${nationality}&title=${title}&message=${message}`, {
			method: 'post',
			headers: {
		        'Accept': 'application/json, text/plain, */*',
		        'Content-Type': 'application/json'
		    }
		})
	})
	
	$(".send-notifications-kwt-all").on('click', function(){
		var title = $(".notif-title").val() || "Default Title";
		var message = $(".notif-msg").val() || "Default Message";
		sendNotification(title, message);
	})
});
