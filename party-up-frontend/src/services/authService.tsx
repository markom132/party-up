interface LoginResponse {
  token?: string;
  message?: string;
}

export const loginUser = async (
  username: string,
  password: string,
): Promise<LoginResponse> => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include', // Include cookies in the request
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

export const logoutUser = async (): Promise<string> => {
  const response = await fetch('http://localhost:8080/api/auth/logout', {
    method: 'POST',
    credentials: 'include', // Include cookies
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

  // Handle responses that are plain text
  const responseText = await response.text();
  return responseText; // Return plain text (e.g., "Logged out successfully")
};
