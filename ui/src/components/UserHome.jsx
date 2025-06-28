import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import countries from '../data/countries';
import indiaStates from '../data/indiaStates';

import indiaHighSvgUrl from '../assets/maps/indiaHigh.svg';

const mapOptions = [
  { id: 'regular map', label: 'Regular', desc: 'View data state-wise', img: 'https://img.icons8.com/color/96/000000/usa-map.png' },
  { id: 'state', label: 'States', desc: 'View data state-wise', img: 'https://img.icons8.com/color/96/000000/usa-map.png' },
  { id: 'constituency', label: 'Constituencies', desc: 'View data by constituency', img: 'https://img.icons8.com/color/96/000000/region-code.png' },
  { id: 'district', label: 'Districts', desc: 'See data district-wise', img: 'https://img.icons8.com/color/96/000000/country.png' },
  { id: 'block', label: 'Blocks', desc: 'See data block-wise', img: 'https://img.icons8.com/color/96/000000/marker.png' }
];

const searchBarStyle = {
  width: '100%',
  padding: '8px 10px',
  marginBottom: 12,
  borderRadius: 6,
  border: '1px solid #ccc',
  fontSize: 15,
  background: '#f8fafd',
  outline: 'none',
  transition: 'border 0.2s'
};

function SidebarList({ title, searchValue, onSearch, items, selectedId, onSelect, showIcon = false }) {
  return (
    <div style={{ width: 220, overflowY: 'auto', borderRight: '1px solid #eee', padding: 16 }}>
      <h3>{title}</h3>
      <input
        type="text"
        placeholder={`Search ${title.toLowerCase()}...`}
        value={searchValue}
        onChange={e => onSearch(e.target.value)}
        style={searchBarStyle}
      />
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {items.map(item => (
          <li
            key={item.id}
            style={{
              padding: '10px 8px',
              margin: '8px 0',
              background: selectedId === item.id ? '#e3eaff' : 'transparent',
              borderRadius: 6,
              cursor: 'pointer',
              fontWeight: selectedId === item.id ? 600 : 400,
              display: 'flex',
              alignItems: 'center',
              gap: showIcon ? 10 : 0
            }}
            onClick={() => onSelect(item)}
          >
            {showIcon && (
              <div style={{
                width: 28,
                height: 28,
                borderRadius: '50%',
                background: '#2a5298',
                color: '#fff',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontWeight: 700,
                fontSize: 15,
                flexShrink: 0
              }}>
                {item.name[0]}
              </div>
            )}
            <span>{item.name}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default function UserHome() {
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [selectedMapOption, setSelectedMapOption] = useState(null);
  const [selectedState, setSelectedState] = useState(null);
  const [countrySearch, setCountrySearch] = useState('');
  const [stateSearch, setStateSearch] = useState('');
  const navigate = useNavigate();

  const filteredCountries = countries.filter(c =>
    c.name.toLowerCase().includes(countrySearch.toLowerCase())
  );
  const filteredStates = indiaStates.filter(s =>
    s.name.toLowerCase().includes(stateSearch.toLowerCase())
  );

  const needsStateSelection =
    selectedCountry?.id === 'india' &&
    (selectedMapOption?.id === 'state' ||
      selectedMapOption?.id === 'district' ||
      selectedMapOption?.id === 'block');

  const mapLabel =
    selectedMapOption?.id === 'state'
      ? 'State'
      : selectedMapOption?.id === 'district'
      ? 'District'
      : selectedMapOption?.id === 'block'
      ? 'Block'
      : '';

  const handleCountrySelect = (country) => {
    setSelectedCountry(country);
    setSelectedMapOption(null);
    setSelectedState(null);
  };

  const handleMapOptionSelect = (option) => {
    setSelectedMapOption(option);
    setSelectedState(null);
    if (selectedCountry.id !== 'india') {
      navigate(`/map/${selectedCountry.id}/${option.id}`);
    }
  };

  const handleStateSelect = (state) => {
    setSelectedState(state);
    // For "district" or "block", you can navigate or show further UI here
    // Example: navigate(`/map/india/${selectedMapOption.id}?state=${state.id}`);
  };

  return (
    <div style={{ display: 'flex', height: '80vh' }}>
      {/* Country selection sidebar */}
      <SidebarList
        title="Country"
        searchValue={countrySearch}
        onSearch={setCountrySearch}
        items={filteredCountries}
        selectedId={selectedCountry?.id}
        onSelect={handleCountrySelect}
      />

      {/* State selection sidebar (like country) */}
      {needsStateSelection && (
        <SidebarList
          title="State"
          searchValue={stateSearch}
          onSearch={setStateSearch}
          items={filteredStates}
          selectedId={selectedState?.id}
          onSelect={handleStateSelect}
          showIcon
        />
      )}

      {/* Main content */}
      <div style={{ flex: 1, display: 'flex', alignItems: 'stretch', justifyContent: 'center', padding: 32 }}>
        {!selectedCountry ? (
          <h2 style={{ margin: 'auto' }}>Please select a country from the left</h2>
        ) : !selectedMapOption ? (
          <div style={{ width: '100%' }}>
            <h2 style={{ textAlign: 'center' }}>{selectedCountry.name}</h2>
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
              gap: 24,
              marginTop: 16
            }}>
              {mapOptions.map(option => (
                <div
                  key={option.id}
                  onClick={() => handleMapOptionSelect(option)}
                  style={{
                    border: '1px solid #e3e3e3',
                    borderRadius: 10,
                    padding: 20,
                    background: '#fff',
                    cursor: 'pointer',
                    boxShadow: '0 2px 8px rgba(30,60,114,0.04)',
                    textAlign: 'center',
                    transition: 'transform 0.2s, box-shadow 0.2s'
                  }}
                  onMouseOver={e => e.currentTarget.style.transform = 'scale(1.03)'}
                  onMouseOut={e => e.currentTarget.style.transform = 'scale(1)'}
                >
                  <img src={option.img} alt={option.label} style={{ height: 64, marginBottom: 12 }} />
                  <h4 style={{ margin: '8px 0 4px 0', color: '#1e3c72' }}>{option.label}</h4>
                  <p style={{ color: '#555', fontSize: '0.95rem' }}>{option.desc}</p>
                </div>
              ))}
            </div>
          </div>
        ) : selectedMapOption.id === 'regular map' ? (
          <div style={{
            flex: 1,
            minWidth: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            background: '#f4f8fc',
            borderRadius: 12,
            boxShadow: '0 2px 12px rgba(30,60,114,0.07)'
          }}>
            <div style={{ width: '1000', height: '1000', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <img src={indiaHighSvgUrl} alt="India Map" style={{ width: '100%', height: '100%' }} />
            </div>
          </div>
        ) : needsStateSelection ? (
          <div style={{
            flex: 1,
            minWidth: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            background: '#f4f8fc',
            borderRadius: 12,
            boxShadow: '0 2px 12px rgba(30,60,114,0.07)'
          }}>
            {selectedState ? (
              <div>
                <h3 style={{ textAlign: 'center', color: '#2a5298' }}>
                  {selectedState.name} {mapLabel} Map
                </h3>
                <div style={{
                  width: 320, height: 320, background: '#e3eaff',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  borderRadius: 10, margin: '0 auto'
                }}>
                  <span style={{ color: '#888' }}>
                    SVG map for {selectedState.name} ({mapLabel}) here
                  </span>
                </div>
              </div>
            ) : (
              <div style={{ color: '#bbb', fontSize: 22, textAlign: 'center' }}>
                Select a state to view its {mapLabel.toLowerCase()} map
              </div>
            )}
          </div>
        ) : (
          <h2 style={{ margin: 'auto' }}>Loading map...</h2>
        )}
      </div>
    </div>
  );
}