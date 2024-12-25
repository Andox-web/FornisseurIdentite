package mg.itu.crypto.models;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motdepasse;

    private LocalDateTime datecreation;

    @Column(unique = true, nullable = false)
    private String codecreation;

    // Getters and setters
}
