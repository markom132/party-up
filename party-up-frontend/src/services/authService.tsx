interface LoginResponse {
  token?: string;
  message?: string;
}

export const loginUser = async (
  username: string,
  password: string
): Promise<LoginResponse> => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    const responseClone = response.clone();

    let errorMessage: string;
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || 'An error occurred';
    } catch {
      errorMessage = await responseClone.text();
    }
    throw new Error(errorMessage);
  }

  return response.json();
};
