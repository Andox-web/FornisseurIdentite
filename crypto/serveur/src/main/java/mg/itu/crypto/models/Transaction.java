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

    @ManyToOne
    @JoinColumn(name = "annonceventeid")
    private AnnonceVente annonceVente;

    @ManyToOne
    @JoinColumn(name = "vendeurid")
    private Utilisateur vendeur;

    @ManyToOne
    @JoinColumn(name = "acheteurid")
    private Utilisateur acheteur;

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

    // Getters and setters
}
