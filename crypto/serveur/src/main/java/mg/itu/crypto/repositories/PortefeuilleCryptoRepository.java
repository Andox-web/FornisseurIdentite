package mg.itu.crypto.repositories;
import mg.itu.crypto.models.Cryptomonnaie;
import mg.itu.crypto.models.PortefeuilleCrypto;
import mg.itu.crypto.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface PortefeuilleCryptoRepository extends JpaRepository<PortefeuilleCrypto, Long> {
    Optional<PortefeuilleCrypto[]> findByUtilisateur(Utilisateur utilisateur);

    Optional<PortefeuilleCrypto> findByUtilisateurAndCryptomonnaie(Utilisateur utilisateur, Cryptomonnaie cryptomonnaie);
    
    @Query("SELECT p FROM PortefeuilleCrypto p ORDER BY p.id DESC LIMIT 1")
    List<PortefeuilleCrypto> findLastPortefeuille();
    

}
