const userTableBody = document.getElementById("usersTableBody");
const searchInput = document.getElementById("searchInput");
const userMessage = document.getElementById("userMessage");

const openAddUserButton = document.getElementById("openAddUserButton");
const addUserModal = document.getElementById("addUserModal");
const cancelAddUserButton = document.getElementById("cancelAddUserButton");
const addUserForm = document.getElementById("addUserForm");
const formMessage = document.getElementById("formMessage");

const logoutButton = document.getElementById("logoutButton");

import {BASE_API_URL, sendRequestTo} from "./util.js";

await loadGuards();

async function loadGuards(){
    let guards = await sendRequestTo(BASE_API_URL + "users/guards");
    await renderGuards(guards);
}

async function renderGuards(guards) {
    userTableBody.innerHTML = "";

    guards.forEach(guard => userTableBody.appendChild(buildGuardRow(guard)))
}

function buildGuardRow(guard) {
    let row = document.createElement('tr');
    row.setAttribute('data-guard-id', guard.userId);
    let nameCell = document.createElement('td');
    nameCell.textContent = guard.name;
    let emailCell = document.createElement('td');
    emailCell.textContent = guard.email;
    let phoneCell = document.createElement('td');
    phoneCell.textContent = guard.phoneNumber;
    let deleteButton = document.createElement('button');
    deleteButton.classList.add('delete-button');
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', deleteGuard);

    row.appendChild(nameCell);
    row.appendChild(emailCell);
    row.appendChild(phoneCell);
    row.appendChild(deleteButton);

    return row;
}
    searchInput.addEventListener("input", async function(){
        const searchText = searchInput.value.trim();

        if (searchText === ""){
            await loadGuards();
            return;
        }

        try {
            const response = await fetch(`/api/users/by-name/${searchText}`);

            if (!response.ok){
                throw new Error("No guard matching the searched name");
            }

            const users = await response.json();

            const guardsOnly = users.filter(function(user){
                return user.userType === "GUARD"; // TODO: Change to rely on abstract class
            });

            await renderGuards(guardsOnly);
        }

        catch (error){
            userMessage.textContent = "No guard matching the searched name"
        }
    });

    openAddUserButton.addEventListener("click", function (){
        formMessage.textContent = "";
        addUserModal.classList.remove("hidden");
    });

    cancelAddUserButton.addEventListener("click", function()
    {
        formMessage.textContent = "";
        addUserModal.classList.add("hidden");
        addUserForm.reset();
    })

    addUserForm.addEventListener("submit", async function (event){
        event.preventDefault();

        const newGuard = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            phoneNumber: document.getElementById("phoneNumber").value,
            password: document.getElementById("password").value,
            userType: "GUARD"
        };


        await sendRequestTo(BASE_API_URL + "users/guard", {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(newGuard)
        });

        addUserModal.classList.add("hidden");
        addUserForm.reset();

        await loadGuards();
    });

    async function deleteGuard(e){
        const confirmed = confirm("Are you sure you want to delete this guard?");

        if (!confirmed){
            return;
        }

        let closestRow = e.target.closest('tr');
        let closestRowId = Number.parseInt(closestRow.getAttribute('data-guard-id'));

        await sendRequestTo(BASE_API_URL + `users/${closestRowId}`, {
            method: "DELETE"
        });

        await loadGuards();
    }

    logoutButton.addEventListener("click", function()
    {
        // TODO: Need to clear login session when we have authentication
        window.location.href = "/login.html";
    })
