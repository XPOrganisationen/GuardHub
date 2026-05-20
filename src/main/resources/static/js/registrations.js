const registrationsTableBody = document.getElementById("registrationsTableBody");



const logoutButton = document.getElementById("logoutButton");

import {BASE_API_URL, sendRequestTo} from "./util.js";

await loadRegistrations();

async function loadRegistrations(status = "PENDING"){
    let registrations = await sendRequestTo(BASE_API_URL + `registrations/registration-status/${status}`);
    registrations.sort ((a, b) => new Date(a.shiftStart) - new Date(b.shiftStart));
    await renderRegistrations(registrations);
}

async function renderRegistrations(registrations) {
    registrationsTableBody.innerHTML = "";

    registrations.forEach(registration => registrationsTableBody.appendChild(buildRegistrationRow(registration)))
}

function buildRegistrationRow(registration) {
    let row = document.createElement('tr');
    row.setAttribute('data-registration-id', registration.registrationId);
    let dateCell = document.createElement('td');
    const shiftDate = new Date(registration.shiftStart);
    const formattedDate = formatDate(shiftDate);
    dateCell.textContent = formattedDate;
    let nameCell = document.createElement('td');
    nameCell.textContent = registration.clientName;
    let guardCell = document.createElement('td');
    guardCell.textContent = registration.guardName;
    let actionsCell = document.createElement('td');
    let approveButton = document.createElement('button');
    approveButton.classList.add('success-button');
    approveButton.textContent = 'Approve';
    approveButton.addEventListener('click', approveRegistration)
    let denyButton = document.createElement('button');
    denyButton.classList.add('delete-button');
    denyButton.textContent = 'Deny';
    denyButton.addEventListener('click', denyRegistration);

    actionsCell.appendChild(approveButton);
    actionsCell.appendChild(denyButton);

    row.appendChild(dateCell);
    row.appendChild(nameCell);
    row.appendChild(guardCell);
    row.appendChild(actionsCell);

    return row;
}

function formatDate(date) {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
}

async function denyRegistration(e){
    const confirmed = confirm("Are you sure you want to deny this registration?");

    if (!confirmed){
        return;
    }

    let closestRow = e.target.closest('tr');
    let closestRowId = Number.parseInt(closestRow.getAttribute('data-registration-id'));

    await sendRequestTo(BASE_API_URL + `registrations/${closestRowId}/reject?adminId=1`, {
        method: "POST"
    });

    await loadRegistrations();
}

async function approveRegistration(e){
    const confirmed = confirm("Are you sure you want to approve this registration?");

    if (!confirmed){
        return;
    }

    let closestRow = e.target.closest('tr');
    let closestRowId = Number.parseInt(closestRow.getAttribute('data-registration-id'));

    // TODO: Get the current admin ID from session/auth context
    const adminId = 1; // Placeholder - replace with actual admin ID from session

    await sendRequestTo(BASE_API_URL + `registrations/${closestRowId}/approve?adminId=${adminId}`, {
        method: "POST"
    });

    await loadRegistrations();
}

logoutButton.addEventListener("click", function()
{
    window.location.href = "/login.html";
})
