package mg.itu.crypto.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.models.Cryptomonnaie;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import mg.itu.crypto.repositories.CryptomonnaieRepository;
import mg.itu.crypto.repositories.TransactionRepository;


@RestController
public class CryptoController {

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    @GetMapping("/cryptos")
    public ResponseEntity<?> getCryptos() {
        List<Cryptomonnaie> cryptos = cryptomonnaieRepository.findAll();
        return ResponseEntity.ok(cryptos);
    }

    @GetMapping("/cryptos/cours")
    public ResponseEntity<?> getCours(@RequestParam Long id) {
        // Optional<Cryptomonnaie> crypto = cryptomonnaieRepository.findById(id);
        // long achat = transactionRepository.countByVendeurIsNullAndCryptomonnaie(id);
        // long vente = transactionRepository.countByAcheteurIsNullAndCryptomonnaie(id);
        // Optional<ChangeCrypto> change = changeCryptoRepository.findLatestChangeByCryptomonnaie(id);
        // ChangeCrypto changeCrypto = change.get();
        // BigDecimal valeur = changeCrypto.getValeur();
        // if(vente > achat){
        //     valeur = valeur.multiply(BigDecimal.valueOf(1.1));
        // } else if(achat > vente){
        //     valeur = valeur.multiply(BigDecimal.valueOf(0.9));
        // }
        // changeCrypto.setValeur(valeur);
        // changeCrypto.setDatechangement(LocalDateTime.now());
        // changeCryptoRepository.save(changeCrypto);

        List<ChangeCrypto> changes = changeCryptoRepository.findChangeByCryptomonnaie(id);

        return ResponseEntity.ok(changes);
    }

    @GetMapping("cryptos/prix")
    public ResponseEntity<?> getPrix(){
        List<Cryptomonnaie> cryptos = cryptomonnaieRepository.findAll();
        List<ChangeCrypto> changes = new ArrayList<>();
        for(Cryptomonnaie crypto : cryptos){
            Optional<ChangeCrypto> change = changeCryptoRepository.findLatestChangeByCryptomonnaie(crypto.getId());
            changes.add(change.get());
        }
        return ResponseEntity.ok(changes);
    }

    // @GetMapping("/cryptos/cours")
    // public ResponseEntity<?> 

}
