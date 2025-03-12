import './App.css';
import Game from "./Game";
import {useEffect, useRef} from "react";
import * as THREE from "three";
import FOG from "vanta/dist/vanta.fog.min";

function App() {
    const vantaRef = useRef(null);

    useEffect(() => {
        const effect = FOG({
            el: vantaRef.current, // Attach to this div
            THREE: THREE,
            mouseControls: true,
            touchControls: true,
            gyroControls: false,
            minHeight: 200.0,
            minWidth: 200.0,
            highlightColor: 0x44f45a,
            midtoneColor: 0x0,
            lowlightColor: 0x74ff74,
            baseColor: 0x146814,
        });

        return () => {
            if (effect) effect.destroy(); // Clean up effect on unmount
        };
    }, []);

  return (
    <div className="App" ref={vantaRef}>
      <header>
          <Game />
      </header>
    </div>
  );
}

export default App;
