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
      gestureHandling: 'greedy',
      streetViewControl: false
    });

    getHints(stage1).forEach(hint => 
      addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text)
    );

    var panorama = map.getStreetView();
    panorama.setPosition(startingLocation);
    panorama.setPov(/** @type {google.maps.StreetViewPov} */({
      heading: 265,
      pitch: 0
    }));
    panorama.setVisible(true);

    var gameInfo = document.getElementById('game-info');
    
    var gameName = document.createElement('h1');
    gameName.innerHTML = data.gameName;
    gameName.className = 'center'
    gameInfo.appendChild(gameName);
    
    
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

function addHintMarker(map, latLng, hint) {
  console.log(latLng);
  
  var infowindow = new google.maps.InfoWindow({
    content: hint
  });

  var marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: "http://maps.google.com/mapfiles/ms/icons/green-dot.png"
  });

  marker.addListener('click', function() {
    infowindow.open(map, marker);
    console.log(hint);
  });
}