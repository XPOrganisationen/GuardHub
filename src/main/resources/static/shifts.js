import {BASE_API_URL, sendRequestTo, shiftDateToTimeString} from "./util.js";
import {openShiftModal, buildModal} from "./shift_modal.js";

window.addEventListener('DOMContentLoaded', init);

let weekOffset = 0;
let view = 'all';

async function init() {
    let exampleGuard = {
        guardId: 2,
        name: "Bob Rivera",
        email: "bob.rivera@example.com",
        password: "$2b$12$guardhash1",
        phoneNumber: "+12125550202",
        role: "GUARD"
    };

    sessionStorage.setItem('loggedInGuard', JSON.stringify(exampleGuard));

    let allShiftsButton = document.getElementById('all_shifts');
    let myShiftsButton = document.getElementById('my_shifts');
    allShiftsButton.addEventListener('click', () => handleChangeViewClick('all'));
    myShiftsButton.addEventListener('click', () => handleChangeViewClick('mine'));

    let goBackOneWeekButton = document.getElementById('calendar-week-navigation-btn-left');
    let goForwardOneWeekButton = document.getElementById('calendar-week-navigation-btn-right');
    goBackOneWeekButton.addEventListener('click', handleGoBackOneWeek);
    goForwardOneWeekButton.addEventListener('click', handleGoForwardOneWeek);

    await renderSchedule(await getShiftsForWeek('all', 0));
}

// weekOffset is the number of weeks either before (if weekOffset < 0), on (== 0) or after (> 0) the current week.
async function getShiftsForWeek(allOrMine, weekOffset) {
    if (allOrMine === 'all') {
        return await sendRequestTo(BASE_API_URL + `shifts?weekOffset=${weekOffset}`);
    } else {
        let loggedInGuard = JSON.parse(sessionStorage.getItem('loggedInGuard'));
        return await sendRequestTo(BASE_API_URL + `shifts/for-guard/${loggedInGuard.guardId}?weekOffset=${weekOffset}`);
    }
}

async function handleGoBackOneWeek() {
    await renderSchedule(await getShiftsForWeek(view, --weekOffset));
}

async function handleGoForwardOneWeek() {
    await renderSchedule(await getShiftsForWeek(view, ++weekOffset));
}

async function handleChangeViewClick(toView) {
    if (toView !== 'all' && toView !== 'mine') {
        console.error('handleChangeViewClick takes one of two options as argument: "all" or "mine".');
        return;
    }

    let allShiftsButton = document.getElementById('all_shifts');
    let myShiftsButton = document.getElementById('my_shifts');
    let activeButton = allShiftsButton.classList.contains('calendar-view-button-active') ? 'all' : 'mine';

    if (toView === activeButton) {
        return;
    }

    allShiftsButton.classList.toggle('calendar-view-button-active');
    myShiftsButton.classList.toggle('calendar-view-button-active');

    if (toView === 'all') {
        view = 'all';
        await renderSchedule(await getShiftsForWeek('all', weekOffset));
    } else {
        view = 'mine';
        await renderSchedule(await getShiftsForWeek('mine', weekOffset));
    }
}

async function renderSchedule(shiftsForOneWeek) {
    let scheduleContainer = document.getElementById('shift-schedule-container');
    scheduleContainer.innerHTML = '';

    scheduleContainer.appendChild(await buildShiftScheduleColumns(shiftsForOneWeek, weekOffset));
}

function allWeekDatesFromReference(reference = new Date()) { // Default constructor = today
    let today = reference;
    today.setHours(0, 0, 0, 0);

    // JS Date starts with 0 = Sunday, but we want 0 = Monday.
    // Say it's Sunday, then today.getDay() returns 0.
    // Adding 6 to it yields 6.
    // 6 % 7 = 6.
    // As such, day now has Sunday = 6 in the new interpretation of Date.
    // Monday in JS Date is 1, which is added to six, then 7 mod 7 yields zero.
    // And so on... The end result is that Monday = 0, ..., Sunday = 6

    const day = (today.getDay() + 6) % 7;
    const monday = new Date(today);
    monday.setDate(today.getDate() - day);

    return Array.from({length: 7}, (_, i) => {
        const dt = new Date(monday);
        dt.setDate(monday.getDate() + i);
        return shiftDateToDateString(dt);
    });
}

function makeWeekdayLabels(weekdays, dates) {
    let weekdayLabels = [];
    let manyOrFew = weekdays.length > dates.length ? 'many' : 'few';

    if (weekdays.length !== dates.length) {
        console.error(`Too ${manyOrFew} weekdays (${weekdays.length}) for the number of dates (${dates.length}).`);
        return weekdayLabels;
    }

    for (let i = 0; i < weekdays.length; i++) {
        weekdayLabels.push(weekdays[i] + ' (' + dates[i] + ')');
    }

    return weekdayLabels;
}

function shiftDateToDateString(shiftDate) {
    let firstStartTimeInList = shiftDate;
    return `${firstStartTimeInList.getDate()}-${firstStartTimeInList.getMonth() + 1}-${firstStartTimeInList.getFullYear()}`;
}

async function buildShiftScheduleColumns(shifts, weekOffset) {
    let row = document.createElement('div');
    row.classList.add('daily-shifts-container');
    let targetReferenceDate = new Date();
    targetReferenceDate.setDate(targetReferenceDate.getDate() + 7 * weekOffset);
    let weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    let weekdayLabels = makeWeekdayLabels(weekdays,
        allWeekDatesFromReference(targetReferenceDate)
    );

    for (let i = 0; i < weekdays.length; i++) {
        let weekday = weekdays[i].toUpperCase();
        let htmlColumn = document.createElement('div');
        htmlColumn.classList.add('daily-shift-column');
        htmlColumn.appendChild(headerCellFromWeekdayString(weekdayLabels[i]));


        if (shifts.hasOwnProperty(weekday)) {
            let shiftList = shifts[weekday];
            
            for (let shift of shiftList) {
                htmlColumn.appendChild(await buildShiftCard(shift));
            }
        }

        row.appendChild(htmlColumn);
    }

    return row;
}

function headerCellFromWeekdayString(weekdayString) {
    let cell = document.createElement('div');
    cell.classList.add('header-cell');
    let b = document.createElement('strong');
    let headerCell = document.createElement('p');
    headerCell.textContent = weekdayString;
    b.appendChild(headerCell);
    cell.appendChild(b);
    return cell;
}

async function buildShiftCard(shift) {
    let shiftContainer = document.createElement('div');
    shiftContainer.classList.add('shift-card');
    shiftContainer.setAttribute('data-shift-id', shift.id);
    shiftContainer.title = shift.title;

    let cardHeader = document.createElement('h4');
    cardHeader.classList.add('shift-card-header');
    cardHeader.textContent = shift.title;

    let italicized = document.createElement('i');
    let locationField = document.createElement('p');
    locationField.classList.add('shift-card-location');
    locationField.textContent = shift.location;
    italicized.appendChild(locationField);

    let timeField = document.createElement('p');
    timeField.textContent = `${shiftDateToTimeString(new Date(shift.shiftStart))} / ${shiftDateToTimeString(new Date(shift.shiftEnd))}`;

    let small = document.createElement('small');
    let registrationsField = document.createElement('p');
    registrationsField.classList.add('shift-card-registrations');
    registrationsField.textContent = `${shift.registrations} / ${shift.requiredGuards}`;
    small.appendChild(registrationsField);

    let bottomRow = document.createElement('div');
    bottomRow.classList.add('bottom-row');

    bottomRow.appendChild(timeField);
    bottomRow.appendChild(small);

    shiftContainer.appendChild(cardHeader);
    shiftContainer.appendChild(italicized);
    shiftContainer.appendChild(bottomRow);

    shiftContainer.addEventListener('click', async () => {
        let main = document.getElementById('shift-main');

        if (document.getElementById('shift-modal-background') === undefined || document.getElementById('shift-modal-background') === null) {
            main.appendChild(await buildModal(shift));
        } else {
            main.replaceChild(await buildModal(shift), document.getElementById('shift-modal-background'));
        }

        openShiftModal();
    });
    
    return shiftContainer;
}