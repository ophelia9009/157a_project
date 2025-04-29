import React from 'react';
import logo from './logo.svg';
import './App.css';
import Home from './pages/home';
import ProfileView from './pages/profileView';
import SubforumView from './pages/subforumView';
import { Route, Routes } from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/subforum" element={<SubforumView />} />
        <Route path="/profile-view" element={<ProfileView />} />
      </Routes>
    </div>
  );
}

export default App;
