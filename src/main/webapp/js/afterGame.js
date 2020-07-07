function loadGameData() {
  const urlParams = new URLSearchParams(window.location.search)
  var gameID = urlParams.get('gameID');

  var fetchParams = new URLSearchParams();
  fetchParams.append('gameID', gameID);
  var request = new Request('/load-game-data', {method: 'POST', body: fetchParams});
  fetch(request).then(response => response.json()).then(async (data) => {
    var gameTitle = document.getElementById('game-title');
    gameTitle.innerHTML = '<h1>' + data.gameName + '</h1>';

    var difficulty = document.getElementById('difficulty');
  });
}