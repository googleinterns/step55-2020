/**
* Adds a map to a page where the id "map" is
*/
function initMapToCreateGame() {
  let latlng = {lat: 0.0, lng: 0.0};

  let map = new google.maps.Map(
    document.getElementById('map'), {
      zoom: 1, 
      center: latlng, 
      mapTypeId: 'hybrid',
      gestureHandling: 'greedy'
  });
  let panorama = map.getStreetView();
  let coordinatesDiv = document.createElement("div");
  coordinatesDiv.id = 'coordinates-viewer';
  panorama.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(coordinatesDiv);
  google.maps.event.addListener(panorama, 'position_changed', function() {
    coordinatesDiv.innerText = "(" + panorama.getPosition().lat() + ", " + panorama.getPosition().lng() + ")";
  });

  // Create the search box and link it to the UI element.
  let input = document.getElementById("pac-input");
  let searchBox = new google.maps.places.SearchBox(input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  // Bias the SearchBox results towards current map's viewport.
  map.addListener("bounds_changed", function() {
    searchBox.setBounds(map.getBounds());
  });

  let markers = [];
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener("places_changed", function() {
    let places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }

    // Clear out the old markers.
    markers.forEach(function(marker) {
      marker.setMap(null);
    });
    markers = [];

    // For each place, get the icon, name and location.
    let bounds = new google.maps.LatLngBounds();
    places.forEach(function(place) {
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      let icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      markers.push(
        new google.maps.Marker({
          map: map,
          icon: icon,
          title: place.name,
          position: place.geometry.location
        })
      );

      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
    });
  map.fitBounds(bounds);
  });

  // Create the initial InfoWindow.
  let infoWindow = new google.maps.InfoWindow();
  infoWindow.className = 'black-text';

  // Configure the click listener.
  map.addListener('click', function(mapsMouseEvent) {
    // Close the current InfoWindow.
    infoWindow.close();

    // Create a new InfoWindow.
    infoWindow = new google.maps.InfoWindow({position: mapsMouseEvent.latLng});
    infoWindow.setContent(mapsMouseEvent.latLng.toString());
    infoWindow.open(map);
  });
}

/**
* Adds a stage button to the page and a corresponding div for the hints
*/
function addNewStage() {
  const stagesList = document.getElementById('stages');
  const numStages = stagesList.getElementsByTagName('input').length;
  
  const newStage = document.createElement('input');
  newStage.value = 'Stage ' + (numStages + 1);
  newStage.type = 'button';
  newStage.className = 'stage' + (numStages + 1);
  

  newStage.addEventListener("click", function(){
    setActive('stage' + (numStages + 1));
  });
  stagesList.appendChild(newStage);

  const newStageHints = document.createElement('div');
  newStageHints.id = getStageNumber(newStage) + 'Hints';
  newStageHints.innerHTML = "<div class='input-field'>"+
                              "<input type='text' id='stage"+(numStages + 1)+"key'>" +
                              "<label for='stage"+(numStages + 1)+"key'>Stage Key</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='stage"+(numStages + 1)+"starter-position'>" +
                              "<label for='stage"+(numStages + 1)+"starter-position'>Starter Position (click on map to get coordinates)</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='stage"+(numStages + 1)+"starter-hint' required>" +
                              "<label for='stage"+(numStages + 1)+"starter-hint'>Starter Hint</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='stage"+(numStages + 1)+"hint1-position'>" +
                              "<label for='stage"+(numStages + 1)+"hint1-position'>Hint 1 Position (click on map to get coordinates)</label>" +
                            "</div>" + 
                            "<div class='input-field'>" +
                              "<input type='text'  id='stage"+(numStages + 1)+"hint1'>" +
                              "<label for='stage"+(numStages + 1)+"hint1'>Hint 1</label>" +
                            "</div>";

  document.getElementById('hints').appendChild(newStageHints);
  setActive('stage' + (numStages + 1));
}

/**
* Adds a hint to the corresponding active stage
*/
function addNewHint() {
  const stageNum = getActiveStageElement().classList[0];
  const activeStageNumHintsDiv = stageNum + 'Hints';
  const activeHints = document.getElementById(activeStageNumHintsDiv);
  const numHintInputBoxes = activeHints.getElementsByTagName('input').length - 1;
  let numHints = numHintInputBoxes/2;
  console.log(stageNum)

  const newHintPos = document.createElement('input');
  newHintPos.id = stageNum + 'hint' + numHints + '-position';
  newHintPos.type= 'text';

  let hintPosLabel = document.createElement('label');
  hintPosLabel.htmlFor = stageNum + 'hint' + numHints + '-position';
  hintPosLabel.innerText = 'Hint ' + numHints + ' Position (click on map to get coordinates)';

  let hintPosDiv = createInputDiv();
  hintPosDiv.appendChild(newHintPos);
  hintPosDiv.appendChild(hintPosLabel);
  activeHints.appendChild(hintPosDiv);

  const newHint = document.createElement('input');
  newHint.id = stageNum + 'hint' + numHints;
  newHint.type= 'text';

  let hintLabel = document.createElement('label');
  hintLabel.htmlFor = stageNum + 'hint' + numHints;
  hintLabel.innerText = 'Hint ' + numHints;
  
  let hintDiv = createInputDiv();
  hintDiv.appendChild(newHint);
  hintDiv.appendChild(hintLabel);
  activeHints.appendChild(hintDiv);
}

/**
* Removes whichever stage is active and sets the element with the class being passed in to have the class 'activeStage'
* @param {string} toSetActive is a class name of the element have the class 'activeStage
*/
function setActive(toSetActive) {
  const activeStage = getActiveStageElement();
  activeStage.classList.remove('activeStage');
  const divToHide = getStageNumber(activeStage);
  document.getElementById(divToHide + 'Hints').classList.add('hidden');

  const newActiveStage = document.getElementsByClassName(toSetActive)[0];
  newActiveStage.classList.add('activeStage');
  document.getElementById(toSetActive + 'Hints').classList.remove('hidden');
}

/**
* Returns the first element with the class 'activeStage'
* @return {Element} the first element with the class 'activeStage'
*/
function getActiveStageElement() {
  return document.getElementsByClassName('activeStage')[0];
}

/**
* Returns the first class of the element being passed in
* @param {Element} stageElement the element of which the first className wants to be retrieved
* @return {string} the first className of the element which should be in the form of "stage" + [the stage number]
*/
function getStageNumber(stageElement) {
  return stageElement.classList[0];
}

/**
* Creats a div that has the class input-field
* @returns {Element} a div with the class 'input-field'
*/
function createInputDiv() {
  let div = document.createElement('div');
  div.className = 'input-field';
  return div;
}

function checkValidLatLng(lat, lng) {
  if (!(lat > -90 && lat < 90)) {
    console.log(lat > -90)
    console.log(lat < 90)
    alert("invalid latitude");
    return false;
  }
  if (!(lng > -180 && lng < 180)) {
    alert("invalid longitiude");
    console.log(lng)
    return false;
  }
  return true;
}

/**
* Gets the data from the form on the createGame.html page and sends it to the server
*/
async function getDataFromGameCreationForm() {
  if(!isSignedIn()) {
    window.alert('You must sign in before creating a game!')
    return;
  }
  let finishButton = document.getElementById("finish");
  finishButton.disable = true;
  let numStages = document.getElementById('stages').getElementsByTagName('input').length;
  let stageKeys = [];
  let stageSpawnLocations = []; // ex: [{'latitude': 1, 'longitude':2}, {'latitude': 3, 'longitude': 4}]
  let stageStarterHints = [];
  let hintLocations = []; // 2d array [[{'latitude': 1, 'longitude':2},{'latitude': 1, 'longitude':2}], [{'latitude': 1, 'longitude':2}]]
  let hintTexts = [];

  let title = document.getElementById('title').value;
  let description = document.getElementById('description').value;
  if (title == "" || description == "") {
    window.alert('You must fill out the title and description!')
    finishButton.disable = false;
    return;
  }

  // Stages and hints are 1 indexed
  for (let count = 1; count <= numStages; count++) {
    let stage = document.getElementById('stage' + count + 'Hints');
    let starterPos = stage.querySelector('#stage' + count + 'starter-position').value;
    if (starterPos == "") {
      window.alert("Starter Position cannot be left empty");
      finishButton.disable = false;
      return;
    }
    starterPos = starterPos.replace(")", "").replace("(", "").replace(" ", "").split(",");

    let dict = {};
    dict['latitude'] = starterPos[0];
    dict['longitude'] = starterPos[1];
    if (isNaN(starterPos[0]) || isNaN(starterPos[1])) {
      window.alert("Input for latitude and longitude must be numbers! In format (123, 456) or 123, 456");
      finishButton.disable = false;
      return;
    }
    if (!checkValidLatLng(starterPos[0], starterPos[1])) {
      finishButton.disable = false;
      return;
    } else if(!(await coordinatesOk(starterPos[0], starterPos[1]))) {
      window.alert("Input coordinates are invalid! Latitude must be between -85 and 85, and longitude must be between -180 and 180. The location must also have Google Street View support.");
      return;
    }
    stageSpawnLocations.push(dict);
    let starterHint = stage.querySelector('#stage' + count + 'starter-hint').value;
    if (starterHint == "") {
      window.alert("Starter hint cannot be left empty");
      finishButton.disable = false;
      return;
    }
    stageStarterHints.push(starterHint);
    let stageKey = stage.querySelector('#stage' + count + 'key').value;
    if (stageKey == "") {
      window.alert("Stage Key cannot be left empty");
      finishButton.disable = false;
      return;
    }
    stageKeys.push(stageKey);

    let numHintsForThisStage = stage.getElementsByTagName('input').length - 1;
    let stageHintsLocation = [];
    let stageHintsText = [];
    for (let hint = 1; hint < numHintsForThisStage/2; hint++) {
      let hintPos = stage.querySelector('#stage' + count + 'hint' + hint + '-position').value;
      hintPos = hintPos.replace(")", "").replace("(", "").split(",");
      dict = {};
      dict['latitude'] = hintPos[0];
      dict['longitude'] = hintPos[1];
      if (isNaN(hintPos[0]) || isNaN(hintPos[1])) {
        window.alert("Input for latitude and longitude must be numbers! In format (123, 456) or 123, 456");
        finishButton.disable = false;
        return;
      }
      if (!checkValidLatLng(hintPos[0], hintPos[1])) {
        finishButton.disable = false;
        return;
      }
      stageHintsLocation.push(dict);
      let hintText = stage.querySelector('#stage' + count + 'hint' + hint).value;
      if (hintText == "") {
        window.alert("Hint text cannot be left empty");
        finishButton.disable = false;
        return;
      }
      stageHintsText.push(hintText);
    }
    hintLocations.push(stageHintsLocation);
    hintTexts.push(stageHintsText);
  }

  let params = new URLSearchParams();
  params.append('gameName', title);
  params.append('gameDescription', description);
  params.append('stageKeys', JSON.stringify(stageKeys));
  params.append('stageSpawnLocations', JSON.stringify(stageSpawnLocations));
  params.append('stageStarterHints', JSON.stringify(stageStarterHints));
  params.append('hintLocations', JSON.stringify(hintLocations));
  params.append('hintTexts', JSON.stringify(hintTexts));

  let tokenEmailDict = tokenAndEmail();
  params.append('email', tokenEmailDict['email']);
  params.append('idToken', tokenEmailDict['token']);
  let request = new Request('/create-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    window.location.replace('gameInfo.html?gameID=' + data);
  });
}

/**
* Checks whether the inputted coordinates are valid for the purposes of a game.
* Latitude must be in the interval [-85, 85] and longitude must be in the interval
* [-180, 180]. The location must also have Street View support.
* 
*/
async function coordinatesOk(lat, lng) {
  if(lat < -85 || 85 < lat) return false;
  if(lng < -180 || 180 < lng) return false;
  let coords = lat + ',' + lng;
  let key = 'AIzaSyDtRpnDqBAeTBM0gjAXIqe2u5vBLj15mtk';
  let res = false;
  await fetch('https://maps.googleapis.com/maps/api/streetview/metadata?location='+coords+'&key='+key).then(response => response.json()).then(async (data) => {
    if(data.status == 'OK') res = true;
  });
  return res;
}
