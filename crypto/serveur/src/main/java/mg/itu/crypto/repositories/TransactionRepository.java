package mg.itu.crypto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mg.itu.crypto.models.Transaction;
import mg.itu.crypto.models.Utilisateur;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUtilisateur(Utilisateur vendeur);

    @Query(value="SELECT t FROM Transaction t where t.isconfirmed = false", nativeQuery = true)
    List<Transaction> findUnconfirmedTransactions();

}