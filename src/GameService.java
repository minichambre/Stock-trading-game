// Socket-based bank service
// Reads and writes ONE line at a time
// Print writer output is set to auto-flush

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;


public class GameService implements Runnable {
	public static CyclicBarrier cb = new CyclicBarrier(4,() -> System.out.println("Round done."));

	private Scanner in;
	private PrintWriter out;
	private Player player;
	private boolean login;
	private Game game;

	public GameService(Game game, Socket socket) {
		this.game = game;
		player = null;
		login = false;
		try {
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override 
	public void run() {
		login();
		while (login) {
			try {
				Request request = Request.parse(in.nextLine());
				String response = execute(game, request);
				// note use of \r\n for CRLF 
				out.println(response + "\r\n");
				if (game.getPlayerDoneVoting(player.name) == true)
				{
					try
					{
						game.setStatus(player.name);
						System.out.println("Thread for: " + player.name + " is now waiting.");
						cb.await();
						game.informDone();

					} catch(Exception e){}

				}
			} catch (NoSuchElementException e) {
				login = false;
			}
		}
		logout();
	}
	public void getVoteCount()
	{

	}

	public void login() {
		out.println("Please enter your desired username");
		try {
			String input = in.nextLine().trim();
			login = true;
			out.println("Welcome " + input);
			System.out.println("Connected: " + input);

			player = new Player(input);


			game.addPlayer(player);
			out.println();
		} catch (NoSuchElementException e) {
		}
	}

	public void logout() {
		if (player != null) {
			System.out.println("Logout: " + player.name);
		}
		try {
			Thread.sleep(2000);
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String execute(Game game, Request request) {
		String s = "";
		try {
			switch (request.type) {
				case GETMONEY:
					return game.getCash();
				case TRADE:
					s="";
					if (request.params[0].toLowerCase().equals("b"))
					{
						//buy request
						s = game.buy(player.name, request.params[1], Integer.parseInt(request.params[2]));
					}
					if (request.params[0].toLowerCase().equals("s"))
					{
						//sell request
						s = game.sell(player.name, request.params[1], Integer.parseInt(request.params[2]));

					}
					return s;
				case VOTE:
					s = "";
					boolean vote;
					if (request.params[1].equals("0"))
					{
						vote = false;
						return game.vote(player.name, Integer.parseInt(request.params[0]), vote);
					}
					else if (request.params[1].equals("1"))
					{
						vote = true;
						return game.vote(player.name, Integer.parseInt(request.params[0]), vote);
					}
					else if (request.params[1].equals("SKIP"))
					{
						game.vote(player.name);
					}
				case GETCARDS:
					s = "";
					for (int x = 0; x < game.getCards().length; x++)
					{
						s += game.getCards()[x].toString() + ",";
					}
					return s;
				case GETPRICES:
					s = "";
					for (int price: game.getPrices()
						 ) {s += Integer.toString(price) + ",";}
					return s;
				case GETSHARES:
					return game.getShares();
				case GETWINNER:
					return game.calculateWinner();
				case INVALID:
					return "Command invalid or failed!";
				case LOGOUT:
					login = false;
					game.removePlayer(player);
					return "Goodbye!";
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
