import React, { useState } from 'react';
import '../assets/Header.css';

const Header = () => {
    const [menuOpen, setMenuOpen] = useState(false);

    const toggleMenu = () => {
        setMenuOpen(!menuOpen);
    };

    return (
        <header className='header'>
            <h1 className="logo">
                Par<span className="accent">t</span>y-<span className="accent">Up</span>
            </h1>
            <nav className='header-nav'>
                <div className={`nav-links ${menuOpen ? 'open' : ''}`}>
                    <a href='#home'>Home</a>
                    <a href='#events'>Events</a>
                    <a href='#explore'>Explore</a>
                    <a href='#profile'>Profile</a>
                </div>
                <div className='auth-buttons'>
                    <a href='#login' className='login-link'>Login</a>
                    <button className='signup-button'>Sign Up</button>
                </div>
                <button className='hamburger-menu' onClick={toggleMenu}>
                    &#9776;
                </button>
            </nav>
        </header>
    );
};

export default Header;