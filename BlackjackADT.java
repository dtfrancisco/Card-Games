package cardGames;
import java.util.List;

public interface BlackjackADT <E> {
	void checkPlayerList();
	Player startGame();
	void hit(Player e);
	Player changeTurn();
	boolean isBust();
	void endGame();
	int countHandForValue (List<Card> hand);
	BlackjackADT <Card> getHand();
}
