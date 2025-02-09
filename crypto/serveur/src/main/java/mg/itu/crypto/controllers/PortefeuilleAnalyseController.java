package mg.itu.crypto.controllers;

import mg.itu.crypto.models.Transaction;
import mg.itu.crypto.models.Utilisateur;
import mg.itu.crypto.repositories.TransactionRepository;
import mg.itu.crypto.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mg.itu.crypto.models.PortefeuilleCrypto;
import mg.itu.crypto.models.PortefeuilleFiat;
import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.repositories.PortefeuilleCryptoRepository;
import mg.itu.crypto.repositories.PortefeuilleFiatRepository;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import org.springframework.data.domain.PageRequest;

@RestController
public class PortefeuilleAnalyseController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortefeuilleCryptoRepository portefeuilleCryptoRepository;

    @Autowired
    private PortefeuilleFiatRepository portefeuilleFiatRepository;

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;

    @GetMapping("/portefeuilleanalyse")
    public ResponseEntity<?> getPortefeuilleAnalyse(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        final List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        List<Transaction> transactionsAll = transactionRepository.findAll();

        if (start != null && end != null) {
            List<Transaction> filteredtransactionsAll = transactionsAll.stream()
                    .filter(t -> !t.getDateTransaction().isBefore(start) && !t.getDateTransaction().isAfter(end))
                    .collect(Collectors.toList());
            transactionsAll = filteredtransactionsAll;
        }
        final List<Transaction> transactions = transactionsAll;

        List<Map<String, Object>> response = utilisateurs.stream().map(utilisateur -> {
            BigDecimal totalAchat = transactions.stream()
                    .filter(t -> t.getUtilisateur().equals(utilisateur) && "achat".equals(t.getTypeTransaction()))
                    .map(Transaction::getQuantitecrypto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalVente = transactions.stream()
                    .filter(t -> t.getUtilisateur().equals(utilisateur) && "vente".equals(t.getTypeTransaction()))
                    .map(Transaction::getQuantitecrypto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal valeurPortefeuilleFiat = portefeuilleFiatRepository.findLastPortefeuilleByUtilisateur(utilisateur.getId(), PageRequest.of(0, 1))
                    .stream().findFirst().map(PortefeuilleFiat::getQuantite).orElse(BigDecimal.ZERO);

            BigDecimal valeurPortefeuilleCrypto = portefeuilleCryptoRepository.findByUtilisateur(utilisateur)
                    .map(pfArray -> {
                        BigDecimal totalCryptoValue = BigDecimal.ZERO;
                        for (PortefeuilleCrypto pf : pfArray) {
                            BigDecimal latestChange = changeCryptoRepository.findLatestChangeByCryptomonnaie(pf.getCryptomonnaie().getId())
                                    .map(ChangeCrypto::getValeur).orElse(BigDecimal.ZERO);
                            totalCryptoValue = totalCryptoValue.add(pf.getQuantite().multiply(latestChange));
                        }
                        return totalCryptoValue;
                    }).orElse(BigDecimal.ZERO);

            BigDecimal valeurPortefeuille = valeurPortefeuilleFiat.add(valeurPortefeuilleCrypto);

            return Map.<String, Object>of(
                    "utilisateur", utilisateur.getNom(),
                    "totalAchat", totalAchat,
                    "totalVente", totalVente,
                    "valeurPortefeuille", valeurPortefeuille
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
