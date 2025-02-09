package mg.itu.crypto.repositories;

import mg.itu.crypto.models.AnnonceVente;
import mg.itu.crypto.models.ChangeCrypto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ChangeCryptoRepository extends JpaRepository<ChangeCrypto, Long> {

    Optional<ChangeCrypto> findById(Long id);

    @Query("SELECT c FROM ChangeCrypto c WHERE c.cryptomonnaie.id = :cryptomonnaieId ORDER BY c.datechangement DESC")
    List<ChangeCrypto> findChangeByCryptomonnaie(@Param("cryptomonnaieId") Long cryptomonnaieId);

    default Optional<ChangeCrypto> findLatestChangeByCryptomonnaie(Long cryptomonnaieId) {
        List<ChangeCrypto> changes = findChangeByCryptomonnaie(cryptomonnaieId);
        return changes.isEmpty() ? Optional.empty() : Optional.of(changes.get(0));
    }

    @Query("SELECT c FROM ChangeCrypto c WHERE c.datechangement BETWEEN :dateMin AND :dateMax")
    List<ChangeCrypto> findAllByDateBetween(@Param("dateMin") LocalDateTime dateMin, @Param("dateMax") LocalDateTime dateMax);
}