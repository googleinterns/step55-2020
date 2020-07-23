/**
* Class to contains data of the current game, used in playGame.js only
*/
class Progress {
  hintsFound;
  stageID;
  map;
  
  /**
  * Constructor for the class to set the hintsFound
  * @param {list} hintsFound a list of the current hints or [] to set it to empty
  */
  constructor(hintsFound) {
    this.hintsFound = hintsFound;
  }
  
  /**
  * Setter method to add a single hint to the hints found list
  * @param {int} hint the number for the hint found
  */
  set addSingleHintFound(hint) {
    if (!this.hintsFound.includes(hint)) {
      this.hintsFound.push(hint);
    }
  }

  /**
  * Setter method to add a mutiple hint to the hints found list
  * @param {list} hints a list with number for each hint
  */
  set addListOfHintsFound(hints) {
    for (let i = 0; i < hints.length; i++) {
      if (!this.hintsFound.includes(hints[i])) {
        this.hintsFound.push(hints[i]);
      }
    }
  }
  
  /**
  * Getter method to get the list of found hints
  * @return {list} a list of the number for each hint that has been found
  */
  get getHintsFound() {
    return this.hintsFound;
  }

  /**
  * Setter method to set the stageID
  * @param {string} stageID the stageID the current player is on and what to store in the stageID attribute of the class
  */
  set setStageID(stageID) {
    this.stageID = stageID;
  }
  
  /**
  * Getter method to get the stageID
  * @return {string} a string with the current stageID
  */
  get getStageID() {
    return this.stageID;
  }

  /**
  * Setter method to set the current map
  * @param {object} map is an object with the current map being played on 
  */
  set setMap(map) {
    this.map = map;
  }
  
  /**
  * Getter method to get the current map
  * @return {object} an object with the current map being played on 
  */
  get getMap() {
    return this.map;
  }

  /**
  * Method to clear the list of found hints
  */
  clearHintsFound() {
    this.hintsFound = [];
  }
}
