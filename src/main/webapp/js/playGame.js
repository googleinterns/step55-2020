async function initMapPlayGame() {
  const params = new URLSearchParams();
  params.append('gameID', 'demogameid')
  var request = new Request('/load-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then(async (data) => {
    var stage1 =  await getStage(data.stages[0]);
    var startingLocation = {lat: stage1.startingLocation.latitude, lng: stage1.startingLocation.longitude};
    var map = new google.maps.Map(
      document.getElementById('playMap'), {
      zoom: 1, 
      center: startingLocation, 
      mapTypeId: 'hybrid',
      gestureHandling: 'greedy'
    });

    var panorama = new google.maps.StreetViewPanorama(
      document.getElementById('playMap'), {
      position: startingLocation
    });
    map.setStreetView(panorama);

    var gameInfo = document.getElementById('game-info');
    
    var gameName = document.createElement('h1');
    gameName.innerHTML = data.gameName;
    gameName.className = 'center'
    gameInfo.appendChild(gameName);
    
    console.log(stage1);
  });
}

async function getStage(stageID) {
  var currStage;
  const params = new URLSearchParams();
  params.append('stageID', stageID)
  var request = new Request('/load-stage-data', {method: 'POST', body: params});
  await fetch(request).then(response => response.json()).then((data) => {
    currStage = data;
  });
  return currStage;
}

function getHints(stage) {
  return stage.hints;
}

function addHintMarker(map, latLng) {
  var marker = new google.maps.Marker({
    position: latLng,
    map: map
  });
}
