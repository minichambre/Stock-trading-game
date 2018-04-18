import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	//public Stock stock;
	public List<Card> cards;

	public Deck() {
		//this.stock = stock;
		cards = new ArrayList<>();
		for (int effect : Card.EFFECTS) {
			cards.add(new Card(effect));
			Collections.shuffle(cards);
		}
	}

	/*
	public Deck(Stock stock, int... effects) {
		this.stock = stock;
		cards = new ArrayList<>();
		for (int effect : effects) {
			cards.add(new Card(effect));
		}
	}
	*/

	public void removeTop()
	{
		if (cards.size() > 0)
		{
			cards.remove((0));
		}

	}


	public Card getTop()
	{
		//Assuming top is the 0 value.

		return cards.get(0);
	}
	@Override
	public String toString() {
		return " " + cards;
	}

	/*
	public static void main(String[] args) {
		Deck[] decks = new Deck[Stock.values().length];
		for (Stock s : Stock.values()) {
			decks[s.ordinal()] = new Deck(s);
		}
		for (Deck d : decks) {
			System.out.println(d);
		}
	}
	*/
}
