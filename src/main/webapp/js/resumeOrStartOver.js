/** 
* Checks if the user has saved progress, if not it goes to play the game otherwise asks the user if they would like to continue or restart
*/
function checkIfUserHasSavedProgress() {
  const urlParams = new URLSearchParams(window.location.search);
  let gameID = urlParams.get('gameID');

  let fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);

  let tokenEmailDict = tokenAndEmail();
  fetchParams.append('email', tokenEmailDict['email']);
  fetchParams.append('idToken', tokenEmailDict['token']);

  let request = new Request('/has-singleplayerprogress-data', {method: 'POST', body: fetchParams});
  fetch(request).then(response => response.json()).then(async (data) => {
    if (data == 0) {
      window.location.replace('playGame.html?gameID=' + gameID);
    } 
  });
}

/** 
* Resets the user progress in the server
*/
function restartGame() {
  if (!isSignedIn()) {
    continueGame();
    return;
  }
  const urlParams = new URLSearchParams(window.location.search)
  let gameID = urlParams.get('gameID');

  let fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);
  let tokenEmailDict = tokenAndEmail();
  fetchParams.append('email', tokenEmailDict['email']);
  fetchParams.append('idToken', tokenEmailDict['token']);

  let request = new Request('/reset-singleplayerprogress-data', {method: 'POST', body: fetchParams});
  fetch(request);
  window.location.replace('playGame.html?gameID=' + gameID);
}

/** 
* Does not reset the user progress in the server and continues to play the game
*/
function continueGame() {
  const urlParams = new URLSearchParams(window.location.search)
  let gameID = urlParams.get('gameID');

  window.location.replace('playGame.html?gameID=' + gameID);
}
