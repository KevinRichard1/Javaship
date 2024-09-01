import java.util.Scanner;

//This class exists for readability and to run the game
public class Main {
    
    public static String[][] p1Board = new String[10][10];
    public static String[][] p2Board = new String[10][10];
    public static Scanner in = new Scanner (System.in);
    public static String players = "";

    public static void main(String[] args) {
        String rShip = "";
        String rXcoord = "";
        String rYcoord = "";
        String rDirection = "";
        
        funcs.freshStart();
        /*                                                _
        _____                 _   __  __                 | |
        |  __ \              | | |  \/  |                | |
        | |__) |___  __ _  __| | | \  / | ___          __| |__
        |  _  // _ \/ _` |/ _` | | |\/| |/ _ \         \     /
        | | \ \  __/ (_| | (_| | | |  | |  __/          \   /
        |_|  \_\___|\__,_|\__,_| |_|  |_|\___|           \_/
        */                                                
        //Uncomment the below lines to view summaries of class methods and attributes
        /*
        aiGame.summaries();
        funcs.extraLines();
        funcs.summaries();
        */
        
        System.out.println("       _____ _    _____   _____ __  __________ ");
        System.out.println("      / /   | |  / /   | / ___// / / /  _/ __\\");
        System.out.println(" __  / / /| | | / / /| | \\__ \\/ /_/ // // /_/ /");
        System.out.println("/ /_/ / ___ | |/ / ___ |___/ / __  // // ____/ ");
        System.out.println("\\____/_/  |_|___/_/  |_/____/_/ /_/___/_/      ");

        System.out.println("Enter the amount of players(1-2)");
        players = in.nextLine();
        System.out.println("Begin by placing your first ship.");

        //Player 1 sets up their board
        while(!funcs.p1BoardSet){
            System.out.println("Please enter the x-coordinate (0-9)");
            rXcoord = in.nextLine();
            System.out.println("Please enter the y-coordinate (0-9)");
            rYcoord = in.nextLine();
            System.out.println("Please enter the ship type. Ship types include(from smallest to largest): Destroyer, Submarine, Cruiser, Battleship, Carrier");
            rShip = in.nextLine();
            System.out.println("Directions include left, right, up, down");
            rDirection = in.nextLine();
            funcs.generateBoard("p1Board", rXcoord, rYcoord, rShip, rDirection);
            funcs.printBoard("p1Board");
        }

        if (players.equals("2")){
            //Adds extra lines in between
            funcs.extraLines();
            //Player 2 sets up their board
            System.out.println("Player 2's Turn");
            while(!funcs.p2BoardSet){
                System.out.println("Please enter the x-coordinate (0-9)");
                rXcoord = in.nextLine();
                System.out.println("Please enter the y-coordinate (0-9)");
                rYcoord = in.nextLine();
                System.out.println("Please enter the ship type. Ship types include(from smallest to largest): Destroyer, Submarine, Cruiser, Battleship, Carrier");
                rShip = in.nextLine();
                System.out.println("Directions include left, right, up, down");
                rDirection = in.nextLine();
                funcs.generateBoard("p2Board", rXcoord, rYcoord, rShip, rDirection);
                funcs.printBoard("p2Board");
            }
            funcs.extraLines();
            
            //Normal gameplay begins
            System.out.println("This verion of Battleship has two additional modes that can each be used once, salvo and reconnaissance");
            System.out.println("To choose a mode type \"salvo\" for a barrage around a point, \"recon\" to reveal random ships, or leave blank for a single attack");
            funcs.rounds();
            
        //Single-player game with AI second player
        } else {
            System.out.println("Generating Computer's Board...");
            aiGame.randomGenerate();
            funcs.extraLines();
            System.out.println("This verion of Battleship has two additional modes, salvo and reconnaissance");
            System.out.println("To choose a mode type \"base\" for a single attack, \"salvo\" for a barrage around a point, or \"recon\" to reveal random ships");
            aiGame.compRounds();
        }
    }
}