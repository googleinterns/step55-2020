function addNewStage() {
    const activeStages = document.getElementsByClassName('activeStage')[0];
    activeStages.classList.remove('activeStage');
    const divToHide = activeStages.classList[0];
    document.getElementById(divToHide + 'Hints').classList.add('hidden');
    const stagesList = document.getElementById('stages');
    const numStages = stagesList.getElementsByTagName('input').length;

    const newStage = document.createElement('input');
    newStage.value = 'Stage ' + (numStages + 1);
    newStage.type= 'button';
    newStage.className = 'stage' + (numStages + 1) + ' activeStage';

    newStage.addEventListener("click", function(){
        setActive('stage' + (numStages + 1));
    });

    const a = document.createElement('a');
    a.href = '#stage' + (numStages + 1);

    a.appendChild(newStage);
    stagesList.appendChild(a);

    const newStageHints = document.createElement('div');
    newStageHints.id = newStage.classList[0] + 'Hints';
    newStageHints.innerHTML = "<input type='text' placeholder='Starter (spawn location and first hint)' id='starter-hint' required>" +
                                "<input type='text' placeholder='Hint 1' id='hint1'>";

    document.getElementById('hints').appendChild(newStageHints);
}

function addNewHint() {
    const activeStageNum = (document.getElementsByClassName('activeStage')[0]).classList[0] + 'Hints';
    const activeHints = document.getElementById(activeStageNum);
    const numHints = activeHints.getElementsByTagName('input').length;

    const newHint = document.createElement('input');
    newHint.id = 'hint' + (numHints);
    newHint.placeholder = 'Hint ' + (numHints);
    newHint.type= 'text';
    activeHints.appendChild(newHint);
}

function setActive(toSetActive) {
    const activeStages = document.getElementsByClassName('activeStage')[0];
    if (activeStages.classList[0] == toSetActive) return;
    activeStages.classList.remove('activeStage');
    const divToHide = activeStages.classList[0];
    document.getElementById(divToHide + 'Hints').classList.add('hidden');
    
    const newActiveStage = document.getElementsByClassName(toSetActive)[0];
    newActiveStage.classList.add('activeStage');
    document.getElementById(toSetActive + 'Hints').classList.remove('hidden');
    console.log(document.getElementsByClassName('hidden'))
}
