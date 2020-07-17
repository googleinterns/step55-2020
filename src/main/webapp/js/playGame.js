/** 
* Initalizes a map where there is an id of 'playMap'
*/
async function initMapToPlayGame() {
  let gameID =  getGameID();
  let params = new URLSearchParams();
  params.append('gameID', gameID);
  let request = new Request('/load-game-data', {method: 'POST', body: params});
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
    let userProgress = await getUserProgress();
    let stageID = userProgress.stageID;
    let initStage = await getStage(stageID);
    let startingLocation = {lat: userProgress.location.latitude, lng: userProgress.location.longitude};
    
    let map = new google.maps.Map(
      document.getElementById('playMap'), {
      center: startingLocation, 
      gestureHandling: 'greedy',
      streetViewControl: false
    });

    let stageHints = initStage.hints;
    if (stageHints.length == null) {
      window.alert('Sorry there was an error retrieving the hints, failure to initialize game');
      window.location.replace('index.html');
      return;
    }
    let markers = [];
    stageHints.forEach(hint => 
      markers.push(addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text, hint.hintNumber, stageID))
    );

    // gets the street view
    let panorama = map.getStreetView();
    // removes the option to exit streetview
    let panoramaOptions = {
      enableCloseButton:false
    };
    panorama.setOptions(panoramaOptions);
    panorama.setPosition(startingLocation);
    // puts the user in streetView
    panorama.setVisible(true);
    createGameInfoOnSideOfMap(data, initStage, panorama, map);

    if (userProgress != null && userProgress.hintsFound != null) {
      userProgress.hintsFound.forEach(hintNum =>
        changeData(map, {lat: initStage.hints[parseInt(hintNum) - 1].location.latitude, lng: initStage.hints[parseInt(hintNum) - 1].location.longitude}, 
                    initStage.hints[parseInt(hintNum) - 1].text, hintNum, stageID, false, markers[parseInt(hintNum) - 1])
      );
    }
  });
}

/**
* Gets the user progress from the server
* @return a the user data in json and null if there is no user data or the user is not logged in
*/
async function getUserProgress() {
  let gameID = getGameID();
  let result;
  
  const params = new URLSearchParams();
  let tokenEmailDict = tokenAndEmail();

  params.append('email', tokenEmailDict['email']);
  params.append('idToken', tokenEmailDict['token']);
  params.append('gameID', gameID);

  let request = new Request('/load-singleplayerprogress-data', {method: 'POST', body: params});
  await fetch(request).then(response => response.json()).then(data => {
    result = data;
  });
  return result;
}

/**
* Gets the gameID from the URL
* @return gameID from the URL or returns null and relocates you to index.html page
*/
function getGameID() {
  const urlParams = new URLSearchParams(window.location.search);
  let gameID = urlParams.get('gameID');
  if(!urlParams.has('gameID')) {
    window.alert('Failure to initialize game');
    window.location.replace('index.html');
    return;
  }
  return gameID;
}

/** 
* Creates the game game info that is on the side of the map on playGame.html
* @param {string} data is the JSON from the server ‘/load-game-data’ 
* @param {string} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {object} panorama the panorama of the map created in initMapPlayGame()
* @param {object} map is map created in initMapPlayGame()
*/
function createGameInfoOnSideOfMap(data, stage, panorama, map) {
  let gameInfo = document.getElementById('game-info');
    
  let gameName = document.createElement('h2');
  gameName.innerHTML = data.gameName;
  gameName.className = 'center';
  gameInfo.appendChild(gameName);

  let gameStage = document.createElement('div');
  gameStage.id = 'stage-counter';
  gameStage.innerHTML = 'You are on stage: ' + stage.stageNumber + '/' + data.stages.length;
  gameStage.className = 'center';
  gameInfo.appendChild(gameStage);

  gameInfo.appendChild(document.createElement('hr'));

  let hintsContainer = document.createElement('div');
  hintsContainer.id = 'hints-container';

  let starterHint = document.createElement('div');
  starterHint.innerHTML = 'Starter: ' + stage.startingHint;
  hintsContainer.appendChild(starterHint);

  // This div is a container for where the hints will be placed on the site
  let hintsDiv = document.createElement('div');
  // This is the ordered list (or <ol> in HTML) for where the hints will be listed
  let hintsOl = document.createElement('ol');
  hintsOl.id = 'hints';

  stage.hints.forEach(hint => 
    hintsOl.appendChild(createHintPlaceHolder(hint.hintNumber))
  );

  hintsContainer.appendChild(hintsOl);
  gameInfo.appendChild(hintsContainer);

  let keySpan = document.createElement('span');
  keySpan.id = 'keybox';
  let enterKeyText = document.createElement('div');
  enterKeyText.id = 'enter-key-text';
  enterKeyText.innerHTML = 'Please enter key to continue:';
  enterKeyText.className = 'center';
  keySpan.appendChild(enterKeyText);

  let placeHolderForWrongInput =  document.createElement('div');
  placeHolderForWrongInput.id = 'wrong-input';
  placeHolderForWrongInput.className = 'wrong-input';
  keySpan.appendChild(placeHolderForWrongInput);

  let inputKeyBox = document.createElement('input');
  inputKeyBox.type = 'text';
  inputKeyBox.classList = 'input-text-color';
  inputKeyBox.id = 'key-input';

  // This checks if the user clicked enter in the key box
  inputKeyBox.addEventListener('keydown', function(e) {
    if (e.which == 13) {
      checkKey(data, stage, panorama, map);
    }
  });
  let inputBoxAndSubmitButton = document.createElement('div');
  inputBoxAndSubmitButton.setAttribute('id', 'inputBoxAndButton');
  inputBoxAndSubmitButton.appendChild(inputKeyBox);

  let buttonToCheckKey = document.createElement('input');
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

/** 
* Checks if the key is the correct key for the current stage
* @param {string} data is the JSON from the server ‘/load-game-data’ 
* @param {string} stage the current stage data from '/load-stage-data' servlet, in the from of JSON
* @param {object} panorama the panorama of the map created in initMapPlayGame()
* @param {object} map is map created in initMapPlayGame()
*/
async function checkKey(data, stage, panorama, map) {
  let keyInput = document.getElementById('key-input');
  let inputValue = keyInput.value;
  if (stage.key.toLowerCase() != inputValue.toLowerCase()) {
    document.getElementById('wrong-input').innerHTML = '<i class="red-text">Wrong Input! Try again!</i>';
    document.getElementById('wrong-input').classList.remove('wrong-input');
    return;
  }
  if (data.stages.length == stage.stageNumber) {
    const urlParams = new URLSearchParams(window.location.search)
    let gameID = urlParams.get('gameID');
    updateUserProgress("N/A", map);
    window.location.replace('afterGame.html?gameID=' + gameID);
    return;
  }

  // This reloads the map and the game info on the side of the map with the next stage data
  // Don't need to add one for nextStageNumber because arrays are 0 indexed and stageNumbers are 1 indexed
  let nextStageNumber = stage.stageNumber;
  let nextStage = await getStage(data.stages[nextStageNumber]);
  let startingLocation = {lat: nextStage.startingLocation.latitude, lng: nextStage.startingLocation.longitude};
  panorama.setPosition(startingLocation);
  document.getElementById('game-info').innerHTML = '';
  createGameInfoOnSideOfMap(data, nextStage, panorama, map);

  let stageHints = nextStage.hints;
  if (stageHints.length == null) {
    window.alert('Sorry there was an error retrieving the hints, failure to initialize game');
    window.location.replace('index.html');
    return;
  }

  stageHints.forEach(hint => {
    addHintMarker(map, {lat: hint.location.latitude, lng: hint.location.longitude}, hint.text, hint.hintNumber, data.stages[nextStageNumber]);
  });

  updateUserProgress(nextStage.stageID, map);
}

/** 
* Creates an li for the hint to be places in the ol with the id being the hintNum
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint #1, #2, #3, etc.)
*/
function createHintPlaceHolder(hintNum) {
  let hintLi = document.createElement('li');
  hintLi.id = hintNum;
  return hintLi;
}

/** 
* Gets the data from the server about the current stage
* @param {string} stageID is the ID of the stage to be retrieved from the server
* @return {string} the JSON data from the server for the stage with the stageID passed in
*/
async function getStage(stageID) {
  let currStage;
  const params = new URLSearchParams();
  params.append('stageID', stageID)
  let request = new Request('/load-stage-data', {method: 'POST', body: params});
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
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint 1, 2, 3, etc.)
* @param {string} stageID the stageID in which the hint is at 
* @return {object} the marker created
*/
function addHintMarker(map, latLng, hint, hintNum, stageID) {  
  let marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: 'images/marker_notfound.png'
  }); 

  marker.addListener('click', function() {
    changeData(map, latLng, hint, hintNum, stageID, true, marker);
  });

  return marker;
}

/** 
* Changes the color of the marker on the map adds the hint to the list of hints found
* @param {object} map the panorama of the map created in initMapPlayGame()
* @param {LatLng} latLng an object that contains the latitude and longitude of where the marker should be
* @param {string} hint the plain text of the hint
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint 1, 2, 3, etc.)
* @param {string} stageID the stageID in which the hint is at 
* @param {boolean} updateProgress boolean indicating if the user progress should be updated or not
* @param {object} marker an optional parameter that passes in the marker to remove
*/
function changeData(map, latLng, hint, hintNum, stageID, updateProgress, marker) {
  if (marker != null) {
    marker.setMap(null);
  }
  
  marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: 'images/marker_found.png'
  }); 

  addHint(hint, hintNum, updateProgress, stageID, map);
}

/** 
* Given the the number of the hint (hintNum) it adds to the element with the id being the hintNum with the text of the hint
* @param {string} hint the plain text of the hint
* @param {int} hintNum the number of the hint, which hint is it (i.e. hint #1, #2, #3, etc.)
* @param {boolean} updateProgress boolean indicating if the user progress should be updated or not
* @param {string} stageID the stageID in which the hint is at
* @param {object} map the panorama of the map created in initMapPlayGame()
*/
function addHint(hint, hintNum, updateProgress, stageID, map) {
  let hintsWithNum = document.getElementById(hintNum);
  hintsWithNum.innerText = hint;
  if (updateProgress) updateUserProgress(stageID, map);
}

/** 
* Gets the info about the user and sends it to the server to update the progress for the game
* @param {string} stageID the stageID in which the hint is at
* @param {object} map the panorama of the map created in initMapPlayGame()
*/
function updateUserProgress(stageID, map) {
  let gameID =  getGameID();
  let params = new URLSearchParams();
  params.append('gameID', gameID);
  
  let latLng = (map.getStreetView().getPosition());
  let location = {"latitude": latLng.lat(), "longitude": latLng.lng()};
  params.append('location', JSON.stringify(location));
  
  params.append('stageID', stageID);
  let hintsDiv = document.getElementById('hints');
  let hints = hintsDiv.getElementsByTagName('li');
  
  let hintsFound = [];
  for (let i = 0; i < hints.length; i++) {
    if (hints[i].innerText != '') {
      hintsFound.push(parseInt(hints[i].id));
    }
  }
  params.append('hintsFound',  JSON.stringify(hintsFound));

  let tokenEmailDict = tokenAndEmail();
  params.append('email', tokenEmailDict['email']);
  params.append('idToken', tokenEmailDict['token']);
  let request = new Request('/update-singleplayerprogress-data', {method: 'POST', body: params});
  fetch(request);
}
