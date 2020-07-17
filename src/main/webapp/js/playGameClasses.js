class progress {
  hintsFound = [];
  stageID;
  map;
  
  constructor(hintsFound) {
    this.hintsFound = hintsFound;
  }

  set addSingleHintFound(hint) {
    if (!this.hintsFound.includes(hint)) {
      this.hintsFound.push(hint);
    }
  }

  set addListOfHintsFound(hints) {
    for (let i = 0; i < hints.length; i++) {
      this.hintsFound.push(hints[i]);
    }
  }

  get getHintsFound() {
    return this.hintsFound;
  }

  get clearHintsFound() {
    this.hintsFound = [];
  }

  set setStageID(stageID) {
    this.stageID = stageID;
  }

  get getStageID() {
    return this.stageID;
  }

  set setMap(map) {
    this.map = map;
  }

  get getMap() {
    return this.map;
  }
}
