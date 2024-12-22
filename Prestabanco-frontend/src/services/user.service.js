import httpClient from "../http-common";

const register = (user) => {
    return httpClient.post("/api/v1/users/register", user);
}

const login = (credentials) => {
    return httpClient.post("/api/v1/users/login", credentials);
}

export default { register, login };