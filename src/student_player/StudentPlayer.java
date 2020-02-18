package student_player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import boardgame.Board;
import boardgame.Move;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;



/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260621462");
    }

    public static int maxDepth = 2;

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...


        Node root = new Node(boardState, boardState.getOpponent() == 1, 0);

        abPruning(root, boardState.getTurnPlayer(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);

        Move myMove = root.getBestMove();


        for(Node node : root.getChildren()) {
        	if(node.getBoard().getWinner() == boardState.getTurnPlayer()) {
        		return node.lastMove;
        	}
        }
        return myMove;

    }


    //-----START CHECKFORWIN FUNCTION-----//
    public static int checkForWin(Node node) {
    	if(node.getBoard().getWinner() != Board.NOBODY) {  //i.e. there's a winner
    		if(node.getBoard().getWinner() == 0) {         //human player wins
    			return 0;
    		}
    		else {
    			return 1;                                  //opponent wins
    		}
    	}
    	else {
    		return -1;
    	}
    }
    //-----END CHECKFORWIN FUNCTION-----//




    //-----START EVALUATEBOARDSTATE FUNCTION-----//
    public int evaluateBoardState(Node node, int playerID, int currentDepth) {
    	int score= 0;

    	if(checkForWin(node) != -1) {
    		if(node.getBoard().getWinner() == playerID) {
    			score += 10000;    //win
    		}
    		else {
    			score -= 10000;   //lose
    		}
    	}

    	int numCenters = 0;
    	int oppCenters = 0;


    	if(node.getBoard().getTurnPlayer() == 0){
    		//WHITE
    		int[] spots= {1,4};
    		for (int i=0; i<2; i++){
    			for (int j=0; j<2; j++) {
    				if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.WHITE) {
    					numCenters += 1;
    				}
    				else if(node.getBoard().getPieceAt(spots[i],spots[j])==Piece.BLACK){
    					oppCenters += 1;
    				}
    			}
    		}

    		for (int i=0; i<2; i++){
    			for (int j=0; j<2; j++) {
    				if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.WHITE) {
    					if (numCenters < 4) {
    						score += 300;
    					}
    					else {
    						score += 50;
    					}
    					//If you have 3 in a row, get a very good score
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE) {
    					score += 150;
    					}
    					//Else, if you don't have 3 in a row, but have 2 in a row and the 3rd is empty, get a good score
    					else if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.EMPTY ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE) {
    						score += 100;
    					}
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK) {
    						if(node.getBoard().getTurnNumber() <= 8) {
    							score -= 1000;
    						}
    						else {
    							score -= 50;
    						}
    					}
    				}
    				//black center
    				else if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.BLACK){
    					score -= 300;
    					//If opponent has 3 in a row, get an equally bad score
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK) {
        					score -= 150;
        				}
    					else if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.EMPTY ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK) {
    						score -= 100;
    					}
						if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK) {
    						if(node.getBoard().getTurnNumber() <= 8) {
    							score -= 1000;
    						}
    						else {
    							score -= 50;
    						}
						}
    				}
    				//empty center
    				else {
						if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK) {
    						if(node.getBoard().getTurnNumber() <= 8) {
    							score -= 1000;
    						}
    						else {
    							score -= 50;
    						}
    					}
					}
    			}
    		}
    	}
    	else {
        // BLACK
    		int[] spots= {1,4};
    		for (int i=0; i<2; i++){
    			for (int j=0; j<2; j++) {
    				if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.BLACK) {
    					numCenters += 1;
    				}
    				else if(node.getBoard().getPieceAt(spots[i],spots[j])==Piece.WHITE){
    					oppCenters += 1;
    				}
    			}
    		}

    		for (int i=0; i<2; i++){
    			for (int j=0; j<2; j++) {
    				if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.BLACK) {
    					if (numCenters < 4) {
    						score += 300;
    					}
    					else {
    						score += 50;
    					}
    					//If you have 3 in a row, get a very good score
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK) {
    					score += 150;
    					}
    					//Else, if you don't have 3 in a row, but have 2 in a row and the 3rd is empty, get a good score
    					else if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.EMPTY ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.BLACK && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.BLACK ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.BLACK || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.BLACK || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.BLACK) {
    						score += 100;
    					}
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE) {
    						if(node.getBoard().getTurnNumber() <= 4) {
    							score -= 1000;
    						}
    						else {
    							score -= 50;
    						}
    					}
    				}
    				//WHITE center
    				else if (node.getBoard().getPieceAt(spots[i],spots[j])==Piece.WHITE){
    					score -= 300;
    					//If opponent has 3 in a row, get an equally bad score
    					if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE) {
        					score -= 150;
        				}
    					else if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.EMPTY ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.EMPTY || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE ||node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.EMPTY && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE) {
    						score -= 100;
    					}
    				}
    				//empty center
    				else {
						if(node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]-1,spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i],spots[j]+1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE || node.getBoard().getPieceAt(spots[i]+1,spots[j]-1)==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j])==Piece.WHITE && node.getBoard().getPieceAt(spots[i]+1,spots[j]+1)==Piece.WHITE) {
    						if(node.getBoard().getTurnNumber() <= 8) {
    							score -= 1000;
    						}
    						else {
    							score -= 50;
    						}
    					}
					}
    			}
    		}

    	}
    	return score;
    }

    //-----END SCORE FUNCTION-----//


    //----- START ALPHA-BETA PRUNING FUNCTION-----//
    public int abPruning(Node node, int playerID, double alpha, double beta, int currentDepth) {
    	if(currentDepth++ == maxDepth || checkForWin(node) != -1) {					//if at bottom of tree, get the score of the node
    		return evaluateBoardState(node, playerID, currentDepth);
    	}
    	if(node.getBoard().getTurnPlayer() == playerID) {							//if max node --> call maxNode function
    		return maxNode(node, playerID, alpha, beta, currentDepth);
    	}
    	else {																	    //if min node --> call minNode function
    		return minNode(node, playerID, alpha, beta, currentDepth);
    	}
    }
    //-----END ALPHA-BETA PRUNING FUNCTION-----//


    //-----START MAX NODE FUNCTION-----//
    public int maxNode(Node node, int playerID, double alpha, double beta, int currentDepth) {
    	List<PentagoMove> currentOptions = node.getBoard().getAllLegalMoves();
    	int index = -1;
    	for(PentagoMove legalMove : currentOptions) {
    		index++;
    		PentagoBoardState clonedState = (PentagoBoardState) node.getBoard().clone();
    		clonedState.processMove(legalMove);   	//execute move on clone

    		//store the data in the nodes
    		Node nodeAfterMove = new Node(clonedState, !node.isMaxPlayer(), node.depth+1);
    		nodeAfterMove.lastMove = legalMove;
    		nodeAfterMove.setParent(node);
    		node.addChild(nodeAfterMove);

    		//call alpha beta on the node
    		int score = abPruning(nodeAfterMove, playerID, alpha, beta, currentDepth);
    		if(score > alpha) {
    			//meat of the alpha beta logic
    			alpha = score;
    			node.setBestMove(legalMove);
    			node.setBestScore(score);
    			node.setBestIndex(index);
    		}
    		if(alpha >= beta) {
    			break;
    		}
    	}
    	return (int) alpha;
    }
    //-----END MAX NODE FUNCTION-----//


    //-----START MIN NODE FUNCTION-----//
    public int minNode(Node node, int playerID, double alpha, double beta, int currentDepth) {
    	List<PentagoMove> currentOptions = node.getBoard().getAllLegalMoves();
    	int index = -1;
    	for(PentagoMove legalMove : currentOptions) {
    		index++;
    		PentagoBoardState clonedState = (PentagoBoardState) node.getBoard().clone(); //clone board
    		clonedState.processMove(legalMove);  //execute move
    		//store the data in the nodes
    		Node nodeAfterMove = new Node(clonedState, !node.isMaxPlayer(), node.depth+1);
    		nodeAfterMove.lastMove = legalMove;
    		nodeAfterMove.setParent(node);
    		node.addChild(nodeAfterMove);

    		//call alpha beta on the node
    		int score = abPruning(nodeAfterMove, playerID, alpha, beta, currentDepth);
    		if(score < beta) {
    			//meat of the alpha beta logic
    			beta = score;
    			node.setBestMove(legalMove);
    			node.setBestScore(score);
    			node.setBestIndex(index);
    		}
    		if(alpha >= beta) {
    			break;
    		}
    	}
    	return (int) beta;
    }
    //-----END MIN NODE FUNCTION-----//


    //-----START NODE CLASS DECLARATION-----//
    public class Node {
    	PentagoBoardState board;
    	public Node parent;
    	public boolean isMaxPlayer;
    	public int score;
    	public int BestIndex;
    	public List<Node> children;
    	public PentagoMove lastMove;
    	public int BestScore = -999999;
    	public int depth;
    	public Move bestMove;

    	public int getBestScore() {
    	return BestScore;
    	}

    	public void setBestScore(int bestScore) {
    		BestScore = bestScore;
    	}

    	public int getBestIndex() {
    		return BestIndex;
    	}

    	public void setBestIndex(int bestIndex) {
    		BestIndex = bestIndex;
    	}

    	public Move getBestMove() {
    		return bestMove;
    	}

    	public void setBestMove(Move bestMove) {
    		this.bestMove = bestMove;
    	}

    	public Node(PentagoBoardState board, boolean isMaxPlayer, int depth) {
    		this.board = board;
    		this.isMaxPlayer = isMaxPlayer;
    		children = new CopyOnWriteArrayList<>();
    		this.depth = depth;
    	}

    	PentagoBoardState getBoard() {
    		return this.board;
    	}

    	boolean isMaxPlayer() {
    		return isMaxPlayer;
    	}

    	int getScore() {
    		return score;
    	}

    	void setScore(int score) {
    		this.score = score;
    	}

    	List<Node> getChildren() {
    		return children;
    	}

    	void addChild(Node newChild) {
    		children.add(newChild);
    	}

    	void setChildren(List<Node> listChildren) {
    		this.children = listChildren;
    	}

    	public PentagoMove getLastMove() {
    		return lastMove;
    	}

    	public void setLastMove(PentagoMove lastMove) {
    		this.lastMove = lastMove;
    	}

    	public Node getParent() {
    		return parent;
    	}

    	public void setParent(Node parent) {
    		this.parent = parent;
    	}

    	public int getDepth() {
    		return depth;
    	}

    	public void setDepth(int depth) {
    		this.depth = depth;
    	}
    }
    //-----END NODE CLASS DECLARATION-----//
}
