import React from 'react';
import { ComposableMap, Geographies, Geography } from "react-simple-maps";

function MapComponent({ data }) {
  const geoUrl = "https://raw.githubusercontent.com/deldersveld/topojson/master/world-countries.json";

  return (
    <ComposableMap>
      <Geographies geography={geoUrl}>
        {({ geographies }) =>
          geographies.map((geo) => {
            const countryCode = geo.properties.ISO_A2;
            const fill = countryCode === data.country ? "#FF5533" : "#EEE";
            return <Geography key={geo.rsmKey} geography={geo} fill={fill} />;
          })
        }
      </Geographies>
    </ComposableMap>
  );
}

export default MapComponent;