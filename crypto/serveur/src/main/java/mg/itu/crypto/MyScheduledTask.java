package mg.itu.crypto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.models.Cryptomonnaie;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import mg.itu.crypto.repositories.CryptomonnaieRepository;
import mg.itu.crypto.repositories.TransactionRepository;

@Service
public class MyScheduledTask {
    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    @Scheduled(fixedRate = 10000) // Exécution toutes les 10 secondes
    public void executeTask() {
        System.out.println("Scheduled task running...");
        List<Cryptomonnaie> cryptos = cryptomonnaieRepository.findAll(); // Récupère toutes les cryptos
        
        for (Cryptomonnaie crypto : cryptos) {
            long achat = transactionRepository.countByVendeurIsNullAndCryptomonnaie(crypto.getId());
            long vente = transactionRepository.countByAcheteurIsNullAndCryptomonnaie(crypto.getId());
            Optional<ChangeCrypto> change = changeCryptoRepository.findLatestChangeByCryptomonnaie(crypto.getId());
    
            if (change.isPresent()) {
                ChangeCrypto changeCrypto = change.get();
                BigDecimal valeur = changeCrypto.getValeur();
                boolean updated = false; // Flag pour savoir si on met à jour ou pas
    
                if (vente > achat) {
                    valeur = valeur.multiply(BigDecimal.valueOf(1.1));
                    updated = true;
                } else if (achat > vente) {
                    valeur = valeur.multiply(BigDecimal.valueOf(0.9));
                    updated = true;
                }
    
                // if (updated) { // On met à jour seulement si nécessaire
                    changeCrypto.setId(null);
                    changeCrypto.setValeur(valeur);
                    changeCrypto.setDatechangement(LocalDateTime.now());
                    changeCryptoRepository.save(changeCrypto);
                // }
            }
        }
    }
    
}
