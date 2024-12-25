package mg.itu.crypto.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "portemonnaiefiat")
public class PortefeuilleFiat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateurid", nullable = false)
    private Utilisateur utilisateur;

    @Column(precision = 20, scale = 8)
    private BigDecimal quantite = BigDecimal.ZERO;

    // Getters and setters
}
