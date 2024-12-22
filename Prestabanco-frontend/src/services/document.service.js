import httpClient from "../http-common";

const create = (data) => {
    return httpClient.post("/api/v1/docs/create", data, {
        headers: {
            "Content-Type": "application/json"
        }
    });
};

const getDocumentsByCreditId = (creditId) => {
    return httpClient.get(`/api/v1/docs/doclist/${creditId}`);
};

const downloadDocument = (id) => {
    return httpClient.get(`/api/v1/docs/download/${id}`, {
        responseType: 'blob' 
    });
};

export default{create, getDocumentsByCreditId, downloadDocument}