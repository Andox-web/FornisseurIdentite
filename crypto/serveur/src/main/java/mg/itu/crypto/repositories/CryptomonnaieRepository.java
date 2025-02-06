package mg.itu.crypto.repositories;

import mg.itu.crypto.models.AnnonceVente;
import mg.itu.crypto.models.Cryptomonnaie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptomonnaieRepository extends JpaRepository<Cryptomonnaie, Long> {

        Optional<Cryptomonnaie> findById(Long id);
        

}