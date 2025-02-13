import React, { useState, useEffect } from "react";

const Game = () => {
    const [ws, setWs] = useState(null);
    const [gameState, setGameState] = useState(null);
    const [hand, setHand] = useState([]);
    const [gameId, setGameId] = useState(null);
    const [playerId, setPlayerId] = useState(null);

    useEffect(() => {
        const socket = new WebSocket("ws://localhost:8080/web/game");

        socket.onopen = () => {
            console.log("Connected to WebSocket");
        };

        socket.onmessage = (message) => {
            const data = JSON.parse(message.data);
            console.log("Received message:", data);

            if (data.gameState) {
                setGameState(data.gameState);
                setGameId(data.gameId);
                setPlayerId(data.playerOneUserId); // Assuming player is player one
            }

            if (data.hand) {
                setHand(data.hand);
            }
        };

        setWs(socket);

        return () => {
            socket.close();
        };
    }, []);

    const sendMessage = (message) => {
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify(message));
        }
    };

    const selectTrump = (card) => {
        sendMessage({ card });
    };

    const playCard = (card) => {
        sendMessage({ card });
    };

    return (
        <div>
            <h1>Rock-Paper-Scissors Game</h1>
            <p>Game State: {gameState}</p>
            {gameState === "SelectingTrump" && (
                <div>
                    <h2>Select a Trump Card</h2>
                    <button onClick={() => selectTrump("Rock")}>
                        <img src={"../public/Rock Card.png"} alt={"Rock Card"}></img></button>
                    <button onClick={() => selectTrump("Paper")}>Paper</button>
                    <button onClick={() => selectTrump("Scissors")}>Scissors</button>
                </div>
            )}
            {gameState === "PlayingCards" && (
                <div>
                    <h2>Your Hand</h2>
                    {hand.map((card, index) => (
                        <button key={index} onClick={() => playCard(card)}>
                            {card}
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
};

export default Game;
