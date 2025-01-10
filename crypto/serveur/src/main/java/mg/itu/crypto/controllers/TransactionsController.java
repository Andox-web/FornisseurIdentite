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
public class TransactionsController {

    @Autowired
    private AnnonceVenteRepository annonceVenteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/transactions")
    public ResponseEntity<?> listeAnnonceVente() {
        try {
            // Récupérer les annonces de vente non vendues (celles qui n'ont pas de transaction associée)
            List<AnnonceVente> annoncesVenteNonVendues = annonceVenteRepository.findByTransactionIsNull();

            // Vérifier si des annonces de vente ont été trouvées
            if (annoncesVenteNonVendues.isEmpty()) {
                return ResponseEntity.status(404).body("Aucune annonce de vente disponible.");
            }

            // Retourner la liste des annonces de vente non vendues
            return ResponseEntity.ok(annoncesVenteNonVendues);
        } catch (Exception e) {
            // En cas d'erreur interne
            return ResponseEntity.status(500).body("Erreur interne. Veuillez réessayer plus tard.");
        }
    }

    @GetMapping("/cryptos/cours")
    public ResponseEntity<?> coursCryptomonnaies() {
        try {
            // Récupérer toutes les cryptomonnaies
            List<Cryptomonnaie> cryptos = cryptomonnaieRepository.findAll();

            // Vérifier si des cryptomonnaies ont été trouvées
            if (cryptos.isEmpty()) {
                return ResponseEntity.status(404).body("Aucune cryptomonnaie disponible.");
            }

            // Retourner la liste des cryptomonnaies avec leurs cours
            // Vous pouvez ajouter un appel à une API externe pour obtenir les cours en temps réel si nécessaire
            return ResponseEntity.ok(cryptos);
        } catch (Exception e) {
            // En cas d'erreur interne
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }


    @PostMapping("/annonce/vente")
    public ResponseEntity<?> creerAnnonceVente(@RequestHeader("Authorization") String authorization, @RequestBody AnnonceVente annonceVente) {
        // Vérifier si le token est présent
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Erreur : Token invalide.");
        }

        String token = authorization.substring(7); // Extraction du token
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByCodecreation(token);

        // Vérifier si l'utilisateur existe
        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erreur : Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier si la cryptomonnaie existe
        Optional<Cryptomonnaie> cryptomonnaieOpt = cryptomonnaieRepository.findById(annonceVente.getCryptomonnaie().getId());
        if (cryptomonnaieOpt.isEmpty()) {
            return ResponseEntity.status(400).body("Erreur : Cryptomonnaie non reconnue.");
        }

        Cryptomonnaie cryptomonnaie = cryptomonnaieOpt.get();

        // Vérifier si l'utilisateur a suffisamment de quantités de cryptomonnaie
        BigDecimal quantiteDisponible = utilisateur.getPortefeuilleCrypto().getQuantite();  // Vous devrez adapter la récupération de la quantité selon la structure de votre modèle
        if (quantiteDisponible.compareTo(annonceVente.getQuantite()) < 0) {
            return ResponseEntity.status(400).body("Erreur : Quantité insuffisante.");
        }

        // Créer l'annonce de vente
        annonceVente.setUtilisateur(utilisateur);
        annonceVente.setCryptomonnaie(cryptomonnaie);

        try {
            annonceVenteRepository.save(annonceVente);
            return ResponseEntity.ok("Annonce créée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne lors de la création de l'annonce.");
        }
    }

    @PostMapping("/transaction/achat")
    public ResponseEntity<?> achatCryptomonnaies(@RequestHeader("Authorization") String authorization, @RequestBody AchatRequest achatRequest) {
        // Vérifier si le token est présent
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Erreur : Token invalide.");
        }

        String token = authorization.substring(7); // Extraction du token
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByCodecreation(token);

        // Vérifier si l'utilisateur existe
        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erreur : Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier si l'annonce existe
        Optional<AnnonceVente> annonceVenteOpt = annonceVenteRepository.findById(achatRequest.getAnnonceId());
        if (annonceVenteOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erreur : Annonce introuvable.");
        }

        AnnonceVente annonceVente = annonceVenteOpt.get();

        // Vérifier si l'annonce est déjà vendue
        if (annonceVente.getVendeur() != null) {
            return ResponseEntity.status(400).body("Erreur : Annonce déjà vendue.");
        }

        // Vérifier si la quantité demandée est disponible
        if (annonceVente.getQuantite().compareTo(achatRequest.getQuantite()) < 0) {
            return ResponseEntity.status(400).body("Erreur : Quantité demandée non disponible.");
        }

        // Vérifier si l'utilisateur a suffisamment de solde
        BigDecimal totalAchat = annonceVente.getCryptomonnaie().getPrix().multiply(achatRequest.getQuantite());
        BigDecimal soldeUtilisateur = utilisateur.getSolde();  // Vous devrez adapter la structure de votre modèle

        if (soldeUtilisateur.compareTo(totalAchat) < 0) {
            return ResponseEntity.status(400).body("Erreur : Solde insuffisant.");
        }

        // Créer la transaction
        Transaction transaction = new Transaction();
        transaction.setAnnonceVente(annonceVente);
        transaction.setAcheteur(utilisateur);
        transaction.setQuantitecrypto(achatRequest.getQuantite());
        transaction.setCryptomonnaie(annonceVente.getCryptomonnaie());

        // Mettre à jour l'annonce de vente
        annonceVente.setQuantite(annonceVente.getQuantite().subtract(achatRequest.getQuantite()));

        // Enregistrer la transaction et mettre à jour l'annonce
        try {
            transactionRepository.save(transaction);
            annonceVenteRepository.save(annonceVente);

            // Débiter le solde de l'acheteur
            utilisateur.setSolde(soldeUtilisateur.subtract(totalAchat));
            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok("Achat effectué avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne lors de l'achat.");
        }
    }


    @GetMapping("/transactions")
    public ResponseEntity<?> historiqueTransactions(@RequestHeader("Authorization") String token) {
        try {
            // Vérification du token (logique à adapter selon votre méthode de validation des tokens)
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByToken(token);
            if (utilisateurOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Token invalide.");
            }

            // Récupérer l'utilisateur
            Utilisateur utilisateur = utilisateurOpt.get();

            // Récupérer l'historique des transactions pour cet utilisateur
            List<Transaction> transactions = transactionRepository.findByAcheteurOrVendeur(utilisateur, utilisateur);

            if (transactions.isEmpty()) {
                return ResponseEntity.ok("Aucune transaction trouvée.");
            }

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }

}
