function loadGameData() {
  var gameID = getGameID();
  const params = new URLSearchParams();
  params.append('gameID', gameID)
  var request = new Request('/load-gamepage-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    printMapName(data);
    printMapDescription(data);
    printMapImage(data);
    printMapRating(data);
    printMapCreator(data);
    printPlayGameButton(data);
    printRateMapButton(data);
    printMapDifficulty(data);
    printMapComments(data);
  });
}

function printMapName(data) {
  var mapName = document.getElementById('map-name');
  mapName.innerHTML = data.gameName;
}

function printMapDescription(data) {
  var mapDescription = document.getElementById('map-description');
  mapDescription.innerHTML = data.gameDescription;
}

function printMapImage(data) {
  var mapImage = document.getElementById('map-image');
  mapImage.innerHTML = data.stageLocations;
}

function printMapRating(data) {
  var mapRating = document.getElementById('map-rating');
  mapRating.innerHTML = data.stars;
}

function printMapCreator(data) {
  var mapCreator = document.getElementById('map-creator');
  mapCreator.innerHTML = data.creatorUsername;
}

function printPlayGameButton(data) {
  var playGameButton = document.getElementById('play-game');
  playGameButton.innerHTML = data.gameName;
}

function printRateMapButton(data) {
  var rateMapButton = document.getElementById('play-game');
  rateMapButton.innerHTML = data.gameName;
}

function printMapDifficulty(data) {
  var rateMapButton = document.getElementById('play-game');
  rateMapButton.innerHTML = data.difficulty;
}

function printMapComments(data) {
  var mapComments = document.getElementById('comments');
//   mapComments.innerHTML = data.gameName;
}
