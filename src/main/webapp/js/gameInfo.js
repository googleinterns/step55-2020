function loadGameData() {
  var gameID = getGameID();
  const params = new URLSearchParams();
  params.append('gameID', gameID)
  var request = new Request('/load-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    printMapName(data);
    printMapImage(data);
    printMapRating(data);
    printMapCreator(data);
    printPlayGameButton(data);
    printPlayGameButton(data);
    printRateMapButton(data)
    // printMapComments(data);
  });
}

function printMapName(data) {
  var mapName = document.getElementById('map-name');
  mapName.innerHTML = data.gameName;
}

function printMapImage(data) {
  var mapImage = document.getElementById('map-image');
//   map.innerHTML = data.gameName;
}

function printMapRating(data) {
  var mapRating = document.getElementById('map-rating');
  mapRating.innerHTML = data.gameName;
}

function printMapCreator(data) {
  var mapCreator = document.getElementById('map-creator');
  mapCreator.innerHTML = data.gameName;
}

function printPlayGameButton(data) {
  var playGameButton = document.getElementById('play-game');
  playGameButton.innerHTML = data.gameName;
}

function printRateMapButton(data) {
  var rateMapButton = document.getElementById('play-game');
  rateMapButton.innerHTML = data.gameName;
}

function printMapComments(data) {
  var mapComments = document.getElementById('comments');
//   mapComments.innerHTML = data.gameName;
}
