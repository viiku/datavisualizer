import React from 'react';
import { Link } from 'react-router-dom';
import populationImg from '../../assets/maps/population.png';
import literacyImg from '../../assets/maps/literacy.png';
// ...other imports...

const exampleMaps = [
  // ...your exampleMaps array...
];

export default function HomePage() {
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
              // ...onClick logic...
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