/**
 * @author Kenny Tram
 * Pieces of code are modifications of Marietta Cameron's code
 */
package KKT_Othello;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Board {
    public static final int ME = 1;
    public static final int OPPONENT = -1;
    public static final int EMPTY = 0;
    public static final int BORDER = -2;
    
    private int directions[] = {-10, -9, 1, 11, 10, 9, -1, -11};
        /**
         * North = -10;
         * Northeast = -9;
         * East = 1;
         * Southeast = 11;
         * South = 10;
         * Southwest = 9;
         * West = -1;
         * Northwest = -11;
         */
    
    public int[] board = new int[100];
    private Player me;
    private Player opponent;
    private String[] colLabels = {" ", " ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
    private String[] rowLabels = {" ", "1", "2", "3", "4", "5", "6", "7", "8", " "};
    
    /**
     * Initializes a game board
     * @param player1 The program's player object
     * @param player2 The opponent's player object
     */
    public Board(Player player1, Player player2) {
        me = player1;
        opponent = player2;
        
        generateEmptyBoard();
        setupBoard(me.getColor());
    }
    
    /**
     * Makes a copy of the provided game board
     * @param oldBoard The game board being copied
     * @param player1 The player's perspective that the board is based on
     * @param player2 The opponent of the above player
     */
    public Board(Board oldBoard, Player player1, Player player2) {
        me = player1;
        opponent = player2;
        for(int i = 0; i < board.length; i++)
            board[i] = oldBoard.board[i];
    }
    
    /**
     * Generates an empty board with borders
     */
    public void generateEmptyBoard() {
        for(int i = 0; i < board.length; i++) {
            if((i < 10) || (i%10 == 0) || (i%10 == 9) || (i > 89))
                board[i] = BORDER;
            else
                board[i] = EMPTY;
        }
    }
    
    /**
     * Places the starting pieces on the board
     * @param myColor The program's assigned color
     */
    public void setupBoard(char myColor) {
        if(myColor == 'B' ) {
            board[45] = ME;
            board[54] = ME;
            board[44] = OPPONENT;
            board[55] = OPPONENT;
        }
        else {
            board[44] = ME;
            board[55] = ME;
            board[45] = OPPONENT;
            board[54] = OPPONENT;
        }
    }
    
    /**
     * Determines the player's best move based on an alpha beta game tree and an evaluation function
     * @return Inferred best move
     */
    public Move getMyMove() {
        double alpha = Double.MIN_VALUE; //-infinity
        double beta = Double.MAX_VALUE; //infinity
        
        ArrayList<Move> moves = generateMoves(me);
        
        if(moves.isEmpty()) {
            Move move = new Move(me.getColor());
            return move;
        }
        
        return alphaBeta(this, 0, me, alpha, beta, 2);
    }
    
    /**
     * Generates all possible moves the player can make
     * @param player Determines which player perspective is being taken into consideration
     * @return A list of valid moves; an empty list if no valid moves are available
     */
    public ArrayList<Move> generateMoves(Player player) {
        ArrayList<Move> moveList = new ArrayList<Move>();
        boolean duplicate = false; //keeps track of duplicate moves to prevent redundancy
        
        for(int i = 11; i < 89; i++) {
            if(board[i] == EMPTY) {
                for(int j = 0; j < directions.length; j++) {
                    if(checkDirection(player.getNumber(), i, directions[j], false)) {
                        Move move = new Move(player.getColor(), i);
                        for(int k = 0; (k < moveList.size()) && !duplicate; k++) {
                            if(moveList.get(k).equals(move))
                                duplicate = true;
                        }
                        if(!duplicate)
                            moveList.add(move);
                    }
                }
            }
            duplicate = false;
        }
        
        return moveList;
    }
    
    /**
     * Updates the board to reflect the move of the player
     * @param currentPlayer Player that made the move
     * @param move Move being applied to the board
     */
    public void applyMove(int currentPlayer, Move move) {
        int index = move.getIndex();
        
        if(index < 0)
            return;
        
        board[index] = currentPlayer;
        for(int i = 0; i < directions.length; i ++) {
            if(checkDirection(currentPlayer, index, directions[i], false)) {
                flipPieces(currentPlayer, index, directions[i]);
            }
        }
        
    }
    
    /**
     * Evaluates the "goodness" of the board based on personal disc count, total 
     * legal moves that can be made (mobility), and number of corner pieces
     * @return Calculated value of the board
     */
    public double evaluate() {
        ArrayList<Move> myMoves = generateMoves(me);
        ArrayList<Move> oppMoves = generateMoves(opponent);
        double netMoves = myMoves.size() + oppMoves.size();
        
        double myPieces = totalPieces(ME);
        double oppPieces = totalPieces(OPPONENT);
        double netPieces = myPieces + oppPieces;
        
        if(myMoves.isEmpty() && oppMoves.isEmpty()) {
            if(myPieces > oppPieces)
                return 750;
            else
                return 0;
        }
        
        double discCount = myPieces * 1; //total personal pieces on the board; least amount of weight
        double totalLegal = myMoves.size() * 100; //total legal moves available; medium amount of weight
        double totalCorner = cornerPieces(this, me) * 1000; //total corner pieces; most amount of weight
        
        return discCount + totalLegal + totalCorner;
    }
    
    /**
     * A search algorithm that produces and evaluates a game tree using a minmax algorithm
     * @param currentBoard Current board being analyzed
     * @param ply The current "turn" or level of the game/game tree
     * @param player The player's perspective that the board is based on
     * @param alpha Value of the best possible the above player can move
     * @param beta Value of the best possible move the opponent can make
     * @param maxDepth The maximum amount of turns/plys the program is predicting ahead
     * @return Inferred best move
     */
    public Move alphaBeta(Board currentBoard, int ply, Player player, 
                          double alpha, double beta, int maxDepth) {
        if(ply >= maxDepth) {
            Move returnMove = new Move(me.getColor());
            returnMove.setValue(currentBoard.evaluate());
            return returnMove;
        }
        else {
            ArrayList<Move> moves = currentBoard.generateMoves(player) ;
            if(moves.isEmpty())
                moves.add(new Move(me.getColor()));
            Move bestMove = moves.get(0);
            for(Move move : moves) {
                Board newBoard = new Board(currentBoard, me, opponent);
                newBoard.applyMove(player.getNumber(), move);
                Move tempMove = alphaBeta(newBoard, ply+1, opponent, 
                                          -beta, -alpha, maxDepth);
                move.setValue(-tempMove.getValue());
                if(move.getValue() > alpha) {
                    bestMove = move;
                    alpha = move.getValue();
                    if(alpha > beta)
                        return bestMove;
                }
            }
            return bestMove;
        }
    }
    
    /**
     * Determines if the game is over
     * @return Playability status of the game
     */
    public boolean gameOver() {
        return (generateMoves(me).isEmpty() && generateMoves(opponent).isEmpty());
    }
    
    /**
     * Reads and returns opponent's proposed move
     * @return Opponent's move
     */
    public Move getOpponent() {
        Scanner reader = new Scanner(System.in);
        System.out.println("C Waiting for opponent's move... ");
        String oppInput = reader.nextLine();
        Move move = new Move(oppInput);
        
        if(!checkLegal(-1, move.getIndex()))
            System.out.println("C Opponent performed an illegal move!\n");
        
        return move;
    }
    
    /**
     * Checks if the passed player's proposed move is legal
     * @param currentPlayer Determines which player perspective is being taken into consideration
     * @param move Proposed move being taken into consideration
     * @return True if the proposed move is legal; false if illegal
     */
    public boolean checkLegal(int currentPlayer, int index) {
        if(index < 0)
            return true;
        
        if(board[index] != EMPTY)
            return false;
        
        for(int i = 0; i < directions.length; i ++) {
            if(checkDirection(currentPlayer, index, directions[i], false)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Recursive method that checks for a potential legal move given a location and direction
     * @param currentPlayer Determines which player perspective is being taken into consideration
     * @param space Index of the current space on the board being analyzed
     * @param direction Direction (in respect to the current space) that is being checked for opposing pieces
     * @param chain True if the recursive method is following a chain of opposing pieces
     * @return True if a move is possible in the given direction; false otherwise
     */
    public boolean checkDirection(int currentPlayer, int space, int direction, boolean chain) {
        if((board[space + direction] != BORDER) && (board[space + direction] == (currentPlayer*-1)))
            return checkDirection(currentPlayer, (space + direction), direction, true);
        else 
            return ((board[space + direction] != BORDER) && (board[space + direction] == currentPlayer) && chain);
    }
    
    /**
     * Flips all opposing pieces between the current player's pieces in the specified direction
     * @param currentPlayer Determines which player perspective is being taken into consideration
     * @param index Location of the piece being placed
     * @param direction Direction of known opposing pieces
     */
    public void flipPieces(int currentPlayer, int index, int direction) {
        index += direction;
        
        while(board[index] != currentPlayer) {
            board[index] = currentPlayer;
            index += direction;
        }
    }
    
    /**
     * Calculates total player pieces
     * @param player The player's perspective that the board is based on
     * @return Total number of above player's pieces on the board
     */
    public int totalPieces(int player) {
        int total = 0;
        
        for(int i = 11; i < board.length; i++) {
            if(board[i] == player)
                total++;
        }
        
        return total;
    }
    
    /**
     * Calculates total empty spaces that are adjacent to player pieces
     * @param player The player's perspective that the board is based on
     * @return Total number of empty spaces adjacent to above player's pieces on the board
     */
    public int totalAdjacent(int player) {
        ArrayList adjIndexes = new ArrayList();
        
        for(int i = 11; i < board.length; i++) {
            if(board[i] == player) {
                for(int j = 0; j < directions.length; j++) {
                    if(board[i+directions[j]] == EMPTY && !adjIndexes.contains(i+j))
                        adjIndexes.add(i+j);
                }
            }
        }
        
        return adjIndexes.size();
    }
    
    /**
     * Calculates total stable (uncapturable) player pieces
     * @param player The player's perspective that the board is based on
     * @return Total number of above player's stable pieces on the board
     */
    public int totalStable(int player) {
        int total = 0;
        
        for(int i = 11; i < board.length; i++) {
            if(board[i] == player && stable(player, board[i]))
                total++;
        }
        
        return total;
    }
    
    /**
     * Determines if the passed piece is stable
     * @param player The owner of the piece being analyzed
     * @param index The position of the piece being analyzed
     * @return True if the piece is stable; false otherwise
     */
    public boolean stable(int player, int index) {
        if((index == 11) || (index == 18) || (index == 81) || (index == 88))
            return true;
        
        for(int i = 11; i < directions.length; i++) {
            if(board[index+directions[i]] == EMPTY) {
                if(checkLegal(-player, board[index+directions[i]]))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Calculates total corner pieces captured 
     * @param currentBoard Current board being analyzed
     * @param player The player's perspective that the board is based on
     * @return Total number of corner pieces captured by player
     */
    public int cornerPieces(Board currentBoard, Player player) {
        int total = 0;
        
        for(int i = 0; i < currentBoard.board.length; i++) {
            if(i == 11 || i == 18 || i == 81 || i == 88)
                if(currentBoard.board[i] == player.getNumber())
                    total++;
        }
        
        return total;
    }
    
    /**
     * Prints the given player's possible legal moves
     * @param player Determines which player perspective is being taken into consideration
     */
    public void printMoves(Player player) {
        ArrayList<Move> moveList = generateMoves(player);
        
        System.out.println("C Possible Moves");
        for(int i = 0; i < moveList.size(); i++) {
            System.out.println("C " + moveList.get(i));
        }
    }

    /**
     * Makes a string representation of the board for printing
     * @return String representation of the current game board
     */
    public String toString() {
        String boardPrint = "C Current Board\nC ";
        
        for(int i = 0; i < colLabels.length; i++)
            boardPrint += colLabels[i] + " ";
        
        int rowIndex = 0;
        for(int i = 0; i < board.length; i++) {
            if(i%10 == 0) {
                boardPrint += "\n";
                boardPrint += "C " + rowLabels[rowIndex] + " ";
                rowIndex++;
            }
            
            if(board[i] == BORDER) {
                boardPrint += "X ";
            }
            else if(board[i] == ME) {
                boardPrint += Character.toString(me.getColor()) + " ";
            }
            else if(board[i] == OPPONENT) {
                boardPrint += Character.toString(opponent.getColor()) + " ";
            }
            else {
                boardPrint += "- ";
            }
        }
        
        return boardPrint;
    }
}
