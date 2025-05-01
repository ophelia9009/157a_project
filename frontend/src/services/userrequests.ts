const BASE_URL = "http://localhost:8080/backend/api/users"; // TODO REPLACE WITH ACTUAL THING

export const registerUser = async (username: string, password: string, email: string) => {
    try {
        const response = await fetch(`${BASE_URL}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password, email }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to register user ${username}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error registering user: ${username}", error);
        throw error;
    }
};

export const loginUser = async (username: string, password: string) => {
    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to login user ${username}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error logging in user: ${username}", error);
        throw error;
    }
};

// Get user profile
export const getUserProfile = async (userId: number) => {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to get user ${userId}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error getting user: ${userId}", error);
        throw error;
    }
};

// Update user profile
export const updateUserProfile = async (userId: number, username: string, email: string) => {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
        body: JSON.stringify({ username, email }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to update user ${userId}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error updating user: ${userId}", error);
        throw error;
    }
};

// Delete user account
export const deleteUserAccount = async (userId: number) => {
    try {
        const response = await fetch(`${BASE_URL}/${userId}`, {
            method: "DELETE",
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to delete user ${userId}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error deleting user: ${userId}", error);
        throw error;
    }
};