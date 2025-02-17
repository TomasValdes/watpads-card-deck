import React, { useState, useEffect } from "react";
import rockCard from './rockCard.png'
import paperCard from './paperCard.png'
import scissorsCard from './scissorsCard.png'


import "./App.css"

const Game = () => {
    const [ws, setWs] = useState(null);
    const [gameState, setGameState] = useState(null);
    const [hand, setHand] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]);
    const [gameId, setGameId] = useState(null);
    const [userId, setUserId] = useState(null);
    const [userName, setUserName] = useState(null);
    const [winnerId, setWinner] = useState(null)

    useEffect(() => {
        const socket = new WebSocket("ws://localhost:8080/web/game");

        socket.onopen = () => {
            console.log("Connected to WebSocket");
        };

        socket.onmessage = (message) => {
            const data = JSON.parse(message.data);
            console.log("Received message:", data);

            if (data.userId) {
                setUserId(data.userId)
                setUserName(data.userName)
            }

            if (data.gameState) {
                setGameState(data.gameState);
                setGameId(data.gameId);

                if (data.winner){
                    setWinner(data.winner)
                }
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
        console.log("Sent message: " + message);
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify(message));
        }
    };

    const selectTrump = (card) => {
        sendMessage({ card });
    };

    const addCardsToDeck = (card) => {
        setSelectedCards((prevCards) => {
            const newCards = [...prevCards, card];

            if (newCards.length === 3) {
                sendMessage({ card: newCards });
            }

            return newCards;
        });
    }

    const playCard = (card) => {
        sendMessage({ card });
    };

    return (
        <div>
            <h1>Rock-Paper-Scissors Game</h1>
            <p>User Name: {userName}</p>
            <p>Game State: {gameState}</p>
            {gameState === null && (
                <div>
                    <h1>Connecting to a game ⏱️</h1>
                </div>
            )}
            {gameState === "SelectingTrump" && (
                <div>
                    <h2>Select a Trump Card</h2>
                    <button onClick={() => selectTrump("Rock")}>
                        <img className={"Card"} src={rockCard} alt={"Rock Card"}></img>
                    </button>
                    <button onClick={() => selectTrump("Paper")}>
                        <img className={"Card"} src={paperCard} alt={"Paper Card"}></img>
                    </button>
                    <button onClick={() => selectTrump("Scissors")}>
                        <img className={"Card"} src={scissorsCard} alt={"Scissors Card"}></img>
                    </button>
                </div>
            )}
            {
                gameState === "DraftingCards" && (
                    <div>
                        <h2>Select three cards to add to the deck</h2>
                        <button onClick={() => addCardsToDeck("Rock")}>
                            <img className={"Card"} src={rockCard} alt={"Rock Card"}></img>
                        </button>
                        <button onClick={() => addCardsToDeck("Paper")}>
                            <img className={"Card"} src={paperCard} alt={"Paper Card"}></img>
                        </button>
                        <button onClick={() => addCardsToDeck("Scissors")}>
                            <img className={"Card"} src={scissorsCard} alt={"Scissors Card"}></img>
                        </button>
                    </div>
                )
            }
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
            {gameState === "Completed" && (
                <div>
                    <h2>{winnerId === userId ? "You win!" : "Better luck next time"}</h2>
                </div>
            )}
        </div>
    );
};

export default Game;
