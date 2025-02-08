package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "typeTransaction", nullable = false)
    private String typeTransaction;

    @ManyToOne
    @JoinColumn(name = "utilisateurid")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "retraitid")
    private Fond retrait;

    @ManyToOne
    @JoinColumn(name = "depotid")
    private Fond depot;

    @ManyToOne
    @JoinColumn(name = "cryptomonnaieid")
    private Cryptomonnaie cryptomonnaie;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantitecrypto;

    private LocalDateTime dateTransaction = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public AnnonceVente getAnnonceVente() {
    //     return annonceVente;
    // }

    // public void setAnnonceVente(AnnonceVente annonceVente) {
    //     this.annonceVente = annonceVente;
    // }

    // public Utilisateur getVendeur() {
    //     return vendeur;
    // }

    // public void setVendeur(Utilisateur vendeur) {
    //     this.vendeur = vendeur;
    // }

    // public Utilisateur getAcheteur() {
    //     return acheteur;
    // }

    // public void setAcheteur(Utilisateur acheteur) {
    //     this.acheteur = acheteur;
    // }


    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Fond getRetrait() {
        return retrait;
    }

    public void setRetrait(Fond retrait) {
        this.retrait = retrait;
    }

    public Fond getDepot() {
        return depot;
    }

    public void setDepot(Fond depot) {
        this.depot = depot;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }

    public BigDecimal getQuantitecrypto() {
        return quantitecrypto;
    }

    public void setQuantitecrypto(BigDecimal quantitecrypto) {
        this.quantitecrypto = quantitecrypto;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }
    
}
