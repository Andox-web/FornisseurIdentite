package mg.itu.crypto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mg.itu.crypto.models.Cryptomonnaie;
import mg.itu.crypto.models.Transaction;
import mg.itu.crypto.models.Utilisateur;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByVendeur(Utilisateur vendeur);
    List<Transaction> findByAcheteur(Utilisateur acheteur);

    @Query(value="SELECT COUNT(t) FROM Transaction t WHERE t.vendeurid IS NULL AND t.cryptomonnaieid = :idCrypto", nativeQuery = true)
    long countByVendeurIsNullAndCryptomonnaie(Long idCrypto);

    // Fonction pour compter les transactions où l'acheteur est null
    @Query(value="SELECT COUNT(t) FROM Transaction t WHERE t.acheteurid IS NULL AND t.cryptomonnaieid = :idCrypto", nativeQuery = true)
    long countByAcheteurIsNullAndCryptomonnaie(Long idCrypto);
}