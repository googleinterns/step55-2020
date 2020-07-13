/**
* Adds the user's username to the input box when the page is loaded
*/
async function getUserName() {
  await fetch('/load-authentication-data').then(response => response.json()).then(async (data) => {
    if (!data.loggedIn) {
      window.location.replace(data.loginUrl);
    }
  });  
  fetch('/load-currentuser-data').then(response => response.json()).then(async (data) => {
    document.getElementById('userName').value = data.username;
  });
}