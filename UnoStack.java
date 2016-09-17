package cardGames;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class UnoStack <E> implements DeckADT<E>{

	private Stack <E> cards;
	private int items;

	public UnoStack() {
		cards = new Stack <E> ();
		items = 0;
	}

	public UnoStack(Stack<E> cards) {
		this.cards = cards;
	}

	public void insert108() {
		// repeat process twice
		for (int h = 0; h < 2; h++) {
			for (int i = 1; i < 5; i++) {
				for (int j = 1; j < 13; j++) {
					@SuppressWarnings("unchecked")
					E newCard = (E) new UnoCard(i,j);
					// throws exception if not valid card
					addCard(newCard);
					items++;
				}
			}
		}

		// add wild cards and 0's
		int color = 1;
		for (int i = 0; i < 4; i++) {
			@SuppressWarnings("unchecked")
			E wild = (E) new UnoCard(5, 13);
			@SuppressWarnings("unchecked")
			E wildDraw4 = (E) new UnoCard(5, 14);
			@SuppressWarnings("unchecked")
			E zeroes = (E) new UnoCard(color, 0);
			// throws exception if not valid card
			addCard(wild);
			addCard(wildDraw4);
			addCard(zeroes);
			color++;
			items+=3;
		}	
	}


	public void addCard(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (((UnoCard) e).getRank() > 14 || ((UnoCard) e).getRank() < 0) {
			throw new IllegalArgumentException();
		}
		if (((UnoCard) e).getColor() > 5 || ((UnoCard) e).getColor() < 1) {
			throw new IllegalArgumentException();
		}
		cards.push(e);
		items++;
	}

	public E removeCard() {
		if (isEmpty()) {
			throw new IllegalArgumentException();
		}
		E curr = null;
		try {
			curr = cards.pop();
		} catch (EmptyStackException e) {
			e.getStackTrace();
		}
		items--;
		return curr;
	}
	
	public E peek() {
		if (isEmpty()) {
			throw new IllegalArgumentException();
		}
		E curr = cards.peek();
		return curr;
	}

	public void randomShuffle() {
		Collections.shuffle(cards);
	}

	// need to use sorting algorithm, Can implement later
	public void shuffleInOrder() {

	}

	public boolean isEmpty() {
		return items == 0;
	}

	public int getNumItems() {
		return items;
	}

	//public int countRank
	// return copy of the deck
	public DeckADT<E> getDeck() {
		DeckADT <E> copy = new DeckStack <E> (cards);
		return copy;
	}

	public int compareTo(DeckStack <Card> e) {
		// TODO: Compare rank. Similar to toString();
		return 0;
	}

	public int size() {
		return items;
	}

	public String toString() {
		Stack <E> temp = cards;
		Stack <E> writeBack = new Stack <E> ();
		String ret = "";
		while (!temp.isEmpty()) {
			writeBack.push(temp.pop());
		}
		while (!writeBack.isEmpty()){
			E curr = null;
			curr = temp.push(writeBack.pop());
			ret += curr;
			if (!writeBack.isEmpty()){
				ret += ", ";
			}
		}
		return ret;

	}

	@Override
	public void insert52() {
		// Won't implement
	}
}
