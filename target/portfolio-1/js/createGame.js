function addNewStage() {
    const activeStages = document.getElementsByClassName('activeStage');
    activeStages[0].classList.remove('activeStage');
    const stagesUl = document.getElementById('stages');
    const numStages = stagesUl.getElementsByTagName('input').length;

    const newStage = document.createElement('input');
    newStage.value = 'Stage ' + (numStages + 1);
    newStage.type= 'button';
    newStage.className = 'stage' + (numStages + 1) + ' activeStage';

    const a = document.createElement('a');
    a.href = '#stage' + (numStages + 1);

    a.appendChild(newStage);
    stagesUl.appendChild(a);

    const newStageHints = document.createElement('div');
    newStageHints.id = newStage.classList[0] + 'Hints';
    newStageHints.innerHTML = "<input type='text' placeholder='Starter (spawn location and first hint)' id='starter-hint' required>" +
                                "<input type='text' placeholder='Hint 1' id='hint1'>"

    document.getElementById('hints').appendChild(newStageHints);
}

function addNewHint() {
    const activeStageNum = (document.getElementsByClassName('activeStage')[0].classList)[0] + 'Hints';
    console.log(activeStageNum)
    const activeHints = document.getElementById(activeStageNum);
    console.log(activeHints)
    const numHints = activeHints.getElementsByTagName('input').length;
    console.log(numHints)

    const newHint = document.createElement('input');
    newHint.id = 'hint' + (numHints);
    newHint.placeholder = 'Hint ' + (numHints);
    newHint.type= 'text';
    activeHints.appendChild(newHint);
    console.log(newHint)
    console.log(activeHints)
}

function setActive() {
    const activeStages = document.getElementsByClassName('activeStage');
    activeStages[0].classList.remove('activeStage');

}
