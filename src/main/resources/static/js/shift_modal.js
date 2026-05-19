import {BASE_API_URL, sendRequestTo} from "./util.js";

export function openShiftModal() {
    let modalBackground = document.getElementById('shift-modal-background');
    modalBackground.classList.remove('hidden');
}

function getLoggedInGuard() {
    return JSON.parse(sessionStorage.getItem('loggedInGuard'));
}

async function guardHasRegistration(guard, shift) {
    return await sendRequestTo(BASE_API_URL + 'registrations/' + shift.id + '/has-registration/' + guard.guardId);
}

async function fetchRegisteredGuardNamesForShift(shift) {
    return await sendRequestTo(BASE_API_URL + 'registrations/guards/' + shift.id);
}

async function registerForJob(shift) {
    await sendRequestTo(BASE_API_URL + `registrations/by-guard-id-and-shift-id/${getLoggedInGuard().guardId}-${shift.id}`, {method: 'POST'});
}

async function unregisterForJob(shift) {
    await sendRequestTo(BASE_API_URL + `registrations/delete-by-shift-and-guard/${shift.id}-${getLoggedInGuard().guardId}`, {method: 'DELETE'});
}

async function buildPeopleColumn(shift) {
    let peopleColumn = document.createElement('div');
    peopleColumn.id = 'shift-modal-people-column';

    let peopleHeader = document.createElement('h2');
    peopleHeader.id = 'shift-modal-people-column-header';
    peopleHeader.textContent = 'Guards working';

    let scrollableListWrapper = document.createElement('div');
    scrollableListWrapper.id = 'shift-modal-people-column-list-wrapper';

    let peopleList = document.createElement('ul');
    peopleList.id = 'shift-modal-people-column-list';

    let guards = await fetchRegisteredGuardNamesForShift(shift);

    guards.forEach(registrant => {
        let listEntry = document.createElement('li');
        listEntry.classList.add('shift-modal-people-column-list-element');
        listEntry.textContent = registrant.name;
        listEntry.title = `Phone: ${registrant.phoneNumber}`;
        peopleList.appendChild(listEntry);
    });

    scrollableListWrapper.appendChild(peopleList);
    peopleColumn.appendChild(peopleHeader);
    peopleColumn.appendChild(scrollableListWrapper);

    if (await guardHasRegistration(getLoggedInGuard(), shift)) {
        let unregisterButton = document.createElement('button');
        unregisterButton.id = 'shift-modal-unregistration-button';
        unregisterButton.classList.add('btn-danger');
        unregisterButton.textContent = 'Unregister';
        unregisterButton.addEventListener('click', () => unregisterForJob(shift));
        peopleColumn.appendChild(unregisterButton);
    } else {
        if (shift.requiredGuards - shift.registrations !== 0) {
            let registrationButton = document.createElement('button');
            registrationButton.id = 'shift-modal-registration-button';
            registrationButton.textContent = 'Register';
            registrationButton.addEventListener('click', () => registerForJob(shift));
            peopleColumn.appendChild(registrationButton);
        }
    }

    return peopleColumn;
}

function buildFieldColumn(shift) {
    let fieldColumn = document.createElement('div');
    fieldColumn.id = 'shift-modal-field-column';

    let title = document.createElement('h2');
    title.id = 'shift-modal-title';
    title.textContent = shift.title;

    let clientInfo = document.createElement('p');
    clientInfo.id = 'shift-modal-client-info';
    clientInfo.innerHTML = `<p>For <strong>${shift.clientName}</strong> (phone: ${shift.clientPhoneNumber})</p>`;

    let description = document.createElement('p');
    description.id = 'shift-modal-description';
    description.textContent = shift.description;
    description.title = shift.description;

    let parkingInstructions = document.createElement('p');
    parkingInstructions.id = 'shift-modal-parking-instructions';
    parkingInstructions.innerHTML = `<p><strong>Parking:</strong> ${shift.parkingInstructions}</p>`;

    let foodServed = document.createElement('p');
    foodServed.id = 'shift-modal-food-served'
    foodServed.innerHTML = `<p><strong>Food:</strong> ${shift.foodServed ? 'Food is served' : 'No food service'}</p>`;

    let location = document.createElement('p');
    location.id = 'shift-modal-location';
    location.innerHTML = `<p><strong>Address:</strong> ${shift.location}</p>`;

    let italicized = document.createElement('i');
    let startEndDate = document.createElement('p');
    startEndDate.id = 'shift-modal-date-field';
    startEndDate.innerHTML = `<p><strong>When:</strong> ${shift.shiftStart.replace('T', ' ')} - ${shift.shiftEnd.replace('T', ' ')}</p>`;
    italicized.appendChild(startEndDate);

    let small = document.createElement('small');
    let participants = document.createElement('p');
    participants.id = 'shift-modal-registration-field';
    participants.innerHTML = `<p><strong>Registrations:</strong> ${shift.registrations} / ${shift.requiredGuards}</p>`;
    small.appendChild(participants);

    fieldColumn.appendChild(title);
    fieldColumn.appendChild(clientInfo);
    fieldColumn.appendChild(description);
    fieldColumn.appendChild(parkingInstructions);
    fieldColumn.appendChild(foodServed);
    fieldColumn.appendChild(location);
    fieldColumn.appendChild(italicized);
    fieldColumn.appendChild(small);

    return fieldColumn;
}

export async function buildModal(shift) {
    let modalBackground = document.createElement('div');
    modalBackground.id = 'shift-modal-background';
    modalBackground.classList.add('hidden');

    let modal = document.createElement('div');
    modal.id = 'shift-modal';

    let closeButton = document.createElement('button');
    closeButton.id = 'shift-modal-close-button';
    closeButton.textContent = 'x';
    closeButton.addEventListener('click', () => modalBackground.classList.add('hidden'));
    closeButton.title = 'Close this window';

    modal.appendChild(closeButton);
    modal.appendChild(buildFieldColumn(shift));
    modal.appendChild(await buildPeopleColumn(shift));
    modalBackground.appendChild(modal);

    return modalBackground;
}