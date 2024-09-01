import java.util.Random;
import java.util.ArrayList;

//This class runs the computer
public class aiGame{
    private static Integer foundCoordX = Integer.valueOf(0);
    private static Integer foundCoordY = Integer.valueOf(0);
    private static int guessX;
    private static int guessY;
    private static boolean locationFound = false;
    public static String randX = "";
    public static String randY = "";
    public static int X;
    public static int Y;
    public static String chosenShip;
    public static String chosenDirection;
    private static Integer i  = Integer.valueOf(0);
    private static Integer j  = Integer.valueOf(0);
    private static boolean rightChecked = false;
    private static boolean upChecked = false;
    private static boolean downChecked = false;
    private static boolean leftChecked = false;
    public static ArrayList<String> direction = new ArrayList<String>();
    public static boolean[][] guessBoard = new boolean[10][10];
    
    //Method to print summaries
    public static void summaries(){
        System.out.println("Here are the main attributes of the AIGame class:");
        System.out.println("chosenShip/chosenDirection: stores the ship and direction that the computer chooses");
        System.out.println("foundCoordX/Y: stores a set of coordinates if it is a hit, randX/Y: stores a random set of coordinates");
        System.out.println("Direction: stores the possible directions");
        System.out.println("Here are the main methods of this class:");
        System.out.println("compRounds: controls the single-player gameplay by receiving input from the player and calling the computer's methods.");
        System.out.println("compGenerate: allows the computer to automatically generate a board based on random x and y-coordinates, ship type, and direction");
        System.out.println("randomGenerate: selects the random input for the above method");
        System.out.println("guess: checks if the received input is a hit or not");
        System.out.println("randomCoords: same as random generate, but only creates coordinates");
    }

    //Code for a single-player game
    public static void compRounds(){
        while(funcs.p1Score<5 && funcs.p2Score<5){
            String Xcoord;
            String Ycoord;

            //Player 1 Turn
            funcs.printBoard("p1Board");
            System.out.println("\nPlayer 1's Turn");
            System.out.println("Select an attack type:");
            funcs.attack = Main.in.nextLine();
            if(funcs.attack.toLowerCase().equals("salvo")){
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                funcs.salvo("p1", Xcoord, Ycoord);
            } else if (funcs.attack.toLowerCase().equals("recon") && funcs.p1Recon < 10){
                funcs.recon("p1");    
            } else if (funcs.p1Recon == 10 && funcs.attack.toLowerCase().equals("recon")){
                System.out.println("I'm sorry Dave. I'm afraid you can't do that.");
                System.out.println("Reconnaissance has been disabled by the computer.");
            } else{
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                funcs.playerTurn("p1", Xcoord, Ycoord);
            }
            for(int g = 0; g < 4; g++){
                System.out.println("");
            }
            //Computer turn
            guess();
            System.out.println("Ships player 1 has sunk: " + funcs.p1Score + "\nShips player 2 has sunk: " + funcs.p2Score);
        }
        if(funcs.p1Score == 5){
            funcs.extraLines();
            System.out.println("Player 1 Wins!!!");
        } else {
            funcs.extraLines();
            System.out.println("The Computer Wins!!!");
        }
    }

    //Method for turns against a computer
    public static void compTurn(String turn, String x, String y){
        int xC = Integer.parseInt(x);
        int yC = Integer.parseInt(y);
        String ship = " [  ] ";
        if (xC >=10 || yC >=10){
            return;
        }

        //Check player turn
        if(turn.equals("p1")){
            //Determines if the coords are a miss or hit
            if(!Main.p2Board[yC][xC].equals(" [  ] ") && !Main.p2Board[yC][xC].equals(" [XX] ")){
                ship = Main.p2Board[yC][xC];
                Main.p2Board[yC][xC] = " [XX] ";
                System.out.println("Hit");
            } else {
                System.out.println("Miss");
            }
        } else if (!(funcs.p1Score == 5)){
            if(!Main.p1Board[yC][xC].equals(" [  ] ") && !Main.p1Board[yC][xC].equals(" [XX] ")){
                ship = Main.p1Board[yC][xC];
                Main.p1Board[yC][xC] = " [XX] ";
                System.out.println("Computer has Hit your ship.");
            } else {
                System.out.println("Computer has Missed your ships");
            }
        }
        funcs.checkShips(turn, ship);
    }

    //method for computer to generate a board
    public static void compGenerate(String pBoard, String x, String y, String shipType, String direction){
        int xC = Integer.parseInt(x);
        int yC = Integer.parseInt(y);
        String boardMarker = " ";
        int length = 0;
        
        //series of if statements to determine the ship type and assign the correct length to it
        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
            boardMarker = " [D ] ";
            length = 2;
        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
            boardMarker = " [S ] ";
            length = 3;
        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
            boardMarker = " [Cr] ";
            length = 3;
        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
            boardMarker = " [B ] ";
            length = 4;
        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
            boardMarker = " [Ca] ";
            length = 5;
        } else{
            return;
        }
        //Checks which player's board needs to be filled
        if(pBoard.equals("p1Board")){
            //Checks if the selected square is empty
            if (Main.p1Board[yC][xC].equals(" [  ] ")){
                //Checks which direction and if the ship fits
                if (direction.toLowerCase().equals("down")) {
                    if(yC+(length-1) <= 10){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC + k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("up")) {
                    if(yC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC - k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("left")) {
                    if(xC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC][xC - k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("right")) {
                    if(xC + (length-1) <= 10){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC][xC + k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else {
                }
            } else{
            }
            if (funcs.shipsPlaced == 5){
                funcs.p1BoardSet = true;
                //Resets the variables for the next player to set the board
                funcs.shipsPlaced = 0;
                funcs.destroyerPlaced = false;
                funcs.submarinePlaced = false;
                funcs.cruiserPlaced = false;
                funcs.battleshipPlaced = false;
                funcs.carrierPlaced = false;
            }
        }else {
            if (Main.p2Board[yC][xC].equals(" [  ] ")){
                //Checks which direction and if the ship fits
                if (direction.toLowerCase().equals("down")) {
                    if(yC+(length-1) < 10 ){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC + k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("up")) {
                    if(yC-(length-1) > 0){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC - k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("left")) {
                    if(xC-(length-1) > 0){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC][xC - k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else if (direction.toLowerCase().equals("right")) {
                    if(xC + (length-1) < 10){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC][xC + k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !funcs.destroyerPlaced){
                            funcs.destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !funcs.submarinePlaced){
                            funcs.submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !funcs.cruiserPlaced){
                            length = 3;
                            funcs.cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !funcs.battleshipPlaced){
                            funcs.battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !funcs.carrierPlaced){
                            funcs.carrierPlaced = true;
                        }
                        funcs.shipsPlaced++;
                    } else {
                    }
                } else {
                }
            } else{
            }
            if (funcs.shipsPlaced == 5){
                funcs.p2BoardSet = true;
            }
        }
    }

    //Method to guess coordinates
    public static void guess(){
        boolean firstTry = false;
        randomCoords();
        X = Integer.parseInt(randX);
        Y = Integer.parseInt(randY);
        guessBoard[Y][X] = true;
        firstTry = true;
        if (locationFound){
            //checks the direction to guess=
            if (!downChecked){
                Integer sum = Integer.valueOf(foundCoordY + j);
                compTurn("p2", foundCoordX.toString(), sum.toString());
                //increments the additional length as long as it fits in the array
                if(foundCoordY + (j+1) < 10){
                    j++;
                }
                //makes sure that the current guess is a hit
                if(Main.p1Board[foundCoordY + j][foundCoordX].equals(" [  ] ") || Main.p1Board[foundCoordY + j][foundCoordX].equals(" [XX] ")){
                    downChecked = true;
                    i=0;
                    j=0;
                }
            } else if (!upChecked){
                Integer sum1 = Integer.valueOf(foundCoordY - j);
                compTurn("p2", foundCoordX.toString(), sum1.toString());
                if(foundCoordY - (j+1) >= 0){
                    j++;
                }
                if(Main.p1Board[foundCoordY - j][foundCoordX].equals(" [  ] ") || Main.p1Board[foundCoordY - j][foundCoordX].equals(" [XX] ")){
                    upChecked = true;
                    i=0;
                    j=0;
                }
            } else if (!leftChecked){
                Integer sum2 = Integer.valueOf(foundCoordX - i);
                compTurn("p2", sum2.toString(), foundCoordY.toString());
                if(foundCoordX - (i+1) >= 0){
                    i++;
                }
                if(Main.p1Board[foundCoordY][foundCoordX-i].equals(" [  ] ") || Main.p1Board[foundCoordY][foundCoordX - i].equals(" [XX] ")){
                    leftChecked = true;
                    i=0;
                    j=0;
                }
            } else if (!rightChecked){
                Integer sum3 = Integer.valueOf(foundCoordX + i);
                compTurn("p2", sum3.toString(), foundCoordY.toString());
                if(foundCoordX + (i+1) < 10){
                    i++;
                }
                if(Main.p1Board[foundCoordY][foundCoordX + i].equals(" [  ] ") || Main.p1Board[foundCoordY][foundCoordX + i].equals(" [XX] ")){
                    rightChecked = true;
                    i=0;
                    j=0;
                }
            }else {
                locationFound = false;
                rightChecked = false;
                upChecked = false;
                downChecked = false;
                leftChecked = false;
            }
        } else{
            //if the coords is a hit, then it sets them to the found vars
            compTurn("p2", randX, randY);
            if(Main.p1Board[Y][X].equals(" [XX] ")){
                locationFound = true;
                foundCoordX = X;
                foundCoordY = Y;
            }
        }
    }

    //Method to randomly generate a board
    public static void randomGenerate(){
        direction.add("down");
        direction.add("up");
        direction.add("left");
        direction.add("right");
        while(!funcs.p2BoardSet){
            randomCoords();
            //Code below for the .nextInt() method from the Random class was taken from this site:
            //https://www.tutorialspoint.com/java/util/random_nextint_inc_exc.htm
            Random rand = new Random();
            int randShip = rand.nextInt(5);
            Random rand2 = new Random();
            int randDirection = rand2.nextInt(4);
            String[] ships = {"destroyer", "submarine", "cruiser", "battleship", "carrier"};
            chosenShip = ships[randShip];
            chosenDirection = direction.get(randDirection);
            compGenerate("p2Board", randX.toString(), randY.toString(), chosenShip, chosenDirection);
        }    
    }

    //Method to randomly choose x and y coords
    public static void randomCoords(){
        Random rand = new Random();
        int tempX = rand.nextInt(9);
        Random rand2 = new Random();
        int tempY = rand2.nextInt(9);
        randX = Integer.toString(tempX);
        randY = Integer.toString(tempY);
    }
}