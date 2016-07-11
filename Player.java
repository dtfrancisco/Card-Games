
package cardGames;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private List <Card> cards;
	//private List <Chip> chips;
	private int funds;
	private int currBet;
	private char type; // for many card games. Determine between player or house
	private int cardValue;
	private boolean hasBust; // blackjack case
	private boolean hasDoubleDowned; // blackjack case
	private boolean hasWon;

	public <E> Player(String name, char type, int funds) {
		if (type != 'p' && type != 'h') {
			throw new IllegalArgumentException();
		}
		this.setName(name);
		this.setType(type); // regular or house 'p' and 'h'
		cards = new ArrayList <Card> ();
		cardValue = 0;
		//chips = new ArrayList <Chip> ();
		currBet = 0;
		this.funds = funds;
		hasBust = false;
		hasDoubleDowned = false;
		hasWon = false;
	}

	public void addCard(Card e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		cards.add(e);
	}

	public boolean loseCard(Card e) {
		if (cards.isEmpty()) {
			return false;
		}
		cards.remove(0);
		return true;
	}

	/**public void addChips(Chip e) {
		// check if valid Chip
		chips.add(e);
	}
	public boolean removeChips(int value) {
		if (value == 0) {
			// nonono
			return false;
		}
		int tempVal = value;
		for (int i = 0; i < chips.size(); i++) {
			if (chips.get(i).getValue() < value) {
				tempVal = chips.get(i).getValue();
				tempVal = value - tempVal;
				if (tempVal == 0) {
					return true;
				}
			}
			else if (chips.get(i).getValue() == tempVal) {
				chips.remove(i);
				return true;
			}
		}
		return false;
	}*/

	/*public void cashOut () {
		int temp = 0;
		for (int i = 0; i < chips.size(); i++) {
			temp += chips.get(i).getValue();
			chips.remove(i);
		}
		funds += temp;
	}

	public void payOut(int value) {
		int temp = 0;
		// algorithm to add chips
		funds -= value;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List <Card> getHand() {
		List <Card> temp = cards;
		return temp;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getCardValue() {
		return cardValue;
	}

	public void setCardValue(int cardValue) {
		this.cardValue = cardValue; 
	}
	
	public int getFunds() {
		return funds;
	}
	
	public void addFunds(int funds) {
		this.funds += funds;
	}
	
	public void removeFunds(int funds) {
		this.funds -= funds;
	}
	
	public void addFunds() {
		this.funds += currBet;
	}
	
	public void removeFunds() {
		this.funds -= currBet;
	}
	
	public void setFunds(int funds) {
		this.funds = funds;
	}
	
	public int getCurrBet() {
		return currBet;
	}
	
	public void setCurrBet(int currBet) {
		this.currBet = currBet;
	}

	public boolean hasBust() {
		return hasBust;
	}

	public void makeBust(boolean hasBust) {
		this.hasBust = hasBust;
	}

	public boolean hasDoubleDowned() {
		return hasDoubleDowned;
	}

	public void makeDoubleDowned(boolean hasDoubleDowned) {
		this.hasDoubleDowned = hasDoubleDowned;
	}
	
	public boolean hasWon() {
		return hasWon;
	}

	public void makeWin(boolean hasWon) {
		this.hasWon = hasWon;
	}

	public String toString() {
		return "Name: "+name+"\tCards: "+cards+"\tType: "+type+"\tBusted: "+hasBust+"\n";
	}

}
