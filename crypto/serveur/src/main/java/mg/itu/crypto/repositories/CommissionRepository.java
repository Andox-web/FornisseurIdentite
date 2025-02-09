package mg.itu.crypto.repositories;

import mg.itu.crypto.models.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CommissionRepository extends JpaRepository<Commission, Long> {

    Optional<Commission> findFirstByOrderByIdAsc();

    @Query("SELECT SUM(c.achatCommission + c.venteCommission) FROM Commission c WHERE c.date BETWEEN :dateMin AND :dateMax AND (:selectedCrypto = 'tous' OR c.crypto.id = :selectedCrypto)")
    BigDecimal calculateSum(String selectedCrypto, LocalDateTime dateMin, LocalDateTime dateMax);

    @Query("SELECT AVG(c.achatCommission + c.venteCommission) FROM Commission c WHERE c.date BETWEEN :dateMin AND :dateMax AND (:selectedCrypto = 'tous' OR c.crypto.id = :selectedCrypto)")
    BigDecimal calculateAverage(String selectedCrypto, LocalDateTime dateMin, LocalDateTime dateMax);
}
