/**
 * @author Kenny Tram
 * Pieces of code are modifications of Marietta Cameron's code
 */
package KKT_Othello;

public class Move {
    private String color;
    private String col;
    private String row;
    private double value;
    private String output;
    
    /**
     * Initializes a move based on the format "B c r" or "W c r"
     * @param input String of format "B c r" or "W c r"
     */
    public Move(String input) {
        if(input.length() > 2) {
            color = Character.toString(input.charAt(0));
            col = Character.toString(input.charAt(2));
            row = Character.toString(input.charAt(4));
            output = color + " " + col + " " + row;
        }
        else {
            color = Character.toString(input.charAt(0));
            col = " ";
            row = " ";
            output = color;
        }
    }
    
    /**
     * Initializes a move based on a given player color and board index
     * @param clr Color of the player making the move
     * @param index Position on the game board
     */
    public Move(char clr, int index) {
        color = Character.toString(clr);
        
        switch (index%10) {
            case 1:
                col = "a";
                break;
            case 2:
                col = "b";
                break;
            case 3:
                col = "c";
                break;
            case 4:
                col = "d";
                break;
            case 5:
                col = "e";
                break;
            case 6:
                col = "f";
                break;
            case 7:
                col = "g";
                break;
            case 8:
                col = "h";
                break;
            default:
                col = "";
                break;
        }
        
        row = Integer.toString(index / 10);
        output = color + " " + col + " " + row;
    }
    
    /**
     * Initializes a "pass" move
     * @param clr Color of the player making the pass
     */
    public Move(char clr) {
        color = Character.toString(clr);
        col = " ";
        row = " ";
        output = color;
    }
    
    /**
     * Returns the corresponding board index of the move
     * @return Board index
     */
    public int getIndex() { 
        switch(col) {
            case "a":
                return Integer.parseInt(row) * 10 + 1;
            case "b":
                return Integer.parseInt(row) * 10 + 2;
            case "c":
                return Integer.parseInt(row) * 10 + 3;
            case "d":
                return Integer.parseInt(row) * 10 + 4;
            case "e":
                return Integer.parseInt(row) * 10 + 5;
            case "f":
                return Integer.parseInt(row) * 10 + 6;
            case "g":
                return Integer.parseInt(row) * 10 + 7;
            case "h":
                return Integer.parseInt(row) * 10 + 8;
            default:
                return -1;
        }
    }
    
    /**
     * Sets/modifies the value of the move
     * @param val Given value of the move
     */
    public void setValue(double val) {
        value = val;
    }
    
    /**
     * Returns the value of the move
     * @return Value of the move
     */
    public double getValue() {
        return value;
    }
    
    /**
     * Determines if both moves are identical
     * @param move Move being compared to
     * @return True if both moves are identical; false otherwise
     */
    public boolean equals(Move move) {
        return(output.equals(move.toString()));
    }
    
    /**
     * Makes a string representation of the move for output
     * @return String representation of the move
     */
    public String toString() {
        return output;
    }
}
