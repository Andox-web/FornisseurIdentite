package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "annoncevente")
public class AnnonceVente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendeurid")
    private Utilisateur vendeur;

    @ManyToOne
    @JoinColumn(name = "cryptomonnaieid")
    private Cryptomonnaie cryptomonnaie;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantite;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal prix;

    private Boolean isvendue = false;

    private LocalDateTime dateAnnonce = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Boolean getIsvendue() {
        return isvendue;
    }

    public void setIsvendue(Boolean isvendue) {
        this.isvendue = isvendue;
    }

    public LocalDateTime getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(LocalDateTime dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }
}
