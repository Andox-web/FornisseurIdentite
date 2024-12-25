package mg.itu.crypto.controllers;

import  mg.itu.crypto.models.*;
import  mg.itu.crypto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    @Autowired
    private PortefeuilleFiatRepository porteMonnaieFiatRepository;

    @Autowired
    private PortefeuilleCryptoRepository porteMonnaieCryptoRepository;

    @Autowired
    private TypeFondRepository typeFondRepository;

    @Autowired
    private FondRepository fondRepository;

    @Autowired
    private AnnonceVenteRepository annonceVenteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/test/all")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();

        response.put("utilisateurs", utilisateurRepository.findAll());
        response.put("cryptomonnaies", cryptomonnaieRepository.findAll());
        response.put("changeCryptos", changeCryptoRepository.findAll());
        response.put("porteMonnaieFiats", porteMonnaieFiatRepository.findAll());
        response.put("porteMonnaieCryptos", porteMonnaieCryptoRepository.findAll());
        response.put("typeFonds", typeFondRepository.findAll());
        response.put("fonds", fondRepository.findAll());
        response.put("annonceVentes", annonceVenteRepository.findAll());
        response.put("transactions", transactionRepository.findAll());

        return response;
    }
}