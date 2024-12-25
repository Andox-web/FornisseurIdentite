package mg.itu.crypto.repositories;

import mg.itu.crypto.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}