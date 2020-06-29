function initMapCreateGame() {
  var myLatlng = {lat: -0.21874964312341205, lng: 12.221368164062483};

  var map = new google.maps.Map(
    document.getElementById('map'), {
      zoom: 1, 
      center: myLatlng, 
      mapTypeId: 'hybrid',
      gestureHandling: 'greedy'
  });

  // Create the initial InfoWindow.
  var infoWindow = new google.maps.InfoWindow();

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
  newStageHints.innerHTML = "<input type='text' id='started-postion' placeholder='Starter Postion (click on map to get corrdinates)'>" +
                            "<input type='text' placeholder='Starter Hint' id='starter-hint' required>" +
                            "<input type='text' id='hint1-postion' placeholder='Hint 1 Postion (click on map to get corrdinates)'>" + 
                            "<input type='text' placeholder='Hint 1' id='hint1'>";

  document.getElementById('hints').appendChild(newStageHints);
  setActive('stage' + (numStages + 1));
}

function addNewHint() {
  const activeStageNum = getActiveStageElement().classList[0] + 'Hints';
  const activeHints = document.getElementById(activeStageNum);
  const numHints = activeHints.getElementsByTagName('input').length;

  const newHintPos = document.createElement('input');
  newHintPos.id = 'hint' + (numHints/2) + '-position';
  newHintPos.placeholder = 'Hint ' + (numHints/2) + ' Postion (click on map to get corrdinates)';
  newHintPos.type= 'text';
  activeHints.appendChild(newHintPos);

  const newHint = document.createElement('input');
  newHint.id = 'hint' + (numHints/2);
  newHint.placeholder = 'Hint ' + (numHints/2);
  newHint.type= 'text';
  activeHints.appendChild(newHint);
}

function setActive(toSetActive) {
  const activeStage = getActiveStageElement();
  activeStage.classList.remove('activeStage');
  const divToHide = getStageNumber(activeStage);
  document.getElementById(divToHide + 'Hints').classList.add('hidden');

  const newActiveStage = document.getElementsByClassName(toSetActive)[0];
  newActiveStage.classList.add('activeStage');
  document.getElementById(toSetActive + 'Hints').classList.remove('hidden');
}

function getActiveStageElement() {
  return document.getElementsByClassName('activeStage')[0];
}

function getStageNumber(stageElement) {
    return stageElement.classList[0];
}
