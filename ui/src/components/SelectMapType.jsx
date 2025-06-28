import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const mapTypes = [
  { id: 'state', label: 'Map by State' },
  { id: 'constituency', label: 'Map by Constituency' },
  { id: 'district', label: 'Map by District' },
  { id: 'block', label: 'Map by Block' },
];

export default function SelectMapType() {
  const location = useLocation();
  const navigate = useNavigate();
  const params = new URLSearchParams(location.search);
  const country = params.get('country');

  return (
    <div style={{ padding: 40, textAlign: 'center' }}>
      <h2>Select Map Type for {country}</h2>
      <div style={{ display: 'flex', justifyContent: 'center', gap: 32, marginTop: 32 }}>
        {mapTypes.map((type) => (
          <button
            key={type.id}
            style={{
              padding: '18px 32px',
              fontSize: '1.1rem',
              borderRadius: 8,
              border: '1px solid #1e3c72',
              background: '#fff',
              color: '#1e3c72',
              cursor: 'pointer',
              fontWeight: 600,
              transition: 'background 0.2s, color 0.2s',
            }}
            onClick={() => navigate(`/map/${country}/${type.id}`)}
          >
            {type.label}
          </button>
        ))}
      </div>
    </div>
  );
}