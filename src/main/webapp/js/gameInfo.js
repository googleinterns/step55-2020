/**
* Loads the game data to the game info page
*/
function loadGameData() {
  var gameID = getGameID();
  const params = new URLSearchParams();
  params.append('gameID', gameID)
  var request = new Request('/load-gamepage-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    printMapNameAndDifficulty(data);
    printMapDescription(data);
    printMapImage(data);
    printMapRating(data);
    printMapCreator(data);
    printPlayGameButton(data, gameID);
    // printRateMapButton(data);
    // printMapComments(data);
  });
}

/**
* Adds the game name and difficulty where the div with id 'map-name' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapNameAndDifficulty(data) {
  var mapName = document.getElementById('map-name');
  var difficultyClass = 'red-text';
  var difficulty = '[Hard]'
  if (data.difficulty == 1) {
    difficultyClass = 'green-text';
    difficulty = '[Easy]';
  } else if (data.difficulty == 2) {
    difficultyClass = 'orange-text';
    difficulty = '[Medium]';
  }
  mapName.innerHTML = '<h1>' + data.gameName + ' <i class=' + difficultyClass +'>' + difficulty + '</i></h1>';
}

/**
* Adds the game description where the div with id 'map-description' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapDescription(data) {
  var mapDescription = document.getElementById('map-description');
  mapDescription.innerHTML = '<h4>Game Description: </h4>';
  mapDescription.innerHTML += '<h5>' + data.gameDescription + '</h5>';
}

/**
* Adds the game image where the div with id 'map-image' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapImage(data) {
  var mapImage = document.getElementById('map-image');
  mapImage.append(createStaticMap(data.stageLocations, 500, data.gameID));
}

/**
* Adds the game rating where the div with id 'map-rating' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapRating(data) {
  var mapRating = document.getElementById('map-rating');
  mapRating.innerHTML = getStarRating(data.stars).replace(/md-18/g, 'large');
}

/**
* Adds the game creator where the div with id 'map-creator' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapCreator(data) {
  var mapCreator = document.getElementById('map-creator');
  mapCreator.innerHTML = '<h4>Created by: ' + data.creatorUsername + '</h4>';
}

/**
* Adds a button to play the game where the div with id 'play-game' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
async function printPlayGameButton(data, gameID) {
  var playGameDiv = document.getElementById('play-game');
  var playGameButton = document.createElement('input');
  playGameButton.type = 'button';
  playGameButton.value = 'Play Game!';  
  playGameButton.id = 'play-game-button'; 
  playGameButton.addEventListener("click", async function(){
    fetch('/load-singleplayerprogress-data?gameID=' + gameID).then(response => response.json()).then(async (data) => {
      if (data == null) {
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
  var rateMapButton = document.getElementById('rate-map');
//   rateMapButton.innerHTML = data.gameName;
}

/**
* Adds the comments for the map game where the div with id 'comments' is
* @param {string} data is the JSON from the server ‘/load-gamepage-data’ 
*/
function printMapComments(data) {
  var mapComments = document.getElementById('comments');
//   mapComments.innerHTML = data.gameName;
}
