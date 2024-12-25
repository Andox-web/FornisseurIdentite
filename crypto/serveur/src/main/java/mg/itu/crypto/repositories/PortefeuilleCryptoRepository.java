package mg.itu.crypto.repositories;
import mg.itu.crypto.models.PortefeuilleCrypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortefeuilleCryptoRepository extends JpaRepository<PortefeuilleCrypto, Long> {
}
