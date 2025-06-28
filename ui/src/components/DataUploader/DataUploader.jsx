// components/DataUploader/DataUploader.jsx
import React, { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import MapRenderer from '../MapRenderer/MapRenderer';
import api from '../../services/api';
import './uploader.css';

export default function DataUploader() {
  const [searchParams] = useSearchParams();
  const [geoJson, setGeoJson] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  
    // Get country params
    const countryId = searchParams.get('country');
    const bbox = JSON.parse(searchParams.get('bbox') || '[[0,0],[0,0]]');
    
    // Set initial map view based on country
    const [mapView, setMapView] = useState({
      center: [
        (bbox[0][0] + bbox[1][0]) / 2, // lat center
        (bbox[0][1] + bbox[1][1]) / 2  // lng center
      ],
      zoom: 5
    });
    
  const mapType = searchParams.get('mapType') || 'osm';

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setIsLoading(true);
    setError(null);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('mapType', mapType);

    try {
      const response = await api.post('/process-csv', formData);
      setGeoJson(response.data);
    } catch (error) {
      console.error('Upload failed:', error);
      setError('Failed to process file. Please check the format and try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const exportData = (format) => {
    if (!geoJson) return;
    
    try {
      let content, mimeType, extension;
      
      if (format === 'geojson') {
        content = JSON.stringify(geoJson, null, 2);
        mimeType = 'application/geo+json';
        extension = 'geojson';
      } else { // CSV
        // Convert GeoJSON features to CSV
        const headers = ['latitude', 'longitude', ...Object.keys(geoJson.features[0].properties || {})];
        const rows = geoJson.features.map(feature => {
          const coords = feature.geometry.coordinates;
          const props = feature.properties || {};
          return [coords[1], coords[0], ...headers.slice(2).map(h => props[h] || '')];
        });
        
        content = [
          headers.join(','),
          ...rows.map(row => row.join(','))
        ].join('\n');
        
        mimeType = 'text/csv';
        extension = 'csv';
      }

      // Create download link
      const blob = new Blob([content], { type: mimeType });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `map-data.${extension}`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Export failed:', err);
      setError('Failed to export data');
    }
  };

  return (
    <div className="upload-container">
      <div className="map-preview">
        <MapRenderer 
          geoJson={null} 
          initialView={mapView}   // <-- Pass initialView!
          mapType={mapType} 
          interactive={false} 
        />
      </div>

      <div className="upload-box">
        <h3>Upload CSV</h3>
        <label htmlFor="csv-upload" className="upload-label">
          {isLoading ? 'Processing...' : 'Choose CSV File'}
          <input 
            id="csv-upload"
            type="file" 
            accept=".csv" 
            onChange={handleFileUpload}
            disabled={isLoading}
          />
        </label>
        {isLoading && <div className="spinner"></div>}
        {error && <div className="error-message">{error}</div>}
      </div>

      {geoJson && (
        <div className="results-section">
          <h3>Processed Data</h3>
          <MapRenderer 
            geoJson={geoJson} 
            initialView={mapView}   // <-- Pass initialView!
            mapType={mapType} 
            interactive={true} 
          />
          <div className="export-buttons">
            <button onClick={() => exportData('geojson')}>
              Export GeoJSON
            </button>
            <button onClick={() => exportData('csv')}>
              Export CSV
            </button>
          </div>
        </div>
      )}
    </div>
  );
}