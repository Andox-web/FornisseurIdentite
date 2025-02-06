package mg.itu.crypto.controllers;

import mg.itu.crypto.models.*;
import mg.itu.crypto.repositories.*;
import mg.itu.crypto.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
public class CryptoController {

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    @GetMapping("/cryptos")
    public ResponseEntity<?> getCryptos() {
        List<Cryptomonnaie> cryptos = cryptomonnaieRepository.findAll();
        return ResponseEntity.ok(cryptos);
    }

    // @GetMapping("/cryptos/cours")
    // public ResponseEntity<?> 

}
