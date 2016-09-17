package cardGames;

import java.util.ArrayList;
import java.util.Scanner;

public class SimpleUno {

	private static UnoGame<UnoCard> u1 = null;
	private static Player <UnoCard> curr = null;
	private static Scanner in = null;

	public static void main(String [] args) {
		initiateGame();
		simulateGame();
	}

	private static void simulateGame() {
		boolean exit = false;
		boolean played = false;
		in = new Scanner(System.in);

		while (!exit) {

			System.out.println("p = play a card, d = draw a card, c = end turn, s = show cards t = top card\n");
			System.out.println("It's "+curr.getName()+"'s turn!\n");
			
			char command = in.next().charAt(0);
			in.nextLine();

			switch (command) {
			case 'p' : // play a card
				System.out.println(u1.getDeck());
				if (!played) {
					System.out.println(curr.getHand());
					System.out.println("Choose the index of the card you'd like to play.\n");
					int index = in.nextInt();
					UnoCard playing = curr.getCard(index);
					if (playing != null) {
						boolean valid = false;
						valid = u1.validateCard(playing);

						if (valid && (playing.getColor() == 5 || playing.getRank() > 9 && playing.getRank() < 15)) { // only for special cases
							if (playing.getColor() == 5 || playing.getRank() == 12 || playing.getRank() == 14) {
								System.out.println("Choose a new color to set the top card to!");
								int color = in.nextInt();
								System.out.print("1 = red, 2 = yellow, 3 = green, 4 = blue: "); 
								playing.setColor(5 + color); // 5 = wild
								playCard(playing);
								played = true;
							}

							else if (playing.getRank() == 10) {
								playCard(playing);
								curr = u1.skipPlayer();
								played = false;
							}

							else if (playing.getRank() == 11) {
								playCard(playing);
								curr = u1.reverseOrder();
								played = false;
							}

							if (playing.getRank() == 12) {
								// force draw 2 after playing
								curr = u1.changeTurn();
								System.out.println(curr.getName()+" must draw 2 cards!\n");
								ArrayList <UnoCard> drawnCards = new ArrayList <UnoCard> ();
								UnoCard e = u1.getDeck().removeCard();
								drawnCards.add(e);
								curr.addCard(e);
								UnoCard f = u1.getDeck().removeCard();
								drawnCards.add(f);
								curr.addCard(f);
								for (UnoCard all : drawnCards) {
									System.out.println(curr.getName() + " drew "+all);
								}
								curr = u1.changeTurn();
								played = false;
							}

							else if (playing.getRank() == 14) {
								// force draw 4 after playing
								curr = u1.changeTurn();
								System.out.println(curr.getName()+" must draw 4 cards!\n");
								ArrayList <UnoCard> drawnCards = new ArrayList <UnoCard> ();
								UnoCard e = u1.getDeck().removeCard();
								drawnCards.add(e);
								curr.addCard(e);
								UnoCard f = u1.getDeck().removeCard();
								drawnCards.add(f);
								curr.addCard(f);
								UnoCard g = u1.getDeck().removeCard();
								drawnCards.add(g);
								curr.addCard(g);
								UnoCard h = u1.getDeck().removeCard();
								drawnCards.add(h);
								curr.addCard(h);

								for (UnoCard all : drawnCards) {
									System.out.println(curr.getName() + " drew "+all);
								}
								curr = u1.changeTurn();
								played = false;
							}

						}

						else if (valid) { // for all normal cards being played
							playCard(playing);
						}

						else {
							// loop again
							System.out.println("Invalid card!\n");
						}
					}
					else {
						// this case shouldn't occur
					}
				}

				else {
					System.out.println("Move already made! Press c to end turn!\n");
				}

				break; 


			case 'd' : 
				UnoCard e = u1.getDeck().removeCard();
				curr.addCard(e);
				break; // draw a card

			case 'c' : 
				curr = u1.changeTurn();
				System.out.println("It's "+curr.getName()+"'s turn!\n");
				played = false;
				break; // change turn after a card is played

			case 's' :
				System.out.println(curr.getHand()+"\n");
				break;

			case 't' :
				System.out.println(u1.getDiscard().peek()+"\n");
			}
		}
	}

	private static void playCard(UnoCard playing) {
		u1.getDiscard().addCard(playing);
		curr.loseCard(playing);
		System.out.println(playing+" added to the deck!");
	}

	private static void initiateGame() {
		ArrayList <Player> players = new ArrayList <Player> ();
		DeckADT <UnoCard> stack = null;

		Player<?> p1 = new Player<Object>("James");
		Player<?> p2 = new Player<Object>("Kevin");
		Player<?> p3 = new Player<Object>("Eric");
		Player<?> p4 = new Player<Object>("Erik");
		stack = new UnoStack <UnoCard>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		stack.insert108();
		//System.out.println(stack.getDeck());
		u1 = new UnoGame<UnoCard>(stack, players);		
		curr = u1.startGame();
	}
}