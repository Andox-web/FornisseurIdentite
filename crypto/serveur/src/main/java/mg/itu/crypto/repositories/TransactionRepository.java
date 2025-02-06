package mg.itu.crypto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.itu.crypto.models.Transaction;
import mg.itu.crypto.models.Utilisateur;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByVendeur(Utilisateur vendeur);
    List<Transaction> findByAcheteur(Utilisateur acheteur);

}