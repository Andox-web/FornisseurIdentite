package mg.itu.crypto.dto;

import java.math.BigDecimal;

public class AchatRequest {
    private Long annonceId;
    private BigDecimal quantite;

    // Getters et setters
    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }
}
