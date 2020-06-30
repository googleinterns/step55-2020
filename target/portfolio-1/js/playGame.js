async function initMapPlayGame() {
  const params = new URLSearchParams();
  params.append('gameID', 'demogameid')
  var request = new Request('/load-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then(async (data) => {
    var stage1 =  await getStage(data.stages[0]);
    var startingLocation = {lat: stage1.startingLocation.latitude, lng: stage1.startingLocation.longitude};
    var map = new google.maps.Map(
      document.getElementById('playMap'), {
      center: startingLocation, 
      gestureHandling: 'greedy',
      streetViewControl: false
    });

    getHints(stage1).forEach(hint => 
      addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text, hint.hintNumber)
    );

    var panorama = map.getStreetView();
    var panoramaOptions = {
      enableCloseButton:false
    };
    panorama.setOptions(panoramaOptions);
    panorama.setPosition(startingLocation);
    panorama.setVisible(true);

    var gameInfo = document.getElementById('game-info');
    
    var gameName = document.createElement('h1');
    gameName.innerHTML = data.gameName;
    gameName.className = 'center'
    gameInfo.appendChild(gameName);

    var gameStage = document.createElement('h2');
    gameStage.innerHTML = 'You are on stage:' + stage1.stageNumber + '/' + data.stages.length;
    gameStage.className = 'center'
    gameInfo.appendChild(gameStage);

    var theWordHints = document.createElement('h3');
    theWordHints.innerHTML = 'Hints:';
    gameInfo.appendChild(theWordHints);

    var starterHint = document.createElement('h3');
    starterHint.innerHTML = 'Starter: ' + stage1.startingHint;
    gameInfo.appendChild(starterHint);

    var hintsDiv = document.createElement('div');
    var hintsOl = document.createElement('ol');
    hintsOl.id = 'hints';

    getHints(stage1).forEach(hint => 
      hintsOl.appendChild(createHintPlaceHolder(hint.hintNumber))
    );

    hintsDiv.appendChild(hintsOl);
    gameInfo.appendChild(hintsDiv);
  });
}

function createHintPlaceHolder(hintNum) {
    var hintLi = document.createElement('li');
    hintLi.id = hintNum;
    return hintLi;
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

function addHintMarker(map, latLng, hint, hintNum) {  
  var infowindow = new google.maps.InfoWindow({
    content: hint
  });

  var marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: "images/marker_exclamation_point.png"
  });

  marker.addListener('click', function() {
    addHint(hint, hintNum)
  });
}

function addHint(hint, hintNum) {
  var hintsWithNum= document.getElementById(hintNum);
  hintsWithNum.innerText = hint;
}
