package mg.itu.crypto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mg.itu.crypto.models.Fond;

public interface FondRepository extends JpaRepository<Fond, Long> {
    Optional<Fond> findByCodefond(String codeFond);

    @Query(value="SELECT f FROM Fond f WHERE f.utilisateurid = :id ORDER BY f.id DESC LIMIT 1", nativeQuery = true)
    Fond findLastByUtilisateurId(@Param("id") Long id);
}