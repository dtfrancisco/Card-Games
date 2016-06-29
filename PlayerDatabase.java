package cardGames;

import java.util.ArrayList;
import java.util.List;

public class PlayerDatabase {
	private List <Player> players;
	
	public PlayerDatabase() {
		players = new ArrayList <Player> ();
	}
	
	public void addPlayer (Player p) {
		players.add(p);
	}
	
	public boolean removePlayer (Player p) {
		if (p == null) {
			return false;
		}
		Player delete = findPlayer(p.getName());
		if (delete != null) {
			players.remove(delete);
			return true;
		}
		return false;
	}
	
	public List <Player> getPlayers() {
		List <Player> curr = players;
		return curr;
	}
	
	public boolean isEmpty() {
		return players.isEmpty();
	}
	
	public Player findPlayer(String p) {
		for (int i = 0; i < players.size(); i++) {
			if (p.equals(players.get(i).getName())) {
				return players.get(i);
			}
		}
		return null;
	}
	
	public Player findPlayer(int index) {
		if (index < 0 || index > players.size()-1) {
			return null;
		}
		return players.get(index);
	}
}
