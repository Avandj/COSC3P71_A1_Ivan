import java.util.*;

//Class that store node for the IDDFS to find the path to the goal of the puzzle
//Also have the method to actually find the goal inside of the class
class TreeNode {

    String[][] board;
    int[] mtSpace;
    TreeNode parent;
    int deapth;

    //Set<String> visitedBoards ;


    public TreeNode(String[][] board, TreeNode parent ) {

        this.board = board;
        this.parent = parent;
        mtSpace = findCharPosition(board, "X");

        if(parent != null) {
            this.deapth = parent.deapth + 1;
        }



//        if(parent.visitedBoards!=null) {
//            visitedBoards=parent.visitedBoards;
//        }
//        else{
//            Set<String> visitedBoards = new HashSet<>();
//        }





    }


    //Converts the string version of the board back to a board
    public String[][] stringToBoard(String boardString) {
        String[] elements = boardString.replaceAll("\\[|\\]", "").split(", ");
        int width = getBoardWidth();

        String[][] board = new String[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j <width; j++) {

                board[i][j] = elements[i*width +j];

            }
        }
        return board;
    }

    //Generates the neighbours for the curent node (available moves) and returns them as a list of type tree node
    public List<TreeNode> generateNeighbors() {


        List<TreeNode> neighbors = new ArrayList<>();

        // Directions that you can take from the blank tile
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};


        for (int i = 0; i < directions.length; i++) {
            int newRow = mtSpace[0] + directions[i][0];
            int newCol = mtSpace[1] + directions[i][1];

            // Checks to see if the direction is admissable
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {

                    neighbors.add(new TreeNode(swap(newRow, newCol, mtSpace[0], mtSpace[1]),this)); //If it is then add it to the list of available children

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

    //Find the position of a character of where it should be
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



    // Iterative Deepening Search  method
    public int IDS( int maxDepth) {

        // check if the goal exist in current depth... if not then increase the depth until Maxdepth
        for (int depth = 0; depth <= maxDepth; depth++) {
            TreeNode goalNode = DFS(this, depth);
            if (goalNode!=null) {
                printBoardRecursive(goalNode);
                return depth;  // Returns true if goal is found at current depth
            }
        }
        return -1;  // Returns false if there is no solution found within maxDepth
    }

    //Deapth first search for the current maxDepth
    TreeNode DFS(TreeNode currentNode, int maxDepth) {



        if (maxDepth == 0) {
            if (currentNode.isGoal()) {
                return currentNode;
            }
            return null;  // Return null if the target isn't found
        }

        // Recursive case: explores the children called (neighbors)
        List<TreeNode> neighbors = currentNode.generateNeighbors();
        for (TreeNode neighbor : neighbors) {
            TreeNode result = neighbor.DFS(neighbor, maxDepth-1);
            if (result != null) {
                return result;  // Returns the  node with solution if found
            }
        }

        return null;  // The solution wasnt found at this depth
    }

    //Recursively prints the state of the board at you do the moves for the solution algorithmn finds
    public void printBoardRecursive(TreeNode node){



        //Base Case
        if(node.getParent()==null){
            return;
        }

        //Recursive function
        printBoardRecursive(node.getParent());

        //Prints the items from the beggining puzzle to the end
        for(int i=0;i< node.getBoard().length;i++){

            for(int j=0;j< node.getBoard().length;j++){



                if(i==node.getBoard().length/2 && j==node.getBoard().length-1 ){
                    System.out.print(node.getBoard()[i][j]+"\t");
                }
                else {
                    System.out.print(node.getBoard()[i][j] + "\t");

                }
            }

            System.out.println();

        }
        System.out.println();
    }

    //Compare the string[][] of current node to the goal board
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

    //Overrides the default equals method to compare two TreeNode objects.
    @Override
    public boolean equals(Object o) {

        //Checks to see if the objects are the same
        if (this == o) return true;

        //Checks to see if the obj is null and if its the same type as type (We cant it to compare node types)
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode other = (TreeNode) o;
        return Arrays.deepEquals(this.board, other.board);


    }

//Override hashCode to generate a hash code based on whats inside of the board
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board); // Generates hash code based on whats inside the board
    }

    //Returns parent
    public TreeNode getParent() {
        return parent;
    }

    //returns the boards width
    public int getBoardWidth(){
        return board.length;
    }

    //returns the board
    public String[][] getBoard(){
        return board;
    }

//    public  Set<String> getVisitedBoards() {
//        return visitedBoards;
//    }
}
