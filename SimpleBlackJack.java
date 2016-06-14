package cardGames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SimpleBlackJack {

	public static List <Player> players = new ArrayList <Player> ();
	public static Player curr = null;
	private static BlackjackStack bj = null;
	public static Scanner in = null;

	public static void main(String[] args) {
		mainMenu();
		updateData();
		in.close();
	}

	private static void mainMenu() {
		boolean exit = false;
		in = new Scanner(System.in);

		while (!exit) {
			System.out.println("\ns = set up game\nb = begin game\nd = display stats\nq = quit");
			char command = in.next().charAt(0);
			in.nextLine();
			switch (command) {
			case 's': setUp(); break;
			case 'b': startGame(); break;
			case 'd': displayStats(); break;
			case 'q': exit = true; break;
			}
		}
	}

	private static void setUp() {
		System.out.println("\n1 = create new players\n2 = load preexisting players from database\n3 = modify player "
				+ "type \n4 = create deck");
		int command = in.nextInt();
		if (command == 1) {
			boolean exit = false;
			while (!exit) {
				System.out.print("Create a player name: ");
				in.nextLine();
				String name = in.nextLine();
				char type = ' ';
				while (type != 'p' && type != 'h') {
					System.out.print("\nSet type for "+name+" (p or h): ");
					type = in.next().charAt(0);
				}
				System.out.print("\nChoose starting funds for "+name+": ");
				int funds = in.nextInt();
				Player newPlayer = new Player(name, type, funds);
				players.add(newPlayer);
				System.out.print("Finished creating players? (y or n): ");
				char quit = in.next().charAt(0);
				if (quit == 'y') {
					exit = true;
				}
			}
		}
		else if (command == 2) {
			String fileName = "BlackjackPlayerDB.txt";
			File f = new File(fileName);
			Scanner fileReader = null;
			try {
				fileReader = new Scanner(f);
				fileReader.nextLine(); // TODO
				while (fileReader.hasNext()) {
					String name = fileReader.next();
					char type = fileReader.next().charAt(0);
					int funds = fileReader.nextInt();
					Player newPlayer = new Player(name, type, funds);
					players.add(newPlayer);
					System.out.println(newPlayer);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} finally {
				fileReader.close();
			}		
		}
		else if (command == 3) {

		}
		else if (command == 4) {
			DeckADT <Card> deck = new DeckStack <Card> ();
			System.out.print("Select the number of decks you want to play with: ");
			int numDecks = in.nextInt();
			for (int i = 0; i < numDecks; i++) {
				deck.insert52();
			}
			bj = new BlackjackStack(deck, players);
			curr = bj.startGame();
		}
		else {
			setUp();
		}
	}

	private static void displayStats() {

	}

	private static void updateData() {
		String readFrom = "BlackjackPlayerDB.txt";
		String writeTo = "BlackjackPlayerResults.txt";
		File e = new File(readFrom);
		File f = new File(writeTo);
		Scanner fileReader = null;
		PrintWriter pw = null;
		try {
			fileReader = new Scanner(e);
			pw = new PrintWriter(f);

			//TODO : Algorithm for this 1. read file for each player, 2. find player, 3. change line
			while (fileReader.hasNext()) {
				for (Player p: players) {
					String name = fileReader.next();
					if (name.equals(p.getName())) {
						System.out.println("yes");
						pw.println(p.getName()+" "+p.getType()+" "+p.getFunds());
					}
					else {
						
					}
					String h = fileReader.nextLine();
					System.out.println(h);
				}
			}
		} catch (FileNotFoundException g) {
			g.printStackTrace();
			System.exit(-1);
		} finally {
			fileReader.close();
			pw.close();
		}		
	}

	private static void startGame() {
		simulateGame();
		System.out.print("Want to play again? (Press y)");
		char command = in.next().charAt(0);
		//in.nextLine()
		System.out.println();
		if (command == 'y') {
			startGame();
		}
	}

	private static void simulateGame() {
		boolean exit = false;
		boolean bust = false;
		boolean hasHit = false;
		//Ability to see first of house's cards 
		System.out.println(curr.getName()+""
				+ ": ["+curr.getHand().get(0)+", ??]");
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
		//reset busts and card values and bets once done
		for (Player p: players) {
			p.getHand().clear(); // remove cards from player
			p.setCurrBet(0);
			p.setCardValue(0);
			p.makeBust(false);
			p.makeDoubleDowned(false);
			p.makeWin(false);
		}
	}

	private static boolean endTurn() {
		boolean bustedPlayers = true;
		for (Player p: players) {
			if (!p.hasBust() && p.getType() == 'p') {
				bustedPlayers = false;
			}
		}
		// if all players have busted
		if (bustedPlayers){
			int value = bj.countHandForValue(curr.getHand());
			curr.setCardValue(value);
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
					System.out.println(curr.getName()+"has bust!\n");
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
}
