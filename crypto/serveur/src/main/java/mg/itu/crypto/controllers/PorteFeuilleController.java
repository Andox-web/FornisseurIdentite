package mg.itu.crypto.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mg.itu.crypto.models.PortefeuilleCrypto;
import mg.itu.crypto.models.PortefeuilleFiat;
import mg.itu.crypto.models.TypeFond;
import mg.itu.crypto.models.Utilisateur;
import mg.itu.crypto.repositories.FondRepository;
import mg.itu.crypto.repositories.TypeFondRepository;
import mg.itu.crypto.repositories.PortefeuilleCryptoRepository;
import mg.itu.crypto.repositories.PortefeuilleFiatRepository;
import mg.itu.crypto.repositories.UtilisateurRepository;

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

    @Autowired
    private TypeFondRepository typeFondRepository;

    @GetMapping("/portefeuille")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String header){
        String token = header.substring(7);
        Optional<Utilisateur> util = utilisateurRepository.findBySessionToken(token);
        
        if(util.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("error", "Aucun utilisateur trouvé"));
        }
        
        Utilisateur u = util.get();
        Optional<PortefeuilleCrypto[]> p_fC_opt = porteMonnaieCryptoRepository.findByUtilisateur(u);
        Optional<PortefeuilleFiat[]> p_fF_opt = porteMonnaieFiatRepository.findByUtilisateur(u);
        
        if(p_fC_opt.isEmpty() || p_fF_opt.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("error", "Aucun portefeuille trouvé pour " + u.getNom()));
        }

        // Création de la réponse JSON
        Map<String, Object> response = new HashMap<>();
        response.put("portefeuille_crypto", p_fC_opt.get());
        response.put("portefeuille_fiat", p_fF_opt.get());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/portefeuille/fond/demande")
    public ResponseEntity<?> demandeFond(@RequestHeader("Authorization") String header, 
                                        @RequestParam BigDecimal montant, 
                                        @RequestParam Long typeFondId) {
        // Extraction du token
        String token = header.substring(7); 
        Optional<Utilisateur> util = utilisateurRepository.findBySessionToken(token);

        // Vérification du token
        if (util.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Token invalide"));
        }

        Utilisateur utilisateur = util.get();
        Optional<PortefeuilleFiat[]> portefeuilleFiatOpt = porteMonnaieFiatRepository.findByUtilisateur(utilisateur);

        // Vérification de l'existence du portefeuille
        if (portefeuilleFiatOpt.isEmpty() || portefeuilleFiatOpt.get().length == 0) {
            return ResponseEntity.status(404).body(Map.of("error", "Portefeuille inexistant"));
        }

        PortefeuilleFiat portefeuilleFiat = portefeuilleFiatOpt.get()[0]; // On suppose qu'il y a un seul portefeuille par utilisateur

        // Vérification du solde suffisant
        if (portefeuilleFiat.getQuantite().compareTo(montant) < 0) {
            return ResponseEntity.status(400).body(Map.of("error", "Solde insuffisant"));
        }

        // Vérification du montant valide
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(400).body(Map.of("error", "Montant invalide"));
        }

        // Récupérer le TypeFond à partir de l'ID
        Optional<TypeFond> typeFondOpt = typeFondRepository.findById(typeFondId);
        if (typeFondOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Type de fond invalide"));
        }

        TypeFond typeFond = typeFondOpt.get();

        // Générer un code de validation et un lien
        String validationCode = UUID.randomUUID().toString();
        String validationLink = "http://localhost:8081/portefeuille/fond/validation?email=" + utilisateur.getEmail() + "&code=" + validationCode;

        // Sauvegarder la demande (simulée ici), vous devrez ajouter la logique pour envoyer l'email
        // Exemple de code d'envoi d'email avec lien de validation (non inclus dans cet exemple)

        return ResponseEntity.ok(Map.of("message", "Demande de " + typeFond.getType() + " effectuée avec succès. Veuillez le confirmer en cliquant sur le lien envoyé par email.",
                                        "lien_validation", validationLink));
    }

    @PostMapping("/portefeuille/fond/validation")
    public ResponseEntity<?> validationFond(@RequestHeader("Authorization") String header,
                                            @RequestParam String email, 
                                            @RequestParam String code) {
        String token = header.substring(7);
        Optional<Utilisateur> util = utilisateurRepository.findBySessionToken(token);

        if (util.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Token invalide"));
        }

        Utilisateur utilisateur = util.get();

        // Vérifier le code de validation et l'email
        // Supposons que nous avons une logique pour valider le code, ici simulée
        if (code == null || !isValidCode(code)) {
            return ResponseEntity.status(400).body(Map.of("error", "Code invalide ou expiré"));
        }

        Optional<PortefeuilleFiat[]> portefeuilleFiatOpt = porteMonnaieFiatRepository.findByUtilisateur(utilisateur);

        if (portefeuilleFiatOpt.isEmpty() || portefeuilleFiatOpt.get().length == 0) {
            return ResponseEntity.status(404).body(Map.of("error", "Portefeuille inexistant"));
        }

        PortefeuilleFiat portefeuilleFiat = portefeuilleFiatOpt.get()[0]; // Assuming only one PortefeuilleFiat per user

        // Mettre à jour la quantité du portefeuille avec la nouvelle valeur
        portefeuilleFiat.setQuantite(portefeuilleFiat.getQuantite().add(BigDecimal.valueOf(100))); // Exemple de montant ajouté
        porteMonnaieFiatRepository.save(portefeuilleFiat);

        return ResponseEntity.ok(Map.of("message", "Demande de dépôt validée avec succès"));
    }

    private boolean isValidCode(String code) {
        // Logique de validation du code (par exemple, vérifier si le code existe et n'est pas expiré)
        return true; // Pour la démonstration, nous retournons toujours true
    }


}

