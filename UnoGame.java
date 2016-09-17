package cardGames;

import java.util.ArrayList;
import java.util.List;

public class UnoGame<E> {
	private DeckADT <E> deck;
	private DeckADT <E> discard;
	private List <Player> players;
	private int index;
	private boolean normalOrder; // true if order is standard, false when reverse card is played

	public UnoGame(DeckADT<E> deck, List <Player> players) {
		// Support already created stacks later
		if (deck.isEmpty()) {
			// need to support later
			this.deck = new DeckStack <E>();
		}
		else {
			this.deck = deck;
			// check deck for validity?
		}

		discard = new DeckStack <E> ();

		if (players.isEmpty()) {
			// need to support later
			this.players = new ArrayList <Player> ();
		}
		else {
			this.players = players;
			if (players.size() < 1 || players.size() > 10) {
				throw new IllegalArgumentException();
			}
		}
		index = 0; // 1st player is index
		normalOrder = true;
	}

	public Player <E> startGame() {
		//deck.randomShuffle();
		// give each player 5 cards
		for (int i = 0; i < players.size(); i++) {
			for (int j = 0; j < 5; j++) {
				E card = deck.removeCard();
				players.get(i).addCard(card);
			}
		}
		E topCard = deck.removeCard();
		discard.addCard(topCard);
		return players.get(index);
	}

	public void hit(Player<E> p) {
		E get = deck.removeCard();
		p.addCard((E) get);
	}
	
	public boolean validateCard(UnoCard e) {
		UnoCard top = (UnoCard) discard.peek();
		
		// normal case
		if ((top.getColor() == e.getColor()) || (top.getColor() + 5 == e.getColor()) || (top.getRank() == e.getRank())) {
			return true;
		}
		
		// special case - wild, wild draw 2 or wild draw 4
		if (e.getColor() == 5 && (e.getRank() == 12 || e.getRank() == 13 || e.getRank() == 14)) {
			return true;
		}
		
		return false;
	}
	
	public boolean playCard(E e) {
		boolean valid = false;
		valid = players.get(index).loseCard(e);
		if (valid) {
			discard.addCard(e);
			return true;
		}
		return false;
	}

	public Player reverseOrder() {
		if (normalOrder) {
			if (index == 0) {
				index = players.size()-1;
			}
			else {
				index--;
			}
		}

		else {
			// if previously were going in reverse order
			if (index == players.size() - 1) {
				index = 0;
			}
			else {
				index++;
			}
		}

		normalOrder = !normalOrder;

		return players.get(index);
	}

	public Player <E> skipPlayer() {
		if (index == players.size() - 1) {
			index = 1;
			return players.get(index);
		}
		else if (index == players.size() - 2) {
			index = 0;
			return players.get(index);
		}
		else {
			index +=2;
			return players.get(index);
		}
	}

	public Player <E> changeTurn() {
		Player curr = players.get(index);	
		if (normalOrder) {
			if (index == players.size() - 1){
				index = 0;
			}
			else {
				index++;
			}
		}
		else {
			if (index == 0) {
				index = players.size() - 1;
			}
			else {
				index--;
			}
		}
		return players.get(index);
	}

	public DeckADT <E> getDeck() {
		return deck;
	}
	
	public DeckADT <E> getDiscard() {
		return discard;
	}

}