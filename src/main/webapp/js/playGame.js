function initMapPlayGame() {
  var fenway = {lat: 42.345573, lng: -71.098326};
  var map = new google.maps.Map(
    document.getElementById('playMap'), {
      zoom: 1, 
      center: fenway, 
      mapTypeId: 'hybrid',
      gestureHandling: 'greedy'
  });
  var panorama = new google.maps.StreetViewPanorama(
    document.getElementById('playMap'), {
    position: fenway,
      pov: {
        heading: 34,
        pitch: 10
      }
    });
  map.setStreetView(panorama);
}
