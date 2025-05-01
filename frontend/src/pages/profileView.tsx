import React, { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import { getUserProfile, updateUserProfile, deleteUserAccount } from "../services/requests";

const ProfileView: React.FC = () => {
    const [userId] = useState<number>(1); // Replace with actual user ID (e.g., from auth context or route params)
    const [username, setUsername] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [subscriptions, setSubscriptions] = useState<string[]>([]); // Placeholder for subscription list
    const [isEditing, setIsEditing] = useState<boolean>(false);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const profile = await getUserProfile(userId);
                setUsername(profile.username);
                setEmail(profile.email);
                setSubscriptions(profile.subscriptions || []); // Assuming subscriptions are part of the profile
            } catch (error) {
                console.error("Error fetching profile:", error);
            }
        };

        fetchProfile();
    }, [userId]);

    const handleGetUser = async () => {
        try {
            const profile = await getUserProfile(userId);
            alert(`User fetched: \nUsername: ${profile.username}\nEmail: ${profile.email}`);
        } catch (error) {
            console.error("Error fetching user:", error);
            alert("Failed to fetch user.");
        }
    };

    const handleUpdateUser = async () => {
        try {
            const newUsername = prompt("Enter new username:", username);
            const newEmail = prompt("Enter new email:", email);

            if (newUsername && newEmail) {
                await updateUserProfile(userId, newUsername, newEmail);
                setUsername(newUsername);
                setEmail(newEmail);
                alert("User updated successfully!");
            } else {
                alert("Update canceled.");
            }
        } catch (error) {
            console.error("Error updating user:", error);
            alert("Failed to update user.");
        }
    };

    const handleDeleteUser = async () => {
        if (window.confirm("Are you sure you want to delete this user? This action cannot be undone.")) {
            try {
                await deleteUserAccount(userId);
                alert("User deleted successfully!");
                // Redirect or handle logout after deletion
            } catch (error) {
                console.error("Error deleting user:", error);
                alert("Failed to delete user.");
            }
        }
    };

    return (
        <div>
            <Navbar />

            <div>
                <h3>TESTING USER STUFF: </h3>
                <button onClick={handleGetUser}>Get User</button>
                <button onClick={handleUpdateUser}>Update User</button>
                <button onClick={handleDeleteUser}>Delete User</button>
            </div>

            <div className="profile-container">
                <h2>User Profile View</h2>
                {isEditing ? (
                    <div>
                        <label>
                            Username:
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </label>
                        <label>
                            Email:
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </label>
                        <button onClick={handleUpdateUser}>Save Changes</button>
                        <button onClick={() => setIsEditing(false)}>Cancel</button>
                    </div>
                ) : (
                    <div>
                        <h3>Username: {username}</h3>
                        <h3>Email: {email}</h3>
                        <h3>Subscriptions:</h3>
                        <ul>
                            {subscriptions.map((sub, index) => (
                                <li key={index}>{sub}</li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
            <div>
                {!isEditing && <button onClick={() => setIsEditing(true)}>Edit Profile</button>}
                <button onClick={handleDeleteUser}>Delete Profile</button>
            </div>
        </div>
    );
};

export default ProfileView;