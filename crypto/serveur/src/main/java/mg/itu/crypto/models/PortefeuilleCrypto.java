package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "portemonnaiecrypto")
public class PortefeuilleCrypto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateurid", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "cryptomonnaieid")
    private Cryptomonnaie cryptomonnaie;

    @Column(precision = 20, scale = 8)
    private BigDecimal quantite = BigDecimal.ZERO;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
}
