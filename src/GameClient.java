
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GameClient {
    public static final String SERVER = "localhost";
    public Scanner in;
    public PrintWriter out;
    public Socket socket;
    public static String name;
    public static int round = 1;


    public GameClient(String playerName) {
        try {
            socket = new Socket(SERVER, GameServer.PORT);
            InputStream instream = socket.getInputStream();
            OutputStream outstream = socket.getOutputStream();
            in = new Scanner(instream);
            // set auto-flush to true
            out = new PrintWriter(outstream, true);
            sendCommand(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        StringBuffer result = new StringBuffer();
        out.println(command);
        while (true) {
            String line = in.nextLine().trim();
            if (line.isEmpty())
                break;
            result.append(line);
        }
        return result.toString();
    }

    public static void gameUpdate(GameClient cl)
    {
        //returns base game data such as stock prices and ownership of cards & money amount players.

        System.out.println("(" + name +")"+" Distribution of shares are as follows: ");
        String[] playerShares = cl.sendCommand("GETSHARES").trim().split("/");
        ArrayList<String[]> playerData = new ArrayList<>();
        for (String player: playerShares)
        {
            playerData.add(player.split(","));
        }

        for (String[] data: playerData)
        {
            if (data[0].toLowerCase().trim().equals(name.toLowerCase().trim()))
            {
                //if this data is the user on this client, format it for readability
                System.out.println("** " + data[0] + " currently holds the following shares:") ;
                System.out.printf("** %-9s %-9s %-9s %-9s %-9s %n", "Apple", "BP", "Cisco", "Dell", "Ericsson");
                System.out.printf("** %-9s %-9s %-9s %-9s %-9s %n", data[1], data[2], data[3], data[4], data[5]);
                System.out.println("** " +data[0] + " has £" + data[6]);
                for (int i = 0; i < 46; i++){System.out.print("-");}
                System.out.println("");
            }
            else
            {
                //otherwise display it normally
                System.out.println(data[0] + " currently holds the following shares:") ;
                System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", "Apple", "BP", "Cisco", "Dell", "Ericsson");
                System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", data[1], data[2], data[3], data[4], data[5]);
                System.out.println(data[0] + " has £" + data[6]);
                for (int i = 0; i < 46; i++){System.out.print("-");}
                System.out.println("");
            }

        }

        System.out.println("Current stock prices are as follows:");
        System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", "Apple", "BP", "Cisco", "Dell", "Ericsson");
        String prices[] = cl.sendCommand("GETPRICES").trim().split(",");
        System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", prices[0], prices[1], prices[2], prices[3], prices[4]);

    }

    public static void Trade(GameClient cl, Scanner s) {
        //method to deal with trading
        System.out.printf("%-9s - %d %n", "Apple", 0);
        System.out.printf("%-9s - %d %n", "BP", 1);
        System.out.printf("%-9s - %d %n", "Cisco", 2);
        System.out.printf("%-9s - %d %n", "Dell", 3);
        System.out.printf("%-9s - %d %n", "Ericsson", 4);

        boolean trading = true;
        int trades = 0;
        while(trading)
        {
            if (trades >= 2){trading = false;
                System.out.println("Stock exchange is now closed.");
                break;}
            System.out.println("(" + name + ")" + "Please type either buy or sell, followed by a comma and then the corresponding stock ID listed above.");
            System.out.println("Enter q to skip trading.");
            String val = "";
            try
            {
                String in = s.nextLine();
                if (in.equals("q")){trading = false; break;}
                String[] input = in.trim().split(",");


                System.out.println("How many would you like to buy/sell?");
                val = s.nextLine();
                if (val.equals("q")){trading= false; break;}
                String x = "TRADE" + " " + input[0].toLowerCase().charAt(0) + " " + input[1] + " " + val;
                if(cl.sendCommand(x).equals("s"))
                {
                    System.out.println("Transaction Successful");
                    trades ++;
                }
                else
                {
                    System.out.println("Transaction failed. Do you have enough money or shares to complete that transaction?");
                }
            }catch(Exception e)
            {
                System.out.println("That didn't work. Check your input syntax and try again.");
            }
        }

    }

    public static void collectVotes(GameClient cl, Scanner s)
    {
        //method to deal with user voting
        String[] cardValues = cl.sendCommand("GETCARDS").trim().split(",");
        System.out.println("Current stock price modifier cards: ");
        System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", "Apple (0)", "BP (1)", "Cisco (2)", "Dell (3)", "Ericsson (4)");
        System.out.printf("%-9s %-9s %-9s %-9s %-9s %n", cardValues[0], cardValues[1], cardValues[2], cardValues[3], cardValues[4]);

        boolean voting = true;
        int votes = 0;
        while(voting)
        {
            if (votes >= 2){voting = false;
                System.out.println("Thank you for your votes.");
                break;}
            System.out.println("(" + name + ")" + "Please enter the Stock identifier number (0-4), followed by a comma and either 0 for no or 1 for yes.");
            System.out.println("Enter q to skip voting.");
            try
            {
                String in = s.nextLine();
                if (in.equals("q"))
                {
                    voting = false;
                    cl.sendCommand("VOTE 0 SKIP");
                    break;
                }
                String[] input = in.trim().split(",");
                String x = "VOTE" + " " + input[0] + " " + input[1];
                String f= cl.sendCommand(x);
                System.out.println("You need to be looking for this ============= : " + f);
                if(f.equals("s"))
                {
                    System.out.println("Vote Successful");
                    votes ++;
                }
                else
                {
                    System.out.println("Vote failed. You can't vote for the same card twice.");
                }
            }catch(Exception e)
            {
                System.out.println("That didn't work. Check your input syntax and try again.");
            }
        }

    }

    public static void declareWinner(Scanner s, GameClient cl)
    {
        //method to calculate winner
        String[] playerData = cl.sendCommand("GETWINNER").trim().split("/");

        ArrayList<String[]> data = new ArrayList<String[]>();
        for (String player: playerData)
        {
            data.add(player.split(","));
        }

        String winner = data.get(0)[0];
        int max = Integer.parseInt(data.get(0)[1]);

        for (String[] player: data)
        {
            System.out.printf("%-9s has £%4s. %n", player[0], player[1]);
            if (Integer.parseInt(player[1]) > max)
            {
                winner = player[0];
                max = Integer.parseInt(player[1]);
            }
        }
        System.out.printf("The winner is: %s, with a total earning of %d. %n", winner, max);
    }


    public static void main(String[] args) throws IOException {

        System.out.println("Hi, before we connect, what would you like to be called?");
        Scanner s = new Scanner(System.in);
        name = s.nextLine();
        GameClient cl = new GameClient(name);
        System.out.println("You are connected as: " + name);

        for (int x = 0; x < 5; x++)
        {
            System.out.printf("This is round %d of 5. %n", (x+1));


            //show gameupdate.
            gameUpdate(cl);

            //trading opportunity.
            System.out.println("(Optional) Would you like to buy or sell any of your shares? Y/N");
            //show cash
            if (s.nextLine().toUpperCase().equals("Y"))
            { Trade(cl,s );}

            collectVotes(cl,s);
            System.out.println("Please wait patiently for other players to finish their turns.");

        }
        declareWinner(s, cl);
        System.out.println("Game Over.");
    }
}