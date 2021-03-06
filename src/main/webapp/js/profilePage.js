var originalUsername;

/**
* Sets up the functionality of the username input box and the submit button.
*/
async function loadProfilePage() {
  let usernameBox = document.getElementById('userName');
  let timer; // current ms since last keyup
  let waitTime = 500; // wait 500ms after last keyup before we ask the server if this username is taken

  if (!isSignedIn()) {
    alert('You must be signed in to access this page')
    window.location = ('index.html');
    return;
  }

  let tokenEmailDict = tokenAndEmail();
  
  const params = new URLSearchParams();
  params.append('email', tokenEmailDict['email']);
  params.append('idToken', tokenEmailDict['token']);
  let request = new Request('/load-currentuser-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then(async (data) => {
    if (data == null) {
      onSignIn();
      loadProfilePage()
      return;
    }
    originalUsername = data.username;
    usernameBox.value = data.username;
  });

  usernameBox.addEventListener("keyup", () => {
    if(event.keyCode === 13) { // enter button
      document.getElementById('submit-username-button').click();
    } else {
      clearTimeout(timer);
      timer = setTimeout(displayAvailability, waitTime);
    }
  });
}

/**
* Checks if there is already a user with this username.
* @param {string} desiredUsername the username to be checked for availability.
* @return {boolean} whether or not this username is already being used.
*/
async function isTaken(desiredUsername) {
  const params = new URLSearchParams();
  params.append('username', desiredUsername);

  let result;
  await fetch('/username-available-data', {method: 'post', body: params}).then(response => response.json()).then(taken => {
    result = taken;
  });
  return result;
}

/**
* Runs a series of checks (length, characters used, availability) to see if this username is valid.
* @param {string} desiredUsername the username to be checked.
* @return {string} 'Available' if this username is good; otherwise a string describing the issue.
*/
async function getAvailabilityText(lowerCaseUserName) {
  if (lowerCaseUserName.length == 0) {
    return 'Username must be at least 1 character';
  } else if (lowerCaseUserName.length > 20) {
    return 'Username cannot be more than 20 characters';
  }
  let pattern = new RegExp(/^[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz._0123456789]*$/);
  if (!pattern.test(lowerCaseUserName)) {
    return 'Only letters, digits, underscores, and periods are allowed';
  } else if (originalUsername.toLowerCase() == lowerCaseUserName) {
    return 'Username currently in use by you';
  } else if (await isTaken(lowerCaseUserName)) {
    return 'Username is already taken';
  }
  return 'Available';
}

/**
* Displays the result of getAvailabilityText in the div element under the username input box.
*/
async function displayAvailability() {
  let desiredUsername = document.getElementById('userName').value;
  let lowerCaseUserName = desiredUsername.toLowerCase();
  let availabilityBox = document.getElementById('username-availability-message');
  availabilityBox.innerText = await getAvailabilityText(lowerCaseUserName);
  if(availabilityBox.innerText == 'Available' || availabilityBox.innerText == 'Username Currently in use by you') {
    availabilityBox.className = 'green-text';
  } else {
    availabilityBox.className = 'red-text';
  }
}

async function submitUsername() {
  if (!isSignedIn()) {
    alert('Please sign back in');
    return;
  }
  
  let desiredUsername = document.getElementById('userName').value;
  let lowerCaseUserName = desiredUsername.toLowerCase();
  let availabilityBox = document.getElementById('username-availability-message');
  await displayAvailability();
  if(availabilityBox.innerText == 'Available') {
    const params = new URLSearchParams();
    let tokenEmailDict = tokenAndEmail();

    params.append('userName', desiredUsername);
    params.append('lowerCaseUserName', lowerCaseUserName);
    params.append('email', tokenEmailDict['email']);
    params.append('idToken', tokenEmailDict['token']);
    
    await fetch('/create-username-data', {method: 'POST', body: params});
    window.location = '/profilePage.html';
  } else if (desiredUsername == originalUsername) {
    return;
  } else {
    alert(availabilityBox.innerText);
  }
}
