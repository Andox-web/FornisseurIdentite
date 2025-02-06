package mg.itu.crypto.repositories;

import mg.itu.crypto.models.AnnonceVente;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AnnonceVenteRepository extends JpaRepository<AnnonceVente, Long> {
    List<AnnonceVente> findByIsvendueFalse();

    Optional<AnnonceVente> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE AnnonceVente a SET a.isvendue = true WHERE a.id = :id")
    int marquerCommeVendu(Long id);

    @Query("SELECT a FROM AnnonceVente a WHERE a.cryptomonnaie.id = :cryptomonnaieId ORDER BY a.dateAnnonce DESC")
    Optional<AnnonceVente> findLastAnnonceVenteByCryptomonnaie(Long cryptomonnaieId);

}
