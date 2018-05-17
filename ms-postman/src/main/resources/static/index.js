//async function init() {
//
//  const registration = await navigator.serviceWorker.register('/sw.js');
//	
//  firebase.initializeApp({
//    messagingSenderId: '770916390748'
//  });
//  const messaging = firebase.messaging();
//  
//  messaging.usePublicVapidKey("BNlB9TOch3VaJDzTId1LMZUFvQ7xmaCCu6tbu9aaYP5zqTM9pak3Jao_fBn6iJSiglljuj2jnQ_wNB1tABxqR2A");
//  
//  messaging.useServiceWorker(registration);	
//  
//  try {
//    await messaging.requestPermission();
//  } catch (e) {
//    console.log('Unable to get permission', e);
//    return;
//  }
//
//  navigator.serviceWorker.addEventListener('message', event => {
//	  console.log("message=====",event);
//    if (event.data === 'newData') {
//      showData();
//    }
//  });
//
//  const currentToken = await messaging.getToken();
//  // fetch('/register', { method: 'post', body: currentToken });
//  showData();
//
//  messaging.onTokenRefresh(async () => {
//    console.log('token refreshed');
//    const newToken = await messaging.getToken();
//    // fetch('/register', { method: 'post', body: currentToken });
//  });
//  
//  messaging.onMessage(function(payload) {
//	console.log('Message received. ', payload);
//  });
//}
//
//async function showData() {
//  const db = await getDb();
//  const tx = db.transaction('jokes', 'readonly');
//  const store = tx.objectStore('jokes');
//  store.getAll().onsuccess = e => showJokes(e.target.result);
//}
//
//function showJokes(jokes) {
//  const table = document.getElementById('outTable');
//
//  jokes.sort((a, b) => parseInt(b.ts) - parseInt(a.ts));
//  const html = [];
//  jokes.forEach(j => {
//    const date = new Date(parseInt(j.ts));
//    html.push(`<div><div class="header">${date.toISOString()} ${j.id} (${j.seq})</div><div class="joke">${j.joke}</div></div>`);
//  });
//  table.innerHTML = html.join('');
//}
//
//async function getDb() {
//  if (this.db) {
//    return Promise.resolve(this.db);
//  }
//  return new Promise(resolve => {
//    const openRequest = indexedDB.open("Chuck", 1);
//
//    openRequest.onupgradeneeded = event => {
//      const db = event.target.result;
//      db.createObjectStore('jokes', { keyPath: 'id' });
//    };
//
//    openRequest.onsuccess = event => {
//      this.db = event.target.result;
//      resolve(this.db);
//    }
//  });
//}
//
//init();
//
//








firebase.initializeApp({
    messagingSenderId: '770916390748'
  });


// [START get_messaging_object]
// Retrieve Firebase Messaging object.
const messaging = firebase.messaging();
// [END get_messaging_object]
// [START set_public_vapid_key]
// Add the public key generated from the console here.
messaging.usePublicVapidKey('BNlB9TOch3VaJDzTId1LMZUFvQ7xmaCCu6tbu9aaYP5zqTM9pak3Jao_fBn6iJSiglljuj2jnQ_wNB1tABxqR2A');
// [END set_public_vapid_key]
// IDs of divs that display Instance ID token UI or request permission UI.
const tokenDivId = 'token_div';
const permissionDivId = 'permission_div';
// [START refresh_token]
// Callback fired if Instance ID token is updated.
messaging.onTokenRefresh(function() {
  messaging.getToken().then(function(refreshedToken) {
    console.log('Token refreshed.');
    // Indicate that the new Instance ID token has not yet been sent to the
    // app server.
    setTokenSentToServer(false);
    // Send Instance ID token to app server.
    sendTokenToServer(refreshedToken);
    // [START_EXCLUDE]
    // Display new Instance ID token and clear UI of all previous messages.
    resetUI();
    // [END_EXCLUDE]
  }).catch(function(err) {
    console.log('Unable to retrieve refreshed token ', err);
    showToken('Unable to retrieve refreshed token ', err);
  });
});
// [END refresh_token]
// [START receive_message]
// Handle incoming messages. Called when:
// - a message is received while the app has focus
// - the user clicks on an app notification created by a service worker
//   `messaging.setBackgroundMessageHandler` handler.
messaging.onMessage(function(payload) {
  console.log('Message received. ', payload);
  // [START_EXCLUDE]
  // Update the UI to include the received message.
  appendMessage(payload);
  // [END_EXCLUDE]
});
// [END receive_message]
function resetUI() {
  clearMessages();
  showToken('loading...');
  // [START get_token]
  // Get Instance ID token. Initially this makes a network call, once retrieved
  // subsequent calls to getToken will return from cache.
  messaging.getToken().then(function(currentToken) {
    if (currentToken) {
      sendTokenToServer(currentToken);
      updateUIForPushEnabled(currentToken);
    } else {
      // Show permission request.
      console.log('No Instance ID token available. Request permission to generate one.');
      // Show permission UI.
      updateUIForPushPermissionRequired();
      setTokenSentToServer(false);
    }
  }).catch(function(err) {
    console.log('An error occurred while retrieving token. ', err);
    showToken('Error retrieving Instance ID token. ', err);
    setTokenSentToServer(false);
  });
  // [END get_token]
}
function showToken(currentToken) {
  // Show token in console and UI.
  var tokenElement = document.querySelector('#token');
  tokenElement.textContent = currentToken;
}
// Send the Instance ID token your application server, so that it can:
// - send messages back to this app
// - subscribe/unsubscribe the token from topics
function sendTokenToServer(currentToken) {
  if (!isTokenSentToServer()) {
    console.log('Sending token to server...');
    // TODO(developer): Send the current token to your server.
    setTokenSentToServer(true);
  } else {
    console.log('Token already sent to server so won\'t send it again ' +
        'unless it changes');
  }
}
function isTokenSentToServer() {
  return window.localStorage.getItem('sentToServer') === 1;
}
function setTokenSentToServer(sent) {
  window.localStorage.setItem('sentToServer', sent ? 1 : 0);
}
function showHideDiv(divId, show) {
  const div = document.querySelector('#' + divId);
  if (show) {
    div.style = 'display: visible';
  } else {
    div.style = 'display: none';
  }
}
function requestPermission() {
  console.log('Requesting permission...');
  // [START request_permission]
  messaging.requestPermission().then(function() {
    console.log('Notification permission granted.');
    // TODO(developer): Retrieve an Instance ID token for use with FCM.
    // [START_EXCLUDE]
    // In many cases once an app has been granted notification permission, it
    // should update its UI reflecting this.
    resetUI();
    // [END_EXCLUDE]
  }).catch(function(err) {
    console.log('Unable to get permission to notify.', err);
  });
  // [END request_permission]
}
function deleteToken() {
  // Delete Instance ID token.
  // [START delete_token]
  messaging.getToken().then(function(currentToken) {
    messaging.deleteToken(currentToken).then(function() {
      console.log('Token deleted.');
      setTokenSentToServer(false);
      // [START_EXCLUDE]
      // Once token is deleted update UI.
      resetUI();
      // [END_EXCLUDE]
    }).catch(function(err) {
      console.log('Unable to delete token. ', err);
    });
    // [END delete_token]
  }).catch(function(err) {
    console.log('Error retrieving Instance ID token. ', err);
    showToken('Error retrieving Instance ID token. ', err);
  });
}
// Add a message to the messages element.
function appendMessage(payload) {
  const messagesElement = document.querySelector('#messages');
  const dataHeaderELement = document.createElement('h5');
  const dataElement = document.createElement('pre');
  dataElement.style = 'overflow-x:hidden;';
  dataHeaderELement.textContent = 'Received message:';
  dataElement.textContent = JSON.stringify(payload, null, 2);
  messagesElement.appendChild(dataHeaderELement);
  messagesElement.appendChild(dataElement);
}
// Clear the messages element of all children.
function clearMessages() {
  const messagesElement = document.querySelector('#messages');
  while (messagesElement.hasChildNodes()) {
    messagesElement.removeChild(messagesElement.lastChild);
  }
}
function updateUIForPushEnabled(currentToken) {
  showHideDiv(tokenDivId, true);
  showHideDiv(permissionDivId, false);
  showToken(currentToken);
}
function updateUIForPushPermissionRequired() {
  showHideDiv(tokenDivId, false);
  showHideDiv(permissionDivId, true);
}
resetUI();
