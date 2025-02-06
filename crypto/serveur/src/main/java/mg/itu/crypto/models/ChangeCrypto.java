package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "changecrypto")
public class ChangeCrypto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cryptomonnaieid", nullable = false)
    private Cryptomonnaie cryptomonnaie;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal valeur;

    @Column(nullable = false)
    private LocalDateTime datechangement = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cryptomonnaie getCryptomonnaie() {
        return cryptomonnaie;
    }

    public void setCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        this.cryptomonnaie = cryptomonnaie;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public LocalDateTime getDatechangement() {
        return datechangement;
    }

    public void setDatechangement(LocalDateTime datechangement) {
        this.datechangement = datechangement;
    }
}
