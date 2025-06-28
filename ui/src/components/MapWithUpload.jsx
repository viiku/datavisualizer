import React, { useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { MapContainer, TileLayer, GeoJSON } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import IndiaSVGMap from './IndiaSVGMap';

export default function MapWithUpload() {
  const { country, type } = useParams();
  const fileInputRef = useRef();
  const [csvFile, setCsvFile] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [geoJson, setGeoJson] = useState(null);
  const [error, setError] = useState('');

  const handleFileChange = (e) => {
    setCsvFile(e.target.files[0]);
    setError('');
  };

  const handleProcess = async () => {
    if (!csvFile) {
      setError('Please upload a CSV file first.');
      return;
    }
    setProcessing(true);
    setError('');
    const formData = new FormData();
    formData.append('file', csvFile);

    try {
      // Replace the URL below with your actual Spring Boot endpoint
      const response = await fetch('http://localhost:8080/api/process-csv', {
        method: 'POST',
        body: formData,
      });
      if (!response.ok) throw new Error('Failed to process file');
      const data = await response.json();
      setGeoJson(data); // assuming backend returns GeoJSON
    } catch (err) {
      setError('Processing failed. Please try again.');
    } finally {
      setProcessing(false);
    }
  };

  // Default map center for demo; you can set per country
  const mapCenter = [20, 78];

  // Example: handle region click
  const handleRegionClick = (regionId) => {
    alert(`Clicked region: ${regionId}`);
    // You can show a popup, overlay data, etc.
  };

  return (
    <div style={{ display: 'flex', height: '90vh' }}>
      <div style={{
        width: 320,
        borderRight: '1px solid #eee',
        padding: 32,
        background: '#f7f9fb',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
      }}>
        <h3 style={{ marginBottom: 24 }}>Upload Data (CSV)</h3>
        <label
          htmlFor="csv-upload"
          style={{
            display: 'block',
            background: '#1e3c72',
            color: '#fff',
            padding: '12px 28px',
            borderRadius: 24,
            cursor: 'pointer',
            fontWeight: 600,
            marginBottom: 16,
            boxShadow: '0 2px 8px rgba(30,60,114,0.08)'
          }}
        >
          {csvFile ? csvFile.name : 'Choose CSV File'}
          <input
            id="csv-upload"
            type="file"
            accept=".csv"
            ref={fileInputRef}
            onChange={handleFileChange}
            style={{ display: 'none' }}
          />
        </label>
        <button
          onClick={handleProcess}
          disabled={processing}
          style={{
            background: processing ? '#ccc' : '#2a5298',
            color: '#fff',
            border: 'none',
            borderRadius: 24,
            padding: '12px 28px',
            fontSize: '1rem',
            fontWeight: 600,
            cursor: processing ? 'not-allowed' : 'pointer',
            marginBottom: 16,
            transition: 'background 0.2s'
          }}
        >
          {processing ? 'Processing...' : 'Process'}
        </button>
        {error && <div style={{ color: 'red', marginBottom: 12 }}>{error}</div>}
        <p style={{ color: '#888', fontSize: 14, textAlign: 'center' }}>
          Upload your constituency-level data as a CSV file.<br />
          Click "Process" to visualize on the map.
        </p>
      </div>
      <div style={{ flex: 1, padding: 32 }}>
        <h2 style={{ textAlign: 'center', marginBottom: 16 }}>
          {country.charAt(0).toUpperCase() + country.slice(1)} – {type.charAt(0).toUpperCase() + type.slice(1)} Map
        </h2>
        <div style={{
          width: '100%',
          height: '80%',
          minHeight: 400,
          background: '#e3eaff',
          borderRadius: 12,
          boxShadow: '0 2px 8px #aaa',
          overflow: 'hidden',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}>
          <IndiaSVGMap onRegionClick={handleRegionClick} regionData={{}} />
        </div>
      </div>
    </div>
  );
}