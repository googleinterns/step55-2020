function addNewStage() {
    const activeStages = document.getElementsByClassName('activeStage');
    activeStages[0].classList.remove('activeStage');
    const stagesUl = document.getElementById('stages');
    const numStages = stagesUl.getElementsByTagName('input').length;

    const newStage = document.createElement('input');
    newStage.id = 'stage' + (numStages + 1);
    newStage.value = 'Stage ' + (numStages + 1);
    newStage.type= 'button';
    newStage.className = 'activeStage';

    const a = document.createElement('a');
    a.href = '#stage' + (numStages + 1);

    a.appendChild(newStage);
    stagesUl.appendChild(a);

    const newStageHints = document.createElement('div');
    newStageHints.className = newStage.id;

    document.getElementById('hints').appendChild(newStageHints);
}

function addNewHint() {
    const activeStageID = document.getElementsByClassName('activeStage')[0].id;
    const activeHints = document.getElementsByClassName(activeStageID);
    const numHints = activeHints[0].getElementsByTagName('input').length;

    const newHint = document.createElement('input');
    newHint.id = 'hint' + (numHints);
    newHint.placeholder = 'Hint ' + (numHints);
    newHint.type= 'text';
    document.getElementsByClassName(activeStageID)[0].appendChild(newHint);
}
