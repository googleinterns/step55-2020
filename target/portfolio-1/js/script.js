/** 
* Changes the page to light mode or dark mode
*/
function changeToOrFromDarkMode() {
  const body = document.body;
  const mode = body.classList;
  if (mode.contains('light-mode')) {
    body.className = 'dark-mode';
  } else {
    body.className = 'light-mode';
  }
  if (typeof(Storage) !== "undefined") {
    sessionStorage.setItem("colorMode", body.className);
  } 
}

// Depreciated

/** 
* Creates the navigation bar and specifies which page is active
* @param {string} page is which HTML the navbar should be placed on
* @example createNavBar("index")
*/
function createNavBar(page) {
  var navbar = document.createElement('nav');

  var navWrapperDiv = document.createElement('div');
  navWrapperDiv.className = 'nav-wrapper';
  navbar.appendChild(navWrapperDiv);

  var containerDiv = document.createElement('div');
  containerDiv.className = 'container';
  navWrapperDiv.appendChild(containerDiv);

  var a = document.createElement('a');
  a.innerHTML = 'Street Explorer';
  a.href = "index.html";
  a.className = 'brand-logo';
  containerDiv.appendChild(a);

  var mobileA = document.createElement('a');
  mobileA.innerHTML = '<i class="material-icons">menu</i>';
  mobileA.href = "#";
  mobileA.className = 'sidenav-trigger';
  mobileA.dataset.target = 'mobile-demo';
  containerDiv.appendChild(mobileA);

  var ul = document.createElement('ul');
  ul.className = 'right hide-on-med-and-down';

  var liBrightness = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = '<i class=\'material-icons\' onclick=\'changeToOrFromDarkMode()\'>brightness_4</i>';
  a.href = "#";
  liBrightness.appendChild(a);

  var liHome= document.createElement('li');
  if (page == 'index') {
    liHome.className = 'active';
  }
  a = document.createElement('a');
  a.innerHTML = ('Home');
  a.href = "index.html";
  liHome.appendChild(a);

  var liCreateGame = document.createElement('li');
  if (page == 'createGame') {
      liCreateGame.className = 'active';
  }
  a = document.createElement('a');
  a.innerHTML = ('Create Game');
  a.href = "createGame.html";
  liCreateGame.appendChild(a);

  var liLogin = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = ('Login');
  a.href = "#";
  liLogin.appendChild(a);

  ul.appendChild(liBrightness);
  ul.appendChild(liHome);
  ul.appendChild(liCreateGame);
  ul.appendChild(liLogin);
  containerDiv.appendChild(ul);

  document.getElementById('nav-bar').appendChild(navbar);
//   TODO(smissak): add side nav bar for mobile view
  document.getElementById('nav-bar').innerHTML += '<ul class="sidenav" id="mobile-demo">' + 
                                                  '<li><a href="#"><i class="material-icons" onclick="changeToOrFromDarkMode()">brightness_4</i></a></li>' + 
                                                  '<li><a href="index.html">Home</a></li>' + 
                                                  '<li><a href="createGame.html">Create Game</a></li>' + 
                                                  '<li><a href="#">Login</a></li> </ul>';
}

//TODO(smissak): add marker for each starting position of each stage to the image
/** 
* Creates a static maps image
* @param {float} latitude is the latitude where the center of the image should be
* @param {float} longitude is the longitiude where the center of the image should be
*/
function createStaticMap(latitude, longitude) { // TODO(smissak): rather than pass in the lat and lng, pass in an array of the lat and lngs to add a marker for the TODO
  var staticImage = document.createElement('img');
  var staticMapURL = 'https://maps.googleapis.com/maps/api/staticmap?center=';
  staticMapURL += latitude + ',' + longitude;
  staticMapURL += '&zoom=13&size=300x300&maptype=roadmap';
  staticMapURL += '&markers=color:red%7C' + latitude + ',' + longitude;
  staticMapURL += '&key=AIzaSyDtRpnDqBAeTBM0gjAXIqe2u5vBLj15mtk';
  staticImage.src = staticMapURL;
  staticImage.classList.add('cursor-pointer');

  staticImage.addEventListener('click', function() {
    window.location.replace('playGame.html');
  });
  return staticImage;
}

/** 
* Creates below the image the information of the game
* @param {string} mapData the JSON data with the map's information
* @param {string} captionID the id of the game info that is under the static image
*/
function createStaticMapCaption(mapData, captionID) {
  var numDifficultyVotes = mapData.numDifficultyVotes;
  var totalDifficulty = mapData.totalDifficulty;
  var avgDifficulty = 0;
  if (numDifficultyVotes > 0 && totalDifficulty > 0) {
    avgDifficulty = math.round(numDifficultyVotes/totalDifficulty);
  }

  var difficulty = 'Easy';
  var difficultyColor = 'green';
  if (avgDifficulty == 2) {
    difficulty = 'Medium';
    difficultyColor = 'orange';
  } else if (avgDifficulty == 3) {
    difficulty = 'Hard';
    difficultyColor = 'red';
  }

  var fiveStars = getStarRating(mapData);

  var staticMapInfo = document.createElement('div');
  staticMapInfo.id = captionID;
  staticMapInfo.innerHTML = '<div style="float:right">' + fiveStars + '</div>';
  staticMapInfo.innerHTML += mapData.gameName + ' <i style="color:' + difficultyColor + '">[' + difficulty + "]</i>";
  staticMapInfo.innerHTML += '<br> By ' + mapData.gameCreator;
  staticMapInfo.style = 'text-align:left; padding: 6px';
  staticMapInfo.classList.add('cursor-pointer');

  staticMapInfo.addEventListener('click', function() {
    window.location.replace('playGame.html');
  });
  return staticMapInfo;
}


/** 
* Creates below the image the star ratings of the map
* @param {string} mapData the JSON data with the map's information
*/
function getStarRating(mapData) {
  var numStarVotes = mapData.numStarVotes;
  var totalStars = mapData.totalStars;
  var avgStars = 0.0;
  if (numStarVotes > 0 || totalStars > 0) {
    avgStars = numStarVotes/totalStars;
  }
  
  var fullStar = '<i class="material-icons md-18">star</i>';
  var halfStar = '<i class="material-icons md-18">star_half</i>';
  var emptyStar = '<i class="material-icons md-18">star_border</i>';

  var fiveStars = '';
  var avgStarsTemp = avgStars;
  for (var i = 0; i < 5; i++) {
    if(avgStarsTemp >= 1.0) {
       fiveStars += fullStar;
    } else if (avgStarsTemp <= 0) {
      fiveStars += emptyStar;
    } else {
      fiveStars += halfStar;
    }
    avgStarsTemp -= 1.0;
  }
  return fiveStars
}

/** 
* Adds a featured map where the ID 'featured-map' is
*/
function loadFeaturedMap() {
  const params = new URLSearchParams();
  params.append('gameID', 'demogameid')
  var request = new Request('/load-game-data', {method: 'POST', body: params});
  fetch(request).then(response => response.json()).then(async (data) => {
    var stage = await getStage(data.stages[0]);
    var featuredMap = createStaticMap(stage.startingLocation.latitude, stage.startingLocation.longitude);
    var featuredMapCaption = createStaticMapCaption(data, 'featured-map-info');
    var featuredMapDiv = document.getElementById('featured-map');
    featuredMapDiv.classList.add('hoverable');
    featuredMapDiv.append(featuredMap);
    featuredMapDiv.append(featuredMapCaption);
  });
}

/** 
* This function is a wrapper function for all of the functions to be called onload of any page
* @param {string} page is which page the onLoadFunction is being called from without the .html 
* @example onLoadFunction("index")
*/
function onLoadFunctions(page) {
  createNavBar(page);

  var elems = document.querySelectorAll('.sidenav');
  var instances = M.Sidenav.init(elems, {});
  
  if (page == 'playGame') {
    initMapToPlayGame();
  } else if (page == 'createGame') {
    initMapToCreateGame();
  } else if (page == 'index') {
    loadFeaturedMap();
  }
  
  if (typeof(Storage) !== "undefined") {
    var color = sessionStorage.getItem("colorMode");
    if (color == null) {
      sessionStorage.setItem("colorMode", "dark-mode");
    }
    document.body.className = color;
  }
}
