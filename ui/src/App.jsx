import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CountryMapGrid from './components/MapSelector/CountryMapGrid';
import UserHome from './components/UserHome';
import SelectMapType from './components/SelectMapType';
import MapWithUpload from './components/MapWithUpload';

function App() {
  return (
    <Routes>
      <Route path="/" element={<CountryMapGrid />} />
      <Route path="/home" element={<UserHome />} />
      <Route path="/select-map-type" element={<SelectMapType />} />
      <Route path="/map/:country/:type" element={<MapWithUpload />} />
      {/* other routes */}
    </Routes>
  );
}

export default App;