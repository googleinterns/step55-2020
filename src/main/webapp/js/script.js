var auth2;

/** 
* The function called each time a user clicks the sign-in button
*/
function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  var profile = googleUser.getBasicProfile();
  console.log("ID: " + profile.getId()); // Don't send this directly to your server!
  console.log('Full Name: ' + profile.getName());

  //console.log('is signed in?', hasSignedIn());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  console.log("ID Token: " + id_token);
  
   createNavBar('index');
}

/** 
* returns the sign-in status of the user
*/
function hasSignedIn() {
  return auth2.isSignedIn.get();
}

/** 
* The function called each time a user clicks the sign-out
*/
async function signOut() {
  auth2 = gapi.auth2.getAuthInstance();
  await auth2.signOut().then(function () {
    console.log('is signed in?', hasSignedIn());
  });
  createNavBar('index');
}

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

// Soon to be depreciated
/** 
* Creates the navigation bar and specifies which page is active
* @param {string} page is which HTML the navbar should be placed on
* @example createNavBar("index")
*/
async function createNavBar(page) {
//  var signinDiv  = document.getElementsByClassName('g-signin2')[0].cloneNode(true);
//   document.getElementsByClassName('g-signin2')[0].parentNode.removeChild(document.getElementsByClassName('g-signin2')[0]);
  let element = document.getElementById('nav-bar')
  element.innerHTML = '<div id="gSignInWrapper">'+
                        '<div id="customBtn" class="customGPlusSignIn">'+
                         '<span class="icon"></span>'+
                         '<span class="buttonText">Google</span>'+
                         '</div>'+
                      '</div>'+
                      '<div id="name"></div>';

  element = document.getElementById('customBtn');

//   console.log(element.id);
//   console.log(auth2);
//   auth2.attachClickHandler(element, {},
//     function(googleUser) {
//       document.getElementById('name').innerText = "Signed in: " + googleUser.getBasicProfile().getName();
//     }, function(error) {
//       alert(JSON.stringify(error, undefined, 2));
//     });


  var navbar = document.createElement('nav');

  let navWrapperDiv = document.createElement('div');
  navWrapperDiv.className = 'nav-wrapper';
  navbar.appendChild(navWrapperDiv);

  let containerDiv = document.createElement('div');
  containerDiv.className = 'container';
  navWrapperDiv.appendChild(containerDiv);

  let a = document.createElement('a');
  a.innerHTML = 'Street Explorer';
  a.href = "index.html";
  a.className = 'brand-logo';
  containerDiv.appendChild(a);

  let mobileA = document.createElement('a');
  mobileA.innerHTML = '<i class="material-icons">menu</i>';
  mobileA.href = "#";
  mobileA.className = 'sidenav-trigger';
  mobileA.dataset.target = 'mobile-demo';
  containerDiv.appendChild(mobileA);

  let ul = document.createElement('ul');
  ul.className = 'right hide-on-med-and-down';

  let liBrightness = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = '<i class=\'material-icons\' onclick=\'changeToOrFromDarkMode()\'>brightness_4</i>';
  a.href = "#";
  liBrightness.appendChild(a);

  let liHome= document.createElement('li');
  if (page == 'index') {
    liHome.className = 'active';
  }
  a = document.createElement('a');
  a.innerHTML = 'Home';
  a.href = "index.html";
  liHome.appendChild(a);

  let liCreateGame = document.createElement('li');
  if (page == 'createGame') {
    liCreateGame.className = 'active';
  }
  a = document.createElement('a');
  a.innerHTML = 'Create Game';
//   console.log('This console', await hasSignedIn())
  a.href = 'createGame.html';


  liCreateGame.appendChild(a);

  var liSignin = document.createElement('li');
//   var signinDiv = document.getElementsByClassName('g-signin2')[0].cloneNode(true);
  var signinAnchor = document.createElement('a');
  signinAnchor.href = '#';
  console.log( document.getElementsByClassName('g-signin2'))

//   signinAnchor.appendChild(signinDiv);
//   liSignin.appendChild(signinDiv);
  
  var liProfile = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = 'Profile';
  a.href = 'profilePage.html';
  liProfile.appendChild(a);

  ul.appendChild(liBrightness);
  ul.appendChild(liHome);
  if (await hasSignedIn()) {
    ul.appendChild(liCreateGame);
    ul.appendChild(liProfile);
  } 
//   ul.appendChild(liSignin);
  
  document.getElementById('nav-bar').innerHTML += '<ul class="sidenav" id="mobile-demo">' + 
                                                    '<li><a href="#"><i class="material-icons" onclick="changeToOrFromDarkMode()">brightness_4</i></a> </li>' + 
                                                    '<li><a href="index.html">Home</a> </li>' + 
                                                  '</ul>';

  let navBarForMobile = document.getElementById('mobile-demo');
    
  navBarForMobile.innerHTML += '<li><a href="createGame.html">Create Game</a> </li>' +  
                                 '<li><a href="profilePage.html">Profile</a> </li>';
  
  
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
  let staticImage = document.createElement('img');
  let staticMapURL = 'https://maps.googleapis.com/maps/api/staticmap?center=';
  staticMapURL += stageLocations[0].latitude + ',' + stageLocations[0].longitude;
  staticMapURL += '&size='+size+'x'+size+'&maptype=roadmap';
  for (let i = 0; i < stageLocations.length; i++) {
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
  let avgDifficulty = mapData.difficulty;

  let difficulty = 'Easy';
  let difficultyColor = 'green-text';
  if (avgDifficulty == 2) {
    difficulty = 'Medium';
    difficultyColor = 'orange-text';
  } else if (avgDifficulty == 3) {
    difficulty = 'Hard';
    difficultyColor = 'red-text';
  }

  let fiveStars = getStarRating(mapData.stars);

  let staticMapInfo = document.createElement('div');
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
  let avgStarsTemp = stars;
  
  let fullStar = '<i class="material-icons md-18">star</i>';
  let halfStar = '<i class="material-icons md-18">star_half</i>';
  let emptyStar = '<i class="material-icons md-18">star_border</i>';

  let fiveStars = '';
  for (let i = 0; i < 5; i++) {
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
    let featuredMap = createStaticMap(data[0].stageLocations, '400', data[0].gameID);
    let featuredMapCaption = createStaticMapCaption(data[0], 'featured-map-info');
    let featuredMapDiv = document.getElementById('featured-map');
    featuredMapDiv.classList.add('hoverable');
    featuredMapDiv.append(featuredMap);
    featuredMapDiv.append(featuredMapCaption);
    let allMaps = document.getElementById('all-maps');
    for (let i = 1; i < data.length; i++) {
      let mapDiv = document.createElement('div');
      mapDiv.classList.add('col');
      mapDiv.classList.add('hoverable');
      mapDiv.id = 'individual-map';

      let mapImage = createStaticMap(data[i].stageLocations, '300', data[i].gameID);
      let mapCaption = createStaticMapCaption(data[i], 'map-info');
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
    let color = localStorage.getItem("colorMode");
    if (color == null) {
      sessionStorage.setItem("colorMode", "light-mode");
    }
    document.body.className = color;
  }

  gapi.load('auth2', function() {
    gapi.auth2.init({
    client_id: '683964064238-ccubt4o7k5oc9pml8n72id8q1p1phukl.apps.googleusercontent.com',
    }).then(function(){
      auth2 = gapi.auth2.getAuthInstance();
      console.log('is signed in?', auth2.isSignedIn.get()); 
    });
    createNavBar(page);
  });

  // These next two lines are for mobile version so that when the three lines are clicked on a side bar is shown
  let elems = document.querySelectorAll('.sidenav');
  let instances = M.Sidenav.init(elems, {});
  
  if (page == 'playGame') {
    initMapToPlayGame();
  } else if (page == 'createGame') {
    initMapToCreateGame();
  } else if (page == 'afterGame') {
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
