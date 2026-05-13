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