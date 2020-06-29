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
}

function onLoadFunctions(page) {
  createNavBar(page);
  if (page == 'playGame') {
    initMapPlayGame();
  } else if (page == 'createGame') {
    initMapCreateGame();
  }
  
  if (typeof(Storage) !== "undefined") {
    var color = sessionStorage.getItem("colorMode");
    if (color == null) {
      sessionStorage.setItem("colorMode", "dark-mode");
    }
    document.body.className = color;
  }
}
