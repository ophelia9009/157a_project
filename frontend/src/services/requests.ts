const BASE_URL = "http://localhost:5000/api/users"; // TODO REPLACE WITH ACTUAL THING

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
            throw new Error(errorData.error || "Failed to register user");
        }

        return await response.json();
    } catch (error) {
        console.error("Error registering user:", error);
        throw error;
    }
};

export const loginUser = async (username: string, password: string) => {

};

// Get user profile
export const getUserProfile = async (userId: number) => {
    
};

// Update user profile
export const updateUserProfile = async (userId: number, username: string, email: string) => {

};

// Delete user account
export const deleteUserAccount = async (userId: number) => {

};