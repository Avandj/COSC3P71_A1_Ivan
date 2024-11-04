import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

//Class that stores the node for the IDA* node
//Also has method to find the path to the goal
public class IDAStarNode {

    String[][] board;
    int[] mtSpace;
    IDAStarNode parent;
    int deapth;

    int g; //depth
    int h; //heuristic
    int f;

    public IDAStarNode(String[][] board, IDAStarNode parent) {

        this.board = board;
        this.parent = parent;

        if (parent != null) {
            this.deapth = parent.deapth + 1;
            this.g = parent.g + 1;
        } else {
            g = 0;
            this.deapth = 0;
        }

        mtSpace = findCharPosition(board, "X");


        this.h = calcHeuristic();
        f = g + h;

    }

    //Method for  implementing Iterative Deepening A* search to find the path to the goal node
    public int idaStar() {
        int cutoff = h;
        Stack<IDAStarNode> path = new Stack<>();
        path.push(this);

        int cheapestCost=Integer.MAX_VALUE;

        while (true) {
            cheapestCost=recurse(path,0,cutoff);
            if(cheapestCost==Integer.MAX_VALUE){
                printBoardRecursive(path.peek());
                return path.peek().deapth;
            }
            else if(cheapestCost==-1){
                return -1;
            }
            else{
                cutoff = cheapestCost;
            }



        }

    }

    //This method is the core recursive function for Iterative Deepening A* search. It explores nodes recursively, updating the path cost and checking whether the goal has been reached.
    public int recurse(Stack<IDAStarNode> path, int cutoff, int currentPathcost){

        IDAStarNode currentNode = path.peek();
        int newCost = currentPathcost+currentNode.getH();

        if(newCost>cutoff){
            return newCost;
        }

        if(currentNode.equals(getGoalState())){

            return Integer.MAX_VALUE;
        }

        int minCost = Integer.MAX_VALUE;

        List<IDAStarNode> neighbours=currentNode.generateNeighbors();


        for(IDAStarNode neighbour:neighbours){

            if(!path.contains(neighbour)){
                path.push(neighbour);
                int childPathCost = recurse(path, cutoff, currentPathcost + 1);

                if(childPathCost==Integer.MAX_VALUE){
                    return Integer.MAX_VALUE;
                }

                minCost=Math.min(childPathCost,childPathCost);
                path.pop();

            }

        }

        return minCost;

    }



    //Generates the neighbours for the current node
    public List<IDAStarNode> generateNeighbors() {


        List<IDAStarNode> neighbors = new ArrayList<>();
        // Directions you can take from the blank tile
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Loop through each possible direction
        for (int i = 0; i < directions.length; i++) {
            int newRow = mtSpace[0] + directions[i][0];
            int newCol = mtSpace[1] + directions[i][1];

            // Checks to see if the direction is admissable
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {


                neighbors.add(new IDAStarNode(swap(newRow, newCol, mtSpace[0], mtSpace[1]), this));


            }
        }

        return neighbors;

    }


    //Swaps 2 tiles on the board
    //Used for calculating the child nodes of current node
    private String[][] swap(int newRow, int newCol, int oldRow, int oldCol) {

        String temp = board[newRow][newCol];

        String[][] newBoard = new String[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = board[i].clone(); // Copies each of the rows
        }

        newBoard[newRow][newCol] = board[oldRow][oldCol];
        newBoard[oldRow][oldCol] = temp;
        return newBoard;
    }

    //Find the position of where a char should be for a given board
    public int[] findCharPosition(String[][] boardX, String target) {
        int size = boardX.length;
        int[] location = new int[2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (boardX[i][j].equals(target)) {
                    location[0] = i;
                    location[1] = j;
                    return location;
                }
            }
        }
        return null;
    }



    //Recursively prints the state of the board at you do the moves for the solution algorithmn finds
    public void printBoardRecursive(IDAStarNode node) {


        //Base Case
        if (node.getParent() == null) {
            return;
        }
        //Recursive function
        printBoardRecursive(node.getParent());

        //Prints the items from the beggining puzzle to the end
        for (int i = 0; i < node.getBoard().length; i++) {

            for (int j = 0; j < node.getBoard().length; j++) {


                if (i == node.getBoard().length / 2 && j == node.getBoard().length - 1) {
                    System.out.print(node.getBoard()[i][j] + "\t");
                } else {
                    System.out.print(node.getBoard()[i][j] + "\t");

                }
            }

            System.out.println();

        }
        System.out.println();
    }

    //Checks to see if the current board is the goal
    public boolean isGoal() {
        if (Arrays.deepEquals(board, getGoalState())) {
            return true;
        }

        return false;
    }

    // Checks if current board state is the goal
    public String[][] getGoalState() {
        int temp = 1;
        String[][] goal = new String[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

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

    public List<IDAStarNode> generateSuc() {

        List<IDAStarNode> nextNodes = new ArrayList<>();// List of states the puzzle can be after a move

        // Directions you can take from the blank tile
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Loop through each possible direction
        for (int i = 0; i < directions.length; i++) {
            int newRow = mtSpace[0] + directions[i][0];
            int newCol = mtSpace[1] + directions[i][1];

            // Checks to see if the direction is admissable
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {


                IDAStarNode temp = new IDAStarNode(swap(newRow, newCol, mtSpace[0], mtSpace[1]), this);
                if (!(temp.equals(this.getParent()))) {
                    nextNodes.add(temp);
                }


            }
        }

        return nextNodes;

    }


    //Uses manhatthan distance to get heuristic
    public int calcHeuristic() {

        int value = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                value += calcManhatthan(i, j);


            }
        }

        //value=Math.max(value,calcID());
        value += calcLinearConflict();

        return value;
    }

    //Calculates the manhatthan disctance for a tile
    public int calcManhatthan(int i, int j) {


        int[] goalLocation = findCharPosition(getGoalState(), board[i][j]);

        int value = Math.abs(i - goalLocation[0]) + Math.abs(j - goalLocation[1]);

        return value;
    }

    //Calcultes the Inversion distance for the board
    public int calcID() {
        int inversions = 0;

        StringBuilder flatBoard = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {

                if (!board[i][j].equals("X")) {
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


    //Calcs linear conflict of the board
    public int calcLinearConflict() {

        int tLinearConflict = 0; //Variable total Linear conflict cost for the board


        for (int i = 0; i < board.length; i++) {

            tLinearConflict += calcRowConflicts(i);

            tLinearConflict += calcColumnConflicts(i);
        }

        return tLinearConflict;


    }

    //Calcs the conflict for a given row
    public int calcRowConflicts(int row) {

        int linearConflict = 0;

        for (int i = 0; i < board.length - 1; i++) {

            int[] goalPosI = findCharPosition(getGoalState(), board[row][i]);

            if (board[row][i].equals("X")) {
                continue;
            } else {


                for (int j = i + 1; j < board.length - 1; j++) {

                    int[] goalPosJ = findCharPosition(getGoalState(), board[row][j]);

                    if (board[row][j].equals("X")) {
                        continue;
                    } else {

                        if (goalPosI[0] == row && goalPosJ[0] == row && goalPosI[1] > goalPosJ[1]) {
                            linearConflict += 2;
                        }

                    }


                }

            }

        }
        return linearConflict;

    }

    //calcs conflic for given col
    public int calcColumnConflicts(int col) {

        int linearConflict = 0;

        for (int i = 0; i < board.length - 1; i++) {

            int[] goalPosI = findCharPosition(getGoalState(), board[i][col]);

            if (board[i][col].equals("X")) {
                continue;
            } else {


                for (int j = i + 1; j < board.length - 1; j++) {

                    int[] goalPosJ = findCharPosition(getGoalState(), board[j][col]);

                    if (board[j][col].equals("X")) {
                        continue;
                    } else {

                        if (goalPosI[1] == col && goalPosJ[1] == col && goalPosI[1] > goalPosJ[1]) {
                            linearConflict += 2;
                        }

                    }


                }

            }

        }
        return linearConflict;

    }


    //Overrides the default equals method to compare two TreeNode objects.
    @Override
    public boolean equals(Object o) {

        //Checks to see if the objects are the same
        if (this == o) return true;

        //Checks to see if the obj is null and if its the same type as type (We cant it to compare node types)
        if (o == null || getClass() != o.getClass()) return false;

        IDAStarNode other = (IDAStarNode) o;
        return Arrays.deepEquals(this.board, other.board);


    }
    //Override hashCode to generate a hash code based on whats inside of the board
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board); // Generates hash code based on whats inside the board
    }

    public IDAStarNode getParent() {
        return parent;
    }



    public String[][] getBoard() {
        return board;
    }

    public int getH() {
        return h;
    }


}
