package cardGames;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Menues {

	public static Scanner in = new Scanner(System.in);

	public static void changeDatabase(PlayerDatabase db) {
		boolean end = false;
		do {

			System.out.println("\n1 = add players to database\n2 = remove players from database\n3= "
					+ " modify player type\n4 = return to main menu");
			int response = in.nextInt();
			switch(response) {
			case 1:
				boolean exit = false;
				in.nextLine();

				while (!exit) {

					System.out.print("Create a player name: ");
					String name = in.nextLine();

					// no duplicate names are allowed
					if (db.findPlayer(name) != null) {
						System.out.println("\n"+name+" already exists in the player database. Choose a different name.\n");
					}

					else {

						char type = ' ';

						while (type != 'p' && type != 'h') {
							System.out.print("\nSet type for "+name+" (p or h): ");
							type = in.next().charAt(0);
						}
						System.out.print("\nChoose starting funds for "+name+": ");
						int funds = in.nextInt();
						Player newPlayer = new Player(name, type, funds);
						db.addPlayer(newPlayer);
						System.out.print("\nFinished creating players? (y or n): ");
						char quit = in.next().charAt(0);
						System.out.println();

						if (quit == 'y') {
							exit = true;
						}
					}
				}
				break;
			case 2:
				boolean quit = false;
				while (!quit) {
					System.out.print("Name player to remove: ");
					System.out.println();
					in.nextLine();
					String name = in.nextLine();
					Player found = db.findPlayer(name);
					boolean success = db.removePlayer(found);
					if (success) {
						System.out.println(found.getName()+" was successfully removed from the database.\n");
					}
					else {
						System.out.println("Error. Player was not found.\n");
					}
					System.out.print("Finished removing players? (y or n): ");
					char q = in.next().charAt(0);
					System.out.println();

					if (q == 'y') {
						quit = true;
					}
				}
				break;
			case 3:
				System.out.print("Name player to modify: ");
				System.out.println();
				in.nextLine();
				String name = in.nextLine();
				Player found = db.findPlayer(name);
				if (found != null) {
					System.out.print(found.getName()+"'s type is: "+found.getType()+" .Press y to change to ");
					if (found.getType() == 'p') {
						System.out.println("h (house)\n");
					}
					else {
						System.out.println("p (player)\n");
					}
					char command = in.next().charAt(0);
					if (command == 'y') {
						if (found.getType() == 'p') {
							found.setType('h');
						}
						else {
							found.setType('p');
						}
					}
				}
				else {
					System.out.println("Error. Player was not found.\n");
				}
				System.out.print("Finished removing players? (y or n): ");
				System.out.println();
				char q = in.next().charAt(0);
				if (q == 'y') {
					quit = true;
				}
				break;
			case 4:
				end = true;
				break;
			}
		} while (!end);
	}

	public static PlayerDatabase setCurrPlayers(PlayerDatabase db) {
		PlayerDatabase curr = new PlayerDatabase ();
		char command = ' ';

		do {
			int counter = 1;
			System.out.println("\nHere's a list of players you have selected for the next game.");
			for (Player p: curr.getPlayers()) {
				System.out.println(counter+": Name: "+p.getName()+" Type: "+p.getType());
				counter++;
			}

			System.out.println("\nHere's a list of players you may choose for the next game.\n");
			counter = 1;

			for (Player p: db.getPlayers()) {
				System.out.println(counter+": Name: "+p.getName()+" Type: "+p.getType());
				counter++;
			}
			System.out.println();

			System.out.print("Enter the number of the player you want to add: ");
			int name = in.nextInt();
			System.out.println("\n");
			Player newPlayer = db.findPlayer(name-1);

			if (newPlayer != null) {
				curr.addPlayer(newPlayer);
			}

			else {
				System.out.println("Enter a valid number\n");
			}
			System.out.print("Continue adding players? (Press q to quit, \nanything to continue creating players");
			in.nextLine();
			command = in.nextLine().charAt(0);
			System.out.println("\n");

		} while (command != 'q');

		return curr;
	}

	public static void displayStats(PlayerDatabase db) {
		boolean exit = false;
		while (!exit) {
			System.out.println("\n1 = show all stats\n2 = show all players\n3= show all 'p'\n4= show all 'h'\n5 = rank"
					+ " funds\n6 = rank wins\n7 = rank losses\n8 = rank draws\n9 = "
					+ "rank money earned\n10 = rank money lost\n11 = quit submenu");
			int response = in.nextInt();
			switch (response) {
			case 1: 
				for (Player p: db.getPlayers()) {
					System.out.println("\nName: "+p.getName()+"; Type: "+p.getType()
					+"; Funds: "+p.getFunds()+"; Wins: "+p.getWins()+"; Losses: "+p.getLosses()
					+"; Draws: "+p.getDraws()+";\nMoney Earned: "+p.getMoneyEarned()
					+"; Money Lost: "+p.getMoneyLost());
				}
				break;
			case 2: 
				for (int i = 0; i < db.getPlayers().size(); i++) {
					System.out.println(db.getPlayers().get(i).getName());
				} 
				break;
			case 3: 
				System.out.println("Players of type 'p' include: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					if (db.getPlayers().get(i).getType() == 'p') {
						System.out.println(db.getPlayers().get(i).getName());
					}
				}
				break;
			case 4: 
				System.out.println("Players of type 'h' include: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					if (db.getPlayers().get(i).getType() == 'h') {
						System.out.println(db.getPlayers().get(i).getName());
					}
				}
				break;
			case 5: 
				List <Player> rankedFunds = new ArrayList <Player> ();
				System.out.println("Players who have the most funds from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedFunds.isEmpty()) {
						rankedFunds.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedFunds.size(); j++) {
							Player curr = rankedFunds.get(j);
							if (player.getFunds() < curr.getFunds()) {
								enter = true;
								rankedFunds.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedFunds.add(player);
						}
					}
				}
				for (int i = rankedFunds.size()-1; i >= 0; i--) {
					Player current = rankedFunds.get(i);
					System.out.println(current.getName()+": "+current.getFunds());
				}
				break;
			case 6: 
				List <Player> rankedWins = new ArrayList <Player> ();
				System.out.println("Players who have the most wins from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedWins.isEmpty()) {
						rankedWins.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedWins.size(); j++) {
							Player curr = rankedWins.get(j);
							if (player.getWins() < curr.getWins()) {
								enter = true;
								rankedWins.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedWins.add(player);
						}
					}
				}
				for (int i = rankedWins.size()-1; i >= 0; i--) {
					Player curr = rankedWins.get(i);
					System.out.println(curr.getName()+": "+curr.getWins());
				}
				break;
			case 7: 
				List <Player> rankedLosses = new ArrayList <Player> ();
				System.out.println("Players who have the most losses from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedLosses.isEmpty()) {
						rankedLosses.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedLosses.size(); j++) {
							Player curr = rankedLosses.get(j);
							if (player.getLosses() < curr.getLosses()) {
								enter = true;
								rankedLosses.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedLosses.add(player);
						}
					}
				}
				for (int i = rankedLosses.size()-1; i >= 0; i--) {
					Player curr = rankedLosses.get(i);
					System.out.println(curr.getName()+": "+curr.getLosses());
				}
				break;
			case 8: 
				List <Player> rankedDraws = new ArrayList <Player> ();
				System.out.println("Players who have the most draws from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedDraws.isEmpty()) {
						rankedDraws.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedDraws.size(); j++) {
							Player curr = rankedDraws.get(j);
							if (player.getDraws() < curr.getDraws()) {
								enter = true;
								rankedDraws.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedDraws.add(player);
						}
					}
				}
				for (int i = rankedDraws.size()-1; i >= 0; i--) {
					Player curr = rankedDraws.get(i);
					System.out.println(curr.getName()+": "+curr.getDraws());
				}
				break;
				
			case 9: 
				List <Player> rankedMGained = new ArrayList <Player> ();
				System.out.println("Players who have the most money earned from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedMGained.isEmpty()) {
						rankedMGained.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedMGained.size(); j++) {
							Player curr = rankedMGained.get(j);
							if (player.getMoneyEarned() < curr.getMoneyEarned()) {
								enter = true;
								rankedMGained.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedMGained.add(player);
						}
					}
				}
				for (int i = rankedMGained.size()-1; i >= 0; i--) {
					Player curr = rankedMGained.get(i);
					System.out.println(curr.getName()+": "+curr.getMoneyEarned());
				}
				break;
			case 10: 
				List <Player> rankedMLost = new ArrayList <Player> ();
				System.out.println("Players who have the most money lost from greatest to least are: \n");
				for (int i = 0; i < db.getPlayers().size(); i++) {
					Player player = db.getPlayers().get(i);
					if (rankedMLost.isEmpty()) {
						rankedMLost.add(player);
					}

					else {
						boolean enter = false;
						for (int j = 0; j < rankedMLost.size(); j++) {
							Player curr = rankedMLost.get(j);
							if (player.getMoneyLost() < curr.getMoneyLost()) {
								enter = true;
								rankedMLost.add(j, player);
								break;
							}
						}
						if (!enter) {
							rankedMLost.add(player);
						}
					}
				}
				for (int i = rankedMLost.size()-1; i >= 0; i--) {
					Player curr = rankedMLost.get(i);
					System.out.println(curr.getName()+": "+curr.getMoneyLost());
				}
				break;
			case 11:
				exit = true;
				break;
			default: 
				System.out.println("A valid number was not entered");
				break;
			}
		}
	}
}