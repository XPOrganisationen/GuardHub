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

import {BASE_API_URL, sendRequestTo} from "../js/util.js";

await loadClients();
injectHeader('Client Management')

async function loadClients(){
    let clients = await sendRequestTo(BASE_API_URL + "clients");
    await renderClients(clients);
}

async function renderClients(clients) {
    clientTableBody.innerHTML = "";

    clients.forEach(client => clientTableBody.appendChild(buildClientRow(client)))
}

function buildClientRow(client) {
    let row = document.createElement('tr');
    row.setAttribute('data-client-id', client.id);
    let nameCell = document.createElement('td');
    nameCell.textContent = client.name;
    let emailCell = document.createElement('td');
    emailCell.textContent = client.email;
    let phoneCell = document.createElement('td');
    phoneCell.textContent = client.phoneNumber;
    let cityCell = document.createElement('td');
    cityCell.textContent = client.city.cityName;
    let addressCell = document.createElement('td');
    addressCell.textContent = client.address;
    let deleteButton = document.createElement('button');
    deleteButton.classList.add('delete-button');
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', deleteClient);

    row.appendChild(nameCell);
    row.appendChild(emailCell);
    row.appendChild(phoneCell);
    row.appendChild(cityCell);
    row.appendChild(addressCell);
    row.appendChild(deleteButton);

    return row;
}


searchInput.addEventListener("input", async function(){
    const searchText = searchInput.value.trim();

    if (searchText === ""){
        await loadClients();
        return;
    }

    try {
        const response = await fetch(`/api/clients/by-name/${searchText}`);

        if (!response.ok){
            throw new Error("No client matching the searched name");
        }

        const clients = await response.json();

        await renderClients(clients);
    }

    catch (error){
        clientMessage.textContent = "No client matching the searched name"
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
        city: {cityName: document.getElementById("city").value} ,
        address: document.getElementById("address").value
    };

    await sendRequestTo(BASE_API_URL + "cities", {
        method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(newClient.city)
    });

    await sendRequestTo(BASE_API_URL + "clients", {
        method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(newClient)
    });

    addClientModal.classList.add("hidden");
    addClientForm.reset();

    await loadClients();
});

async function deleteClient(e){
    const confirmed = confirm("Are you sure you want to delete this client?");

    if (!confirmed){
        return;
    }

    let closestRow = e.target.closest('tr');
    let closestRowId = Number.parseInt(closestRow.getAttribute('data-client-id'));

    await sendRequestTo(BASE_API_URL + `clients/${closestRowId}`, {
        method: "DELETE"
    });

    await loadClients();
}

logoutButton.addEventListener("click", function()
{
    // TODO: Need to clear login session when we have authentication
    window.location.href = "/login.html";
})