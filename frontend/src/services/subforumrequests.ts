const BASE_URL = "http://localhost:8080/api/subforums"; // TODO REPLACE WITH ACTUAL THING

export const makeSubforum = async (name: string, description: string) => {
    try {
        const response = await fetch(`${BASE_URL}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, description }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to Subforum ${name}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error making Subforum: ${name}", error);
        throw error;
    }
};


// Get Subforum
export const getSubforum = async (SubforumID: number) => {
    try {
        const response = await fetch(`${BASE_URL}/${SubforumID}`);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to get Subforum ${SubforumID}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error getting Subforum: ${SubforumID}", error);
        throw error;
    }
};

// Update Subforum
export const updateSubforum = async (SubforumID: number, name: string, description: string) => {
    try {
        const response = await fetch(`${BASE_URL}/${SubforumID}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
        body: JSON.stringify({ name, description }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to update Subforum ${SubforumID}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error updating Subforum: ${SubforumID}", error);
        throw error;
    }
};

// Delete Subforum
export const deleteSubforum = async (SubforumID: number) => {
    try {
        const response = await fetch(`${BASE_URL}/${SubforumID}`, {
            method: "DELETE",
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "Failed to delete Subforum ${SubforumID}");
        }

        return await response.json();
    } catch (error) {
        console.error("Error deleting Subforum: ${SubforumID}", error);
        throw error;
    }
};