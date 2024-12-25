package mg.itu.crypto.repositories;

import mg.itu.crypto.models.Cryptomonnaie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptomonnaieRepository extends JpaRepository<Cryptomonnaie, Long> {
}