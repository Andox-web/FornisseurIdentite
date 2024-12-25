package mg.itu.crypto.repositories;

import mg.itu.crypto.models.PortefeuilleFiat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortefeuilleFiatRepository extends JpaRepository<PortefeuilleFiat, Long> {
}
