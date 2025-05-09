export const config = {
    apiUrl: import.meta.env.VITE_API_URL,
    ws: {
        url: import.meta.env.VITE_WS_URL,
        topic: import.meta.env.VITE_WS_TOPIC,
        send: import.meta.env.VITE_WS_SEND   
    },
    localStorageKey: import.meta.env.VITE_LOCAL_STORAGE_KEY,
};
