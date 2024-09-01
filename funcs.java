import java.util.Random;

public class funcs{
    //bool to check if the board has been set
    public static boolean p1BoardSet = false;
    public static boolean p2BoardSet = false;
    public static boolean destroyerPlaced = false;
    public static boolean submarinePlaced = false;
    public static boolean cruiserPlaced = false;
    public static boolean battleshipPlaced = false;
    public static boolean carrierPlaced = false;

    //Other game variables
    public static int shipsPlaced = 0;
    public static int p1Score = 0;
    public static int p2Score = 0;
    public static String attack = "";
    public static String[][] tempBoard = new String[10][10];
    public static int p1Salvo = 0;
    public static int p2Salvo = 0;
    public static int p1Recon = 0;

    //Method to print summaries
    //Uncomment below and in Main class to see the code
    public static void summaries(){
        System.out.println("Here are the main attributes of the funcs(player) class:");
        System.out.println("p1/p2BoardSet and x-shipPlaced: checks what ships have been placed and if the player's board has been set");
        System.out.println("score: stores the players' scores");
        System.out.println("attack: stores the attack type");
        System.out.println("tempBoard: stores a temporary board for recon");
        System.out.println("salvo: stores the amount of salvo used by either player");
        System.out.println("Here are the main methods of this class:");
        System.out.println("rounds: controls the two-player gameplay by receiving input from the players.");
        System.out.println("generateBoard: allows the players to generate a board based on selected x and y-coordinates, ship type, and direction");
        System.out.println("checkShips: checks how many ships have been sunk.");
        System.out.println("printBoard: prints out the board based on the turn");
        System.out.println("recon: randomly selects 5 coordinates and displays it on the temp board");
        System.out.println("salvo: attacks a 3x3 grid around the selected coordinates");
    }

    //Master game method, switches between player boards until the game is won
    public static void rounds(){
        while(p1Score<5 && p2Score<5){
            String Xcoord;
            String Ycoord;

            //Player 1 Turn
            printBoard("p1Board");
            System.out.println("\nPlayer 1's Turn");
            System.out.println("Select an attack type:");
            attack = Main.in.nextLine();
            //Determines the attack type
            if(attack.toLowerCase().equals("salvo")){
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                salvo("p1", Xcoord, Ycoord);
            } else if (attack.toLowerCase().equals("recon")){
                recon("p1");    
            } else{
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                playerTurn("p1", Xcoord, Ycoord);
            }
            extraLines();
            //Player 2 Turn
            printBoard("p2Board");
            System.out.println("Player 2's Turn");
            System.out.println("Select an attack type:");
            attack = Main.in.nextLine();
            if(attack.toLowerCase().equals("salvo")){    
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                salvo("p2", Xcoord, Ycoord);
            } else if (attack.toLowerCase().equals("recon")){
                recon("p2");    
            } else{
                System.out.println("Please enter the x-coordinate (0-9)");
                Xcoord = Main.in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                Ycoord = Main.in.nextLine();
                playerTurn("p2", Xcoord, Ycoord);
            }
            extraLines();
            System.out.println("Ships player 1 has sunk: " + p1Score + "\nShips player 2 has sunk: " + p2Score);
        }
        if(p1Score == 5){
            extraLines();
            System.out.println("Player 1 Wins!!!");
        } else {
            extraLines();
            System.out.println("Player 2 Wins!!!");
        }
    }

    //Method to receive player 'attack' coordinates and update board correspondingly
    public static void playerTurn(String turn, String x, String y){
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
        } else if (!(p1Score == 5)){
            if(!Main.p1Board[yC][xC].equals(" [  ] ") && !Main.p1Board[yC][xC].equals(" [XX] ")){
                ship = Main.p1Board[yC][xC];
                Main.p1Board[yC][xC] = " [XX] ";
                System.out.println("Hit");
            } else {
                System.out.println("Miss");
            }
        }
        checkShips(turn, ship);
    }   

    //Method to fill in the board with the requested ships
    public static void generateBoard(String pBoard, String x, String y, String shipType, String direction){
        int xC = Integer.parseInt(x);
        int yC = Integer.parseInt(y);
        String boardMarker = " ";
        int length = 0;
        
        //series of if statements to determine the ship type and assign the correct length to it
        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
            boardMarker = " [D ] ";
            length = 2;
        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
            boardMarker = " [S ] ";
            length = 3;
        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
            boardMarker = " [Cr] ";
            length = 3;
        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
            boardMarker = " [B ] ";
            length = 4;
        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
            boardMarker = " [Ca] ";
            length = 5;
        } else{
            System.out.println("Invalid ship type or this ship has already been placed. Please try again.");
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
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("up")) {
                    if(yC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC - k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("left")) {
                    if(xC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC][xC - k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("right")) {
                    if(xC + (length-1) <= 10){
                        for (int k = 0; k<length; k++){
                            Main.p1Board[yC][xC + k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else {
                    System.out.println("Invalid direction. Please try again.");
                }
            } else{
                System.out.println("This coordinate is already filled");
            }
            if (shipsPlaced == 5){
                p1BoardSet = true;
                //Resets the variables for the next player to set the board
                shipsPlaced = 0;
                destroyerPlaced = false;
                submarinePlaced = false;
                cruiserPlaced = false;
                battleshipPlaced = false;
                carrierPlaced = false;
            }
        }else {
            if (Main.p2Board[yC][xC].equals(" [  ] ")){
                //Checks which direction and if the ship fits
                if (direction.toLowerCase().equals("down")) {
                    if(yC+(length-1) <= 10){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC + k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("up")) {
                    if(yC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC - k][xC] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("left")) {
                    if(xC-(length-1) >= 0){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC][xC - k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else if (direction.toLowerCase().equals("right")) {
                    if(xC + (length-1) <= 10){
                        for (int k = 0; k<length; k++){
                            Main.p2Board[yC][xC + k] = boardMarker;
                        }
                        if (shipType.toLowerCase().equals("destroyer") && !destroyerPlaced){
                            destroyerPlaced = true;
                        }else if (shipType.toLowerCase().equals("submarine") && !submarinePlaced){
                            submarinePlaced =true;
                        } else if (shipType.toLowerCase().equals("cruiser") && !cruiserPlaced){
                            length = 3;
                            cruiserPlaced = true;
                        } else if (shipType.toLowerCase().equals("battleship") && !battleshipPlaced){
                            battleshipPlaced = true;
                        } else if (shipType.toLowerCase().equals("carrier") && !carrierPlaced){
                            carrierPlaced = true;
                        }
                        shipsPlaced++;
                    } else {
                        System.out.println("The ship does not fit. Please try again.");
                    }
                } else {
                    System.out.println("Invalid direction. Please try again.");
                }
            } else{
                System.out.println("This coordinate is already filled");
            }
            if (shipsPlaced == 5){
                p2BoardSet = true;
            }
        }
    }

    //Checks if either player's ships have been sunk
    public static void checkShips(String turn, String ship){
        int squaresFound = 0;
        for (int i = 0; i<10; i++){
            for (int j = 0; j<10; j++){
                if (turn.equals("p1")){
                    if(Main.p2Board[i][j].equals(ship)){
                        
                        squaresFound++;
                    }
                } else{
                    if(Main.p1Board[i][j].equals(ship)){
                        squaresFound++;
                    }
                }
            }
        }
        if(squaresFound == 0 && turn.equals("p1")){
            p1Score++;
            System.out.println("Player 1 has sunk a ship.");
        } else if(squaresFound == 0 && turn.equals("p2")){
            p2Score++;
            System.out.println("Player 2 has sunk a ship.");
        }
    }

    //Method to print the board
    public static void printBoard(String pBoard){
        if (pBoard.equals("p1Board")){
            for (int i=0; i<10; i++){
                for (int k=0; k<10; k++){
                    System.out.print(Main.p1Board[i][k] + " ");
                }
                System.out.println("");
            }
        } else if(pBoard.equals("tempBoard")){
            for (int i=0; i<10; i++){
                for (int k=0; k<10; k++){
                    System.out.print(tempBoard[i][k] + " ");
                }
                System.out.println("");
            }
        } else{
            for (int i=0; i<10; i++){
                for (int k=0; k<10; k++){
                    System.out.print(Main.p2Board[i][k] + " ");
                }
                System.out.println(" ");
            }
        }
    }

    //Method to add extra lines between player turns
    public static void extraLines(){
        for (int i = 0; i < 10; i++){
            System.out.println("");
        }
    }

    //Method to create a blank board for both players
    public static void freshStart(){
        for (int i = 0; i<10; i++){
            for (int j = 0; j<10; j++){
                Main.p1Board[i][j] = " [  ] ";
                Main.p2Board[i][j] = " [  ] ";
                tempBoard[i][j] = " [  ] ";
            }
        }
    }    

    public static void recon(String turn){
        int x;
        int y;
        for(int i = 0; i < 5; i++){
            Random rand = new Random();
            x = rand.nextInt(9);
            Random rand2 = new Random();
            y = rand2.nextInt(9);
            System.out.println("Coordinates checked: " + x + ", " + y);
              if (turn.equals("p1")){
                tempBoard[y][x] = Main.p2Board[y][x];
              } else {
                tempBoard[y][x] = Main.p1Board[y][x];
              }
        }
        printBoard("tempBoard");
        p1Recon++;
    }

    public static void salvo(String turn, String x, String y){
        int numX = Integer.parseInt(x);
        int numY = Integer.parseInt(y);
        Integer i = Integer.valueOf(numY-1);
        Integer j = Integer.valueOf(numX-1);
        if(Integer.parseInt(Main.players) == 2){
            if (turn.equals("p1") && p1Salvo < 3){
                while(i < numY+2){
                    while(j < numX+2){
                        if(i >= 0 && i < 10 && j >= 0 && j < 10){
                            System.out.println("Torpedo successfully fired to (" + j + ", " + i + ")");
                            playerTurn("p1", i.toString(), j.toString());   
                        } else {
                            System.out.println("Computing Error. Obstacle detected. Missile not Fired.");
                        }
                        j++;
                    }
                    j=Integer.valueOf(numX-1);
                    i++;
                }
            } else if (turn.equals("p2") && p2Salvo < 3){
                while(i < numY+2 ){
                    while(j < numX+2){
                        if(i >= 0 && i < 10 && j >= 0 && j < 10){
                            System.out.println("Torpedo successfully fired to (" + j + ", " + i + ")");
                            playerTurn("p2", i.toString(), j.toString());
                        } else {
                            System.out.println("Computing Error. Obstacle detected. Missile not Fired.");
                        }
                        j++;
                    }
                    j=Integer.valueOf(numX-1);
                    i++;
                }
            } else {
                System.out.println("You have used all your salvo attacks.");
                }
        } else {
            while(i < numY+2){
                while(j < numX+2){
                    if(i >= 0 && i < 10 && j >= 0 && j < 10){
                        System.out.println("Torpedo successfully fired to (" + j + ", " + i + ")");
                        aiGame.compTurn("p1", j.toString(), i.toString());   
                    } else {
                        System.out.println("Computing Error. Obstacle detected. Missile not Fired.");
                    }
                    j++;
                }
                j=Integer.valueOf(numX-1);
                i++;
            }
        }
    }
}