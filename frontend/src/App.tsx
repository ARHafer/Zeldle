import { useEffect, useState } from 'react';
import { getStatus } from './api/statusApi';
import './App.css';

function App() {

  const [status, setStatus] = useState(false);
  const statusString = status ? "Online" : "Offline";

  useEffect(() => {
    getStatus().then(setStatus);
  }, [])

  return <h1>Backend Status: {statusString}</h1>
}

export default App
