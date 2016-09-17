package cardGames;

public class UnoCard {
	private int color;
	private int rank;

	public UnoCard(int color, int rank) {
				
		if (color < 1 || color > 5) {
			throw new IllegalArgumentException();
		}
		
		if (rank < 0 || rank > 14) {
			throw new IllegalArgumentException();
		}
		
		this.color = color;
		this.rank = rank;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor (int color) {
		this.color = color;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String toString() {
		String str = "";
		switch(color) {
		case 1: str += "red "; break;
		case 2: str += "yellow "; break;
		case 3: str += "green "; break;
		case 4: str += "blue "; break;
		case 5: str += "wild"; break;
		case 6: str += "wild red"; break;
		case 7: str += "wild yellow"; break;
		case 8: str += "wild green"; break;
		case 9: str += "wild blue"; break;

		}
		
		switch(rank) {
		case 10: str += "skip"; break;
		case 11: str += "reverse"; break;
		case 12: str += "draw 2"; break;
		case 13: break;
		case 14: str += " draw 4"; break;
		default: str += rank; break;
		}
		return str;
	}
}
