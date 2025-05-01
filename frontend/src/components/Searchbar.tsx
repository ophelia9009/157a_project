import React, { useState } from 'react';

const Searchbar: React.FC = () => {
    const [query, setQuery] = useState('');

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log('Input changed:', event.target.value); // TODO 
    };

    const handleSearch = () => {
        console.log('Search query:', query); // TODO 
       };

    return (
        <div>
            <input
                type="text"
                placeholder="Search..."
                value={query}
                onChange={handleInputChange}
            />
            <button onClick={handleSearch}>
                Search
            </button>
        </div>
    );
};

export default Searchbar;