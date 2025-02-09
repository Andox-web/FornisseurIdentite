package mg.itu.crypto.repositories;

import mg.itu.crypto.models.PourcentageCommission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PourcentageCommissionRepository extends JpaRepository<PourcentageCommission, Long> {

    Optional<PourcentageCommission> findFirstByOrderByIdAsc();
}
