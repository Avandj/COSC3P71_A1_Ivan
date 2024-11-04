import java.util.*;

public class AStarSearch {

    private PriorityQueue<Node> openList;
    private HashSet<Node> closedList;
    private int statesChecked;

    public AStarSearch() {
        openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        closedList = new HashSet<>();
        statesChecked=0;
    }

    public int search(Node startingNode) {
        openList.add(startingNode);
        Node currentNode=new Node();


        while (!openList.isEmpty()) {
             currentNode = openList.poll();

             statesChecked++;

            if(currentNode.isGoal()){


                printBoardRecursive(currentNode);

                return currentNode.getG();
            }
            else{
                closedList.add(currentNode);
            }
            List<Node> nextNodes=currentNode.generateSuc();





            for (int i=0; i<nextNodes.size(); i++){



                if(closedList.contains(nextNodes.get(i))){

                    //If the node is already on the list of checked nodes then we dont need to check it again
                    continue;

                }

                if (!(openList.contains(nextNodes.get(i)))) {
                    openList.add(nextNodes.get(i));
                }
                else{

                    //If board is the same as one in the open list it checks to see if the f value is lower if it is then it take the higher one out and puts the new one in
                    for(Node openNode:openList){
                        if(openNode.equals(nextNodes.get(i))&& nextNodes.get(i).getG()<openNode.getG()){
                            openList.remove(openNode);
                            openList.add(nextNodes.get(i));
                            break;
                        }
                    }
                }


            }



        }


        int notSolvable=-1;

        return notSolvable;
    }

    //Recursively prints the state of the board at you do the moves for the solution algorithmn finds
    public void printBoardRecursive(Node node){


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
                    System.out.print(node.getBoard()[i][j]+"\t"+node.getG());
                }
                else {
                    System.out.print(node.getBoard()[i][j] + "\t");

                }
            }

            System.out.println();

        }
        System.out.println();
    }

    public int getStatesChecked() {
        return statesChecked;
    }
}
