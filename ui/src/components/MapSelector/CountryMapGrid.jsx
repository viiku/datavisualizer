import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './countryGrid.css';
import populationImg from '../../assets/maps/population.png';
import literacyImg from '../../assets/maps/literacy.png';
import gdpImg from '../../assets/maps/gdp.png';
import urbanizationImg from '../../assets/maps/urbanization.png';
import liquorImg from '../../assets/maps/liquor.png';

const exampleMaps = [
  {
    id: 'population',
    title: 'Population Density',
    description: 'See how population is distributed across countries.',
    img: populationImg
  },
  {
    id: 'literacy',
    title: 'Literacy Rate',
    description: 'Compare literacy rates by country.',
    img: literacyImg
  },
  {
    id: 'gdp',
    title: 'GDP per Capita',
    description: 'Visualize GDP per capita across the globe.',
    img: gdpImg
  },
  {
    id: 'urbanization',
    title: 'Urbanization',
    description: 'See the percentage of urban population by country.',
    img: urbanizationImg
  },
  {
    id: 'Liquor consumption',
    title: 'Liquor consumption',
    description: 'See the percentage of Liquor consumption by country.',
    img: liquorImg
  }
];

export default function CountryMapGrid() {
  const navigate = useNavigate();

  return (
    <div>
      <div className="hero-section">
        <div className="hero-content">
          <h1>Data Visualizer</h1>
          <p>
            Interactive maps for population, literacy, age, and more.<br />
            Explore global data visually and intuitively.
          </p>
          <Link to="/home" className="create-map-link">
            <button className="create-map-btn">Create your own maps</button>
          </Link>
        </div>
      </div>

      <div className="country-grid-container">
        <h2 className="section-title">Example Data Maps</h2>
        <div className="country-grid">
          {exampleMaps.map(example => (
            <div
              key={example.id}
              className="country-card"
              onClick={() => navigate(`/upload?example=${example.id}`)}
            >
              <div className="country-thumbnail">
                <img src={example.img} alt={example.title} />
              </div>
              <h3>{example.title}</h3>
              <p className="card-desc">{example.description}</p>
            </div>
          ))}
        </div>
      </div>

      <footer className="footer">
        <div>
          &copy; {new Date().getFullYear()} Data Visualizer &mdash; Made with ❤️
        </div>
      </footer>
    </div>
  );
}