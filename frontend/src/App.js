import './App.css';
import Game from "./Game";
import {useState} from "react";

function App() {
    const [joinedGame, setJoinedGame] = useState(false);
  return (
    <div className="App">
      <header className="App-header">
        {!joinedGame ? (
          <button onClick={() => setJoinedGame(true)}>Join Game</button>
        ) : (
          <Game />
        )}
      </header>
    </div>
  );
}

export default App;
