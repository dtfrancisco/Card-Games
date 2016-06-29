package cardGames;

import java.util.ArrayList;
import java.util.List;

public class BlackjackStack{
	private DeckADT <Card> deck;
	private List <Player> players;
	private int index;

	public BlackjackStack(DeckADT <Card> deck, List <Player> players) {
		// Support already created stacks later
		if (deck.isEmpty()) {
			// need to support later
			this.deck = new DeckStack <Card>();
		}
		else {
			this.deck = deck;
			// check deck for validity?
		}

		if (players.isEmpty()) {
			// need to support later
			this.players = new ArrayList <Player> ();
		}
		else {
			this.players = players;
			if (players.size() == 1) {
				throw new IllegalArgumentException();
			}
			checkPlayerList();
		}
		index = 0; // 1st player is index
	}

	private void checkPlayerList() {
		int houses = 0;
		int change = 0;
		boolean reshift = false;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getType() == 'h') {
				houses++;
				if (i != players.size() - 1) {
					reshift = true;
					change = i;
				}
			}
		}
		if (houses != 1) {
			throw new IllegalArgumentException();
		}
		// make house last person to play regardless of user input
		if (reshift) {
			Player p = players.remove(change);
			players.add(p);
		}
	}

	public Player startGame() {
		deck.randomShuffle();
		// give each player two cards
		for (int i = 0; i < players.size(); i++) {
			Card one = deck.removeCard();
			Card two = deck.removeCard();
			players.get(i).addCard(one);
			players.get(i).addCard(two);
		}
		return players.get(index);
	}

	public void hit(Player p) {
		Card get = deck.removeCard();
		p.addCard(get);
	}

	public Player changeTurn() {
		Player curr = players.get(index);
		// Dequeue current player, then enqueue it
		if (index+1 == players.size()){
			for (int i = 0; i < players.size(); i++) {
				if (!players.get(i).hasBust()) {
					index = i;
					// return first player who hasn't busted
					return players.get(i);
				}
			}
		}
		// start new turn
		else {
			for (int i = index+1; i < players.size(); i++) {
				if (!players.get(i).hasBust()) {
					index = i;
					return players.get(i);
				}
			}
		}
		// this shouldn't be executed, needed to prevent compiler error
		return curr;
	}

	public boolean isBust() {
		List <Card> hand = players.get(index).getHand();
		int value = countHandForValue(hand);
		if (value > 21) {
			players.get(index).setCardValue(0);
			players.get(index).makeBust(true);
			return true;
		}
		// if player doesn't exist, still return false
		return false;
	}

	public void endGame() {
		for (int i = 0; i < players.size()-1; i++) {
			if (players.get(i).getCardValue() > players.get(players.size()-1)
					.getCardValue() && !players.get(i).hasBust()) {
				players.get(i).makeWin(true);
				
				// transfer funds from house to player

				int fundsGained;
				if (players.get(i).getCardValue() == 21) {
					// If player gets a 21, player gets 1.5 x his/her original bet
					//TODO: Number rounds down. Not sure if this is desired or not
					fundsGained = players.get(i).getCurrBet()+ (players.get(i).getCurrBet()/2);					
					players.get(i).addFunds(fundsGained);
				}
				
				else {
					fundsGained = players.get(i).getCurrBet();
					players.get(i).addFunds();
				}
				
				players.get(players.size()-1).removeFunds(fundsGained);

				System.out.println(players.get(i).getName()+" beat "+
						players.get(players.size()-1).getName()+" and won "+fundsGained+" dollars! \n");
			}
			else {
				
				// House doesn't bust but player does
				if (!players.get(players.size()-1).hasBust() && players.get(i).hasBust()) {
					
					int fundsLost = players.get(i).getCurrBet();
					// transfer funds from player to house
					players.get(i).removeFunds();
					players.get(players.size()-1).addFunds(fundsLost);

					System.out.println(players.get(i).getName()+" was defeated by "+
							players.get(players.size()-1).getName()+" and will give "+players.get(players.size()-1).getName()
							+" "+fundsLost+" dollars!\n");
										
				}
				
				// Both player and house bust or both player and house have the same card value
				else {
					System.out.println(players.get(i).getName()+" draws with "
							+players.get(players.size()-1).getName()+"! Neither "+players.get(i).getName()+" nor"
							+players.get(players.size()-1).getName()+" gain or lose any money!\n");
				}
			}
			System.out.println(players.get(i).getName()+": "+players.get(i).getHand()+" = "+players.get(i).getCardValue()+"\n");
		}
		
		// Displays house's hand
		System.out.println(players.get(players.size()-1).getName()+": "+players.get(players.size()-1).getHand()+
				" = "+players.get(players.size()-1).getCardValue()+"\n");
	}

	public int countHandForValue (List <Card> hand) {
		List <Card> temp = hand;
		int value = 0;
		boolean setAceCase = false;
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).getRank() == 1) {
				value += 1;
				setAceCase = true;
			}
			else if (temp.get(i).getRank() == 11 || temp.get(i).getRank() == 12
					|| temp.get(i).getRank() == 13) {
				// face cards count as 10
				value += 10;
			}
			else { // if not ace or face card
				value += temp.get(i).getRank();
			}
		}
		// only 1 ace can be worth 11
		if (setAceCase) {
			int tmp = value + 10;
			if (tmp <= 21){				
				value += 10;
			}
		}
		return value;
	}

	public DeckADT <Card> getDeck() {
		return deck;
	}

}
