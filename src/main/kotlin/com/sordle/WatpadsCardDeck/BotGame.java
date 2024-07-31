import RockPaperScissors.CARDS;
import RockPaperScissors.GAME_STATE;
import RockPaperScissors.RESULT;
import Player;

import java.util.*;
import java.util.stream.Collectors;

public class BotGame {
  private final ArrayList<CARDS> cardOptions = new ArrayList<>(Arrays.asList(CARDS.ROCK, CARDS.PAPER, CARDS.SCISSORS);
  private final short HAND_SIZE = 3;
  private final short CARDS_TO_REVEAL = 2;
  private final Random random;
  private Player player; // the object of the player playing on the site
  private Player bot; // the object of the bot (completely controlled in this class
  private LinkedList<CARDS> startingDeck; // 
  private LinkedList<CARDS> currentDeck; // the deck after cards have been split up (gets cloned off of starting deck)
  
  private Player revealCardTo = null; // dictate the player to reveal the top two cards to

  private GAME_STATE state; // the current state of the game

  private short cardCounter = 0;

  private final RESULT[][] movesToResult = {
    {RESULT.TIE, RESULT.P2, RESULT.P1},
    {RESULT.P1, RESULT.TIE, RESULT.P2},
    {RESULT.P2, RESULT.P1, RESULT.TIE},
  };

  /**
   * @param p1 the player who is playing
   * Creates a new game between a player and a bot
   */ 
  public BotGame(Player p1) {
    player = p1;
    bot = new Player();
    startingDeck = new LinkedList<>(cardOptions);
    state = CARD_DRAFT;
    random = new Random();
  }

  /**
   * @param p1 the player who is playing
   * @param rand the random number generator to be used.
   * SHOULD ONLY BE USED FOR DEBUG.
   * Controllable game that is repeatable for testing purposes.
   * Constructs debugging game between player and a bot.
   */ 
  protected BotGame(Player p1, Random rand) {
    player = p1;
    bot = new Player();
    startingDeck = new LinkedList<>(cardOptions);
    state = CARD_DRAFT;
    random = rand;
  }

  /**
   * @param card the card selected by the user
   * Drafts the card into the starting deck then has the bot do so as well.
   * After the bot drafts its cards, the cards are distributed and the game state changes to playing cards
   */ 
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

  /**
   * distributes the cards between the players at random
   */ 
  private void distributeCards() {
    Collections.shuffle(startingDeck);

    currentDeck = new LinkedList<>(startingDeck);

    for (int i = 0; i < HAND_SIZE; i++) {
      player.hand.add(currentDeck.pop());
      bot.hand.add(currentDeck.pop());
    }
  }

  /**
   * Determines the winner of the game and is dependent on win condition.
   * @return the winner of the game and null if neither player has won yet. updates game state if appropriate to do so
   */ 
  public Player getWinner() {
   Player winner = getWinner(player, bot);
  
    if (winner.move == winner.winningCard) {
      state = GAME_STATE.ROUND_RESULTS;
      winner.score++;
      return winner;
    }
    
   return null;
  }

  /**
   * @param p1 player 1 relective in {@link #RESULT}
   * @param p2 player 2 reflective in {@link #RESULT}
   * Determines the winner of the standard game without taking into account win conditions
   * @return the winner according to the decision matrix as a result reflecting the order of the parameters
   */ 
  private RESULT getWinner(Player p1, Player p2) {
    return movesToResult[p1.move][p2.move];
  } 

  /**
   * Drafts the cards from the bot randomly
   */ 
  private void draftCardBot() {
    for(int i = 0; i < HAND_SIZE; i++) {
	startingDeck.add(cardOptions.get(random.nextInt(cardOptions.size())));
    }
  }

  /**
   * @param index the index of the card in the players hand to remove
   * plays the card and removes it from the player's hand
   */ 
  public void playCard(int index) {
     player.move = player.hand.remove(index);
  }

  /**
   * @return the options of what cards you can select from
   */
  public List<CARDS> getDraftOptions() {
    return cardOptions;
  }

  public void play_again() {
    // update some sort of score in db here as well as games played

    state = GAME_STATE.CARD_DRAFT;
  }

}


