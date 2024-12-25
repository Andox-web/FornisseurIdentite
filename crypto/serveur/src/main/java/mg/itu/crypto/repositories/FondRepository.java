package mg.itu.crypto.repositories;

import mg.itu.crypto.models.Fond;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FondRepository extends JpaRepository<Fond, Long> {
}