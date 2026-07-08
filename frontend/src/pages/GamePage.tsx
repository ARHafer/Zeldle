import { useEffect, useState } from 'react';
import { getItems } from '../api/itemApi';
import { getStatus } from '../api/statusApi';
import type { Item } from '../types/Item';
import ItemList from '../components/ItemList';

function GamePage() {

  const [items, setItems] = useState<Item[]>([]);
  const [status, setStatus] = useState(false);
  const statusString = status ? "Online" : "Offline";

  useEffect(() => {
    getItems().then(setItems);
    getStatus().then(setStatus);
  }, [])

  return (
  <><h1>Backend Status: {statusString}</h1> 
  <br/> 
  <ItemList items={items}/></>
  )
}

export default GamePage