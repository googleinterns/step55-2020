/** 
* Initalizes a map where there is an id of 'playMap'
*/
async function initMapToPlayGame() {
  const params = new URLSearchParams();
  params.append('gameID', 'demogameid')
  var request = new Request('/load-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then(async (data) => {
    if (data == null) {
      window.alert('The data for this game does not exist, failure to initialize game');
      window.location.replace('index.html');
      return;
    }
    if (data.stages == null || data.stages.length <= 0) {
      window.alert('There are no stages, failure to initialize game');
      window.location.replace('index.html');
      return;
    }
    var initStage =  await getStage(data.stages[0]);
    var startingLocation = {lat: initStage.startingLocation.latitude, lng: initStage.startingLocation.longitude};
    var map = new google.maps.Map(
      document.getElementById('playMap'), {
      center: startingLocation, 
      gestureHandling: 'greedy',
      streetViewControl: false
    });
    var stageHints = getHints(initStage);
    if (stageHints.length == null) {
      window.alert('Sorry there was an error retrieving the hints, failure to initialize game');
      window.location.replace('index.html');
      return;
    }

    stageHints.forEach(hint => 
      addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text, hint.hintNumber)
    );

    // gets the street view
    var panorama = map.getStreetView();
    // removes the option to exit streetview
    var panoramaOptions = {
      enableCloseButton:false
    };
    panorama.setOptions(panoramaOptions);
    panorama.setPosition(startingLocation);
    // puts the user in streetView
    panorama.setVisible(true);

    createGameInfoOnSideOfMap(data, initStage, panorama);
  });
}

/** 
* Creates the game game info that is on the side of the map on playGame.html
* @param {String} data is the JSON from the server ‘/load-game-data’ 
* @param {String} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {StreetViewPanorama} map the panorama of the map created in initMapPlayGame()
*/
function createGameInfoOnSideOfMap(data, stage, map) {
    var gameInfo = document.getElementById('game-info');
    
    var gameName = document.createElement('h1');
    gameName.innerHTML = data.gameName;
    gameName.className = 'center'
    gameInfo.appendChild(gameName);

    var gameStage = document.createElement('h2');
    gameStage.innerHTML = 'You are on stage:' + stage.stageNumber + '/' + data.stages.length;
    gameStage.className = 'center'
    gameInfo.appendChild(gameStage);

    var theWordHints = document.createElement('h3');
    theWordHints.innerHTML = 'Hints:';
    gameInfo.appendChild(theWordHints);

    var starterHint = document.createElement('h3');
    starterHint.innerHTML = 'Starter: ' + stage.startingHint;
    gameInfo.appendChild(starterHint);

    // This div is a container for where the hints will be placed on the site
    var hintsDiv = document.createElement('div');
    // This is the ordered list (or <ol> in HTML) for where the hints will be listed
    var hintsOl = document.createElement('ol');
    hintsOl.id = 'hints';

    getHints(stage).forEach(hint => 
      hintsOl.appendChild(createHintPlaceHolder(hint.hintNumber))
    );

    hintsDiv.appendChild(hintsOl);
    gameInfo.appendChild(hintsDiv);

    var enterKeyText = document.createElement('h2');
    enterKeyText.innerHTML = 'Please Enter The Key To Complete The Stage:';
    enterKeyText.className = 'center'
    gameInfo.appendChild(enterKeyText);

    var inputKeyBox = document.createElement('input');
    inputKeyBox.type = 'text';
    inputKeyBox.className = 'center';
    inputKeyBox.classList = 'input-text-color';
    inputKeyBox.id = 'key-input';
    inputKeyBox.style = 'width: 30%';

    // This checks if the user clicked enter in the key box
    inputKeyBox.addEventListener('keydown', function(e) {
      if (e.which == 13) {
        checkKey(data, stage, map);
      }
    });
    gameInfo.appendChild(inputKeyBox);

    var buttonToCheckKey = document.createElement('input');
    buttonToCheckKey.type = 'button';
    buttonToCheckKey.className = 'center';
    buttonToCheckKey.value = 'Submit';
    buttonToCheckKey.addEventListener('click', function() {
      checkKey(data, stage, map);
    });
    gameInfo.appendChild(buttonToCheckKey);
}

//TODO(smissak): TEST this so if there is more than one page and the input key is correct,
// it adds the new stage rather than redirecting to the after game page
/** 
* Checks if the key is the correct key for the current stage
* @param {String} data is the JSON from the server ‘/load-game-data’ 
* @param {String} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {StreetViewPanorama} map the panorama of the map created in initMapPlayGame()
*/
function checkKey(data, stage, map) {
  var keyInput = document.getElementById('key-input');
  var inputValue = keyInput.value;
  if (stage.key.toLowerCase() != inputValue.toLowerCase()) {
    window.alert('Wrong key, please try again!');
    return;
  }
  if (data.stages.length == stage.stageNumber) {
    window.location.replace('afterGame.html');
    return;
  }

  // This reloads the map and the game info on the side of the map with the next stage data
  var nextStageNumber = stage.stageNumber + 1;
  var nextStage = getStage(data.stages[nextStageNumber]);
  var startingLocation = {lat: nextStage.startingLocation.latitude, lng: nextStage.startingLocation.longitude};
  map.setPosition(startingLocation);
  document.getElementById('game-info').innerHTML = '';
  createGameInfoOnSideOfMap(data, stage, map);
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
    icon: 'images/marker_exclamation_point.png'
  });

  marker.addListener('click', function() {
    addHint(hint, hintNum)
  });
}

function addHint(hint, hintNum) {
  var hintsWithNum = document.getElementById(hintNum);
  hintsWithNum.innerText = hint;
}
