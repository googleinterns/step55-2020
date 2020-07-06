/**
* Adds a map to a page where the id "map" is
*/
function initMapToCreateGame() {
  var latlng = {lat: 0.0, lng: 0.0};

  var map = new google.maps.Map(
    document.getElementById('map'), {
      zoom: 1, 
      center: latlng, 
      mapTypeId: 'hybrid',
      gestureHandling: 'greedy'
  });

  // Create the search box and link it to the UI element.
  var input = document.getElementById("pac-input");
  var searchBox = new google.maps.places.SearchBox(input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

  // Bias the SearchBox results towards current map's viewport.
  map.addListener("bounds_changed", function() {
    searchBox.setBounds(map.getBounds());
  });

  var markers = [];
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener("places_changed", function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }

    // Clear out the old markers.
    markers.forEach(function(marker) {
      marker.setMap(null);
    });
    markers = [];

    // For each place, get the icon, name and location.
    var bounds = new google.maps.LatLngBounds();
    places.forEach(function(place) {
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      var icon = {
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
  var infoWindow = new google.maps.InfoWindow();
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
  newStageHints.innerHTML = "<input type='text' id='key' placeholder='Stage Key'>" +
                            "<input type='text' id='starter-position' placeholder='Starter Position (click on map to get coordinates)'>" +
                            "<input type='text' placeholder='Starter Hint' id='starter-hint' required>" +
                            "<input type='text' id='hint1-position' placeholder='Hint 1 Position (click on map to get coordinates)'>" + 
                            "<input type='text' placeholder='Hint 1' id='hint1'>";

  document.getElementById('hints').appendChild(newStageHints);
  setActive('stage' + (numStages + 1));
}

/**
* Adds a hint to the corresponding active stage
*/
function addNewHint() {
  const activeStageNum = getActiveStageElement().classList[0] + 'Hints';
  const activeHints = document.getElementById(activeStageNum);
  const numHints = activeHints.getElementsByTagName('input').length - 1;

  const newHintPos = document.createElement('input');
  newHintPos.id = 'hint' + (numHints/2) + '-position';
  newHintPos.placeholder = 'Hint ' + (numHints/2) + ' Position (click on map to get coordinates)';
  newHintPos.type= 'text';
  activeHints.appendChild(newHintPos);

  const newHint = document.createElement('input');
  newHint.id = 'hint' + (numHints/2);
  newHint.placeholder = 'Hint ' + (numHints/2);
  newHint.type= 'text';
  activeHints.appendChild(newHint);
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
* Gets the data from the form on the createGame.html page and sends it to the server
*/
function getDataFromGameCreationForm() {
  const numStages = document.getElementById('stages').getElementsByTagName('input').length;
  var stageKeys = [];
  var stageSpawnLocations = []; // ex: [{'latitude': 1, 'longitude':2}, {'latitude': 3, 'longitude': 4}]
  var stageStarterHints = [];
  var hintLocations = []; // 2d array [[{'latitude': 1, 'longitude':2},{'latitude': 1, 'longitude':2}], [{'latitude': 1, 'longitude':2}]]
  var hintTexts = [];

  // Stages and hints are 1 indexed
  for (var count = 1; count <= numStages; count++) {
    var stage = document.getElementById('stage' + count + 'Hints');
    var starterPos = stage.querySelector('#starter-position').value;
    starterPos = starterPos.replace(")", "").replace("(", "").replace(" ", "").split(",");

    var dict = {};
    dict['latitude'] = starterPos[0];
    dict['longitude'] = starterPos[1];
    if (isNaN(starterPos[0]) || isNaN(starterPos[1])) {
      window.alert("Input for latitude and longitude must be numbers! In format (123, 456) or 123, 456");
      return;
    }
    stageSpawnLocations.push(dict);
    stageStarterHints.push(stage.querySelector('#starter-hint').value);

    stageKeys.push(stage.querySelector('#key').value);

    var numHintsForThisStage = stage.getElementsByTagName('input').length - 1;
    var stageHintsLocation = []
    var stageHintsText = []
    for (var hint = 1; hint < numHintsForThisStage/2; hint++) {
      var hintPos = stage.querySelector('#hint' + hint + '-position').value;
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

  var params = new URLSearchParams();
  params.append('gameName', document.getElementById('title').value);
  params.append('gameDescription', document.getElementById('description').value);
  params.append('stageKeys', JSON.stringify(stageKeys));
  params.append('stageSpawnLocations', JSON.stringify(stageSpawnLocations));
  params.append('stageStarterHints', JSON.stringify(stageStarterHints));
  params.append('hintLocations', JSON.stringify(hintLocations));
  params.append('hintTexts', JSON.stringify(hintTexts));
  var request = new Request('/create-game-data', {method: 'POST', body: params});
  fetch(request);
}
