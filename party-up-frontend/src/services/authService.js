export const loginUser = async (username, password) => {
    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
        // try to parse response as JSON, if response is not in JSON than use plain text
        let errorMessage;
        try {
            const errorData = await response.json();
            errorMessage = errorData;
        } catch (e) {
            errorMessage = await response.text();
        }
        throw new Error(errorMessage);
    }

    return await response.json();
}