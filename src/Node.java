import java.lang.reflect.AccessibleObject;
import java.util.*;

public class Node {

    String[][] board;

    private int g; //cost to reach this state
    private int h;//Estimate to the goal based on heuristic
    private int f;//Total cost based on sum of g and h
    private int[] mtSpace= new int[2];
    private Node parent;




    public Node( String[][] board,Node parent) {

        this.parent=parent;
        this.board = board;

        if(parent != null) {
            this.g = parent.getG() + 1;
        }
        else{


                g = 0;

        }
        mtSpace=findCharPosition(board,"X");

        this.h=calcHeuristic();
        f=g+h;


    }

    public Node (){
        g=0;
    }

    //Method to check if puzzle is solvable before solving it
    public boolean isSolvable() {

        int size = board.length;
        int inversions = calcID();//Store the inversions for the board in inversions variable

        int blancRow = size - mtSpace[0];

        //If grid width is even then checks if the blank tile row from the bottom is even and inversions are odd.
        // Also checks if the blank tile row from the bottom is odd and inversions are even. If any of these are true then it is solvable
        if (size % 2 == 0) {
            if (blancRow % 2 == 0 && inversions % 2 == 1) {
                return true;
            } else if (blancRow % 2 == 1 && inversions % 2 == 0) {
                return true;
            } else {
                return false;
            }
        } else { // For odd numbered widths it checks if the inversions are even if this is true then it is solvable
            if (inversions % 2 == 0) {
                return true;
            }
        }

        return false;


    }


    //Generates the next moves for the current node and returns as a list
    public  List<Node> generateSuc(){

        List <Node>nextNodes = new ArrayList<>();// List of states the puzzle can be after a move

        // Directions you can take from the blank tile
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Loop through each possible direction
        for (int i=0;i<directions.length;i++) {
            int newRow = mtSpace[0] + directions[i][0];
            int newCol = mtSpace[1] + directions[i][1];

            // Checks to see if the direction is admissable
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {


                Node temp = new Node(swap(newRow,newCol,mtSpace[0],mtSpace[1]),this);
                if(!(temp.equals(this.getParent()))){
                    nextNodes.add(temp);
                }


            }
        }

        return nextNodes;

    }

    //Swaps 2 tiles
    private  String[][] swap(int newRow, int newCol, int oldRow, int oldCol) {

        String temp=board[newRow][newCol];

        String[][] newBoard = new String[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = board[i].clone(); // Copies each of the rows
        }

        newBoard[newRow][newCol] = board[oldRow][oldCol];
        newBoard[oldRow][oldCol] = temp;
        return newBoard;
    }

    //Use manhatthan distance to get heuristic
    public int calcHeuristic(){

        int value=0;

        for (int i =0; i<board.length; i++){
            for (int j=0; j<board.length; j++){
                value+=calcManhatthan(i,j);


            }
        }

        //value=Math.max(value,calcID());
        value+=calcLinearConflict();

        return value;
    }

    //Calculates the manhatthan distance for a tile
    public int calcManhatthan(int i,int j){



        int[] goalLocation=findCharPosition(getGoalState(),board[i][j]);

        int value=Math.abs(i-goalLocation[0])+Math.abs(j-goalLocation[1]);

        return value;
    }

    //Calculates the inversion distance for the board
    public int calcID(){
        int inversions=0;

        StringBuilder flatBoard =new StringBuilder();

        for(int i=0;i<board.length;i++){
            for(int j=0;j<board.length;j++){

                if(!board[i][j].equals("X")) {
                    flatBoard.append(board[i][j]);
                }
            }
        }

        // Counts inversions
        for (int i = 0; i < flatBoard.length(); i++) {
            for (int j = i + 1; j < flatBoard.length(); j++) {
                if (Integer.parseInt(String.valueOf(flatBoard.charAt(i))) > Integer.parseInt(String.valueOf(flatBoard.charAt(j)))) {
                    inversions++;
                }
            }
        }

        return inversions;
    }




    //Claculates the linear conflic for the board
    public int calcLinearConflict(){

        int tLinearConflict=0; //Variable total Linear conflict cost for the board



        for(int i=0;i<board.length;i++){

            tLinearConflict+=calcRowConflicts(i);

            tLinearConflict+=calcColumnConflicts(i);
        }

        return tLinearConflict;


    }

    public int calcRowConflicts(int row){

        int linearConflict=0;

        for(int i=0;i<board.length-1;i++){

            int[] goalPosI= findCharPosition(getGoalState(),board[row][i]);

            if(board[row][i].equals("X")){
                continue;
            }
            else {


                for (int j = i+1; j < board.length - 1; j++) {

                    int[]goalPosJ= findCharPosition(getGoalState(),board[row][j]);

                    if(board[row][j].equals("X")){
                        continue;
                    }
                    else{

                        if(goalPosI[0] == row && goalPosJ[0] == row && goalPosI[1] > goalPosJ[1]){
                            linearConflict+=2;
                        }

                    }


                }

            }

        }
        return linearConflict;

    }

    public int calcColumnConflicts(int col){

        int linearConflict=0;

        for(int i=0;i<board.length-1;i++){

            int[] goalPosI= findCharPosition(getGoalState(),board[i][col]);

            if(board[i][col].equals("X")){
                continue;
            }
            else {


                for (int j = i+1; j < board.length - 1; j++) {

                    int[]goalPosJ= findCharPosition(getGoalState(),board[j][col]);

                    if(board[j][col].equals("X")){
                        continue;
                    }
                    else{

                        if(goalPosI[1] == col && goalPosJ[1] == col && goalPosI[1] > goalPosJ[1]){
                            linearConflict+=2;
                        }

                    }


                }

            }

        }
        return linearConflict;

    }


    // Checks if current board state is the goal
    public String[][] getGoalState() {
        int temp=1;
        String[][] goal= new String[board.length][board.length];
        for(int i=0;i< board.length;i++){
            for(int j=0;j< board.length;j++){

                if (i == board.length - 1 && j == board.length - 1) {
                    goal[i][j] = "X"; // Sets the last cell as the blank space (0 or X)
                } else {
                    goal[i][j] = String.valueOf(temp);
                    temp++;
                }
            }
        }

        return goal;
    }

    public boolean isGoal(){
        if(Arrays.deepEquals(board,getGoalState())){
            return true;
        }

        return false;
    }

    public  int[] findCharPosition(String[][] boardX,String target) {
        int size = boardX.length;
        int[] location = new int[2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardX[i][j].equals( target)) {
                    location[0] = i;
                    location[1] = j;
                    return location;
                }
            }
        }
        return null;
    }

    //Method to overwrite equals method because the Hashmap and List objects use it internally...this will help compare if 2 nondes are equal
    @Override
    public boolean equals(Object o) {

        //Checks to see if the objects are the same
        if(this==o) return true;

        //Checks to see if the obj is null and if its the same type as type (We cant it to compare node types)
        if(o==null||getClass()!=o.getClass()) return false;

        Node other= (Node)o;
        return Arrays.deepEquals(this.board,other.board);


    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board); // Generates hash code based on whats inside the board
    }



    public int getG() {
        return g;
    }

    public int getF() {
        return f;
    }

    public String[][] getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }
}
