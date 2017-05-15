/**
 * @author Kenny Tram
 * Pieces of code are modifications of Marietta Cameron's code
 */
package KKT_Othello;
import java.util.Scanner;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    public static final int ME = 1;
    public static final int OPPONENT = -1;
    public static final int EMPTY = 0;
    public static final int BORDER = -2;
    
    private Board gameBoard;
    private int currentPlayer;
    private Player me;
    private Player opponent;
    private Scanner input = new Scanner(System.in);
    private int moveNum;
    
    static double timeAllocation[] = {0.015, 0.015, 0.015, 0.015, 0.025, 0.025, 0.025, 0.025, 0.025, 0.025,
                                      0.048,  0.048, 0.048, 0.048, 0.048, 0.048, 0.050, 0.051, 0.052, 0.053,
                                      0.044,  0.045, 0.049, 0.049, 0.049, 0.051, 0.053, 0.055, 0.057, 0.059,
                                      0.060, 0.060, 0.061, 0.062, 0.063, 0.064, 0.065, 0.065, 0.065, 0.065,
                                      0.167, 0.168, 0.169, 0.169, 0.171, 0.172, 0.173, 0.175, 0.180, 0.180,
                                      0.181, 0.187, 0.196, 0.199, 0.220, 0.220, 0.220, 0.220, 0.220, 0.220,
                                      0.220, 0.250, 0.250, 0.250, 0.250, 0.250, 0.250, 0.250, 0.250, 0.250
                                     };

    public int timeRemaining; //keeps track of the remaining time
    Timer timer;
    public static boolean timeUP;  //tells if time is up
    
    /**
     * Initializes and runs an Othello game
     */
    public Game() {
        System.out.println("C Initializing Board");
        assignColors();
        gameBoard = new Board(me, opponent);        
        timeRemaining = 10 * 60;
        if(me.getColor() == 'B')
            currentPlayer = ME;
        else
            currentPlayer = OPPONENT;
                
        while (!gameBoard.gameOver()) {
            Move move;
            if(currentPlayer == ME) {
                System.out.println(gameBoard);
                gameBoard.printMoves(me);
                move = getMyMove(gameBoard);
                System.out.println(move);
            }
            else {
                //gameBoard.printMoves(opponent);
                move = gameBoard.getOpponent();
            }
            gameBoard.applyMove(currentPlayer, move);
            
            currentPlayer = -1*currentPlayer;  //switches players
        }
    }
    
    /**
     * Assigns each player's respective colors based on a given input
     */
    public void assignColors() {
       String directions = input.nextLine();
       if(directions.equals("I B")) {
           me = new Player('B', 1);
           opponent = new Player('W', -1);
       }
       else {
           me = new Player('W', 1);
           opponent = new Player('B', -1);
       }
       
       System.out.println("R " + me.getColor());
    }
    
    /**
     * Calculates and returns the inferred best move
     * @param board Representation of the current board
     * @return Inferred best move
     */
    public Move getMyMove(Board board){
        moveNum++;
        Move move;
        
        timeUP = false;
        timer = new Timer();
        
        int timeForMove = (int)(timeAllocation[moveNum]*(double)timeRemaining); 
        
        System.out.println("C Move Time:  " + timeForMove);
        timer.schedule(new InterruptTask(), timeForMove*1000);
        
        move = board.getMyMove();
        //move = board.bestMove(me);
        
        if (!timeUP)
          timer.cancel();
        timeRemaining -= timeForMove;
        
        System.out.println("C Remaining Time: " + timeRemaining);
        
        return move;
    }
    
    /**
     * Interrupts the current task if no time remains
     */
    class InterruptTask extends TimerTask {
        public void run() {
            System.out.println("C ***NO TIME REMAINING***");
            timeUP = true;
            timer.cancel();
        }
    }
    
    /**
     * Main method; runs the Othello game
     */
    public static void main(String [] args) {
        Game othelloMatch = new Game();
    }
}


