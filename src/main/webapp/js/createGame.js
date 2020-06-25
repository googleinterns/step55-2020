function addStage() {
    var stagesDiv = document.getElementById('stages');
    const numStages = stagesDiv.getElementsByTagName('input').length;

    const newStage = document.createElement('input');
    newStage.id = 'stage' + (numStages + 1);
    newStage.value = 'Stage ' + (numStages + 1);
    newStage.type= 'button';
    stagesDiv.appendChild(newStage);
}