package mg.itu.crypto.controllers;

import mg.itu.crypto.models.*;
import mg.itu.crypto.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PorteFeuilleController {

    @Autowired
    private PortefeuilleFiatRepository porteMonnaieFiatRepository;

    @Autowired
    private PortefeuilleCryptoRepository porteMonnaieCryptoRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Repository pour récupérer l'utilisateur

    @Autowired
    private FondRepository fondRepository;

    @GetMapping("/portefeuille")
    public ResponseEntity<?> consultationPortefeuille(@RequestHeader("Authorization") String authorization) {
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

        // Calculer la quantité totale (fiat + cryptomonnaies)
        BigDecimal totalFiat = porteMonnaieFiatRepository
                .findByUtilisateur(utilisateur)
                .map(PortefeuilleFiat::getQuantite)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalCrypto = porteMonnaieCryptoRepository
                .findAllByUtilisateur(utilisateur)
                .stream()
                .map(PortefeuilleCrypto::getQuantite)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = totalFiat.add(totalCrypto);

        // Retourner le total
        return ResponseEntity.ok(total);
    }
    
    @PostMapping("/portefeuille/fond/demande")
    public ResponseEntity<?> initiationDemandeFond(
            @RequestHeader("Authorization") String authorization,
            @RequestParam BigDecimal montant,
            @RequestParam String typeFond // "DÉPÔT" ou "RETRAIT"
    ) {
        // Vérification du token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Erreur : Token invalide.");
        }

        String token = authorization.substring(7); // Extraction du token
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByCodecreation(token);

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erreur : Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérification du montant
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(400).body("Erreur : Montant invalide.");
        }

        // Vérification du type de fond
        if (!typeFond.equalsIgnoreCase("DÉPÔT") && !typeFond.equalsIgnoreCase("RETRAIT")) {
            return ResponseEntity.status(400).body("Erreur : Type de fond invalide.");
        }

        // Récupération du portefeuille fiat
        BigDecimal soldeFiat = porteMonnaieFiatRepository
                .findByUtilisateur(utilisateur)
                .map(PortefeuilleFiat::getQuantite)
                .orElse(BigDecimal.ZERO);

        // Vérification du solde pour un retrait
        if (typeFond.equalsIgnoreCase("RETRAIT") && soldeFiat.compareTo(montant) < 0) {
            return ResponseEntity.status(400).body("Erreur : Solde insuffisant.");
        }

        // Création de la demande de fond
        Fond fond = new Fond();
        fond.setUtilisateur(utilisateur);
        fond.setValeur(montant);
        fond.setTypeFond(new TypeFond(typeFond)); // Vous devrez ajuster cette partie si TypeFond est en base de données.
        fond.setIstransaction(false);
        fond.setIsvalid(false);
        fond.setCodefond(UUID.randomUUID().toString());
        fond.setExpireat(LocalDateTime.now().plusHours(24)); // Expiration dans 24 heures

        fondRepository.save(fond);

        // Simulation de l'envoi d'un email (ici, affichage du lien dans la réponse)
        String lienValidation = "http://localhost:5000/portefeuille/fond/valider?email="
                + utilisateur.getEmail() + "&code=" + fond.getCodefond();

        return ResponseEntity.ok("Demande de " + typeFond + " effectuée avec succès. "
                + "Veuillez la confirmer en cliquant sur le lien envoyé par email : " + lienValidation);
    }

    @PostMapping("/portefeuille/fond/validation")
    public ResponseEntity<?> validationDemandeFond(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String email,
            @RequestParam String code
    ) {
        // Vérification du token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Erreur : Token invalide.");
        }

        String token = authorization.substring(7); // Extraction du token
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByCodecreation(token);

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erreur : Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérification de la demande de fond
        Optional<Fond> fondOpt = fondRepository.findByCodefond(code);
        if (fondOpt.isEmpty()) {
            return ResponseEntity.status(400).body("Erreur : Code de fond invalide.");
        }

        Fond fond = fondOpt.get();

        // Vérifier si le code de fond a expiré
        if (fond.getExpireat().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body("Erreur : Code de fond expiré.");
        }

        // Vérifier si l'email correspond à l'utilisateur
        if (!fond.getUtilisateur().getEmail().equals(email)) {
            return ResponseEntity.status(400).body("Erreur : L'email ne correspond pas.");
        }

        // Si la demande est déjà validée ou en cours de transaction, la refuser
        if (fond.getIsvalid() || fond.getIstransaction()) {
            return ResponseEntity.status(400).body("Erreur : Cette demande est déjà traitée.");
        }

        // Valider la demande de fond
        fond.setIsvalid(true);

        // Si c'est un dépôt, ajouter le montant au portefeuille de l'utilisateur
        if (fond.getTypeFond().getType().equalsIgnoreCase("DÉPÔT")) {
            PortefeuilleFiat portefeuille = porteMonnaieFiatRepository
                    .findByUtilisateur(utilisateur)
                    .orElse(new PortefeuilleFiat());

            portefeuille.setUtilisateur(utilisateur);
            portefeuille.setQuantite(portefeuille.getQuantite().add(fond.getValeur()));
            porteMonnaieFiatRepository.save(portefeuille);
        }

        // Si c'est un retrait, vérifier si l'utilisateur a assez de fonds
        if (fond.getTypeFond().getType().equalsIgnoreCase("RETRAIT")) {
            PortefeuilleFiat portefeuille = porteMonnaieFiatRepository
                    .findByUtilisateur(utilisateur)
                    .orElse(new PortefeuilleFiat());

            if (portefeuille.getQuantite().compareTo(fond.getValeur()) < 0) {
                return ResponseEntity.status(400).body("Erreur : Solde insuffisant.");
            }

            // Si le solde est suffisant, procéder au retrait
            portefeuille.setQuantite(portefeuille.getQuantite().subtract(fond.getValeur()));
            porteMonnaieFiatRepository.save(portefeuille);
        }

        // Marquer la transaction comme traitée
        fond.setIstransaction(true);
        fondRepository.save(fond);

        // Retourner un message de confirmation
        return ResponseEntity.ok("Demande de fond validée avec succès.");
    }

}

