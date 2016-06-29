package cardGames;
public class Card {
	private int rank;
	private int suit;
	private boolean alphabetical;
	private boolean alternating;
	private boolean reverseOrder;

	public Card(int rank, int suit) {
		if (suit < 1 || suit > 4) {
			throw new IllegalArgumentException();
		}
		if (rank < 1 || rank > 13) {
			throw new IllegalArgumentException();
		}
		this.rank = rank;
		this.suit = suit;
	}

	public int getRank() {
		return rank;
	}

	public int getSuit() {
		return suit;
	}

	public int compareTo(Card e) {
		if (this.suit > e.getSuit()) {
			return 1;
		}
		// cards suit is equal
		else if (this.suit == e.getSuit() && this.rank > e.getRank()) {
			return 1;
		}
		else if (this.suit == e.getSuit() && this.rank == e.getRank()) {
			return 0;
		}
		return -1;	
	}

	// depending on the game 
	public void setSuitOrder() {

	}

	public String toString() {
		String str = "";
		if (rank == 1) {
			str += "ace ";
		}
		else if (rank == 11) {
			str += "jack ";
		}
		else if (rank == 12) {
			str += "queen ";
		}
		else if (rank == 13) {
			str += "king ";
		}
		else {
			str += rank+" ";
		}
		switch(suit) {
		case 1: str += "of clubs"; break;
		case 2: str += "of diamonds"; break;
		case 3: str += "of hearts"; break;
		case 4: str += "of spades"; break;
		}
		return str;
	}
}
