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
    var stageHints = initStage.hints;
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

    createGameInfoOnSideOfMap(data, initStage, panorama, map);
  });
}

/** 
* Creates the game game info that is on the side of the map on playGame.html
* @param {string} data is the JSON from the server ‘/load-game-data’ 
* @param {string} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {object} panorama the panorama of the map created in initMapPlayGame()
* @param {object} map is map created in initMapPlayGame()
*/
function createGameInfoOnSideOfMap(data, stage, panorama, map) {
    var gameInfo = document.getElementById('game-info');
    
    var gameName = document.createElement('h2');
    gameName.innerHTML = data.gameName;
    gameName.className = 'center'
    gameInfo.appendChild(gameName);

    var gameStage = document.createElement('h3');
    gameStage.innerHTML = 'You are on stage:' + stage.stageNumber + '/' + data.stages.length;
    gameStage.className = 'center'
    gameInfo.appendChild(gameStage);

    var theWordHints = document.createElement('h4');
    theWordHints.innerHTML = 'Hints:';
    gameInfo.appendChild(theWordHints);

    var starterHint = document.createElement('h4');
    starterHint.innerHTML = 'Starter: ' + stage.startingHint;
    gameInfo.appendChild(starterHint);

    // This div is a container for where the hints will be placed on the site
    var hintsDiv = document.createElement('div');
    // This is the ordered list (or <ol> in HTML) for where the hints will be listed
    var hintsOl = document.createElement('ol');
    hintsOl.id = 'hints';

    stage.hints.forEach(hint => 
      hintsOl.appendChild(createHintPlaceHolder(hint.hintNumber))
    );

    hintsDiv.appendChild(hintsOl);
    gameInfo.appendChild(hintsDiv);

    var keySpan = document.createElement('span');
    keySpan.setAttribute('id', 'keybox');
    var enterKeyText = document.createElement('h3');
    enterKeyText.innerHTML = 'Please enter key to continue:';
    enterKeyText.className = 'center'
    keySpan.appendChild(enterKeyText);

    var inputKeyBox = document.createElement('input');
    inputKeyBox.type = 'text';
    inputKeyBox.className = 'center';
    inputKeyBox.classList = 'input-text-color';
    inputKeyBox.id = 'key-input';

    // This checks if the user clicked enter in the key box
    inputKeyBox.addEventListener('keydown', function(e) {
      if (e.which == 13) {
        checkKey(data, stage, panorama, map);
      }
    });
    var inputBoxAndSubmitButton = document.createElement('div');
    inputBoxAndSubmitButton.setAttribute('id', 'inputBoxAndButton');
    inputBoxAndSubmitButton.appendChild(inputKeyBox);

    var buttonToCheckKey = document.createElement('input');
    buttonToCheckKey.type = 'button';
    buttonToCheckKey.id = "key-input-button";
    buttonToCheckKey.value = 'Submit';
    buttonToCheckKey.addEventListener('click', function() {
      checkKey(data, stage, panorama, map);
    });
    inputBoxAndSubmitButton.appendChild(buttonToCheckKey);
    keySpan.appendChild(inputBoxAndSubmitButton);
    gameInfo.appendChild(keySpan);
}

//TODO(smissak): TEST this so if there is more than one page and the input key is correct,
// it adds the new stage rather than redirecting to the after game page
/** 
* Checks if the key is the correct key for the current stage
* @param {string} data is the JSON from the server ‘/load-game-data’ 
* @param {string} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {object} panorama the panorama of the map created in initMapPlayGame()
* @param {object} map is map created in initMapPlayGame()
*/
async function checkKey(data, stage, panorama, map) {
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
  console.log(nextStageNumber);
  var nextStage = await getStage(data.stages[nextStageNumber]);
  console.log(nextStage);
  var startingLocation = {lat: nextStage.startingLocation.latitude, lng: nextStage.startingLocation.longitude};
  panorama.setPosition(startingLocation);
  document.getElementById('game-info').innerHTML = '';
  createGameInfoOnSideOfMap(data, nextStage, panorama, map);

  var stageHints = nextStage.hints;
  if (stageHints.length == null) {
    window.alert('Sorry there was an error retrieving the hints, failure to initialize game');
    window.location.replace('index.html');
    return;
  }

  stageHints.forEach(hint => 
    addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text, hint.hintNumber)
  );
}

/** 
* Creates an li for the hint to be places in the ol with the id being the hintNum
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint #1, #2, #3, etc.)
*/
function createHintPlaceHolder(hintNum) {
    var hintLi = document.createElement('li');
    hintLi.id = hintNum;
    return hintLi;
}

/** 
* Gets the data from the server about the current stage
* @param {string} stageID is the ID of the stage to be retrieved from the server
* @return {string} the JSON data from the server for the stage with the stageID passed in
*/
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

/** 
* Adds a marker to the map containing the hint's data
* @param {object} map the panorama of the map created in initMapPlayGame()
* @param {LatLng} latLng an object that contains the latitude and longitude of where the marker should be
* @param {string} hint the plain text of the hint
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint #1, #2, #3, etc.)
*/
function addHintMarker(map, latLng, hint, hintNum) {  
  var marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: 'images/marker_exclamation_point.png'
  });

  marker.addListener('click', function() {
    addHint(hint, hintNum)
  });
}

/** 
* Given the the number of the hint (hintNum) it adds to the element with the id being the hintNum with the text of the hint
* @param {string} hint the plain text of the hint
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint #1, #2, #3, etc.)
*/
function addHint(hint, hintNum) {
  var hintsWithNum = document.getElementById(hintNum);
  hintsWithNum.innerText = hint;
}
