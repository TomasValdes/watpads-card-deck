/**
* Represents the states that the game could be in.
* General game flow should go down the line as they appear in order
*/
enum class GameStates {
// select the card you want to win with
 SELECTING_TRUMP,

 // select the cards you would like to add to the shared deck
 CARD_DRAFT,

 // select the cards you would like to play each round
 PLAYING_CARDS,

 // reveal the blind to the winner who didn't win on win condition
 REVEALING_CARDS,
 
 // display the results of the game
 ROUND_RESULTS
}
