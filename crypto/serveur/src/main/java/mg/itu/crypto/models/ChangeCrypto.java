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

    // Getters and setters
}