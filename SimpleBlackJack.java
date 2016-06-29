package cardGames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SimpleBlackJack {

	private static PlayerDatabase allPlayers = new PlayerDatabase();
	private static PlayerDatabase currPlayers = new PlayerDatabase();
	private static Player curr = null;
	private static BlackjackStack bj = null;
	public static Scanner in = null;
	private static int numDecks = 1;

	public static void main(String[] args) {
		loadDatabase();
		mainMenu();
		updateData();
		in.close();
	}

	private static void loadDatabase() {
		String fileName = "BlackjackPlayerDB.txt";
		File f = new File(fileName);
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(f);
			while (fileReader.hasNext()) {
				String name = fileReader.next();
				char type = fileReader.next().charAt(0);
				int funds = fileReader.nextInt();
				Player newPlayer = new Player(name, type, funds);
				allPlayers.addPlayer(newPlayer);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			fileReader.close();
		}		
	}

	private static void mainMenu() {
		boolean exit = false;
		in = new Scanner(System.in);

		while (!exit) {

			System.out.println("\ns = set up game\nm = modify database\nd = display stats\nq = quit");
			char command = in.next().charAt(0);
			in.nextLine();

			switch (command) {
			case 's': setUp(); break;
			case 'm': modifyDatabase(); break;
			case 'd': displayStats(); break;
			case 'q': exit = true; break;
			}
		}
	}

	private static void setUp() {
		currPlayers = Menues.setCurrPlayers(allPlayers);
		setCurrDeck();
		startGame();
	}

	private static void modifyDatabase() {
		Menues.changeDatabase(allPlayers);
	}

	private static void setCurrDeck() {
		System.out.println("\n1 = create standard 52 card deck\n2 = create a multideck");
		int response = in.nextInt();
		if (response == 1) {
			createDeck();
			System.out.println("\nDeck properly created\n");
		}
		else if (response == 2) {
			specialDeck();
			System.out.println("\nDeck properly created\n");
		}
		else {
			System.out.println("\nA deck was not created!\n");
		}		
	}

	private static void createDeck() {
		DeckADT <Card> deck = new DeckStack <Card> ();
		for (int i = 0; i < numDecks; i++) {
			deck.insert52();
		}
		try {
			bj = new BlackjackStack(deck, currPlayers.getPlayers());
			curr = bj.startGame();		
		} catch (IllegalArgumentException e) {
			System.out.println("\nWhen choosing a list of players, one and only one of them must be of type house\n");
			currPlayers.getPlayers().clear();
			setUp();
		}
	}

	private static void specialDeck() {
		int decks;
		do {
			System.out.print("\nSelect the number of decks you want to play with (1-8): ");
			decks = in.nextInt();
			numDecks = decks;
		} while (decks <= 8 && decks >= 1); // maximum of 8, minimum of 1 deck 
		createDeck();
	}

	private static void displayStats() {
		//TODO Add more things to PlayerDB.txt. Like wins, losses, total earned and gained. Need to change load/save methods
		//and constructor for Player. Should be able to sort certain things

	}

	private static void startGame() {
		simulateGame();
		System.out.print("Want to play again? (Press y)");
		char command = in.next().charAt(0);
		//in.nextLine()
		System.out.println();
		if (command == 'y') {
			createDeck();
			startGame();
		}
	}

	private static void simulateGame() {
		boolean exit = false;
		boolean bust = false;
		boolean hasHit = false;
		curr = currPlayers.getPlayers().get(0);
		makeBets();
		//Ability to see first of house's cards 
		//curr = currPlayers.getPlayers().get(currPlayers.getPlayers().size()-1);
		System.out.println(curr.getName()+""
				+ ": ["+curr.getHand().get(0)+", ??]\n");
		curr = bj.changeTurn();


		while (!exit) {
			System.out.println("\nIt is "+curr.getName()+"'s turn!\n");
			if (curr.getType() == 'p') {
				if (!curr.hasDoubleDowned()) {
					System.out.println("\nCommands\nc = check hand\nh = hit\ns= stay\nd = double down"
							+ "\nv = get hand value\nf = fold");
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
					case 'd':
						if (!hasHit && !curr.hasDoubleDowned()) {
							curr.makeDoubleDowned(true);
							bj.hit(curr);
							bust = bj.isBust();
							if (bust) {
								System.out.println(curr.getName()+" busted!\n"+curr.getHand());
								hasHit = false;
								curr = bj.changeTurn();
							}
							else {
								System.out.println(curr.getHand());
							}
							int value1 = bj.countHandForValue(curr.getHand());
							curr.setCardValue(value1);
						}
						else {
							System.out.println("Move not valid.");
						}
						break;
					case 'v': System.out.println(bj.countHandForValue(curr.getHand())); break;
					case 'f': 
						hasHit = false;
						curr.makeBust(true);
						int value2 = bj.countHandForValue(curr.getHand());
						curr.setCardValue(value2);
						curr = bj.changeTurn();
						break; //remove player from active game
					}
				}
				else {
					curr = bj.changeTurn();
				}
			}
			else {	// Determine if the game is over or not
				exit = endTurn();
			}
		}
		//TODO: reset busts and card values and bets once done (add to add/remove funds once done with game instead?)
		for (Player p: currPlayers.getPlayers()) {
			p.getHand().clear(); // remove cards from player
			p.setCurrBet(0);
			p.setCardValue(0);
			p.makeBust(false);
			p.makeDoubleDowned(false);
			p.makeWin(false);
		}
	}

	private static void makeBets() {
		while (curr.getType() != 'h') {
			int bet = 0;
			while (bet < 5 || bet > 50) {
				System.out.print(curr.getName()+", select an amount to bet. Min= 5, Max= 50: ");
				bet = in.nextInt();
				curr.setCurrBet(bet);
				System.out.println();
				//TODO: Case when player cannot afford bet
				if (bet > curr.getFunds()) {
					curr.setFunds(curr.getFunds()+bet);
				}
			} 
			curr = bj.changeTurn();
		}
		// returns current player as house
	}

	private static boolean endTurn() {
		boolean bustedPlayers = true;
		for (Player p: currPlayers.getPlayers()) {
			if (!p.hasBust() && p.getType() == 'p') {
				bustedPlayers = false;
			}
		}
		// if all players have busted
		if (bustedPlayers){
			//int value = bj.countHandForValue(curr.getHand());
			//curr.setCardValue(value);
			curr.setCardValue(0);
			bj.endGame();
			return true;
		}
		else {
			int value = bj.countHandForValue(curr.getHand());
			curr.setCardValue(value);
			if (value >= 17) {
				System.out.println(curr.getName()+" has hit to the appropriate amount.\n");
				// would use isBust but takes more lines
				if (value > 21) {
					System.out.println(curr.getName()+" has bust!\n");
					curr.setCardValue(0);
					curr.makeBust(true);
				}
				// check all active player hands and end game
				bj.endGame();
				return true;
			}
			else {
				bj.hit(curr);
				System.out.println(curr.getName()+" hit!");
				curr = bj.changeTurn();
				return false;
			}
		}
	}

	private static void updateData() {
		String writeTo = "BlackjackPlayerDB.txt";
		File f = new File(writeTo);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(f);
			for (Player p: allPlayers.getPlayers()) {
				pw.println(p.getName()+" "+p.getType()+" "+p.getFunds());
			}
		} catch (FileNotFoundException g) {
			g.printStackTrace();
			System.exit(-1);
		} finally {
			pw.close();
		}		
	}
}
