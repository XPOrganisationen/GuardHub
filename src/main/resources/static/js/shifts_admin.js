import { BASE_API_URL, sendRequestTo } from "./util.js";

const shiftsTableBody = document.getElementById("shiftsTableBody");
const searchInput = document.getElementById("searchInput");
const shiftMessage = document.getElementById("shiftMessage");

const openAddShiftButton = document.getElementById("openAddShiftButton");
const addShiftModal = document.getElementById("addShiftModal");
const cancelAddShiftButton = document.getElementById("cancelAddShiftButton");
const addShiftForm = document.getElementById("addShiftForm");
const formMessage = document.getElementById("formMessage");

const editShiftModal = document.getElementById("editShiftModal");
const cancelEditShiftButton = document.getElementById("cancelEditShiftButton");
const editShiftForm = document.getElementById("editShiftForm");
const editFormMessage = document.getElementById("editFormMessage");

const logoutButton = document.getElementById("logoutButton");

let allLoadedShifts = [];
let editingShiftId = null;

await loadShifts();

async function loadShifts() {
    const shifts = await sendRequestTo(BASE_API_URL + "shifts?weekOffset=0");
    allLoadedShifts = Object.values(shifts).flat();
    renderShifts(allLoadedShifts);
}

function renderShifts(shifts) {
    shiftsTableBody.innerHTML = "";
    shiftMessage.textContent = "";

    if (shifts.length === 0) {
        shiftMessage.textContent = "No shifts found.";
        return;
    }

    shifts.forEach(shift => shiftsTableBody.appendChild(buildShiftRow(shift)));
}

function buildShiftRow(shift) {
    const row = document.createElement("tr");
    row.setAttribute("data-shift-id", shift.id);

    const titleCell = document.createElement("td");
    titleCell.textContent = shift.title;

    const clientCell = document.createElement("td");
    clientCell.textContent = shift.clientName;

    const startCell = document.createElement("td");
    startCell.textContent = new Date(shift.shiftStart).toLocaleString();

    const endCell = document.createElement("td");
    endCell.textContent = new Date(shift.shiftEnd).toLocaleString();

    const guardsCell = document.createElement("td");
    guardsCell.textContent = `${shift.registrations} / ${shift.requiredGuards}`;

    const actionsCell = document.createElement("td");

    const editButton = document.createElement("button");
    editButton.classList.add("secondary-button");
    editButton.textContent = "Edit";
    editButton.addEventListener("click", () => openEditModal(shift));

    const deleteButton = document.createElement("button");
    deleteButton.classList.add("delete-button");
    deleteButton.textContent = "Delete";
    deleteButton.addEventListener("click", () => deleteShift(shift.id, row));

    actionsCell.appendChild(editButton);
    actionsCell.appendChild(deleteButton);

    row.appendChild(titleCell);
    row.appendChild(clientCell);
    row.appendChild(startCell);
    row.appendChild(endCell);
    row.appendChild(guardsCell);
    row.appendChild(actionsCell);

    return row;
}

// --- Add shift ---

openAddShiftButton.addEventListener("click", function () {
    formMessage.textContent = "";
    addShiftModal.classList.remove("hidden");
});

cancelAddShiftButton.addEventListener("click", function () {
    formMessage.textContent = "";
    addShiftModal.classList.add("hidden");
    addShiftForm.reset();
});

addShiftForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const newShift = {
        title: document.getElementById("title").value,
        clientId: parseInt(document.getElementById("clientId").value),
        shiftStart: document.getElementById("shiftStart").value,
        shiftEnd: document.getElementById("shiftEnd").value,
        requiredGuardAmount: parseInt(document.getElementById("requiredGuards").value),
        description: document.getElementById("description").value,
        parkingInstructions: document.getElementById("parkingInstructions").value,
        foodServed: document.getElementById("foodServed").checked
    };

    try {
        await sendRequestTo(BASE_API_URL + "shifts", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newShift)
        });

        addShiftModal.classList.add("hidden");
        addShiftForm.reset();
        await loadShifts();
    } catch (error) {
        formMessage.textContent = "Failed to create shift. Please check the details and try again.";
    }
});

// --- Edit shift ---

function openEditModal(shift) {
    editingShiftId = shift.id;
    editFormMessage.textContent = "";

    document.getElementById("editTitle").value = shift.title;
    document.getElementById("editShiftStart").value = shift.shiftStart.slice(0, 16);
    document.getElementById("editShiftEnd").value = shift.shiftEnd.slice(0, 16);
    document.getElementById("editRequiredGuards").value = shift.requiredGuards;
    document.getElementById("editDescription").value = shift.description;
    document.getElementById("editParkingInstructions").value = shift.parkingInstructions ?? "";
    document.getElementById("editFoodServed").checked = shift.foodServed;

    editShiftModal.classList.remove("hidden");
}

cancelEditShiftButton.addEventListener("click", function () {
    editShiftModal.classList.add("hidden");
    editShiftForm.reset();
    editingShiftId = null;
});

editShiftForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const updatedShift = {
        title: document.getElementById("editTitle").value,
        shiftStart: document.getElementById("editShiftStart").value,
        shiftEnd: document.getElementById("editShiftEnd").value,
        requiredGuardAmount: parseInt(document.getElementById("editRequiredGuards").value),
        description: document.getElementById("editDescription").value,
        parkingInstructions: document.getElementById("editParkingInstructions").value,
        foodServed: document.getElementById("editFoodServed").checked
    };

    try {
        await sendRequestTo(BASE_API_URL + `shifts/${editingShiftId}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedShift)
        });

        editShiftModal.classList.add("hidden");
        editShiftForm.reset();
        editingShiftId = null;
        await loadShifts();
    } catch (error) {
        editFormMessage.textContent = "Failed to update shift. Please check the details and try again.";
    }
});

// --- Delete shift ---

async function deleteShift(shiftId, row) {
    const confirmed = confirm("Are you sure you want to delete this shift?");
    if (!confirmed) return;

    await sendRequestTo(BASE_API_URL + `shifts/${shiftId}`, {
        method: "DELETE"
    });

    row.remove();
    allLoadedShifts = allLoadedShifts.filter(s => s.id !== shiftId);

    if (allLoadedShifts.length === 0) {
        shiftMessage.textContent = "No shifts found.";
    }
}

// --- Search ---

searchInput.addEventListener("input", function () {
    const searchText = searchInput.value.trim().toLowerCase();

    if (searchText === "") {
        renderShifts(allLoadedShifts);
        return;
    }

    const filtered = allLoadedShifts.filter(shift =>
        shift.title.toLowerCase().includes(searchText)
    );

    if (filtered.length === 0) {
        shiftsTableBody.innerHTML = "";
        shiftMessage.textContent = "No shifts matching the search.";
        return;
    }

    renderShifts(filtered);
});

// --- Logout ---

logoutButton.addEventListener("click", function () {
    window.location.href = "/login.html";
});
