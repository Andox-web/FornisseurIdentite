package mg.itu.crypto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mg.itu.crypto.models.Utilisateur;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

//  Optional<Utilisateur> findByCodecreation(String codecreation);

    @Query(value = """
        SELECT u.* FROM utilisateur u
        JOIN session s ON s.utilisateurid = u.id
        WHERE s.token = :token
        """, nativeQuery = true)
    Optional<Utilisateur> findBySessionToken(@Param("token") String token);


}