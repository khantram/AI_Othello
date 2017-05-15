/**
 * @author Kenny Tram
 * Pieces of code are modifications of Marietta Cameron's code
 */
package KKT_Othello;

public class Player {
    private char color;
    private int number;
    
    /**
     * Initializes a player
     * @param clr Color of the player ('B' or "W')
     * @param num currentPlayer constant (1 or -1)
     */
    public Player(char clr, int num) {
        color = clr;
        number = num;
    }
    
    /**
     * Modifies the player's color
     * @param clr Color of the player ('B' or "W') 
     */
    public void setColor(char clr) {
        color = clr;
    }
    
    /**
     * Modifies the player's number
     * @param clr currentPlayer constant (1 or -1)
     */
    public void setNumber(int num) {
        number = num;
    }
    
    /**
     * Returns the player's color
     * @return Color of the player ('B' or "W') 
     */
    public char getColor() {
        return color;
    }
    
    /**
     * Returns the player's number/constant
     * @return currentPlayer constant (1 or -1)
     */
    public int getNumber() {
        return number;
    }
}
