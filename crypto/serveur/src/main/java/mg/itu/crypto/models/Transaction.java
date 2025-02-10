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

    private String typeTransaction;

    @ManyToOne
    @JoinColumn(name = "utilisateurid", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "retraitid", nullable = true)
    private Fond retrait;

    @ManyToOne
    @JoinColumn(name = "depotid", nullable = true)
    private Fond depot;

    @ManyToOne
    @JoinColumn(name = "cryptomonnaieid", nullable = false)
    private Cryptomonnaie cryptomonnaie;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantitecrypto;

    @Column(nullable = false)
    private Boolean isConfirmed = false;

    @Column(nullable = false)
    private Boolean isConfirmedAdmin = false;

    @Column(nullable = false)
    private LocalDateTime dateTransaction = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

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

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }
}
