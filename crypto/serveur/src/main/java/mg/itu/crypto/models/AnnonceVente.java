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

    // Getters and setters
}
