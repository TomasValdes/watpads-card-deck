import React, {useState, useEffect, useRef} from "react";
import rockCard from './rockCard.png'
import paperCard from './paperCard.png'
import scissorsCard from './scissorsCard.png'


import "./App.css"

const Game = () => {
    const gameId = useRef(null);
    const userId = useRef(null);
    const winnerId = useRef(null)
    const [ws, setWs] = useState(null);
    const [gameState, setGameState] = useState(null);
    const [hand, setHand] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]);
    const [userName, setUserName] = useState(null);
    const [revealedCards, setRevealedCards] = useState(null);
    const [trumpCard, setTrumpCard] = useState(null);
    const [selfMove, setSelfMove] = useState();
    const [opponentMove, setOpponentMove] = useState();
    const [isMoveBeingRevealed, setIsMoveBeingRevealed] = useState(false);
    const [isCardButtonDisabled, setIsCardButtonDisabled] = useState(false);
    const cardTypes = {
        Rock: rockCard,
        Paper: paperCard,
        Scissors: scissorsCard
    };
    const GameStates = Object.freeze({
        SELECTING_TRUMP: "SelectingTrump",
        DRAFTING_CARDS: "DraftingCards",
        PLAYING_CARDS: "PlayingCards",
        REVEALING_CARDS: "RevealingCards",
        COMPLETED: "Completed"
    });

    const ResponseTypes = Object.freeze({
        GAME: "GameResponse",
        PLAYER: "PlayerResponse",
        REVEAL: "RevealCardsResponse",
        USER: "UserResponse",
    });

    const CardButton = ({cardName, onClick}) => (
        <button className="Card-button" onClick={() => onClick(cardName)} disabled={isCardButtonDisabled}>
            <img className="Card" src={cardTypes[cardName]} alt={`${cardName} Card`}/>
        </button>
    );

    const FormatGameBoard = ({text, cards}) => (
        <div>
            <h2>
                {text}
            </h2>
            {cards}
        </div>
    );

    const RoundReveal = () => {
        setTimeout(
            () => {
                setIsMoveBeingRevealed(false)
            }, 4000);

        return (
            <div>
                <img className="Card" style={{rotate: '90'}} src={cardTypes[opponentMove]}
                     alt={`Opponent playe d ${opponentMove}`}/>
                <br/>
                <img className="Card" src={cardTypes[selfMove]} alt={`I played ${selfMove}`}/>
            </div>
        )
    };

    useEffect(() => {
        const socket = new WebSocket("ws://localhost:8080/web/game");

        socket.onopen = () => {
            console.log("Connected to WebSocket");
        };

        socket.onmessage = (message) => {
            const data = JSON.parse(message.data);
            console.log("Received message:", data);

            switch (data.responseType) {
                case ResponseTypes.USER:
                    userId.current = data.userId;
                    setUserName(data.userName);
                    break;
                case ResponseTypes.GAME:
                    setIsCardButtonDisabled(false);
                    setGameState(data.gameState);
                    gameId.current = data.gameId;

                    /**
                     * Assign the games winner
                     */
                    if (data.gameState === GameStates.COMPLETED) {
                        winnerId.current = data.winner;
                    }
                    /**
                     * Make sure correct move is attributed to correct player and set gameboard to revealing state
                     */
                    if (data.gameState === GameStates.REVEALING_CARDS) {
                        setIsMoveBeingRevealed(true);
                        if (data.playerOneUserId === userId.current) {
                            setSelfMove(data.playerOneMove);
                            setOpponentMove(data.playerTwoMove);
                        } else {
                            setSelfMove(data.playerTwoMove);
                            setOpponentMove(data.playerOneMove);
                        }
                    }
                    break;
                case ResponseTypes.PLAYER:
                    setHand(data.hand);
                    break;
                case ResponseTypes.REVEAL:
                    setRevealedCards(data.revealedCards);
                    break;
                default:
                    break;
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
            setIsCardButtonDisabled(true);
            setTrumpCard(card);
            sendMessage({card});
        }
    };

    const addCardsToDeck = (card) => {
        setSelectedCards((prevCards) => {
            const newCards = [...prevCards, card];

            if (newCards.length === 3) {
                setIsCardButtonDisabled(true);
                sendMessage({card: newCards});
            }

            return newCards;
        });
    };

    const playCard = (card) => {
        setIsCardButtonDisabled(true);
        sendMessage({card});
    };

    const GameBoard = () => {
        /**
         * Don't want the board to update while the results of a round are being shown to players
         */
        if (isMoveBeingRevealed){
            return (<RoundReveal/>);
        }

        switch (gameState) {
            case null:
                return (
                    <div>
                        <h1>Connecting to a game ⏱️</h1>
                    </div>
                )
            case GameStates.SELECTING_TRUMP:
                return (
                    <FormatGameBoard
                        text={!trumpCard ? "Select a trump card" : "Waiting for opponent"}
                        cards={Object.keys(cardTypes).map((cardName) => (
                            <CardButton key={cardName} cardName={cardName} onClick={selectTrump}/>
                        ))}
                    />
                )
            case GameStates.DRAFTING_CARDS:
                return (
                    <FormatGameBoard
                        text={selectedCards.length < 3 ? "Select three cards to add to the deck"
                            : "Waiting for opponent"}
                        cards={Object.keys(cardTypes).map((cardName) => (
                            <CardButton key={cardName} cardName={cardName} onClick={addCardsToDeck}/>
                        ))}
                    />
                )
            case GameStates.REVEALING_CARDS:
                return (<RoundReveal/>);
            case GameStates.PLAYING_CARDS:
                return (
                    <FormatGameBoard
                        text={"Your Hand"}
                        cards={hand.map((cardName, index) => (
                            <CardButton key={index} cardName={cardName} onClick={playCard}/>
                        ))}
                    />
                )
            case GameStates.COMPLETED:
                return (
                    <div>
                        <h2>{winnerId.current === userId.current ? "You win!" : "Better luck next time"}</h2>
                    </div>
                )
        }
    }

    return (
        <div className="Game">
            <header className="Game-header">
                <h2 className="Game-header">Trump Card:</h2>
                {trumpCard &&
                    <img className="Game-header" src={cardTypes[trumpCard]} alt={`${trumpCard} Card`}/>
                }

                {revealedCards &&
                    <h2 className="Game-header">Revealed Cards:</h2>
                }

                {revealedCards && (
                    revealedCards.map((cardName, index) => (
                        <img key={index} className="Game-header" src={cardTypes[cardName]} alt={`${cardName} Card`}/>
                    ))
                )
                }
            </header>
            <div className="Game-body">
                <GameBoard/>
            </div>
        </div>
    );
};

export default Game;
