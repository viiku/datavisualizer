import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

export default function IndiaSVGMap({ onRegionClick, regionData }) {
  const [selected, setSelected] = useState(null);
  const svgRef = useRef();

  useEffect(() => {
    const svg = d3.select(svgRef.current);
    svg.selectAll('path')
      .style('fill', function() {
        const id = d3.select(this).attr('id');
        if (selected === id) return '#2a5298';
        if (regionData && regionData[id]) return '#90caf9';
        return '#eee';
      })
      .style('cursor', 'pointer')
      .on('click', function(event) {
        const regionId = d3.select(this).attr('id');
        setSelected(regionId);
        if (onRegionClick) onRegionClick(regionId);
      });
  }, [selected, regionData, onRegionClick]);

  return (
    <div style={{ width: '100%', height: '100%' }}>
      {/* Paste your SVG markup below and attach ref to <svg> */}
      <svg ref={svgRef} viewBox="..." width="100%" height="100%">
        {/* ... all your <path id="..." ... /> and other SVG content ... */}
      </svg>
    </div>
  );
}