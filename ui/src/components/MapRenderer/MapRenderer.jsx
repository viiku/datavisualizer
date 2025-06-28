// components/MapRenderer/MapRenderer.jsx
import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import './mapStyles.css';

export default function MapRenderer({ geoJson, initialView }) {
  const mapRef = useRef(null);
  const layerRef = useRef(null);
  const containerRef = useRef(null);

  useEffect(() => {
    // Clean up any previous map instance before creating a new one
    if (mapRef.current) {
      mapRef.current.remove();
      mapRef.current = null;
    }
    // Ensure the container is empty
    if (containerRef.current) {
      containerRef.current.innerHTML = '';
      mapRef.current = L.map(containerRef.current).setView(
        initialView.center,
        initialView.zoom
      );
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
      }).addTo(mapRef.current);
    }
    return () => {
      if (mapRef.current) {
        mapRef.current.remove();
        mapRef.current = null;
      }
    };
  }, [initialView]);

  useEffect(() => {
    if (!geoJson || !mapRef.current) return;
    if (layerRef.current) {
      layerRef.current.remove();
    }
    layerRef.current = L.geoJSON(geoJson, {
      pointToLayer: (feature, latlng) => {
        return L.circleMarker(latlng, {
          radius: 8,
          fillColor: '#ff7800',
          color: '#000',
          weight: 1,
          opacity: 1,
          fillOpacity: 0.8
        });
      }
    }).addTo(mapRef.current);
    mapRef.current.fitBounds(layerRef.current.getBounds());
  }, [geoJson]);

  return (
    <div
      ref={containerRef}
      className={`map-view ${initialView ? 'interactive' : 'preview'}`}
      style={{ width: '100%', height: initialView ? 600 : 300 }}
    />
  );
}