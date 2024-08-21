# Overview
We needed a way to send commands via the web socket to play the game. \n

## Table
Here is the general structure of those commands: \n

| Player | Command | arg |
| :-: | :-: | :-: |
| Player | Ready | null |
| Player | Join | Room: # |
| Player | Join | random |
| Player | Join | bot |
| Player | select | card: CardType |



