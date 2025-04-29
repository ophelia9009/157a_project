import React from "react";
import { Link } from "react-router-dom";

const Navbar: React.FC = () => {
    return (
        <nav className="navbar">
            <div className="navbar-menu">
                <ul className="navbar-items">
                    <p>temp nav bar</p>
                    <li><Link to="/">home</Link></li>
                    <li><Link to="/subforum">subforum</Link></li>
                    <li><Link to="/profile-view">profile view</Link></li>
                </ul>
            </div>
        </nav>
    );
}
export default Navbar;