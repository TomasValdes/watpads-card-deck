import React, {useState, useEffect} from "react";
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
    const [revealedCards, setRevealedCards] = useState(null);
    const [trumpCard, setTrumpCard] = useState(null);
    const [selfMove, setSelfMove] = useState();
    const [opponentMove, setOpponentMove] = useState();
    const cardTypes = {
        Rock: rockCard,
        Paper: paperCard,
        Scissors: scissorsCard
    };
    const GameStates = Object.freeze({
        SELECTING_TRUMP: Symbol("SelectingTrump"),
        DRAFTING_CARDS: Symbol("DraftingCards"),
        PLAYING_CARDS: Symbol("PlayingCards"),
        REVEALING_CARDS: Symbol("RevealingCards"),
        COMPLETED: Symbol("Completed")
    });

    const ResponseTypes = Object.freeze({
        GAME: Symbol("GameResponse"),
        PLAYER: Symbol("PlayerResponse"),
        REVEAL: Symbol("RevealCardsResponse"),
        USER: Symbol("UserResponse"),
    });

    const CardButton = ({cardName, onClick}) => (
        <button onClick={() => onClick(cardName)}>
            <img className="Card" src={cardTypes[cardName]} alt={`${cardName} Card`}/>
        </button>
    );

    useEffect(() => {
        const socket = new WebSocket("ws://localhost:8080/web/game");

        socket.onopen = () => {
            console.log("Connected to WebSocket");
        };

        socket.onmessage = (message) => {
            const data = JSON.parse(message.data);
            console.log("Received message:", data);


            switch (data.responseType){
                case ResponseTypes.USER:
                    setUserId(data.userId)
                    setUserName(data.userName)
                    break;
                case ResponseTypes.GAME:
                    setGameState(data.gameState);
                    setGameId(data.gameId);

                    if (data.winner) {
                        setWinner(data.winner)
                    }

                    /**
                     * Make sure correct move is attributed to correct player
                     */
                    if (data.gameState === GameStates.REVEALING_CARDS){
                        if (data.playerOneUserId === userId){
                            setSelfMove(data.playerOneMove);
                            setOpponentMove(data.playerTwoMove);
                        } else{
                            setSelfMove(data.playerTwoMove);
                            setOpponentMove(data.playerOneMove);
                        }
                    }
                    break;
                case ResponseTypes.PLAYER:
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
        if (!trumpCard) {
            setTrumpCard(card)
            sendMessage({card});
        }
    };

    const addCardsToDeck = (card) => {
        setSelectedCards((prevCards) => {
            const newCards = [...prevCards, card];

            if (newCards.length === 3) {
                sendMessage({card: newCards});
            }

            return newCards;
        });
    }

    const playCard = (card) => {
        sendMessage({card});
    };

    return (
        <div>
            <p style={{textAlign: 'left'}}>Trump Card:
                {trumpCard &&
                    <img className="Card" src={cardTypes[trumpCard]} alt={`${trumpCard} Card`}/>
                }
            </p>
            <div className="Game-body">
                <h1>Rock-Paper-Scissors Game</h1>
                <p>Game State: {gameState}</p>
                <p>User Name: {userName}</p>
                <p>Cards revealed from deck: {revealedCards}</p>
                {gameState === null && (
                    <div>
                        <h1>Connecting to a game ⏱️</h1>
                    </div>
                )}
                {gameState === GameStates.SELECTING_TRUMP && (
                    <div>
                        <h2>{!trumpCard ? "Select a trump card"
                            : "Waiting for opponent"}
                        </h2>
                        {Object.keys(cardTypes).map((cardName) => (
                            <CardButton key={cardName} cardName={cardName} onClick={selectTrump}/>
                        ))}
                    </div>
                )}
                {gameState === GameStates.DRAFTING_CARDS && (
                    <div>
                        <h2>{selectedCards.length < 3 ? "Select three cards to add to the deck"
                            : "Waiting for opponent"}
                        </h2>
                        {Object.keys(cardTypes).map((cardName) => (
                            <CardButton key={cardName} cardName={cardName} onClick={addCardsToDeck}/>
                        ))}
                    </div>
                )
                }
                {gameState === GameStates.PLAYING_CARDS && (
                    <div>
                        <h2>Your Hand</h2>
                        {hand.map((cardName, index) => (
                            <CardButton key={index} cardName={cardName} onClick={playCard}/>
                        ))}
                    </div>
                )}
                {gameState === GameStates.REVEALING_CARDS && (
                    <div>
                        {revealPlays()}
                    </div>
                )}
                {gameState === GameStates.COMPLETED && (
                    <div>
                        <h2>{winnerId === userId ? "You win!" : "Better luck next time"}</h2>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Game;
