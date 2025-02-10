package mg.itu.crypto.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mg.itu.crypto.models.AnnonceVente;
import mg.itu.crypto.models.Cryptomonnaie;
import mg.itu.crypto.models.PortefeuilleCrypto;
import mg.itu.crypto.models.PortefeuilleFiat;
import mg.itu.crypto.models.Transaction;
import mg.itu.crypto.models.Utilisateur;
import mg.itu.crypto.models.ChangeCrypto;
import mg.itu.crypto.repositories.AnnonceVenteRepository;
import mg.itu.crypto.repositories.ChangeCryptoRepository;
import mg.itu.crypto.repositories.CryptomonnaieRepository;
import mg.itu.crypto.repositories.PortefeuilleCryptoRepository;
import mg.itu.crypto.repositories.PortefeuilleFiatRepository;
import mg.itu.crypto.repositories.TransactionRepository;
import mg.itu.crypto.repositories.UtilisateurRepository;


@RestController
public class TransactionsController {

    @Autowired
    private AnnonceVenteRepository annonceVenteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Repository pour récupérer l'utilisateur

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    @Autowired
    private PortefeuilleCryptoRepository porteMonnaieCryptoRepository;

    @Autowired
    private PortefeuilleFiatRepository porteMonnaieFiatRepository;

    @Autowired
    private ChangeCryptoRepository changeCryptoRepository;


    @GetMapping("/transaction/vente")
    public ResponseEntity<?> listeAnnonceVente(){
        List<AnnonceVente> non_vendue = annonceVenteRepository.findByIsvendueFalse();
        return ResponseEntity.ok(non_vendue);
    }

    @PostMapping("/transaction/achat")
    public ResponseEntity<?> transactionAchat(@RequestHeader("Authorization") String token, @RequestParam Long annonce_id, @RequestParam BigDecimal quantite){
        Optional<AnnonceVente> annonce_opt = annonceVenteRepository.findById(annonce_id);
        if(annonce_opt.isEmpty()){
            return ResponseEntity.status(404).body("Aucun annonce trouvé");
        }
        AnnonceVente annonce = annonce_opt.get();
        if(annonce.getQuantite().compareTo(quantite)<0){
            return ResponseEntity.status(400).body("Quantité demandée non disponible.");
        }

        return ResponseEntity.ok("achat effectuee avec succes");
        
    }

    @GetMapping("/transactions/perso")
    public ResponseEntity<?> historique(@RequestHeader("Authorization") String header){
        String token = header.substring(7);
        Optional<Utilisateur> util = utilisateurRepository.findBySessionToken(token);
        
        if(util.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("error", "Aucun utilisateur trouvé"));
        }
        
        Utilisateur u = util.get();
        List<Transaction> vente = transactionRepository.findByUtilisateur(u);
        HashMap<String, List<Transaction>> rep = new HashMap<>();
        rep.put("perso", vente);
        return ResponseEntity.ok(rep);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> historiques(){
        List<Transaction> historique = transactionRepository.findAll();
        HashMap<String, List<Transaction>> rep = new HashMap<>();
        rep.put("rehetra", historique);
        return ResponseEntity.ok(rep);
    }


    // @PostMapping("/vente")
    // public ResponseEntity<?> creerAnnonce(@RequestHeader("Authorization") String header, @RequestBody AnnonceVente annonce) {
    //     String token = header.substring(7);
        
    //     // Vérifier si le vendeur existe
    //     Optional<Utilisateur> vendeurOpt = utilisateurRepository.findBySessionToken(token);
    //     if (vendeurOpt.isEmpty()) {
    //         return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
    //     }

    //     // Vérifier si la cryptomonnaie existe
    //     Optional<Cryptomonnaie> cryptoOpt = cryptomonnaieRepository.findById(annonce.getCryptomonnaie().getId());
    //     if (cryptoOpt.isEmpty()) {
    //         return ResponseEntity.badRequest().body("Cryptomonnaie non reconnue.");
    //     }

    //     Optional<PortefeuilleCrypto> pf_opt = porteMonnaieCryptoRepository.findByUtilisateurAndCryptomonnaie(vendeurOpt.get(), annonce.getCryptomonnaie());

    //     if(pf_opt.isEmpty()){
    //         return ResponseEntity.badRequest().body("Quantite insuffisante");
    //     }
    //     PortefeuilleCrypto pf = pf_opt.get();

    //     // Vérifier si l'utilisateur a assez de crypto pour vendre
    //     BigDecimal quantiteDispo = pf.getQuantite();
    //     if (quantiteDispo.compareTo(annonce.getQuantite()) < 0) {
    //         return ResponseEntity.badRequest().body("Quantité insuffisante.");
    //     }

    //     // Sauvegarder l'annonce
    //     annonce.setVendeur(vendeurOpt.get());
    //     annonce.setCryptomonnaie(cryptoOpt.get());
    //     annonceVenteRepository.save(annonce);

    //     return ResponseEntity.ok("Annonce créée avec succès.");
    // }

    @PostMapping("/vente")
    public ResponseEntity<?> vente(@RequestHeader("Authorization") String header, @RequestParam Long crypto_id, @RequestParam BigDecimal quantite) {
        String token = header.substring(7);
    
        // Vérifier si le vendeur existe
        Optional<Utilisateur> vendeurOpt = utilisateurRepository.findBySessionToken(token);
        if (vendeurOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
        }
    
        // Vérifier si la cryptomonnaie existe
        Optional<Cryptomonnaie> cryptoOpt = cryptomonnaieRepository.findById(crypto_id);
        if (cryptoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cryptomonnaie non reconnue.");
        }
    
        Utilisateur vendeur = vendeurOpt.get();
        Cryptomonnaie crypto = cryptoOpt.get();
    
        // Vérifier si l'utilisateur a assez de crypto pour vendre
        Optional<PortefeuilleCrypto> pf_opt = porteMonnaieCryptoRepository.findByUtilisateurAndCryptomonnaie(vendeur, crypto);
        if (pf_opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Quantité insuffisante.");
        }

        PortefeuilleCrypto pf = pf_opt.get();
        BigDecimal quantiteDispo = pf.getQuantite();
        if (quantiteDispo.compareTo(quantite) < 0) {
            return ResponseEntity.badRequest().body("Quantité insuffisante.");
        }
    
        // Créer l'annonce de vente
    
        // Enregistrer une transaction (sans acheteur pour l'instant)
        Transaction transaction = new Transaction();
        transaction.setTypeTransaction("vente");
        transaction.setUtilisateur(vendeur);
        transaction.setQuantitecrypto(quantite);
        transaction.setCryptomonnaie(crypto);
        transactionRepository.save(transaction);
    
        return ResponseEntity.ok("Annonce créée avec succès.");
    }
       



    // @PostMapping("/achat")
    // public ResponseEntity<?> achat(@RequestHeader("Authorization") String header, @RequestParam Long crypto_id, @RequestParam BigDecimal quantite){
    //     // Optional<AnnonceVente> annonce_opt = annonceVenteRepository.findById(annonce_id);
    //     String token = header.substring(7);

    //     Optional<Utilisateur> u_opt = utilisateurRepository.findBySessionToken(token);
    //     if(u_opt.isEmpty()){
    //         return ResponseEntity.badRequest().body("utilisateur n'existe pas");
    //     }

    //     Utilisateur utilisateur = u_opt.get();

    //     Optional<Cryptomonnaie> crypto_opt = cryptomonnaieRepository.findById(crypto_id);
    //     if(crypto_opt.isEmpty()){
    //         return ResponseEntity.badRequest().body("cryptomonnaie n'existe pas");
    //     }
    //     Cryptomonnaie crypto = crypto_opt.get();
    //     Optional<PortefeuilleFiat> pf_opt = porteMonnaieFiatRepository.findLastPortefeuilleByUtilisateur(utilisateur.getId(), PageRequest.of(0, 1)).stream().findFirst();;

    //     PortefeuilleFiat pf = pf_opt.get();

    //     Optional<AnnonceVente> annonce_opt = annonceVenteRepository.findLastAnnonceVenteByCryptomonnaie(crypto_id);

    //     if(annonce_opt.isEmpty()){
    //         return ResponseEntity.status(404).body("Aucun annonce trouvé");
    //     }
    //     AnnonceVente annonce = annonce_opt.get();

    //     BigDecimal prix_u = annonce.getPrix().divide(annonce.getQuantite(), RoundingMode.HALF_UP);

    //     BigDecimal prix = prix_u.multiply(quantite);

    //     if(prix.compareTo(pf.getQuantite())<0){
    //         return ResponseEntity.status(400).body("Insuffisant.");
    //     }

    //     return ResponseEntity.ok("achat effectuee avec succes");
        
    // }

    @PostMapping("/achat")
    public ResponseEntity<?> achat(@RequestHeader("Authorization") String header, @RequestParam Long crypto_id, @RequestParam BigDecimal quantite) {
        String token = header.substring(7);

        Optional<Utilisateur> u_opt = utilisateurRepository.findBySessionToken(token);
        if (u_opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur n'existe pas");
        }

        Utilisateur utilisateur = u_opt.get();

        Optional<Cryptomonnaie> crypto_opt = cryptomonnaieRepository.findById(crypto_id);
        if (crypto_opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cryptomonnaie n'existe pas");
        }
        Cryptomonnaie crypto = crypto_opt.get();

        Optional<PortefeuilleFiat> pf_opt = porteMonnaieFiatRepository.findLastPortefeuilleByUtilisateur(utilisateur.getId(), PageRequest.of(0, 1)).stream().findFirst();
        PortefeuilleFiat pf = pf_opt.get();

        Optional<ChangeCrypto> change_opt = changeCryptoRepository.findLatestChangeByCryptomonnaie(crypto_id);
        if (change_opt.isEmpty()) {
            return ResponseEntity.status(404).body("Aucun cours trouvé");
        }
        ChangeCrypto change = change_opt.get();

        BigDecimal prix_u = change.getValeur();
        BigDecimal prix = prix_u.multiply(quantite);

        if (prix.compareTo(pf.getQuantite()) < 0) {
            return ResponseEntity.status(400).body("Fonds insuffisants.");
        }

        // Créer la transaction
        Transaction transaction = new Transaction();
        transaction.setTypeTransaction("achat");
        // transaction.setVendeur(annonce.getVendeur());
        transaction.setUtilisateur(utilisateur);
        transaction.setQuantitecrypto(quantite);
        transaction.setCryptomonnaie(crypto);
        transaction.setIsConfirmed(true);
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Achat effectué avec succès.");
    }

    @GetMapping("/transactions/confirmer")
    public ResponseEntity<?> confirmerTransaction(@RequestHeader("Authorization") String header, @RequestParam Long transaction_id) {
        String token = header.substring(7);

        Optional<Utilisateur> u_opt = utilisateurRepository.findBySessionToken(token);
        if (u_opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur n'existe pas");
        }

        Utilisateur utilisateur = u_opt.get();

        Optional<Transaction> transaction_opt = transactionRepository.findById(transaction_id);
        if (transaction_opt.isEmpty()) {
            return ResponseEntity.status(404).body("Transaction non trouvée");
        }
        Transaction transaction = transaction_opt.get();
        transaction.setIsConfirmed(true);
        transactionRepository.save(transaction);
        return ResponseEntity.ok("Transaction confirmée avec succès");
    }

}
