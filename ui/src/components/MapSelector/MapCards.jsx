// components/MapSelector/MapCards.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './cards.css';

const maps = [
  {
    id: 'osm',
    name: 'OpenStreetMap',
    thumbnail: 'https://maps.nls.uk/os/openstreetmap/poster-55-40/assets/osm-55-40.jpg',
    description: 'Standard street map'
  },
  {
    id: 'satellite',
    name: 'Satellite',
    thumbnail: 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/World_map_-_low_resolution.jpg/1200px-World_map_-_low_resolution.jpg',
    description: 'Aerial imagery'
  }
];

// Example country data; replace with your actual country list and thumbnails
const countries = [
  { code: 'IN', name: 'India', thumbnail: 'https://flagcdn.com/in.svg' },
  { code: 'US', name: 'United States', thumbnail: 'https://flagcdn.com/us.svg' },
  { code: 'FR', name: 'France', thumbnail: 'https://flagcdn.com/fr.svg' },
  { code: 'BR', name: 'Brazil', thumbnail: 'https://flagcdn.com/br.svg' },
  { code: 'CN', name: 'China', thumbnail: 'https://flagcdn.com/cn.svg' },
  // Add more countries as needed
];

export default function MapCards() {
  const navigate = useNavigate();

  return (
    <>
      <div className="map-grid">
        {maps.map((map) => (
          <div 
            key={map.id} 
            className="map-card"
            onClick={() => navigate(`/upload?mapType=${map.id}`)}
          >
            <img src={map.thumbnail} alt={map.name} />
            <h3>{map.name}</h3>
            <p>{map.description}</p>
          </div>
        ))}
      </div>
      <div className="map-horizontal-scroll">
        {countries.map((country) => (
          <div
            key={country.code}
            className="map-card country-card"
            onClick={() => navigate(`/upload?country=${country.code}`)}
          >
            <img src={country.thumbnail} alt={country.name} />
            <h3>{country.name}</h3>
          </div>
        ))}
      </div>
    </>
  );
}