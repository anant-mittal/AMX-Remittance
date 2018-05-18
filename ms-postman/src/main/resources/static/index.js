async function init() {

  const registration = await navigator.serviceWorker.register('/sw1.js');
	
  firebase.initializeApp({
    messagingSenderId: '770916390748'
  });
  const messaging = firebase.messaging();
  
  messaging.useServiceWorker(registration);	
  
  try {
    await messaging.requestPermission();
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

init();



