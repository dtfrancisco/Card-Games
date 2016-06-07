package cardGames;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class SimpleBlackJack {
	public static void main(String[] args) {
		/*DeckADT<Card> deck = new DeckStack <Card>() ;
			deck.insert52();
			System.out.println(deck);
			deck.randomShuffle();
			int num = deck.getNumItems();
			System.out.println(num);
			System.out.println(deck);
			Card e = deck.removeCard();*/
		blackjack();
	}

	private static void blackjack() {
		DeckADT <Card> deck = new DeckStack <Card> ();
		Player p1 = new Player("David", 'p');
		Player p2 = new Player("Eric", 'p');
		Player p3 = new Player("Mr. House", 'h');
		List <Player> players = new ArrayList <Player> ();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		p1.setFunds(100);
		p2.setFunds(100);
		p3.setFunds(100);
		Scanner in = new Scanner(System.in);
		boolean exit = false;
		boolean bust = false;
		BlackjackStack bj = new BlackjackStack(deck, players);
		Player curr = null;
		curr = bj.startGame();
		boolean hasHit = false;
		//Ability to see first of house's cards 
		System.out.println(players.get(players.size()-1).getName()+""
				+ ": ["+players.get(players.size()-1).getHand().get(0)+", ??]");
		for (Player p: players) {
			boolean validBet = false;
			while (p.getType() == 'p' && !validBet) {
				System.out.print(p.getName()+", enter amount to bet: ");
				int bet = in.nextInt();
				if (bet <= p.getFunds() && bet >= 1) {
					validBet = true;
				}
				else {
					System.out.println("Invalid bet.");
				}
			}
		}
		while (!exit) {
			System.out.println("\nIt is "+curr.getName()+"'s turn!\n");
			if (curr.getType() == 'p') {
				System.out.println("\nCommands\nc = check hand\nh = hit\ns= stay\nv = get hand value\nf = fold");
				char command = in.next().charAt(0);
				in.nextLine();
				switch (command) {
				case 'c': System.out.println(curr.getHand()); break;
				case 'h': 
					if (!hasHit) {
						hasHit = true;
						bj.hit(curr); 
						bust = bj.isBust();
						if (bust) {
							System.out.println(curr.getName()+" busted!\n"+curr.getHand());
							hasHit = false;
							curr = bj.changeTurn();
						}
					}
					else {
						System.out.println("You can only hit once per turn. Type 's' to end turn.");
					}
					break;
				case 's':
					hasHit = false;
					int value = bj.countHandForValue(curr.getHand());
					curr.setCardValue(value);
					curr = bj.changeTurn(); break;
				case 'v': System.out.println(bj.countHandForValue(curr.getHand())); break;
				case 'f': 
					curr.makeBust(true);
					int value2 = bj.countHandForValue(curr.getHand());
					curr.setCardValue(value2);
					curr = bj.changeTurn();
					break; //remove player from active game
				}
			}
			else {	// Mr. House
				boolean bustedPlayers = true;
				for (Player p: players) {
					if (!p.hasBust() && p.getType() == 'p') {
						bustedPlayers = false;
					}
				}
				if (bustedPlayers){
					int value = bj.countHandForValue(curr.getHand());
					curr.setCardValue(value);
					bj.endGame();
					exit = true;
				}
				else {
					int value = bj.countHandForValue(curr.getHand());
					curr.setCardValue(value);
					if (value >= 17) {
						System.out.println(curr.getName()+" has hit to the appropriate amount.\n");
						// would use isBust but takes more lines
						if (value > 21) {
							System.out.println(curr.getName()+"has bust!\n");
							curr.makeBust(true);
						}
						// check all active player hands and end game
						bj.endGame();
						exit = true;
					}
					else {
						bj.hit(curr);
						System.out.println(curr.getName()+" hit!");
						curr = bj.changeTurn();
					}
				}
			}
			//reset busts and card values and bets once done
		}
		in.close();
	}
}