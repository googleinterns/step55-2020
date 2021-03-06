var auth2;

// Deprecated
/** 
* initalizes the GoogleAuth instance and creates the nav bar
* @param {string} page is which HTML the navbar should be placed on
*/
async function init(page) {
  await gapi.load('auth2', async function() {
    await gapi.auth2.init({
      client_id: '683964064238-ccubt4o7k5oc9pml8n72id8q1p1phukl.apps.googleusercontent.com',
    })
    auth2 = await gapi.auth2.getAuthInstance();
    createNavBar(page);
  }); 
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
    localStorage.setItem("colorMode", body.className);
  } 
}

/** 
* The function called when the user signs in
*/
function onSignIn() {
  let params = new URLSearchParams();
  let tokenEmailDict = tokenAndEmail();
  params.append('email', tokenEmailDict['email']);
  params.append('idToken', tokenEmailDict['token']);
  let request = new Request('create-userid-data', {method: 'POST', body: params});
  fetch(request);
}

/** 
* Checks to see if a user is signed in or not
* @return {boolean} the sign-in status of the user 
*/
function isSignedIn() {
  return auth2.isSignedIn.get();
}

/** 
* This signs out the user then reproduces the nav bar
* @param {string} page is which HTML the navbar should be placed on
*/
async function signOut(page) {
  auth2.signOut().then(function () {
    createNavBar(page);
  });
}

/**
* Gets the token and email of the current user
* @return a dictionary with a token and the email the google sign in API
*/
function tokenAndEmail() {
  let googleUser = auth2.currentUser.get();
  var id_token = googleUser.getAuthResponse().id_token;
  var profile = googleUser.getBasicProfile();
  var email = profile.getEmail(); 
  return {"email": email, "token": id_token};
}

/** 
* Creates the side nav bar for if the screen is small. Typically for mobile use
* @param {string} page is which HTML the navbar should be placed on
*/
function createSideNav(page) {
  document.getElementById('nav-bar').innerHTML += '<ul class="sidenav" id="mobile-demo">' + 
                                                    '<li><a href="#"><i class="material-icons" onclick="changeToOrFromDarkMode()">brightness_4</i></a> </li>' + 
                                                    '<li><a href="index.html">Home</a> </li>' + 
                                                  '</ul>';

  let navBarForMobile = document.getElementById('mobile-demo');
  if (isSignedIn()) {
    navBarForMobile.innerHTML += '<li><a href="createGame.html">Create Game</a> </li>' +  
                                 '<li><a href="profilePage.html">Profile</a> </li>' + 
                                 '<li><a href="#" onclick="signOut(\''+page+'\')">Sign Out</a> </li>';
  }
  let googleSignIn = document.createElement('div');
  googleSignIn.id = 'gSignInWrapper';
  googleSignIn.innerHTML += '<div id="customBtn2" class="customGPlusSignIn">'+
                               '<span class="icon"></span>' +
                               '<span class="buttonText">Sign in with Google</span></div>';
                               
  if (isSignedIn()) {
    googleSignIn.classList.add("hidden");
  } 

  navBarForMobile.appendChild(googleSignIn);

  auth2.attachClickHandler(document.getElementById('customBtn2'), {},
    function(googleUser) { 
        onSignIn();
        createNavBar(page)
    }, function(error) {
      alert(JSON.stringify(error, undefined, 2));
  });

  // These next two lines are for mobile version so that when the three lines are clicked on a side bar is shown
  let elems = document.querySelectorAll('.sidenav');
  let instances = M.Sidenav.init(elems, {});
}

/** 
* Creates the navigation bar and specifies which page is active
* @param {string} page is which HTML the navbar should be placed on
* @example createNavBar("index")
*/
async function createNavBar(page) {
  let elems = document.querySelectorAll('.sidenav-overlay');
  if (elems != undefined) {
    for (let elem = 0; elem < elems.length; elem++) {
        elems[elem].style="display: none;"
    }
  }
  
  document.getElementById('nav-bar').innerHTML = "";
  createSideNav(page);
  let navbar = document.createElement('nav');

  let navWrapperDiv = document.createElement('div');
  navWrapperDiv.className = 'nav-wrapper';
  navbar.appendChild(navWrapperDiv);

  let a = document.createElement('a');
  a.innerHTML = 'Street Explorer';
  a.href = "index.html";
  a.className = 'brand-logo';
  navWrapperDiv.appendChild(a);

  let mobileA = document.createElement('a');
  mobileA.innerHTML = '<i class="material-icons">menu</i>';
  mobileA.href = "#";
  mobileA.className = 'sidenav-trigger';
  mobileA.dataset.target = 'mobile-demo';
  navWrapperDiv.appendChild(mobileA);

  let ul = document.createElement('ul');
  ul.className = 'right hide-on-med-and-down';

  let liBrightness = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = '<i class=\'material-icons\'>brightness_4</i>';
  a.href = "#";
  a.addEventListener('click', function() {
    changeToOrFromDarkMode();
  });
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
  a.href = 'createGame.html';
  liCreateGame.appendChild(a);

  var liSignin = document.createElement('li');
  var signinAnchor = document.createElement('a');
  signinAnchor.href = '#';

  let googleSignIn = document.createElement('div');
  googleSignIn.id = 'gSignInWrapper';
  googleSignIn.innerHTML += '<div id="customBtn" class="customGPlusSignIn">'+
                               '<span class="icon"></span>' +
                               '<span class="buttonText">Sign In with Google</span></div>';
                        
  signinAnchor.appendChild(googleSignIn);
  liSignin.appendChild(signinAnchor);
  
  var liProfile = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = 'Profile';
  a.href = 'profilePage.html';
  liProfile.appendChild(a);

  var liSignout = document.createElement('li');
  a = document.createElement('a');
  a.innerHTML = 'Sign Out';
  if (page == 'profilePage') {
      a.href = 'index.html';
  } else {
    a.href = '#';
  }
  a.addEventListener('click', function() {
    signOut(page);
  });

  liSignout.appendChild(a);

  ul.appendChild(liBrightness);
  ul.appendChild(liHome);
  if (await isSignedIn()) {
    ul.appendChild(liCreateGame);
    ul.appendChild(liProfile);
    ul.appendChild(liSignout);
    liSignin.classList.add("hidden");
  } 
  ul.appendChild(liSignin);

  navWrapperDiv.appendChild(ul);

  document.getElementById('nav-bar').appendChild(navbar);
  
  auth2.attachClickHandler(document.getElementById('customBtn'), {},
    function(googleUser) { 
        onSignIn();
        createNavBar(page)
    }, function(error) {
      alert(JSON.stringify(error, undefined, 2));
  });
}

/** 
* Creates a static maps image
* @param {array} stageLocations contains objects with longitude and latitude
* @param {string} size is dimension of the static image in pixels (ex: '200' for a 200x200 image)
* @return {Element} an img element is returned with the stage starting locations marked on the image
*/
function createStaticMap(stageLocations, size, mapKey) { 
  let staticImage = document.createElement('img');
  let staticMapURL = 'https://maps.googleapis.com/maps/api/staticmap?center=';
  staticMapURL += stageLocations[0].latitude + ',' + stageLocations[0].longitude;
  staticMapURL += '&size='+size+'x'+size+'&maptype=roadmap';
  for (let i = 0; i < stageLocations.length; i++) {
    staticMapURL += '&markers=color:red%7C' + stageLocations[i].latitude + ',' + stageLocations[i].longitude;
  }
  staticMapURL += '&key=' + mapKey;
  staticImage.src = staticMapURL;
  staticImage.classList.add('cursor-pointer');
  return staticImage;
}

/** 
* Creates below the image the information of the game
* @param {string} mapData the JSON data with the map's information
* @param {string} captionID the id of the game info that is under the static image
*/
function createStaticMapCaption(mapData, captionID) {
  let avgDifficulty = Math.round(mapData.difficulty);

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
    window.location = 'gameInfo.html?gameID=' + mapData.gameID;
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
* Adds a featured map where the ID 'featured-map' is and to where the ID 'all-maps' is
*/
function loadMaps(mapKey) {
  // Gets rid of the loading gif once the maps are loaded
  var gamesLoaded = false;
  var intervalId = window.setInterval(function() {
    if (gamesLoaded) {
      window.clearInterval(intervalId);
      document.getElementById("loading").classList.add('fade-out');
    }
  }, 100);

  fetch('/load-mainpage-data?page='+0).then(response => response.json()).then(async (data) => {
    var featuredMapText = document.getElementById('featured-map-text');
    if (data.length == 0) {
       featuredMapText.innerHTML = "No maps to play. Log in to create a map!";
       gamesLoaded = true;
       return;
    }

    let moreMapsButtonDiv = document.getElementById('button-for-more-maps');
    moreMapsButtonDiv.innerHTML = "<input type='button' id='more-maps' value='Load More Maps' onclick='loadMoreMaps(1)'/>";
    featuredMapText.innerHTML = "Featured Map:";
    let featuredMap = createStaticMap(data[0].stageLocations, '400', mapKey);
    let featuredMapCaption = createStaticMapCaption(data[0], 'featured-map-info');
    let featuredMapDiv = document.getElementById('featured-map');
    featuredMapDiv.classList.add('hoverable');
    featuredMapDiv.append(featuredMap);
    featuredMapDiv.append(featuredMapCaption);
    featuredMapDiv.addEventListener('click', function() {
      window.location = 'gameInfo.html?gameID=' + data[0].gameID;
    });

    if (data.length == 0 || data.length < 20) {
      let moreMapsButton = document.getElementById('more-maps');
      moreMapsButton.className = 'hidden';
    }
    removedFirst = data.splice(1,data.length);
    addMaps(removedFirst);
    gamesLoaded = true;
  });
}

/** 
* Fetches the data of the maps that will be added to the maps to the page
*/
function loadMoreMaps(pageNum) {
  fetch('/load-mainpage-data?page='+pageNum).then(response => response.json()).then((data) => {
    addMaps(data);
    let moreMapsButton = document.getElementById('more-maps');
    if (data.length == 0 || data.length < 20) {
      moreMapsButton.className =  'hidden';
    } else {
      moreMapsButton.setAttribute( "onClick", "loadMoreMaps("+ (pageNum + 1) +")" );
    }
  });
}

/** 
* Adds maps where the ID 'all-maps' is
* @param {object} data the information retrieved from '/load-mainpage-data' servlet
*/
async function addMaps(data) {
  let mapKey;
  await fetch('/load-mapsapikey-data').then(response => response.json()).then((data) => {
    mapKey = data;
  });
  let allMaps = document.getElementById('all-maps');
  for (let i = 0; i < data.length; i++) {
    let mapDiv = document.createElement('div');
    mapDiv.classList.add('col');
    mapDiv.classList.add('hoverable');
    mapDiv.id = 'individual-map';

    let mapImage = createStaticMap(data[i].stageLocations, '300', mapKey);
    mapImage.addEventListener('click', function() {
      window.location = 'gameInfo.html?gameID=' + data[i].gameID;
    });
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
}

// Deprecated
/** 
* Stops the JS from executing any code for the amount of miliseconds provided
* @param {int} ms number miliseconds to stop the JS for
*/
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
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
      localStorage.setItem("colorMode", "light-mode");
    }
    document.body.className = color;
  }

  if (document.body.className == "null" || document.body.className == "undefined") {
    document.body.className = "light-mode";
  }

  let mapKey;
  await fetch('/load-mapsapikey-data').then(response => response.json()).then((data) => {
    mapKey = data;
  });

//   <meta name="google-signin-client_id" content="683964064238-ccubt4o7k5oc9pml8n72id8q1p1phukl.apps.googleusercontent.com">
  let apiScript = document.createElement('script');
  apiScript.src = 'https://maps.googleapis.com/maps/api/js?key='+mapKey+'&libraries=places';
  document.head.appendChild(apiScript);

  let clientID;
  await fetch('/load-clientid-data').then(response => response.json()).then((data) => {
    clientID = data;
  });

  let googleClientID = document.createElement('meta');
  googleClientID.content = clientID;
  googleClientID.name = "google-signin-client_id";
  document.head.appendChild(googleClientID);

  gapi.load('auth2', async function() {
    gapi.auth2.init({
      client_id: '683964064238-ccubt4o7k5oc9pml8n72id8q1p1phukl.apps.googleusercontent.com',
    })
    auth2 = await gapi.auth2.getAuthInstance();
    createNavBar(page);

    if (page == 'playGame') {
      initMapToPlayGame();
    } else if (page == 'createGame') {
      if(!isSignedIn()) {
        window.alert('You need to sign in to create a game!');
      }
      initMapToCreateGame();
    } else if (page == 'afterGame') {
      loadGameName();
    } else if (page == 'resumeOrStartOver') {
      // checkIfUserHasSavedProgress();
    } else if (page == 'profilePage') {
      loadProfilePage();
    } else if (page == 'gameInfo') {
      loadGameData();
    } else if (page == 'index') {
      loadMaps(mapKey);
      $(document).ready(function(){
        $('.materialbox').materialbox();
      });
    }
  }); 
  
}
