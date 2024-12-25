package mg.itu.crypto.repositories;

import mg.itu.crypto.models.ChangeCrypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeCryptoRepository extends JpaRepository<ChangeCrypto, Long> {
}