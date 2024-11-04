import com.sun.source.tree.Tree;

import javax.swing.plaf.basic.BasicTreeUI;
import java.io.*;
import java.time.temporal.Temporal;
import java.util.Scanner;  // Import the Scanner class

public class Main {


    public static void main(String[] args) {

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        int size=-1;
        String[][] puzzle=null;

       // String algorithmn = myObj.nextLine();
        int alg=2;
        int numSteps=0;

        try (BufferedReader br = new BufferedReader(new FileReader("src/puzzle.txt"))) {


            alg = Integer.parseInt(br.readLine());
             size = Integer.parseInt(br.readLine()); // read in the size of the array to solve
            System.out.println(size);

            if (size > 0) {
                puzzle = new String[size][size];  // Initialize the puzzle array with the correct size
            } else {
                System.out.println("Error: Invalid puzzle size.");
                return;
            }

            // reads the rest of the file to get the puzzle grid
            for(int i=0;i<size;i++){

                String line=br.readLine();

                String[] rowElements = line.trim().split("\\s+");

                for(int j=0;j<size;j++){
                        puzzle[i][j]=rowElements[j];
                        System.out.print(puzzle[i][j]+"\t");

                }

                System.out.println();

            }

            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            System.out.println("Error: The first line must be a valid number representing the puzzle size.");
            return;
        }


       Node root= new Node(puzzle,null);






        TreeNode treeRoot = new TreeNode(puzzle,null);
        IDAStarNode idaStarRoot= new IDAStarNode(puzzle,null);


        if(!root.isSolvable()){


            AStarSearch Asearch= new AStarSearch();


            System.out.println("Error: Puzzle not solvable.");
        }else{
            switch (alg){
                case 1:
                     numSteps=treeRoot.IDS(31);
                case 2:
                    AStarSearch Asearch= new AStarSearch();
                     numSteps=Asearch.search(root);
                case 3:
                     numSteps=idaStarRoot.idaStar();



            }



            System.out.println("Puzzle Solved with "+numSteps+" steps.");
        }





    }







}