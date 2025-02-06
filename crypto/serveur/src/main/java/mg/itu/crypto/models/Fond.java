package mg.itu.crypto.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fond")
public class Fond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateurid")
    private Utilisateur utilisateur;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal valeur;

    @ManyToOne
    @JoinColumn(name = "typefond")
    private TypeFond typeFond;

    private Boolean istransaction = false;

    private Boolean isvalid = false;

    private String codefond;

    private LocalDateTime expireat;

    private LocalDateTime datefond = LocalDateTime.now();

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

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public TypeFond getTypeFond() {
        return typeFond;
    }

    public void setTypeFond(TypeFond typeFond) {
        this.typeFond = typeFond;
    }

    public Boolean getIstransaction() {
        return istransaction;
    }

    public void setIstransaction(Boolean istransaction) {
        this.istransaction = istransaction;
    }

    public Boolean getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Boolean isvalid) {
        this.isvalid = isvalid;
    }

    public String getCodefond() {
        return codefond;
    }

    public void setCodefond(String codefond) {
        this.codefond = codefond;
    }

    public LocalDateTime getExpireat() {
        return expireat;
    }

    public void setExpireat(LocalDateTime expireat) {
        this.expireat = expireat;
    }

    public LocalDateTime getDatefond() {
        return datefond;
    }

    public void setDatefond(LocalDateTime datefond) {
        this.datefond = datefond;
    }
}
