const userTableBody = document.getElementById("usersTableBody");
const searchInput = document.getElementById("searchInput");
const userMessage = document.getElementById("userMessage");

const openAddUserButton = document.getElementById("openAddUserButton");
const addUserModal = document.getElementById("addUserModal");
const cancelAddUserButton = document.getElementById("cancelAddUserButton");
const addUserForm = document.getElementById("addUserForm");
const formMessage = document.getElementById("formMessage");

const logoutButton = document.getElementById("logoutButton");

import {BASE_API_URL, sendRequestTo} from "../util.js";

loadGuards();

async function loadGuards(){
    let guards = await sendRequestTo(BASE_API_URL + "users/guards");
    renderGuards(guards);
}

async function renderGuards(guards) {
    userTableBody.innerHTML = "";

    for (const guard of guards) {
        const row = document.createElement("tr");

        row.innerHTML = `
        <td>${guard.name}</td>
        <td>${guard.email}</td>
        <td>${guard.phoneNumber}</td>
        <td>
            <button class="delete-button" >
            Delete
            </button> 
        </td> `

        userTableBody.appendChild(row);

        let deleteButton = document.querySelector(".delete-button");

        deleteButton.addEventListener("click", async () => await deleteGuard(guard.id))

    }
}
    searchInput.addEventListener("input", async function(){
        const searchText = searchInput.value.trim();

        if (searchText === ""){
            loadGuards();
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

            renderGuards(guardsOnly);
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
            userType: "GUARD" // TODO: Adapt it for abstract class
        };


        let guards = await sendRequestTo(BASE_API_URL + "users/guard", {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(newGuard)
        });

        addUserModal.classList.add("hidden");
        addUserForm.reset();

        loadGuards();
    });

    async function deleteGuard(id){
        const confirmed = confirm("Are you sure you want to delete this guard?");

        if (!confirmed){
            return;
        }

        const response = await sendRequestTo(`/api/users/${id}`, {
            method: "DELETE"
        });

        loadGuards();
    }

    logoutButton.addEventListener("click", function()
    {
        // TODO: Need to clear login session when we have authentication
        window.location.href = "/login.html";
    })
