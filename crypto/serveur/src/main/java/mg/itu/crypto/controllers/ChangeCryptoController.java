package mg.itu.crypto.controllers;

import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/changecryptos")
public class ChangeCryptoController {

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    // Endpoint pour récupérer la liste de tous les changements de valeur
    @GetMapping
    public ResponseEntity<List<ChangeCrypto>> getAllChangeCryptos() {
        List<ChangeCrypto> changeCryptos = changeCryptoRepository.findAll();
        return ResponseEntity.ok(changeCryptos);
    }
}
