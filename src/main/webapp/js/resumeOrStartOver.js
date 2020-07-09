/** 
* Checks if the user has saved progress, if not it goes to play the game otherwise asks the user if they would like to continue or restart
*/
function checkIfUserHasSavedProgress() {
  const urlParams = new URLSearchParams(window.location.search)
  var gameID = urlParams.get('gameID');

  var fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);
  var request = new Request('/has-singleplayerprogress-data', {method: 'POST', body: fetchParams});
  fetch(request).then(response => response.json()).then(async (data) => {
    console.log(data);
    if (data == 0) {
      window.location.replace('playGame.html?gameID=' + gameID);
    } 
  });
}

/** 
* Resets the user progress in the server
*/
function restartGame() {
  const urlParams = new URLSearchParams(window.location.search)
  var gameID = urlParams.get('gameID');

  var fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);
  var request = new Request('/load-singleplayerprogress-data', {method: 'POST', body: fetchParams});
  fetch(request);
  window.location.replace('playGame.html?gameID=' + gameID);
}

/** 
* Does not reset the user progress in the server and continues to play the game
*/
function continueGame() {
  const urlParams = new URLSearchParams(window.location.search)
  var gameID = urlParams.get('gameID');

  window.location.replace('playGame.html?gameID=' + gameID);
}
