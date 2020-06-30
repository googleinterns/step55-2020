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

    createGameInfoOnSideOfMap(data, stage1);
  });
}

function createGameInfoOnSideOfMap(data, stage) {
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

    var hintsDiv = document.createElement('div');
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
    inputKeyBox.id = 'key-input';
    inputKeyBox.style = 'width: 30%';

    // This checks if the user clicked enter in the key box
    inputKeyBox.addEventListener('keydown', function(e) {
      if (e.which == 13) {
        checkKey(data, stage.key);
      }
    });
    gameInfo.appendChild(inputKeyBox);

    var buttonToCheckKey = document.createElement('input');
    buttonToCheckKey.type = 'button';
    buttonToCheckKey.value = 'Submit';
    buttonToCheckKey.addEventListener('click', function() {
      checkKey(data, stage.key);
    });
    gameInfo.appendChild(buttonToCheckKey);
}

//TODO: Change this so if there is more than one page and the input key is correct,
// it adds the new stage rather than redirecting to the after game page
function checkKey(data, key) {
  var keyInput = document.getElementById('key-input');
  var inputValue = keyInput.value;

  if (key == inputValue ) {
      window.location.replace('afterGame.html');
  } else {
      window.alert('Wrong key, please try again!');
  }
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
