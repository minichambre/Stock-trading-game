import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Tests {

	// Sample round, see FAQ at https://orb.essex.ac.uk/ce/ce303/restricted/assign/ce303assignFaq2017.html 

	public Game game;

	// sample data
	/*
	public static Deck[] sampleDecks() {
		return new Deck[] { 
				new Deck(Stock.Apple, -20, 5, 10, -5, -10, 20), 
				new Deck(Stock.BP, 20, 5, 10, -5, -10, -20),
				new Deck(Stock.Cisco, 20, -5, 10, -20, -10, 5), 
				new Deck(Stock.Dell, 5, -20, 10, 20, -10, -5),
				new Deck(Stock.Ericsson, -10, 10, 20, 5, -20, -5) 
		};
	}

	public static int[][] sampleShares() {
		return new int[][] { 
			{ 3, 0, 1, 4, 2 }, 
			{ 2, 2, 5, 0, 1 }, 
			{ 4, 1, 0, 1, 4 } 
		};
	}

	@Before
	public void setup() {
		game = new Game(Tests.sampleDecks(), Tests.sampleShares());
	}

	@Test
	public void tradeP0() {
		game.sell(0, Stock.Apple, 3);
		game.buy(0, Stock.Cisco, 6);
		Assert.assertArrayEquals(game.getShares(0), new int[] { 0, 0, 7, 4, 2 });
		Assert.assertEquals(game.getCash(0), 182);
	}

	@Test
	public void tradeP1() {
		game.buy(1, Stock.BP, 4);
		Assert.assertArrayEquals(game.getShares(1), new int[] { 2, 6, 5, 0, 1 });
		Assert.assertEquals(game.getCash(1), 88);
	}

	@Test
	public void tradeP2() {
		game.sell(2, Stock.Ericsson, 4);
		game.sell(2, Stock.Apple, 4);
		Assert.assertArrayEquals(game.getShares(2), new int[] { 0, 1, 0, 1, 0 });
		Assert.assertEquals(game.getCash(2), 1300);
	}

	@Test
	public void vote() {
		game.vote(0, Stock.Cisco, true);
		game.vote(0, Stock.Ericsson, false);
		game.vote(1, Stock.BP, true);
		game.vote(1, Stock.Cisco, true);
		game.vote(2, Stock.Apple, true);
		game.vote(2, Stock.BP, false);
		game.executeVotes();
		Assert.assertArrayEquals(game.getPrices(), new int[] { 80, 100, 120, 100, 100 });
		Card[] cards = new Card[] { new Card(5), new Card(20), new Card(-5), new Card(5), new Card(10) };
		Assert.assertArrayEquals(game.getCards(), cards);
	}
	*/
	@Test
	public void testRandomShares() {
		//Test that 10 random shares are produced.
		Player test = new Player("Dan");
		int sum = 0;
		sum += test.shares.get("Apple");

		sum += test.shares.get("BP");

		sum += test.shares.get("Cisco");

		sum += test.shares.get("Dell");

		sum += test.shares.get("Ericsson");

		Assert.assertTrue(sum == 10);
	}

	@Test
	public void testChangeOfStockPrice() {
		Game game = new Game();
		game.changeStock("a", -10);
		System.out.println(game.ApplePrice);
		Assert.assertTrue(game.ApplePrice == 90);
	}

	@Test
	public void testBuyingStock() {
		Game game = new Game();
		Player bob = new Player("Bob");
		game.addPlayer(bob);
		int currentAppleShares = bob.shares.get("Apple");
		System.out.println(currentAppleShares);
		game.buy("Bob", "Apple", 3);
		Assert.assertTrue(bob.shares.get("Apple") == (currentAppleShares + 3));
		System.out.println(bob.shares.get("Apple"));
	}

	@Test
	public void testSellingStock() {
		Game game = new Game();
		Player bob = new Player("Bob");
		game.addPlayer(bob);
		if (bob.shares.get("Apple") < 3) {
			bob.modifyShares("Apple", 3, '+');}
		int currentAppleShares = bob.shares.get("Apple");
		System.out.println(currentAppleShares);
		game.sell("Bob", "Apple", 3);
		Assert.assertTrue(bob.shares.get("Apple") == (currentAppleShares - 3));
		System.out.println(bob.shares.get("Apple"));


	}

	@Test
	public void testUserCastingVote()
	{
		Game game = new Game();
		Player bob = new Player("Bob");
		game.addPlayer(bob);

		game.initialiseVoting();
		Assert.assertTrue(game.votes[2] == 0);
		game.vote("Bob", 2, true );
		Assert.assertTrue(game.votes[2] == 1);
		game.vote("Bob", 2, true);
		Assert.assertTrue(game.votes[2] == 1);
		//assert vote of [2] increased by 1
	}

	@Test
	public void testVotingEffect()
	{
		Game game = new Game();
		Player bob = new Player("Bob");
		game.addPlayer(bob);
		int val  = game.ApplePrice;
		System.out.println(val);
		game.vote("Bob", 0, true);
		game.executeVotes();
		Assert.assertTrue(val != game.ApplePrice);
		System.out.println(game.ApplePrice);
	}

	@Test
	public void testVotingRemoval()
	{
		Game game = new Game();
		Player bob = new Player("Bob");
		game.addPlayer(bob);
		int val = game.Dell.cards.size();
		System.out.println(val);
		game.vote("bob", 3, false);
		game.executeVotes();
		Assert.assertTrue(val != game.Dell.cards.size());
		System.out.println(game.Dell.cards.size());
	}
}
