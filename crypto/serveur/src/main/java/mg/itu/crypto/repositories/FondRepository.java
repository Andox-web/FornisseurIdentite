package mg.itu.crypto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.itu.crypto.models.Fond;

public interface FondRepository extends JpaRepository<Fond, Long> {
    Optional<Fond> findByCodefond(String codeFond);

}