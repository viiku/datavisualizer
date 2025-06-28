// services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // Spring Boot backend
  headers: {
    'Content-Type': 'multipart/form-data'
  }
});

export default {
  post: (url, data) => api.post(url, data),
  get: (url) => api.get(url)
};