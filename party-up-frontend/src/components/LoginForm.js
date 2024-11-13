import React, { useState } from 'react';
import '../assets/LoginForm.css';

const LoginForm = () => {
    // State for username and password
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    // Function for handling login request
    const handleSubmit = (e) => {
        e.preventDefault();
        let hasError = false;
        const newError = { username: '', password: '' }

        // Check entry params
        if (!username) {
            newError.username('Please enter username.');
            hasError = true;
        }

        if (!password) {
            newError.username('Please enter password.');
            hasError = true;
        }

        setError(newError);
        if (!hasError) {
            console.log('Login successfful: ', { username, password });
        }
    };

    return (
        <div className='login-container'>
            <div className='login-form'>
                <h1 className='form-header'>Login</h1>
                <form onSubmit={handleSubmit}>
                    <div className='input-container'>
                        <i className="fas fa-user"></i>
                        <input
                            type='text'
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder='Enter your username'
                            required
                            className={error.username ? 'error-input' : ''}
                        />
                        {error.username && <p className="error-text">{error.username}</p>}
                    </div>
                    <div className='input-container'>
                        <i className='fas fa-lock'></i>
                        <input
                            type='password'
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder='Enter your password'
                            required
                        />
                        {error.password && <p className="error-text">{error.password}</p>}
                    </div>
                    <button type='submit'>Login</button>
                    <p className='forgot-password'>
                        <a href='#forgot-password'>Forgot Password?</a>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;