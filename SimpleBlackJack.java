package cardGames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SimpleBlackJack {

	private static PlayerDatabase allPlayers = new PlayerDatabase();
	private static PlayerDatabase currPlayers = new PlayerDatabase();
	private static Player currPlayer = null;
	private static BlackjackStack bj = null;
	public static Scanner in = null;
	private static int numDecks;
	private static int minBet;
	private static int maxBet;
	private static char hitOn17;

	public static void main(String[] args) {
		loadDatabase();
		loadSettings();
		mainMenu();
		updateData();
		updateSettings();
		in.close();
	}

	private static void loadSettings() {
		String fileName = "Settings.txt";
		File f = new File(fileName);
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(f);
			numDecks = fileReader.nextInt();
			minBet = fileReader.nextInt();
			maxBet = fileReader.nextInt();
			hitOn17 = fileReader.next().charAt(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			fileReader.close();
		}
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
				int wins = fileReader.nextInt(); 
				int losses = fileReader.nextInt();
				int draws = fileReader.nextInt(); 
				int moneyGained = fileReader.nextInt();
				int moneyLost = fileReader.nextInt(); 

				Player newPlayer = new Player(name, type, funds, 
						wins, losses, draws, moneyGained, moneyLost);
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

			System.out.println("\nb = begin game\nc = view current settings\ns = alter settings\nm = modify database\nd = display stats\nq = quit");
			char command = in.next().charAt(0);
			in.nextLine();

			switch (command) {
			case 'b': setUp(); break;
			case 'c': viewCurrSettings();;break;
			case 's': changeSettings(); break;
			case 'm': Menues.changeDatabase(allPlayers); break;
			case 'd': Menues.displayStats(allPlayers); break;
			case 'q': exit = true; break;
			}
		}
	}

	private static void viewCurrSettings() {
		System.out.println("\nNum. Decks = "+numDecks+"\nCurr Min. bet = "+minBet
				+"\nCurr Max. bet = "+maxBet+"\nSoft/hard 17 = "+hitOn17+"\n");		
	}

	private static void changeSettings() {
		boolean exit = false;
		while (!exit){
			System.out.println("\n1 = alter number of decks\n2 = change min/max bet\n3 = change soft/hard 17"
					+ "\n4 = view current settings\n5 = exit");
			int response = in.nextInt();
			switch (response) {
			case 1: 
				int decks;
				decks = Menues.setNumDecks(); 
				numDecks = decks;
				break;
			case 2: 
				int [] minMax = new int[2];
				minMax = Menues.setMinMaxBet(); 
				minBet = minMax[0];
				maxBet = minMax[1];
				break;
			case 3: 
				char type;
				do {
					System.out.print("Press s to make the dealer hit on a soft 17, press h to implement a hard 17: ");
					type = in.next().charAt(0);
					System.out.println("\n");
				} while (type != 's' && type != 'h');
				break;
			case 4: viewCurrSettings(); break;
			case 5: exit = true; break;
			}
		}
	}

	private static void setUp() {
		currPlayers = Menues.setCurrPlayers(allPlayers);
		createDeck();
		startGame();
	}

	private static void createDeck() {
		DeckADT <Card> deck = new DeckStack <Card> ();
		for (int i = 0; i < numDecks; i++) {
			deck.insert52();
		}
		try {
			bj = new BlackjackStack(deck, currPlayers.getPlayers());
			currPlayer = bj.startGame();		
		} catch (IllegalArgumentException e) {
			System.out.println("\nWhen choosing a list of players, one and only one of them must be of type house\n");
			currPlayers.getPlayers().clear();
			setUp();
		}
	}

	private static void startGame() {
		simulateGame();
		System.out.print("Want to play again? (Press y): ");
		char command = in.next().charAt(0);
		System.out.println();
		if (command == 'y') {
			createDeck();
			startGame();
		}
	}

	private static void makeBets() {
		while (currPlayer.getType() != 'h') {
			int bet = 0;
			while (bet < minBet || bet > maxBet) {
				System.out.print(currPlayer.getName()+", select an amount to bet. Min= "+minBet+", Max= "
						+maxBet+": ");
				bet = in.nextInt();
				currPlayer.setCurrBet(bet);
				System.out.println();
			} 
			currPlayer = bj.changeTurn();
		}
		currPlayer = bj.changeTurn();
		// returns current player as first player
	}

	private static void simulateGame() {
		boolean exit = false;
		boolean bust = false;
		boolean hasHit = false;
		currPlayer = currPlayers.getPlayers().get(0);
		makeBets();		
		//Print all player hands and see first of house's cards 
		System.out.println("\n");

		while (!exit) {
			printGameInfo();

			if (currPlayer.getType() == 'p') {
				if (!currPlayer.hasDoubleDowned()) {
					System.out.println("\nIt is "+currPlayer.getName()+"'s turn!\n");
					System.out.println("\nCommands\nc = check hand\nh = hit\ns = stay\nd = "
							+ "double down\nv = get hand value\nf = surrender (only on first turn)");
					char command = in.next().charAt(0);
					in.nextLine();
					switch (command) {
					// TODO: might add back when double down/splitting is implemented
					case 'c': System.out.println(currPlayer.getHand()); break;
					case 'h': 
						if (!hasHit) {
							currPlayer.setFirstTurn(false); // if on first turn
							hasHit = true;
							bj.hit(currPlayer); 
							bust = bj.isBust();
							if (bust) {
								System.out.println(currPlayer.getName()+" busted!\n"+currPlayer.getHand()+"\n");
								hasHit = false;
							}
						}
						else {
							System.out.println("You can only hit once per turn. Type 's' to end turn.\n");
						}
						break;

					case 's':
						hasHit = false;
						int value = bj.countHandForValue(currPlayer.getHand());
						currPlayer.setCardValue(value);
						currPlayer.setFirstTurn(false);
						currPlayer = bj.changeTurn(); 
						break;

					case 'd':
						if (!hasHit && !currPlayer.hasDoubleDowned()) {
							currPlayer.setFirstTurn(false); // if on first turn
							currPlayer.makeDoubleDowned(true);
							bj.hit(currPlayer);
							int bet = currPlayer.getCurrBet();
							// double original bets
							currPlayer.setCurrBet(bet * 2);
							bust = bj.isBust();
							if (bust) {
								System.out.println(currPlayer.getName()+" busted!\n"+currPlayer.getHand()+"\n");
								hasHit = false;
								currPlayer = bj.changeTurn();
							}
							else {
								System.out.println(currPlayer.getHand());
							}
							int value1 = bj.countHandForValue(currPlayer.getHand());
							currPlayer.setCardValue(value1);
							currPlayer = bj.changeTurn(); 
						}
						else {
							System.out.println("\nMove not valid.\n");
						}
						break;

					case 'v': System.out.println(bj.countHandForValue(currPlayer.getHand())); break;

					case 'f': 
						if (currPlayer.getFirstTurn()) {
							hasHit = false;
							currPlayer.makeBust(true);
							int value2 = bj.countHandForValue(currPlayer.getHand());
							currPlayer.setCardValue(value2);
							int bet = currPlayer.getCurrBet();
							// half original bet
							currPlayer.setCurrBet(bet /2);
							currPlayer = bj.changeTurn();
						}
						else {
							System.out.println("\nMove not valid.\n");
						}
						break; //remove player from active game
					}
				}
				else {
					System.out.println("\n"+currPlayer.getName()+"'s turn is skipped because they double downed.");
					currPlayer = bj.changeTurn();
				}
			}
			else {	// Determine if the game is over or not
				System.out.println("\nIt is "+currPlayer.getName()+"'s turn!\n");
				exit = endTurn();
			}
		}

		for (Player p: currPlayers.getPlayers()) {
			p.getHand().clear(); // remove cards from player
			p.setCurrBet(0);
			p.setCardValue(0);
			p.setFirstTurn(true);
			p.makeBust(false);
			p.makeDoubleDowned(false);
			p.makeWin(false);
		}
	}

	private static void printGameInfo() {
		for (int i = 0; i < currPlayers.getPlayers().size() - 1; i++) {
			System.out.println(currPlayers.getPlayers().get(i));
		}

		System.out.println(currPlayers.getPlayers().get(currPlayers.getPlayers().size() - 1).getName()
				+ ": ["+currPlayers.getPlayers().get(currPlayers.getPlayers().size() - 1).getHand().get(0)+", ??]\n");
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
			currPlayer.setCardValue(0);
			bj.endGame();
			return true;
		}
		else {
			int value = bj.countHandForValue(currPlayer.getHand());
			currPlayer.setCardValue(value);
			//TODO: Implement soft 17 feature
			boolean hit = false;
			if (value == 17) {
				if (hitOn17 == 's') {
					for (Card e: currPlayer.getHand()){
						if (e.getRank() == 1) {
							hit = true;
							break;
						}
					}
				}
			}
			if (value >= 17 && !hit) {
				System.out.println(currPlayer.getName()+" has hit to the appropriate amount.\n");

				//TODO: would use isBust() but takes more lines
				if (value > 21) {
					System.out.println(currPlayer.getName()+" has bust!\n");
					currPlayer.setCardValue(0);
					currPlayer.makeBust(true);
				}

				// check all active player hands and end game
				bj.endGame();
				return true;
			}
			else {
				bj.hit(currPlayer);
				System.out.println(currPlayer.getName()+" hit!\n");
				currPlayer = bj.changeTurn();
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
				pw.println(p.getName()+" "+p.getType()+" "+p.getFunds()+" "+p.getWins()+" "
						+p.getLosses()+" "+p.getDraws()+" "+p.getMoneyEarned()+" "+p.getMoneyLost());
			}

		} catch (FileNotFoundException g) {
			g.printStackTrace();
			System.exit(-1);
		} finally {
			pw.close();
		}		
	}

	private static void updateSettings() {
		String writeTo = "Settings.txt";
		File f = new File(writeTo);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(f);
			pw.println(numDecks);
			pw.println(minBet);
			pw.println(maxBet);
			pw.println(hitOn17);
		} catch (FileNotFoundException g) {
			g.printStackTrace();
			System.exit(-1);
		} finally {
			pw.close();
		}		
	}
}