package mg.itu.crypto.repositories;

import mg.itu.crypto.models.TypeFond;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeFondRepository extends JpaRepository<TypeFond, Long> {

    Optional<TypeFond> findById(Long id);
}