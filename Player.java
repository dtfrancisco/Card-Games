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
	private int wins;
	private int losses;
	private int draws;
	private int moneyEarned;
	private int moneyLost;
	private boolean firstTurn;

	public <E> Player(String name, char type, int funds) {
		
		initConstructorBasics(name, type, funds);
		this.wins = 0;
		this.losses = 0;
		this.draws = 0;
		this.moneyEarned = 0;
		this.moneyLost = 0;
	}
	
	public <E> Player(String name, char type, int funds, 
			int wins, int losses, int draws, int moneyEarned, int moneyLost) {
		
		initConstructorBasics(name, type, funds);

		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
		this.moneyEarned = moneyEarned;
		this.moneyLost = moneyLost;
	}
	
	private void initConstructorBasics(String name, char type, int funds) {
		
		if (type != 'p' && type != 'h') {
			throw new IllegalArgumentException();
		}
		
		this.setName(name);
		this.setType(type); // regular or house 'p' and 'h'
		cards = new ArrayList <Card> ();
		cardValue = 0;
		//chips = new ArrayList <Chip> ();
		currBet = 0;
		firstTurn = true;
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
	
	// for special cases (ie. 21 in blackjack yields 1.5x original bet)
	public void addFunds(int funds) {
		this.funds += funds;
		moneyEarned += funds;
	}
	
	// for special cases. The house gives this amount to the player
	public void removeFunds(int funds) {
		this.funds -= funds;
		moneyLost += funds;
	}
	
	public void addFunds() {
		this.funds += currBet;
		moneyEarned += currBet;
	}
	
	public void removeFunds() {
		this.funds -= currBet;
		moneyLost += currBet;
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

	public int getWins() {
		return wins;
	}

	public void addWin() {
		wins += 1;
	}

	public int getLosses() {
		return losses;
	}

	public void addLoss() {
		losses += 1;
	}

	public int getDraws() {
		return draws;
	}

	public void addDraw() {
		draws += 1;
	}
	
	// added when funds are added to account
	public int getMoneyEarned() {
		return moneyEarned;
	}
	
	// added when funds are removed from an account
	public int getMoneyLost() {
		return moneyLost;
	}
	
	public boolean getFirstTurn() {
		return firstTurn;
	}
	
	public void setFirstTurn(boolean firstTurn) {
		this.firstTurn = firstTurn;
	}
	
	public int compareTo(Player p1) {
		if (getFunds() > p1.getFunds())
			return 1;
		if (getFunds() < p1.getFunds())
			return -1;
		return 0;
	}
	
	// Blackjack
	public String toString() {
		return name+": "+cards+" , Busted: "+hasBust
				+" , Current bet: "+currBet+"\n";
	}
}
