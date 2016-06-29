package cardGames;

public interface DeckADT <E> {
	void insert52();
	void addCard (E card);
	E removeCard ();
	void randomShuffle();
	void shuffleInOrder();
	boolean isEmpty();
	int getNumItems();
	DeckADT<E> getDeck();
}
