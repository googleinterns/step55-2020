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
                              "<input type='text' id='key'>" +
                              "<label for='key'>Stage Key</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='starter-position'>" +
                              "<label for='starter-position'>Starter Position (click on map to get coordinates)</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='starter-hint' required>" +
                              "<label for='starter-hint'>Starter Hint</label>" +
                            "</div>" +
                            "<div class='input-field'>" +
                              "<input type='text' id='hint1-position'>" +
                              "<label for='hint1-position'>Hint 1 Position (click on map to get coordinates)</label>" +
                            "</div>" + 
                            "<div class='input-field'>" +
                              "<input type='text'  id='hint1'>" +
                              "<label for='hint1'>Hint 1</label>" +
                            "</div>";

  document.getElementById('hints').appendChild(newStageHints);
  setActive('stage' + (numStages + 1));
}

/**
* Adds a hint to the corresponding active stage
*/
function addNewHint() {
  const activeStageNum = getActiveStageElement().classList[0] + 'Hints';
  const activeHints = document.getElementById(activeStageNum);
  const numHintInputBoxes = activeHints.getElementsByTagName('input').length - 1;
  let numHints = numHintInputBoxes/2;

  const newHintPos = document.createElement('input');
  newHintPos.id = 'hint' + numHints + '-position';
  newHintPos.type= 'text';

  let hintPosLabel = document.createElement('label');
  hintPosLabel.for = 'hint' + numHints + '-position';
  hintPosLabel.innerText = 'Hint ' + numHints + ' Position (click on map to get coordinates)';

  let hintPosDiv = createInputDiv();
  hintPosDiv.appendChild(newHintPos);
  hintPosDiv.appendChild(hintPosLabel);
  activeHints.appendChild(hintPosDiv);

  const newHint = document.createElement('input');
  newHint.id = 'hint' + numHints;
  newHint.type= 'text';

  let hintLabel = document.createElement('label');
  hintLabel.for = 'hint' + numHints;
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

/**
* Gets the data from the form on the createGame.html page and sends it to the server
*/
function getDataFromGameCreationForm() {
  const numStages = document.getElementById('stages').getElementsByTagName('input').length;
  let stageKeys = [];
  let stageSpawnLocations = []; // ex: [{'latitude': 1, 'longitude':2}, {'latitude': 3, 'longitude': 4}]
  let stageStarterHints = [];
  let hintLocations = []; // 2d array [[{'latitude': 1, 'longitude':2},{'latitude': 1, 'longitude':2}], [{'latitude': 1, 'longitude':2}]]
  let hintTexts = [];

  // Stages and hints are 1 indexed
  for (let count = 1; count <= numStages; count++) {
    let stage = document.getElementById('stage' + count + 'Hints');
    let starterPos = stage.querySelector('#starter-position').value;
    starterPos = starterPos.replace(")", "").replace("(", "").replace(" ", "").split(",");

    let dict = {};
    dict['latitude'] = starterPos[0];
    dict['longitude'] = starterPos[1];
    if (isNaN(starterPos[0]) || isNaN(starterPos[1])) {
      window.alert("Input for latitude and longitude must be numbers! In format (123, 456) or 123, 456");
      return;
    }
    stageSpawnLocations.push(dict);
    stageStarterHints.push(stage.querySelector('#starter-hint').value);

    stageKeys.push(stage.querySelector('#key').value);

    let numHintsForThisStage = stage.getElementsByTagName('input').length - 1;
    let stageHintsLocation = []
    let stageHintsText = []
    for (let hint = 1; hint < numHintsForThisStage/2; hint++) {
      let hintPos = stage.querySelector('#hint' + hint + '-position').value;
      hintPos = hintPos.replace(")", "").replace("(", "").split(",");
      dict = {};
      dict['latitude'] = hintPos[0];
      dict['longitude'] = hintPos[1];
      if (isNaN(hintPos[0]) || isNaN(hintPos[1])) {
        window.alert("Input for latitude and longitude must be numbers! In format (123, 456) or 123, 456");
        return;
      }
      stageHintsLocation.push(dict);
      stageHintsText.push(stage.querySelector('#hint' + hint).value);
    }
    hintLocations.push(stageHintsLocation);
    hintTexts.push(stageHintsText);
  }

  let params = new URLSearchParams();
  params.append('gameName', document.getElementById('title').value);
  params.append('gameDescription', document.getElementById('description').value);
  params.append('stageKeys', JSON.stringify(stageKeys));
  params.append('stageSpawnLocations', JSON.stringify(stageSpawnLocations));
  params.append('stageStarterHints', JSON.stringify(stageStarterHints));
  params.append('hintLocations', JSON.stringify(hintLocations));
  params.append('hintTexts', JSON.stringify(hintTexts));
  let request = new Request('/create-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then((data) => {
    window.location.replace('gameInfo.html?gameID=' + data);
  });
}
