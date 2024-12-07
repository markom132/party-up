export interface RegisterData {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  birthDay: string;
}

export interface RegisterResponse {
  success: boolean;
  message?: string;
  errors?: { [key: string]: string };
}

const API_BASE_URL = 'http://localhost:8080/api';

export const registerUser = async (
  data: RegisterData,
): Promise<RegisterResponse> => {
  try {
    const response = await fetch(`${API_BASE_URL}/create-user`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
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

    const responseData = await response.json();
    return {
      success: true,
      message: responseData.message,
      errors: responseData.errors,
    };
  } catch (error: any) {
    return {
      success: false,
      message: error.message || 'An unexpected error occurred',
    };
  }
};
