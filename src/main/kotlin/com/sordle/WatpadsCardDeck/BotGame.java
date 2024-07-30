import RockPaperScissors.CARDS;
import RockPaperScissors.GAME_STATE;
import RockPaperScissors.RESULT;

import java.util.*;
import java.util.stream.Collectors;

public class BotGame {
  private final ArrayList<CARDS> cardOptions = new ArrayList<>(Arrays.asList(CARDS.ROCK, CARDS.PAPER, CARDS.SCISSORS);
  private final short HAND_SIZE = 3;
  private final short CARDS_TO_REVEAL = 2;
  private final Random random = new Random();
  private Player player;
  private Player bot;
  private LinkedList<CARDS> startingDeck;
  private LinkedList<CARDS> currentDeck;
  private boolean revealTopCard = false;

  private GAME_STATE state;

  private short cardCounter = 0;

  private final RESULT[][] movesToResult = {
    {RESULT.TIE, RESULT.P2, RESULT.P1},
    {RESULT.P1, RESULT.TIE, RESULT.P2},
    {RESULT.P2, RESULT.P1, RESULT.TIE},
  };

  public BotGame(Player p1) {
    player = p1;
    bot = new Player();
    startingDeck = new LinkedList<>(cardOptions);
    state = CARD_DRAFT;
  }

  public void playerDraftCard(CARDS card) {
    startingDeck.add(card);
    cardCounter++;
    if (cardCounter == HAND_SIZE) {
      draftCardBot();
      distributeCards();
      state = GAME_STATE.PLAYING_CARDS;
      cardCounter = 0;
    }

  }

  private void distributeCards() {
    Collections.shuffle(startingDeck);

    currentDeck = new LinkedList<>(startingDeck);

    for (int i = 0; i < HAND_SIZE; i++) {
      player.hand.add(currentDeck.pop());
      bot.hand.add(currentDeck.pop());
    }
  }

  public Player getWinner() {
    
  }

  public RESULT getWinner(Player p1, Player p2) {
    return movesToResult[p1.move][p2.move];
  } 

  private void draftCardBot() {
    for(int i = 0; i < HAND_SIZE; i++) {
	startingDeck.add(cardOptions.get(random.nextInt(cardOptions.size())));
    }
  }

  public playCard(int index) {
     player.move = player.hand.remove(index);
  }



  public List<CARDS> getDraftOptions() {
    return cardOptions;
  }



}


