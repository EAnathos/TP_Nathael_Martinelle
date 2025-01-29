package fr.eanathos;

public record Titulaire(String nom, String prenom, String adresse) {

    @Override
    public String toString() {
        return "Titulaire: " + nom + " " + prenom + ", Adresse: " + adresse;
    }
}

