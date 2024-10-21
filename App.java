import java.util.Scanner;

public class App {

    //static String[][] goBoard= new String[9][9];

    //Black: "-*"
    //White "-o"
    static String[][] goBoard= {
                                    {null,null,"-*","-*",null,null,null,null,null},
                                    {null,"-*","-o","-o","-*",null,null,null,null},
                                    {null,"-*","-o",null,"-o","-*",null,null,null},
                                    {null,"-*","-o","-o","-o","-*",null,null,null},
                                    {null,"-*","-o",null,"-o","-*",null,null,null},
                                    {null,null,"-*","-o","-o","-*",null,null,null},
                                    {null,null,null,"-*","-*",null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null},
                                    {null,null,null,null,null,null,null,null,null}
    };

    //Each will be initalized to false
    static boolean[][] lives = new boolean[9][9];
    static boolean[][] territory = new boolean[9][9];
    static boolean[][] beenChecked = new boolean[9][9];

    static int checked(int x, int y, String currentPiece, boolean[][] beenChecked){
        if (x < 0 || x >= 9 || y < 0 || y >= 9) {
            return 0;  // Out of bounds
        }
        if (beenChecked[y][x]) {
            return 0;  // Already checked, treat as empty (or not revisited)
        }
        beenChecked[y][x] = true;  // Mark this spot as checked
        // Check if the spot is empty
        if (goBoard[y][x] == null) {
            return 0;  // Empty space
        }
        // Check if the spot is an ally or enemy
        if (goBoard[y][x].equals(currentPiece)) {
            return 2;  // Ally piece
        }
        else{
            return 1;  // Enemy piece
        }
    }

    static boolean isSurrounded(int moveX, int moveY, String piece, boolean[][] beenChecked){
        if (moveX < 0 || moveX >= 9 || moveY < 0 || moveY >= 9) {
            return true;  // Out of bounds, treat as surrounded
        }
        if (goBoard[moveY][moveX] == null){
            return false; //No adjacent peice found
        }
        if (beenChecked[moveY][moveX]){
            return true; //Already checked, initially checks next
        }
        beenChecked[moveY][moveX] = true; //checks piece for first time and marks it as checked

        // If we find an enemy piece, continue checking around it
        if (!goBoard[moveY][moveX].equals(piece)) {
            return true;  // Enemy piece is treated as a boundary
        }

        //Recursivly check surrounding pieces in all directions to see if group is surrounded
        boolean up = isSurrounded(moveX, moveY -1, piece, beenChecked);
        boolean down = isSurrounded(moveX, moveY +1, piece, beenChecked);
        boolean left = isSurrounded(moveX -1, moveY, piece, beenChecked);
        boolean right = isSurrounded(moveX +1, moveY, piece, beenChecked);

        return up && down && left && right;
    }

    static int whiteCaptured = 0;
    static int blackCaptured = 0;
    static void isAlive(int moveX, int moveY, boolean[][] beenChecked, boolean[][] lives){
        if (beenChecked[moveY][moveX]){
            return; //Already checked
        }
        String piece = goBoard[moveY][moveX];
        if (piece == null){
            return; //if spot is empty, nothing to check
        }
        boolean surrounded = isSurrounded(moveX, moveY, piece, beenChecked);

        //if surrounded, capture pieces
        if (surrounded){
            capture(moveX, moveY, piece);
        }
    }

    static void capture(int moveX, int moveY, String piece){
        if (moveX < 0 || moveX >= 9 || moveY < 0 || moveY >= 9) {
            return;  // Out of bounds, do nothing
        }
        if (goBoard[moveY][moveX] == null || !goBoard[moveY][moveX].equals(piece)) {
            return;  // Not the correct piece, do nothing
        }
    
        goBoard[moveY][moveX] = null;  // Capture the piece (set to null)
        lives[moveY][moveX] = false;  // Mark as no longer alive
    
        // Recursively capture connected pieces
        capture(moveX + 1, moveY, piece);
        capture(moveX - 1, moveY, piece);
        capture(moveX, moveY + 1, piece);
        capture(moveX, moveY - 1, piece);
    }

    static void showGoBoard(String[][]g){
        System.out.println("  1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < goBoard.length; i++){
            for (int j = 0; j < goBoard[i].length; j++){
                if(goBoard[i][j] == null){
                    if( j == 0){
                        System.out.print(i + 1 + " ");
                        
                        System.out.print("+");
                    }
                    else{
                        System.out.print("-+");
                    }
                }
                else{
                    System.out.print(goBoard[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {

        Boolean proceed = true;
        Boolean playerTurn = false; //True for white, False for black

        Scanner myObj = new Scanner(System.in);

        // Initialize boolean arrays for tracking lives and checked positions
        boolean[][] lives = new boolean[9][9];       // Track if pieces are alive
        boolean[][] beenChecked = new boolean[9][9]; // Track if spots have been checked

        while(proceed){
            String piececolor = (playerTurn) ? "-o" : "-*";

            if (piececolor == "-o"){
                System.out.println("White's turn");
            }
            else{
                System.out.println("Black's turn");
            }

            int moveX = -1, moveY = -1;

            //Only accepts valid X cordinates
            while (moveX < 1 || moveX > 9){
                System.out.println("Please insert X coordinate between 1 and 9: ");
                moveX = myObj.nextInt();
            }
        
            //Only accepts valid Y cordinates
            while (moveY < 1 || moveY > 9){
                System.out.println("Please insert Y coordinate between 1 and 9: ");
                moveY = myObj.nextInt();
            }

            //Subtract 1 so board can be 1 to 9 instead of 0 to 8
            moveX -= 1; moveY -= 1;

            if (goBoard[moveY][moveX] != null){  
                System.out.println("That spot is already taken. Please choose a different move.");
            }
            else if (moveX == 0){ //Makes sure left side of board is properly printed. moveX is zero since that is it's real index for the leftmost side
                piececolor = (playerTurn) ? moveY + 1 + " o" : moveY + 1 + " *"; //Adds the column numbers after they are replaced and properly aligns the piece with the board
                goBoard[moveY][moveX] = piececolor; // Place the piece
            }
            else{
                goBoard[moveY][moveX] = piececolor; // Place the piece
            }

            showGoBoard(goBoard);

            // Reset the beenChecked array before running the check
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    beenChecked[i][j] = false; // Reset for new turn
                }
            }

            // After the piece is placed, check if any groups of pieces need to be captured
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (goBoard[i][j] != null) {
                        isAlive(j, i, beenChecked, lives); // Check if the piece at (i, j) is alive
                    }
                }
            }

            playerTurn = !playerTurn; //Switch Turns

        }

        myObj.close();
    }
}
