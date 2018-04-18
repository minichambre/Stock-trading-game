
import java.util.ArrayList;

public class Game {

	private int roundCount = 0;
	private ArrayList<Player> players = new ArrayList<Player>();
	public Integer ApplePrice,BpPrice,CiscoPrice,DellPrice,EricssonPrice;
	public Deck Apple, BP, Cisco, Dell, Ericsson;
	public int[] votes = new int[5];
	public int threads = 0;
	
	// create a random game 
	public Game() {
		//set all stocks to £100 price
		ApplePrice=BpPrice=CiscoPrice=DellPrice=EricssonPrice = 100;

		//shuffle 5 decks of influence cards
		Apple = new Deck();
		BP = new Deck();
		Cisco = new Deck();
		Dell = new Deck();
		Ericsson = new Deck();
		initialiseVoting();


	}

	public synchronized void informDone()
	{
		//ensures votes are only executed once all players have voted.
		threads++;
		if (threads == 2)
		{
			executeVotes();
			threads = 0;
		}
	}

	public void initialiseVoting()
	{
		//set all votes to 0
		for (int x =0 ; x< votes.length; x++)
		{
			votes[x] = 0;
		}
	}

	// create a game with specific initial decks and share holdings
	// used for unit testing 
	public Game(Deck[] decks, int[][] shares) {
	}


	public void changeStock(String s, int val)
	{
		//method to assist in changing price of stock
		switch (s.toUpperCase()) {
			case "A" :
				ApplePrice += val;
				break;
			case "B" :
				BpPrice += val;
				break;
			case "C" :
				CiscoPrice += val;
				break;
			case "D" :
				DellPrice += val;
				break;
			case "E" :
				EricssonPrice += val;
				break;
	}
	}

	public String getCash()
	{
		//return all players cash
		String s = "";
		for (Player p : players)
		{
			s += String.valueOf(p.getCash()) + ",";
		}
		return s;
	}

	public String getShares()
	{
		//return all player shares
		String s = "";
		for (Player p : players)
		{
			s +=  p.name + "," + p.shares.get("Apple").toString() + "," + p.shares.get("BP").toString()+ "," + p.shares.get("Cisco").toString() + ","+ p.shares.get("Dell").toString() + "," + p.shares.get("Ericsson") +"," + p.getCash() + "/";
		}
		return s;
	}

	public int[] getPrices() {
		//get prices of current shares
		return new int[] {ApplePrice, BpPrice, CiscoPrice, DellPrice, EricssonPrice};
	}

	public Card[] getCards()
	{
		//get next card off the top of each of the 5 decks
		return new Card[] {Apple.getTop(), BP.getTop(), Cisco.getTop(), Dell.getTop(), Ericsson.getTop()};
	}

	public synchronized void executeVotes()
	{
		//voting algorithm
		for (int x = 0; x < votes.length; x++)
		{
			if (votes[x] > 0)
			{
				//execute votes[x] effect
				Card[] cards = getCards();
				int val = cards[x].effect;
				switch (x)
				{
					case 0:
						changeStock("A", val);
						Apple.removeTop();
						break;
					case 1:
						changeStock("B", val);
						BP.removeTop();
						break;
					case 2:
						changeStock("C", val);
						Cisco.removeTop();
						break;
					case 3:
						changeStock("D", val);
						Dell.removeTop();
						break;
					case 4:
						changeStock("E", val);
						Ericsson.removeTop();
						break;
				}

			}
			else if (votes[x]  < 0)
			{
				//discard votes[x]
				switch (x)
				{
					case 0:
						Apple.removeTop();
						break;
					case 1:
						BP.removeTop();
						break;
					case 2:
						Cisco.removeTop();
						break;
					case 3:
						Dell.removeTop();
						break;
					case 4:
						Ericsson.removeTop();
						break;
				}
			}
		}

		initialiseVoting();
		for (Player p : players)
		{
			p.votes = 0;
			p.lastVote = -1;
		}
	}

	public synchronized void setStatus(String playerName)
	{
		//called after a round ends, and resets a players votes so they can vote twice more.
		for (Player p: players)
		{
			if (p.name.equals(playerName))
			{
				p.votes = 0;

			}
		}
	}
	public String buy(String player, String s, int amount) {
		Player p = new Player("");
		for (Player x : players
				) {
			if (x.name.equals(player)) {
				p = x;
			}
		}
		int cost = 0;
		switch (s) {
			case "0":
				cost = ApplePrice;
				s = "Apple";
				break;
			case "1":
				cost = BpPrice;
				s= "BP";
				break;
			case "2":
				cost = CiscoPrice;
				s = "Cisco";
				break;
			case "3":
				cost = DellPrice;
				s = "Dell";
				break;
			case "4":
				cost = EricssonPrice;
				s = "Ericsson";
				break;
		}
		if (p.getCash() >= (cost * amount)+(amount * 3)) {
			//allow purchase
			p.changeCash('-', (cost * amount)+ (amount * 3));
			p.modifyShares(s,amount,'+');
			return "s";

		} else {//no purchase}
			return "You do not have enough money to do that.";
		}
	}

	public String sell(String player, String s, int amount) {
		//sell stock
		Player p = new Player("");
		for (Player x : players
				) {
			if (x.name.equals(player)) {
				p = x;
				break;
			}
		}
		int cost = 0;
		switch (s) {
			case "0":
				cost = ApplePrice;
				s= "Apple";
				break;
			case "1":
				cost = BpPrice;
				s= "BP";
				break;
			case "2":
				cost = CiscoPrice;
				s= "Cisco";
				break;
			case "3":
				cost = DellPrice;
				s= "Dell";
				break;
			case "4":
				cost = EricssonPrice;
				s= "Ericsson";
				break;
		}

		if (p.shares.get(s) >= amount)
		{
			//Player has shares to sell
			p.modifyShares(s,amount,'-');
			p.changeCash('+', (amount * cost));
			return "s";
		}
		else
		{
			//Player doesn't own enough shares of this type
			return "You don't own enough " + s + " shares to do this.";
		}
	}


	public void vote(String playerName)
	{
		//method for when a player skips voting
		for (Player p: players) {
			if (p.name.equals(playerName))
			{
				p.votes = 2;
			}
		}
	}

	public synchronized String calculateWinner()
	{
		//returns all players and their earnings summed up.
		String sums = "";

		for (Player p :players)
		{
			int sum = p.getCash();
			sum += (p.shares.get("Apple") * ApplePrice);
			sum += (p.shares.get("BP") * BpPrice);
			sum += (p.shares.get("Cisco") * CiscoPrice);
			sum += (p.shares.get("Dell") * DellPrice);
			sum += (p.shares.get("Ericsson") * EricssonPrice);

			sums += p.name + "," + String.valueOf(sum) + "/";

		}
		return sums;
	}


	public synchronized String vote(String playerName, int s, boolean yes) {
		//votes for a card
		Player player = new Player("");
		for (Player p : players) {
			if (p.name.equals(playerName))
			{
				player = p;
				break;
			}
		}

		//Check user hasn't voted more than twice, and isn't voting
		//for the same stock twice in one round.
		if (player.votes < 2 && player.lastVote != s )
		{
			//they may vote
			player.lastVote = s;
			if (yes)
			{
				votes[s]++;
			}
			else
			{
				votes[s]--;
			}
			player.votes++;
			return "s";

		}
		else
		{
			System.out.println(player.votes);
			return "f";
		}
	}
	//checks and returns if a player has completed the voting phase.
	public boolean getPlayerDoneVoting(String playerName)
	{
		Player player = new Player("");
		for (Player p: players)
		{
			if (p.name.equals(playerName))
			{
				player = p;
				break;
			}
		}
		if (player.votes == 2)
		{
			return true;
		}
		return false;
	}

	//add a player to the list of players.
	public void addPlayer(Player player)
	{
		players.add(player);
	}

	//remove a player from the game if they disconnect.
	public void removePlayer(Player player)
	{
		for (Player p: players)
		{
			if (p.name.equals(player.name))
			{
				players.remove(p);
			}
		}
	}
}
