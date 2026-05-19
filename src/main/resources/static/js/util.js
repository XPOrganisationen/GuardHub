export const BASE_API_URL = "http://localhost:8080/api/";

export function shiftDateToTimeString(shiftDate) {
    return `${('0' + shiftDate.getHours()).slice(-2)}:${('0' + shiftDate.getMinutes()).slice(-2)}`;
}

export async function sendRequestTo(URL, requestOptions) {
    let response;

    try {
        response = await fetch(URL, requestOptions);
    } catch (e) {
        console.error(e);
    }

    if (!response.ok) {
        console.error("Got unexpected response: ", response.status);
    }

    return await response.json();
}

export function injectHeader(title) {
    let topbar = document.querySelector('.topbar');

    let divBrand = document.createElement('div');
    divBrand.classList.add('topbar-brand');

    let logo = document.createElement('img');
    logo.src =  '../images/GuardhubLogo.png';
    logo.alt = "GuardHub logo";
    logo.classList.add('topbar-logo');
    divBrand.appendChild(logo);

    let h1Title = document.createElement('h1');
    h1Title.textContent = title;

    let logoutButton = document.createElement('button');
    logoutButton.classList.add('logout-button');
    logoutButton.id = 'logoutButton';
    logoutButton.textContent = 'Log out'

    topbar.appendChild(divBrand);
    topbar.appendChild(h1Title);
    topbar.appendChild(logoutButton);
}