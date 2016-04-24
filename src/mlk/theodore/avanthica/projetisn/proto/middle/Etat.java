package mlk.theodore.avanthica.projetisn.proto.middle;

public class Etat {
	private String description;
	private String nomFichierMusique;
	public Etat(String description, String nomFichierMusique) {
		this.description = description;
		this.nomFichierMusique = nomFichierMusique;
	}
	
	public String getDescription() {
		return description;
	}
	public String getNomFichierMusique() {
		return nomFichierMusique;
	}
}
