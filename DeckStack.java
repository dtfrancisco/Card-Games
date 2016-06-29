package cardGames;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class DeckStack<E> implements DeckADT<E>{

	private Stack <E> cards;
	private int items;
	
	public DeckStack() {
		cards = new Stack <E> ();
		items = 0;
	}
	
	public DeckStack(Stack<E> cards) {
		this.cards = cards;
	}

	public void insert52() {
		for (int i = 1; i < 14; i++) {
			for (int j = 1; j < 5; j++) {
				@SuppressWarnings("unchecked")
				E newCard = (E) new Card(i,j);
				// throws exception if not valid card
				addCard(newCard);
				items++;
			}
		}
	}


	public void addCard(E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (((Card) e).getRank() > 13 || ((Card) e).getRank() < 1) {
			throw new IllegalArgumentException();
		}
		if (((Card) e).getSuit() > 4 || ((Card) e).getSuit() < 1) {
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
		String ret = "";
		Stack <E> temp = cards;
		SimpleQueue<E> writeBack = new SimpleQueue <E> ();
		while (!temp.isEmpty()) {
			writeBack.enqueue(temp.pop());
		}
		while (!writeBack.isEmpty()){
			E curr = null;
			try {
				curr = temp.push(writeBack.dequeue());
				ret += curr;
				if (!writeBack.isEmpty()){
					ret += ", ";
				}
			} catch (EmptyQueueException e) {
				e.printStackTrace();
			}
		}
		return ret;

	}
}
