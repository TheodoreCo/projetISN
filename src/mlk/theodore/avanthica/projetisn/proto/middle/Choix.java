package mlk.theodore.avanthica.projetisn.proto.middle;

public class Choix {
	private int id; // identifiant
	private String description; // texte qui décrit le choix
	
	public Choix(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
