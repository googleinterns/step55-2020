function initMap() {
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