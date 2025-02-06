package mg.itu.crypto.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import mg.itu.crypto.models.PortefeuilleFiat;
import mg.itu.crypto.models.Utilisateur;

public interface PortefeuilleFiatRepository extends JpaRepository<PortefeuilleFiat, Long> {
    Optional<PortefeuilleFiat[]> findByUtilisateur(Utilisateur utilisateur);

    // La méthode save est héritée de JpaRepository
    // PortefeuilleFiat save(PortefeuilleFiat portefeuilleFiat);

    Optional<PortefeuilleFiat> findByUtilisateurAndQuantiteGreaterThanEqual(Utilisateur utilisateur, BigDecimal montant);

    // @Query("SELECT p FROM portemonnaiefiat p ORDER BY p.id DESC")
    // List<PortefeuilleFiat> findLastPortefeuille();

    // @Query("SELECT p FROM portemonnaiefiat p WHERE p.utilisateur.id = :utilisateurId ORDER BY p.id DESC")
    // Optional<PortefeuilleFiat> findLastPortefeuilleByUtilisateur(Long utilisateurId);

    @Query("SELECT p FROM PortefeuilleFiat p WHERE p.utilisateur.id = :utilisateurId ORDER BY p.id DESC")
    List<PortefeuilleFiat> findLastPortefeuilleByUtilisateur(Long utilisateurId, PageRequest pageable);

}
