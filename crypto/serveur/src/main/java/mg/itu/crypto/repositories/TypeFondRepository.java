package mg.itu.crypto.repositories;

import mg.itu.crypto.models.TypeFond;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeFondRepository extends JpaRepository<TypeFond, Long> {
}