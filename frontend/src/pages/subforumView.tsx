import React from "react";  
import Navbar from "../components/Navbar";
import Searchbar from "../components/Searchbar";

const SubforumView: React.FC = () => {
    return (
        <div>
            <Navbar />
            <Searchbar />
            
            <h1>Subforum View</h1>
            <p>This is the subforum view page.</p>
        </div>
    );
}
export default SubforumView;