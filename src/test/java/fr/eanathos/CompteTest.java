package fr.eanathos;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CompteTest {

    private Compte compte;
    private Titulaire titulaire;

    private static final String NOM = "John";
    private static final String PRENOM = "Doe";
    private static final String ADRESSE = "3 rue de la Paix, Paris";
    private static final int NUMERO_COMPTE = 12345;
    private static final BigDecimal SOLDE = new BigDecimal(1000);
    private static final BigDecimal DECOUVERT_MAXIMAL = new BigDecimal(800);
    private static final BigDecimal DEBIT_MAXIMAL = new BigDecimal(1000);

    @BeforeAll
    public static void setUpAll() {
        // Méthode exécutée une fois avant tous les tests
    }

    @AfterAll
    public static void tearDownAll() {
        // Méthode exécutée une fois après tous les tests
    }

    @BeforeEach
    public void setUp() {
        titulaire = new Titulaire(NOM, PRENOM, ADRESSE);
        compte = new Compte(NUMERO_COMPTE, titulaire, SOLDE, DECOUVERT_MAXIMAL, DEBIT_MAXIMAL);
    }

    @AfterEach
    public void tearDown() {
        // Méthode exécutée après chaque test
    }

    @DisplayName("Test de la méthode getNumeroCompte")
    @Test
    void getNumeroCompte() {
        // Given
        int numeroCompteAttendu = NUMERO_COMPTE;

        // When
        int numeroCompteObserve = compte.getNumeroCompte();

        // Then
        assertEquals(numeroCompteAttendu, numeroCompteObserve);
    }

    @DisplayName("Test de la méthode getTitulaire")
    @Test
    void getTitulaire() {
        // Given
        Titulaire titulaireAttendu = titulaire;

        // When
        Titulaire titulaireObserve = compte.getTitulaire();

        // Then
        assertEquals(titulaireAttendu, titulaireObserve);
    }

    @DisplayName("Test de la méthode getSolde")
    @Test
    void getSolde() {
        // Given
        BigDecimal soldeAttendu = SOLDE;

        // When
        BigDecimal soldeObserve = compte.getSolde();

        // Then
        assertEquals(soldeAttendu, soldeObserve);
    }

    @DisplayName("Test de la méthode getDecouvertMaximal")
    @Test
    void getDecouvertMaximal() {
        // Given
        BigDecimal decouvertMaximalAttendu = DECOUVERT_MAXIMAL;

        // When
        BigDecimal decouvertMaximalObserve = compte.getDecouvertMaximal();

        // Then
        assertEquals(decouvertMaximalAttendu, decouvertMaximalObserve);
    }

    @DisplayName("Test de la méthode setDecouvertMaximal")
    @Test
    void setDecouvertMaximal() {
        // Given
        BigDecimal nouveauDecouvertMaximalAttendu = new BigDecimal(1200);

        // When
        compte.setDecouvertMaximal(nouveauDecouvertMaximalAttendu);

        // Then
        assertEquals(nouveauDecouvertMaximalAttendu, compte.getDecouvertMaximal());
    }

    @DisplayName("Test de la méthode getDebitMaximal")
    @Test
    void getDebitMaximal() {
        // Given
        BigDecimal debitMaximalAttendu = DEBIT_MAXIMAL;

        // When
        BigDecimal debitMaximalObserve = compte.getDebitMaximal();

        // Then
        assertEquals(debitMaximalAttendu, debitMaximalObserve);
    }

    @DisplayName("Test de la méthode estADecouvert")
    @Test
    void estADecouvert() {
        // Given
        boolean estADecouvertAvant = compte.estADecouvert();

        // Débit fait en deux fois pour éviter l'erreur de débit maximal autorisé
        BigDecimal premierDebit = SOLDE.divide(new BigDecimal(2));
        BigDecimal secondDebit = premierDebit.add(new BigDecimal(1));

        // When
        compte.debiter(premierDebit);
        compte.debiter(secondDebit);
        boolean estADecouvertApres = compte.estADecouvert();

        // Then
        assertFalse(estADecouvertAvant, "Le compte ne doit pas être à découvert initialement.");
        assertTrue(estADecouvertApres, "Le compte doit être à découvert après avoir dépassé le solde.");
    }

    @DisplayName("Test de la méthode crediter avec un montant positif")
    @Test
    void crediterAvecMontantPositif() {
        // Given
        BigDecimal creditPositifDe500 = new BigDecimal(500);
        BigDecimal soldeAttendu = SOLDE.add(creditPositifDe500);

        // When
        compte.crediter(creditPositifDe500);

        // Then
        assertEquals(soldeAttendu, compte.getSolde());
    }

    @DisplayName("Test de la méthode crediter avec un montant négatif")
    @Test
    void crediterMontantNegatif() {
        // Given
        BigDecimal creditNegatifDe500 = new BigDecimal(-500);
        String messageAttendu = "Le montant à créditer doit être positif.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.crediter(creditNegatifDe500), messageAttendu);
    }

    @DisplayName("Test de la méthode debiter avec un montant positif")
    @Test
    void debiterMontantPositif() {
        // Given
        BigDecimal debitPositifDe500 = new BigDecimal(500);
        BigDecimal soldeAttendu = SOLDE.subtract(debitPositifDe500);

        // When
        boolean debitEffectue = compte.debiter(debitPositifDe500);

        // Then
        assertTrue(debitEffectue);
        assertEquals(soldeAttendu, compte.getSolde());
    }

    @DisplayName("Test de la méthode debiter avec un montant négatif")
    @Test
    void debiterMontantNegatif() {
        // Given
        BigDecimal debitNegatifDe500 = new BigDecimal(-500);
        String messageAttendu = "Le montant à débiter doit être positif.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.debiter(debitNegatifDe500), messageAttendu);
    }

    @DisplayName("Test de la méthode debiter avec un montant supérieur au débit maximal autorisé")
    @Test
    void debiterMontantSuperieurADebitMaximal() {
        // Given
        BigDecimal debitSuperieurADebitMaximal = DEBIT_MAXIMAL.add(new BigDecimal(1));
        String messageAttendu = "Le montant dépasse le débit maximal autorisé.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.debiter(debitSuperieurADebitMaximal), messageAttendu);
    }

    @DisplayName("Test de la méthode effectuerVirement avec un montant positif valide")
    @Test
    void effectuerVirementMontantPositif() {
        // Given
        Compte autreCompte = new Compte(54321, titulaire, new BigDecimal(500), DECOUVERT_MAXIMAL, DEBIT_MAXIMAL);
        BigDecimal montantDuVirement = new BigDecimal(300);
        BigDecimal soldeAttendu = SOLDE.subtract(montantDuVirement);
        BigDecimal soldeAutreCompteAttendu = autreCompte.getSolde().add(montantDuVirement);

        // When
        boolean virementEffectue = compte.effectuerVirement(autreCompte, montantDuVirement);

        // Then
        assertTrue(virementEffectue);
        assertEquals(soldeAttendu, compte.getSolde());
        assertEquals(soldeAutreCompteAttendu, autreCompte.getSolde());
    }

    @DisplayName("Test de la méthode effectuerVirement avec un montant négatif")
    @Test
    void effectuerVirementMontantNegatif() {
        // Given
        Compte autreCompte = new Compte(54321, titulaire, new BigDecimal(500), DECOUVERT_MAXIMAL, DEBIT_MAXIMAL);
        BigDecimal montantDuVirement = new BigDecimal(-300);
        String messageAttendu = "Le montant du virement doit être positif.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.effectuerVirement(autreCompte, montantDuVirement), messageAttendu);
    }

    @DisplayName("Test de la méthode effectuerVirement avec un montant supérieur au débit maximal autorisé")
    @Test
    void effectuerVirementMontantSuperieurADebitMaximal() {
        // Given
        Compte autreCompte = new Compte(54321, titulaire, new BigDecimal(500), DECOUVERT_MAXIMAL, DEBIT_MAXIMAL);
        BigDecimal montantDuVirement = DEBIT_MAXIMAL.add(new BigDecimal(1));
        String messageAttendu = "Le montant dépasse le débit maximal autorisé.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.effectuerVirement(autreCompte, montantDuVirement), messageAttendu);
    }

    @DisplayName("Test de la méthode effectuerVirement avec un montant supérieur au solde")
    @Test
    void effectuerVirementMontantSuperieurAuSolde() {
        // Given
        Compte autreCompte = new Compte(54321, titulaire, new BigDecimal(500), DECOUVERT_MAXIMAL, DEBIT_MAXIMAL);
        BigDecimal montantDuVirement = SOLDE.add(new BigDecimal(1));
        String messageAttendu = "Le montant dépasse le débit maximal autorisé.";

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> compte.effectuerVirement(autreCompte, montantDuVirement), messageAttendu);
    }

    @DisplayName("Test de la méthode getDebitAutorise")
    @Test
    void getDebitAutorise() {
        // Given
        BigDecimal debitAutoriseAttendu = DEBIT_MAXIMAL.add(DECOUVERT_MAXIMAL);

        // When
        BigDecimal debitAutoriseObserve = compte.getDebitAutorise();

        // Then
        assertEquals(debitAutoriseAttendu, debitAutoriseObserve);
    }

    @DisplayName("Test de la méthode toString")
    @Test
    void testToString() {
        // Given
        String compteAttendu = "Compte n° " + NUMERO_COMPTE + " | " + titulaire.toString() + " | Solde: " + SOLDE + "€ | Découvert max: " + DECOUVERT_MAXIMAL + "€ | Débit max: " + DEBIT_MAXIMAL + "€";

        // When
        String compteObserve = compte.toString();

        // Then
        assertEquals(compteAttendu, compteObserve);
    }
}
