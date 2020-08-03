/** 
* Add's the game name to the page
*/
function loadGameName() {
  const urlParams = new URLSearchParams(window.location.search);
  let gameID = urlParams.get('gameID');

  if (gameID == null) {
    alert("Failure to load game");
    window.location ("index.html");
    return;
  }

  let fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);
  let request = new Request('/load-game-data', {method: 'POST', body: fetchParams});
  fetch(request).then(response => response.json()).then(async (data) => {
    if (data == null) {
      alert("Failure to load game, this is not a valid game");
      window.location = ("index.html");
      return;
    }
    let gameTitle = document.getElementById('game-title');
    gameTitle.innerHTML = '<h1>' + data.gameName + '</h1>';    
  });
  window.addEventListener("beforeunload", function(event) { 
      sendDataToServer(""); 
      delete event['returnValue'];
  });
}

/** 
* Creates a row of the number of stars the user clicked on and places then at className 'stars'
* @param {int} numOfStars the number of stars the user clicked on
*/
function stars(numOfStars) {  
  let startOfStar = '<i class="large material-icons" onclick="stars(';
  let fullStar = ')">star</i>';
  let halfStar = ')">star_half</i>';
  let emptyStar = ')">star_border</i>';

  let count = 0;
  let fiveStars = '';
  for (let i = 0; i < 5; i++) {
    if(numOfStars >= 1.0) {
       count++;
       fiveStars += startOfStar + (i+1) + fullStar;
    } else if (numOfStars <= 0) {
      fiveStars += startOfStar + (i+1) + emptyStar;
    } else {
      fiveStars += startOfStar + (i+1) + halfStar;
    }
    numOfStars -= 1.0;
  }

  let starsDiv = document.getElementsByClassName('stars')[0];
  starsDiv.id = count;
  starsDiv.innerHTML = fiveStars;
}

/** 
* Gets the data from the page and sends it to the server
* @param {string} nextAction tells the function whether to play again or go back to home
*/
function sendDataToServer(nextAction) { 
  let starsCount = document.getElementsByClassName('stars')[0].id;

  let difficultyButtons = document.getElementsByName('difficulty'); 
  let difficulty = 0;
  for(i = 0; i < difficultyButtons.length; i++) { 
    if(difficultyButtons[i].checked) { 
      if (difficultyButtons[i].value == 'Easy') {
        difficulty = 1;
      } else if (difficultyButtons[i].value == 'Medium') {
        difficulty = 2;
      } else if (difficultyButtons[i].value == 'Hard') {
        difficulty = 3;
      }
    } 
  } 

  const urlParams = new URLSearchParams(window.location.search);
  let gameID = urlParams.get('gameID');

  let fetchParams = new URLSearchParams();
  fetchParams.append('starVote', starsCount);
  fetchParams.append('difficultyVote', difficulty);
  fetchParams.append('gameID', gameID);
  let request = new Request('/update-feedback-data', {method: 'POST', body: fetchParams});
  if (nextAction == 'Play Again') {
    window.location = ('playGame.html?gameID=' + gameID);
    return;
  } else if (nextAction == 'Back To Home') {
    window.location = ('index.html');
  } else {
    fetch(request);
  }
}
