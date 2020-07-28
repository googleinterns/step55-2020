/**
* Loads the game data to the game info page
*/
function loadGameData() {
  let gameID = getGameID();
  const params = new URLSearchParams();
  params.append('gameID', gameID);
  let request = new Request('/load-gamepage-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    printMapName(data);
    printDifficulty(data);
    printMapCreator(data);
    printMapDescription(data);
    printMapImage(data);
    printMapRating(data);
    printPlayGameButton(gameID);
    // printRateMapButton(data);
    // printMapComments(data);
  });
}

/**
* Adds the game name and difficulty where the div with id 'map-name' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapName(data) {
  let mapName = document.getElementById('map-name');
  mapName.innerHTML = '<h3>' + data.gameName + '</h3>';
}

/**
* Adds the game name and difficulty where the div with id 'map-name' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printDifficulty(data) {
  let mapDifficulty = document.getElementById('map-difficulty');
  let difficultyClass = 'green-text';
  let difficulty = '[Easy]';
  if (Math.round(data.difficulty) == 2) {
    difficultyClass = 'orange-text';
    difficulty = '[Medium]';
  } else if (Math.round(data.difficulty )== 3) {
    difficultyClass = 'red-text';
    difficulty = '[Hard]';
  }  

  mapDifficulty.innerHTML = '<h5><i class=' + difficultyClass + '>' + difficulty + '</i></h5>';
}

/**
* Adds the game description where the div with id 'map-description' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapDescription(data) {
  let mapDescription = document.getElementById('map-description');
  mapDescription.innerHTML = '<p>Game Description: </p>';
  mapDescription.innerHTML += '<p class="max-width-half">' + data.gameDescription + '</p>';
  mapDescription.maxWidth = "50%";
}

/**
* Adds the game image where the div with id 'map-image' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapImage(data) {
  let mapImage = document.getElementById('map-image');
  let img = createStaticMap(data.stageLocations, 500, data.gameID);
  img.classList.remove('cursor-pointer');
  mapImage.append(img);
}

/**
* Adds the game rating where the div with id 'map-rating' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapRating(data) {
  let mapRating = document.getElementById('map-rating');
  mapRating.innerHTML = getStarRating(data.stars).replace(/md-18/g, 'md-36');
}

/**
* Adds the game creator where the div with id 'map-creator' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapCreator(data) {
  let mapCreator = document.getElementById('map-creator');
  mapCreator.innerHTML = '<h4>Created by: ' + data.creatorUsername + '</h4>';
}

/**
* Adds a button to play the game where the div with id 'play-game' is
* @param {string} gameID teh gameID of the current game
*/
async function printPlayGameButton(gameID) {
  let playGameDiv = document.getElementById('play-game');
  let playGameButton = document.createElement('input');
  playGameButton.type = 'button';
  playGameButton.value = 'Play Game!';  
  playGameButton.id = 'play-game-button'; 
  playGameButton.addEventListener("click", function(){
    if (!isSignedIn()) {
      window.location.replace('playGame.html?gameID=' + gameID);
      return;
    }
    const params = new URLSearchParams();
    let tokenEmailDict = tokenAndEmail();

    params.append('email', tokenEmailDict['email']);
    params.append('idToken', tokenEmailDict['token']);
    params.append('gameID', gameID);
    let request = new Request('/has-singleplayerprogress-data', {method: 'POST', body: params});
    fetch(request).then(response => response.json()).then(async (data) => {
      if (data == 0) {
        window.location.replace('playGame.html?gameID=' + gameID);
      } else {
        window.location.replace('resumeOrStartOver.html?gameID=' + gameID);
      }
    }); 
  }); 
  playGameDiv.append(playGameButton);
}

/**
* Adds a button to rate the game where the div with id 'rate-map' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printRateMapButton(data) {
  let rateMapButton = document.getElementById('rate-map');
//   rateMapButton.innerHTML = data.gameName;
}

/**
* Adds the comments for the map game where the div with id 'comments' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapComments(data) {
  let mapComments = document.getElementById('comments');
//   mapComments.innerHTML = data.gameName;
}
