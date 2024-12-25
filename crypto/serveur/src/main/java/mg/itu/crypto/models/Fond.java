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

    // Getters and setters
}
