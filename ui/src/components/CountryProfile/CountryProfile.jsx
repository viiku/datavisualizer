import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import MapRenderer from '../MapRenderer/MapRenderer';
import './countryProfile.css';

const countryData = {
  usa: {
    name: 'United States',
    capital: 'Washington, D.C.',
    population: '331 million',
    area: '9.8M km²',
    regions: [
      { id: 'california', name: 'California', bbox: [[32.528832, -124.482003], [42.009516, -114.131211]] },
      { id: 'texas', name: 'Texas', bbox: [[25.837377, -106.645646], [36.500704, -93.508292]] }
    ]
  },
  india: {
    name: 'India',
    capital: 'New Delhi',
    population: '1.4 billion',
    area: '3.3M km²',
    regions: [
      { id: 'maharashtra', name: 'Maharashtra', bbox: [[15.602, 72.66], [22.028, 80.89]] },
      { id: 'tamilnadu', name: 'Tamil Nadu', bbox: [[8.088, 76.13], [13.705, 80.35]] }
    ]
  }
  // Add more countries...
};

export default function CountryProfile() {
  const { countryId } = useParams();
  const navigate = useNavigate();
  const [country, setCountry] = useState(null);
  const [selectedRegion, setSelectedRegion] = useState(null);

  useEffect(() => {
    // In a real app, you would fetch this from your API
    setCountry(countryData[countryId]);
  }, [countryId]);

  if (!country) return <div className="loading">Loading country data...</div>;

  return (
    <div className="country-profile">
      <div className="country-header">
        <h1>{country.name}</h1>
        <div className="country-meta">
          <span>Capital: {country.capital}</span>
          <span>Population: {country.population}</span>
          <span>Area: {country.area}</span>
        </div>
      </div>

      <div className="country-content">
        <div className="country-map">
          <img
            src={`https://staticmap.openstreetmap.de/staticmap.php?center=${(country.bbox[0][0]+country.bbox[1][0])/2},${(country.bbox[0][1]+country.bbox[1][1])/2}&zoom=4&size=300x200&maptype=mapnik`}
            alt={country.name}
          />
        </div>

        <div className="regions-section">
          <h2>States/Provinces</h2>
          <div className="region-grid">
            {country.regions.map(region => (
              <div 
                key={region.id}
                className={`region-card ${selectedRegion?.id === region.id ? 'active' : ''}`}
                onClick={() => setSelectedRegion(region)}
              >
                <div className="region-thumbnail">
                  <img
                    src={`https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=200&height=120&zoom=6&area=rect:${region.bbox[0][1]},${region.bbox[0][0]},${region.bbox[1][1]},${region.bbox[1][0]}`}
                    alt={region.name}
                  />
                </div>
                <h3>{region.name}</h3>
              </div>
            ))}
          </div>
        </div>

        {selectedRegion && (
          <div className="region-detail">
            <h3>Viewing: {selectedRegion.name}</h3>
            <div className="detail-map">
              <MapRenderer
                geoJson={null}
                initialView={{
                  center: [
                    (selectedRegion.bbox[0][0] + selectedRegion.bbox[1][0]) / 2,
                    (selectedRegion.bbox[0][1] + selectedRegion.bbox[1][1]) / 2
                  ],
                  zoom: 7
                }}
              />
            </div>
            <button 
              className="upload-button"
              onClick={() => navigate(`/upload?region=${selectedRegion.id}&bbox=${JSON.stringify(selectedRegion.bbox)}`)}
            >
              Upload Data for {selectedRegion.name}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}