import { injectHeader } from "./util.js";

const clientTableBody = document.getElementById("clientsTableBody");
const searchInput = document.getElementById("searchInput");
const clientMessage = document.getElementById("clientMessage");

const openAddClientButton = document.getElementById("openAddClientButton");
const addClientModal = document.getElementById("addClientModal");
const cancelAddClientButton = document.getElementById("cancelAddClientButton");
const addClientForm = document.getElementById("addClientForm");
const formMessage = document.getElementById("formMessage");

const logoutButton = document.getElementById("logoutButton");

loadClients();
injectHeader('Client Management')

async function loadClients(){
    try {
        const response = await fetch("api/clients")

        if (!response.ok){
            throw new Error("Could not load clients");
        }

        const clients = await response.json();

        renderClients(clients);
    }
    catch (error){
        clientMessage.textContent = "Could not load clients";
    }
}

function renderClients(clients) {
    clientTableBody.innerHTML = "";

    clients.forEach(function (client) {
        const row = document.createElement("tr");

        const city = JSON.stringify(client.city)

        row.innerHTML = `
        <td>${client.name}</td>
        <td>${client.email}</td>
        <td>${client.phoneNumber}</td>
        <td>${city}</td>
        <td>${client.address}</td>
        <td>
            <button class="delete-button" onclick="deleteClient(${client.id})">
            Delete
            </button> 
        </td>
        `

        clientTableBody.appendChild(row);
    });
}
searchInput.addEventListener("input", async function(){
    const searchText = searchInput.value.trim();

    if (searchText === ""){
        loadClients();
        return;
    }

    try {
        const response = await fetch(`/api/clients/${searchText}`);

        if (!response.ok){
            throw new Error("No client matching the searched name or city");
        }

        const clients = await response.json();

        renderClients(clients);
    }

    catch (error){
        userMessage.textContent = "No client matching the searched name or city"
    }
});

openAddClientButton.addEventListener("click", function (){
    formMessage.textContent = "";
    addClientModal.classList.remove("hidden");
});

cancelAddClientButton.addEventListener("click", function()
{
    formMessage.textContent = "";
    addClientModal.classList.add("hidden");
    addClientForm.reset();
})

addClientForm.addEventListener("submit", async function (event){
    event.preventDefault();

    const newClient = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phoneNumber: document.getElementById("phoneNumber").value,
        city: document.getElementById("city").value,
        address: document.getElementById("address").value
    };

    try {
        const response = await fetch("api/clients/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newClient)
        });

        if (!response.ok){
            throw new Error("Could not add client");
        }

        addClientModal.classList.add("hidden");
        addClientForm.reset();

        loadClients();
    }

    catch (error){
        formMessage.textContent = "Could not add client";
    }
});

async function deleteClient(id){
    const confirmed = confirm("Are you sure you want to delete this client?");

    if (!confirmed){
        return;
    }

    try{
        const response = await fetch(`/api/clients/${id}`, {
            method: "DELETE"
        });

        if (!response.ok){
            throw new Error("Could not delete client");
        }

        loadClients();
    }

    catch(error){
        clientMessage.textContent = "Could not delete client";
    }
}

logoutButton.addEventListener("click", function()
{
    // TODO: Need to clear login session when we have authentication
    window.location.href = "/login.html";
})