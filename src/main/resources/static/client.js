//import { EmployeeForm } from "./employee.form.js";
//import { EmployeeStore } from "./employee.store.js";
//import { EmployeeTable } from "./employee.table.js";

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("#clientForm");
    const table = document.querySelector("#clientTableBody");

    const employeeStore = EmployeeStore();
    const employeeForm = EmployeeForm(form, employeeStore);
    const employeeTable = EmployeeTable(table, employeeStore, (employee) => employeeForm.fillForm(employee));

    employeeStore.init()
});

export const BASE_URL = "http://localhost:8080/api/clients/";

const BASE_URL_CLIENTS = `${BASE_URL}`;

export async function fetchClients() {
    const response = await fetch(`${BASE_URL_CLIENTS}`);
    if (!response.ok) {
        throw new Error('Failed to fetch clients');
    }
    return response.json();
}

export async function createClient(client) {
    const response = await fetch(`${BASE_URL_CLIENTS}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(client),
    });
    if (!response.ok) {
        throw new Error('Failed to add client');
    }
    return response.json();
}

export async function updateClient(clientId, client) {
    const response = await fetch(`${BASE_URL_CLIENTS}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(client),
    });
    if (!response.ok) {
        throw new Error('Failed to update client');
    }
    return response.json();
}

export async function deleteClient(clientId) {
    const response = await fetch(`${BASE_URL_CLIENTS}${clientId}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete client');
    }
}