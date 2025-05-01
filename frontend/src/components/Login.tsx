import React, { useState } from 'react';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = () => {
        console.log('Logging in with', { username, password });
    };

    const handleCancel = () => {
        setUsername('');
        setPassword('');
    };

    return (
        <div>
            <h1>Login</h1>
            <form
                onSubmit={(e) => {
                    e.preventDefault();
                    handleLogin();
                }}
            >
                <div>
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <button type="button" onClick={handleCancel}>
                        Cancel
                    </button>
                    <button type="submit">Login</button>
                </div>
            </form>
        </div>
    );
};

export default Login;