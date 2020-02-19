# pentagoSwap
Game-playing agent to play fictional "Pentago Swap" game. Majority of commits are from course TA defining various classes,
defining the random player, and creating the GUI for the game. My implementation was the strategy of the agent, namely
the functions chooseMove(), checkForWin(), evaluateBoardState(), abPruning(), maxNode(), & minNode(). These functions can be found at src/student_player/src/student_player/StudentPlayer.java.

# Launch GUI & Play Game
To launch the GUI, execute run the command 'java -cp bin boardgame.Server -p 8123 -t 300000' in the root directory of the project. To start playing, click Launch --> Launch human player and then Launch --> Launch client (student_player.StudentPlayer).

# How to Play
There are 4 quadrants containing 9 squares each. To place a piece, click on a square. To swap two quadrants, click on one quadrant and then the one you want to swap it with. Swapping two quadrants keeps pieces in their squares but moves them with the quadrant. 

A turn consists of placing a piece and then swapping any two quadrants. To win, you must have 5 pieces uninterrupted in a row, column, or diagonal. You and the agent take turns until one of you achieves 5 pieces in a row.


