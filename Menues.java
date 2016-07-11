package cardGames;
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
			System.out.println("\nHere's a list of players you have selected for the next game.\n");
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

			System.out.println("Enter the number of the player you want to add\n");
			int name = in.nextInt();
			Player newPlayer = db.findPlayer(name-1);

			if (newPlayer != null) {
				curr.addPlayer(newPlayer);
			}

			else {
				System.out.println("Enter a valid number\n");
			}
			System.out.println("Continue adding players? (Press q to quit, anything to continue creating players");
			in.nextLine();
			command = in.nextLine().charAt(0);	

		} while (command != 'q');

		return curr;
	}
}