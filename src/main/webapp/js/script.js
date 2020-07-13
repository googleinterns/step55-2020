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
    localStorage.setItem("colorMode", body.className);
  } 
}

// Soon to be depreciated
/** 
* Creates the navigation bar and specifies which page is active
* @param {string} page is which HTML the navbar should be placed on
* @param {boolean} loggedIn a boolean which is true if the user is logged in and false if not
* @param {string} url if loggedIn is true then this url contains the logout url and otherwise the login url
* @example createNavBar("index")
*/
async function createNavBar(page, loggedIn, url) {
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
  a.innerHTML = 'Home';
  a.href = "index.html";
  liHome.appendChild(a);

  var liCreateGame = document.createElement('li');
  if (page == 'createGame') {
      liCreateGame.className = 'active';
  }
  a = document.createElement('a');
  a.innerHTML = 'Create Game';
  a.href = 'createGame.html';
  
  var liLogin;
  var liLogout;
  var logoutUrl;
  liLogin = document.createElement('li');
  if (loggedIn) {
    liCreateGame.appendChild(a);
    
    a = document.createElement('a');
    a.innerHTML = 'My Profile';
    a.href = 'profilePage.html';
    
    liLogout = document.createElement('li');
    logoutUrl = document.createElement('a');
    logoutUrl.innerHTML = 'Log out';
    logoutUrl.href = url;
  } else {
    a = document.createElement('a');
    a.innerHTML = 'Login';
    a.href = url;
  }

  liLogin.appendChild(a);

  ul.appendChild(liBrightness);
  ul.appendChild(liHome);
  ul.appendChild(liCreateGame);
  ul.appendChild(liLogin);
  
  document.getElementById('nav-bar').innerHTML += '<ul class="sidenav" id="mobile-demo">' + 
                                                    '<li><a href="#"><i class="material-icons" onclick="changeToOrFromDarkMode()">brightness_4</i></a> </li>' + 
                                                    '<li><a href="index.html">Home</a> </li>' + 
                                                  '</ul>';

  var navBarForMobile = document.getElementById('mobile-demo');
  
  if (liLogout != undefined) {
    liLogout.appendChild(logoutUrl);
    ul.appendChild(liLogout);
    navBarForMobile.innerHTML += '<li><a href="createGame.html">Create Game</a> </li>' +  
                                 '<li><a href="profilePage.html">My Profile</a> </li>'+ 
                                 '<li><a href="'+url+'">Log out</a> </li>';
  } else {
    navBarForMobile.innerHTML += '<li><a href="'+url+'">Log in</a> </li>';
  }
  
  containerDiv.appendChild(ul);

  document.getElementById('nav-bar').appendChild(navbar);
}

/** 
* Creates a static maps image
* @param {array} stageLocations contains objects with longitude and latitude
* @param {string} size is dimension of the static image in pixels (ex: '200' for a 200x200 image)
* @param {String} gameID is gameID from the server of the game that was clicked on
* @return {Element} an img element is returned with the stage starting locations marked on the image
*/
function createStaticMap(stageLocations, size, gameID) { 
  var staticImage = document.createElement('img');
  var staticMapURL = 'https://maps.googleapis.com/maps/api/staticmap?center=';
  staticMapURL += stageLocations[0].latitude + ',' + stageLocations[0].longitude;
  staticMapURL += '&size='+size+'x'+size+'&maptype=roadmap';
  for (var i = 0; i < stageLocations.length; i++) {
    staticMapURL += '&markers=color:red%7C' + stageLocations[i].latitude + ',' + stageLocations[i].longitude;
  }
  staticMapURL += '&key=AIzaSyDtRpnDqBAeTBM0gjAXIqe2u5vBLj15mtk';
  staticImage.src = staticMapURL;
  staticImage.classList.add('cursor-pointer');

  staticImage.addEventListener('click', function() {
    window.location.replace('gameInfo.html?gameID=' + gameID);
  });
  return staticImage;
}

/** 
* Creates below the image the information of the game
* @param {string} mapData the JSON data with the map's information
* @param {string} captionID the id of the game info that is under the static image
*/
function createStaticMapCaption(mapData, captionID) {
  var avgDifficulty = mapData.difficulty;

  var difficulty = 'Easy';
  var difficultyColor = 'green-text';
  if (avgDifficulty == 2) {
    difficulty = 'Medium';
    difficultyColor = 'orange-text';
  } else if (avgDifficulty == 3) {
    difficulty = 'Hard';
    difficultyColor = 'red-text';
  }

  var fiveStars = getStarRating(mapData.stars);

  var staticMapInfo = document.createElement('div');
  staticMapInfo.id = captionID;
  staticMapInfo.innerHTML = '<div style="float:right">' + fiveStars + '</div>';
  staticMapInfo.innerHTML += '<div id="title-div">' + mapData.gameName + ' </div><i class="' + difficultyColor + '">[' + difficulty + "]</i>";
  staticMapInfo.innerHTML += '<br> By ' + mapData.creatorUsername;
  staticMapInfo.style = 'text-align:left; padding: 6px';
  staticMapInfo.classList.add('cursor-pointer');

  staticMapInfo.addEventListener('click', function() {
    window.location.replace('gameInfo.html?gameID=' + mapData.gameID);
  });

  return staticMapInfo;
}

/** 
* Creates below the image the star ratings of the map
* @param {int} stars number of stars to be shown
*/
function getStarRating(stars) {
  var avgStarsTemp = stars;
  
  var fullStar = '<i class="material-icons md-18">star</i>';
  var halfStar = '<i class="material-icons md-18">star_half</i>';
  var emptyStar = '<i class="material-icons md-18">star_border</i>';

  var fiveStars = '';
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
* Adds a featured map where the ID 'featured-map' is and the rest of the maps where the ID 'all-maps' is
*/
function loadMaps() {
  fetch('/load-mainpage-data').then(response => response.json()).then(async (data) => {
    var featuredMap = createStaticMap(data[0].stageLocations, '400', data[0].gameID);
    console.log(data[0].stageLocations[0].latitude)
    console.log(data[0].stageLocations[0].longitude)
    var featuredMapCaption = createStaticMapCaption(data[0], 'featured-map-info');
    var featuredMapDiv = document.getElementById('featured-map');
    featuredMapDiv.classList.add('hoverable');
    featuredMapDiv.append(featuredMap);
    featuredMapDiv.append(featuredMapCaption);
    var allMaps = document.getElementById('all-maps');
    for (var i = 1; i < data.length; i++) {
        var mapDiv = document.createElement('div');
        mapDiv.classList.add('col');
        mapDiv.classList.add('hoverable');
        mapDiv.id = 'individual-map';

        var mapImage = createStaticMap(data[i].stageLocations, '300', data[i].gameID);
        var mapCaption = createStaticMapCaption(data[i], 'map-info');
        mapImage.classList.add('materialbox');
        mapImage.classList.add('responsive-img');
        mapImage.classList.add('width300');

        mapCaption.classList.add('materialbox');
        mapCaption.classList.add('width300');

        mapDiv.append(mapImage)
        mapDiv.append(mapCaption)
        allMaps.append(mapDiv);
    }
  });
}

/** 
* This function is a wrapper function for all of the functions to be called onload of any page
* @param {string} page is which page the onLoadFunction is being called from without the .html 
* @example onLoadFunction("index")
*/
async function onLoadFunctions(page) {
  if (typeof(Storage) !== "undefined") {
    var color = localStorage.getItem("colorMode");
    if (color == null) {
      localStorage.setItem("colorMode", "light-mode");
    }
    document.body.className = color;
  }

  var loggedIn = false;
  var url;
  await fetch('/load-authentication-data').then(response => response.json()).then(async (data) => {
    loggedIn = data.loggedIn;
    if (data.loggedIn) {
      url = data.logoutUrl;
    } else {
      url = data.loginUrl;
    }
  }); 

  createNavBar(page, loggedIn, url);

  // These next two lines are for mobile version so that when the three lines are clicked on a side bar is shown
  var elems = document.querySelectorAll('.sidenav');
  var instances = M.Sidenav.init(elems, {});
  
  if (page == 'playGame') {
    initMapToPlayGame();
  } else if (page == 'createGame') {
    if (!loggedIn) {
      window.location.replace(url);
    }
    initMapToCreateGame();
  }  else if (page == 'afterGame') {
    loadGameName();
  } else if (page == 'resumeOrStartOver') {
    checkIfUserHasSavedProgress();
  } else if (page == 'profilePage') {
    getUserName();
  } else if (page == 'gameInfo') {
    loadGameData();
  } else if (page == 'index') {
    loadMaps();
 	$(document).ready(function(){
      $('.materialbox').materialbox();
 	});
  }
}
