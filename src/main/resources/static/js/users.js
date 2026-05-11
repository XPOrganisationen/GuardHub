const userTableBody = document.getElementById("usersTableBody");
const searchInput = document.getElementById("searchInput");
const userMessage = document.getElementById("userMessage");

const openAddUserButton = document.getElementById("openAddUserButton");
const addUserModal = document.getElementById("addUserModal");
const cancelAddUserButton = document.getElementById("cancelAddUserButton");
const addUserForm = document.getElementById("addUserForm");
const formMessage = document.getElementById("formMessage");

const logoutButton = document.getElementById("logoutButton");

loadGuards();

async function loadGuards(){
    try {
        const response = await fetch("api/users/by-type/GUARD") // TODO: Need to make this work with our new abstract classes.

        if (!response.ok){
            throw new Error("Could not load guards");
        }

        const guards = await response.json();

        renderGuards(guards);
    }
    catch (error){
        userMessage.textContent = "Could not load guards";
    }
}

function renderGuards(guards) {
    userTableBody.innerHTML = "";

    guards.forEach(function (guard) {
        const row = document.createElement("tr");

        row.innerHTML = `
        <td>${guard.name}</td>
        <td>${guard.email}</td>
        <td>${guard.phoneNumber}</td>
        <td>
            <button class="delete-button" onclick="deleteGuard(${guard.id})">
            Delete
            </button> 
        </td>
        `

        userTableBody.appendChild(row);
    });
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
            nname: document.getElementById("name").value,
            email: document.getElementById("email").value,
            phoneNumber: document.getElementById("phoneNumber").value,
            password: document.getElementById("password").value,
            userType: "GUARD" // TODO: Adapt it for abstract class
        };

        try {
            const response = await fetch("api/users", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(newGuard)
            });

            if (!response.ok){
                throw new Error("Could not add guard");
            }

            addUserModal.classList.add("hidden");
            addUserForm.reset();

            loadGuards();
        }

        catch (error){
            formMessage.textContent = "Could not add guard";
        }
    });

    async function deleteGuard(id, name){
        const confirmed = confirm("Are you sure you want to delete this guard?");

        if (!confirmed){
            return;
        }

        try{
            const response = await fetch(`/api/users/${id}`, {
                method: "DELETE"
            });

            if (!response.ok){
                throw new Error("Could not delete guard");
            }

            loadGuards();
        }

        catch(error){
            userMessage.textContent = "Could not delete guard";
        }
    }

    logoutButton.addEventListener("click", function()
    {
        // TODO: Need to clear login session when we have authentication
        window.location.href = "/login.html";
    })
