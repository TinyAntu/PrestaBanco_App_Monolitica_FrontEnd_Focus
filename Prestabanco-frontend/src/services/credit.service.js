import httpClient from "../http-common";

const simulate = (capital, interest, years) => {
    return httpClient.get("/api/v1/credits/simulate", {
        params: {
            capital,
            annual_interest: interest, // Make sure this matches the backend parameter name
            years,
        },
    });
};

const remove = (id) => {
    return httpClient.delete(`/api/v1/credits/${id}`)
}

const create = (data, userId) => {
    return httpClient.post(`/api/v1/credits/create?userId=${userId}`, data, {
        headers: {
            'Content-Type': 'application/json', // Usar 'application/json' para enviar el objeto JSON
        },
    });
};

const getAll = () => {
    return httpClient.get('/api/v1/credits/getAll');
}

const evaluateStep1 = (creditId) => {
    return httpClient.get(`/api/v1/credits/R1/${creditId}`);
};

const evaluateStep4 = (creditId) => {
    return httpClient.get(`/api/v1/credits/R4/${creditId}`);
};

const evaluateStep5 = (creditId) => {
    return httpClient.get(`/api/v1/credits/R5/${creditId}`);
};

const evaluateStep6 = (creditId) => {
    return httpClient.get(`/api/v1/credits/R6/${creditId}`);
};

  const update = (id, creditData) => {
    return httpClient.put(`/api/v1/credits/update/${id}`, creditData);
};

const getAllById = (userId) => {
    return httpClient.get(`/api/v1/credits/getAll/${userId}`);
};

const follow1 = (creditId) =>{
    return httpClient.get(`/api/v1/credits/E1/${creditId}`);
};

const totalCost = (creditId) =>{
    return httpClient.get(`/api/v1/credits/total/${creditId}`);
};

export default{simulate, remove, create, getAll, evaluateStep1, follow1, update, getAllById, evaluateStep4,evaluateStep5, evaluateStep6, totalCost};