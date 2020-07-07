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

/** 
* Creates a row of the number of stars the user clicked on
* @param {int} numOfStars the number of stars the user clicked on
*/
function stars(numOfStars) {  
  var fullStar = '<i class="large material-icons">star</i>';
  var halfStar = '<i class="large material-icons">star_half</i>';
  var emptyStar = '<i class="large material-icons">star_border</i>';

  var fiveStars = '';
  for (var i = 0; i < 5; i++) {
    if(numOfStars >= 1.0) {
       fiveStars += fullStar;
    } else if (numOfStars <= 0) {
      fiveStars += emptyStar;
    } else {
      fiveStars += halfStar;
    }
    numOfStars -= 1.0;
  }
  return fiveStars
}
