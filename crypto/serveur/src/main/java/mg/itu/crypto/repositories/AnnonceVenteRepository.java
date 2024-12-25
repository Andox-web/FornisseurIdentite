package mg.itu.crypto.repositories;

import mg.itu.crypto.models.AnnonceVente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnonceVenteRepository extends JpaRepository<AnnonceVente, Long> {
}
