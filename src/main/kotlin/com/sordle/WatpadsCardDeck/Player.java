package Demo.Models;

import Demo.RockPaperScissors;
import Demo.RockPaperScissors.RESULT;

import java.util.ArrayList;


public class Player {
  public ArrayList<RockPaperScissors.CARDS> hand;
  public int score = 0;
  public RockPaperScissors.CARDS move;
  public RockPaperScissors.CARDS winningCard;
  public boolean played;

  private int id; // should  match value in the database

  public Player(ArrayList<RockPaperScissors.CARDS> playerDeck){
    this.hand = playerDeck;
    played = false;
  }

  public Player(){
    this.hand = new ArrayList<>();
  }

  public boolean equals(Player other) {
    return this.id == other.id;
  }

  
}
