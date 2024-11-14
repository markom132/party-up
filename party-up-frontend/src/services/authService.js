export const loginUser = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    const responseClone = response.clone();

    let errorMessage;
    try {
      const errorData = await response.json();
      errorMessage = errorData;
    } catch {
      errorMessage = await responseClone.text();
    }
    throw new Error(errorMessage);
  }

  return await response.json();
};
