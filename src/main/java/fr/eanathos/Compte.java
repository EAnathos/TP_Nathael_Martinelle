package fr.eanathos;

import java.math.BigDecimal;

public class Compte {
    private final int numeroCompte;
    private final Titulaire titulaire;
    private BigDecimal solde;
    private BigDecimal decouvertMaximal;
    private final BigDecimal debitMaximal;

    public Compte(int numeroCompte, Titulaire titulaire, BigDecimal solde, BigDecimal decouvertMaximal, BigDecimal debitMaximal) {
        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.solde = solde != null ? solde : BigDecimal.ZERO;
        this.decouvertMaximal = decouvertMaximal != null ? decouvertMaximal : new BigDecimal("800");
        this.debitMaximal = debitMaximal != null ? debitMaximal : new BigDecimal("1000");
    }

    public int getNumeroCompte() {
        return numeroCompte;
    }

    public Titulaire getTitulaire() {
        return titulaire;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public BigDecimal getDecouvertMaximal() {
        return decouvertMaximal;
    }

    public void setDecouvertMaximal(BigDecimal decouvertMaximal) {
        this.decouvertMaximal = decouvertMaximal;
    }

    public BigDecimal getDebitMaximal() {
        return debitMaximal;
    }

    public boolean estADecouvert() {
        return solde.compareTo(BigDecimal.ZERO) < 0;
    }

    public void crediter(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant à créditer doit être positif.");
        }
        solde = solde.add(montant);
    }

    public boolean debiter(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant à débiter doit être positif.");
        }
        if (montant.compareTo(debitMaximal) > 0) {
            throw new IllegalArgumentException("Le montant dépasse le débit maximal autorisé.");
        }
        if (solde.subtract(montant).compareTo(decouvertMaximal.negate()) >= 0) {
            solde = solde.subtract(montant);
            return true;
        }
        return false;
    }

    public boolean effectuerVirement(Compte autreCompte, BigDecimal montant) {
        if (this.debiter(montant)) {
            autreCompte.crediter(montant);
            return true;
        }
        return false;
    }

    public BigDecimal getDebitAutorise() {
        return debitMaximal.add(decouvertMaximal);
    }

    @Override
    public String toString() {
        return "Compte n° " + numeroCompte + " | " + titulaire.toString() + " | Solde: " + solde + "€ | Découvert max: " + decouvertMaximal + "€ | Débit max: " + debitMaximal + "€";
    }
}
