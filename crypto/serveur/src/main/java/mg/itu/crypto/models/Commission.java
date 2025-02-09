package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal achatCommission;
    private BigDecimal venteCommission;

    @ManyToOne
    @JoinColumn(name = "crypto_id")
    private Cryptomonnaie crypto;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAchatCommission() {
        return achatCommission;
    }

    public void setAchatCommission(BigDecimal achatCommission) {
        this.achatCommission = achatCommission;
    }

    public BigDecimal getVenteCommission() {
        return venteCommission;
    }

    public void setVenteCommission(BigDecimal venteCommission) {
        this.venteCommission = venteCommission;
    }

    public Cryptomonnaie getCrypto() {
        return crypto;
    }

    public void setCrypto(Cryptomonnaie crypto) {
        this.crypto = crypto;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
