/**
* Adds the user's username to the input box when the page is loaded
*/
async function getUserName() {
  var usernameBox = document.getElementById('userName');
  var timer; // current ms since last keyup
  var waitTime = 500; // wait 500ms after last keyup before we ask the server if this username is taken

  await fetch('/load-authentication-data').then(response => response.json()).then(async (data) => {
    if (!data.loggedIn) {
      window.location.replace(data.loginUrl);
    }
  });  
  fetch('/load-currentuser-data').then(response => response.json()).then(async (data) => {
    usernameBox.value = data.username;
  });

  usernameBox.addEventListener("keyup", () => {
    clearTimeout(timer);
    timer = setTimeout(displayAvailability, waitTime);
  });
}

async function isAvailable(desiredUsername) {
  const params = new URLSearchParams();
  params.append('username', desiredUsername);

  let result;
  await fetch('/username-available-data', {method: 'post', body: params}).then(response => response.json()).then(taken => {
    result = !taken;
  });
  return result;
}

async function displayAvailability() {
  desiredUsername = document.getElementById('userName').value;
  available = await isAvailable(desiredUsername);

  var availabilityBox = document.getElementById('username-availability-message');
  if(available) {
      availabilityBox.innerText = ('Available');
      availabilityBox.className = 'green-text';
  } else {
      availabilityBox.innerText = ('Username is taken');
      availabilityBox.className = 'red-text';
  }
}
