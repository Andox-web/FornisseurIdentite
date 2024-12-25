using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Threading.Tasks;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Services;
using FournisseurIdentite.Utils;
using System.ComponentModel.DataAnnotations;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class AuthentificationController : ControllerBase
    {
        private readonly SessionService _sessionService;
        private readonly AppDbContext _dbContext;
        private readonly AuthentificationService _authentificationService;
        private readonly TentativeService _tentaviveService;

        public AuthentificationController(SessionService sessionService, AppDbContext dbContext,AuthentificationService authentificationService,TentativeService tentativeService)
        {
            _sessionService = sessionService;
            _dbContext = dbContext;
            _authentificationService = authentificationService;
            _tentaviveService = tentativeService;
        }

        [HttpPost("Authentification")]
        public IActionResult Authenticate(String email, int pin)
        {
            // Étape 1 : Récupérer les authentifications valides depuis la base de données
            var authentifications = _dbContext.Authentifications
                .Where(a => a.Email == email && !a.Used)
                .OrderByDescending(a => a.DateCreation)
                .AsEnumerable(); // Convertir en IEnumerable pour traitement en mémoire
            
            // Étape 2 : Appliquer le filtre supplémentaire en mémoire
            var authentification = authentifications
                .Where(a => HashUtility.VerifyHash(pin.ToString(), a.Pin))
                .FirstOrDefault();

            var statutBloque = _dbContext.Statuts
                    .FirstOrDefault(s => s.Nom == "bloque");
                
            
            if (authentification == null)
            {
                Statut statut = _tentaviveService.incrementTentative(email);
                if (statut!=null)
                {
                    return BadRequest(new { message = "PIN invalide ou déjà utilisé. Votre compte a ete bloque", });    
                }
                return BadRequest(new { message = "PIN invalide ou déjà utilisé." });
            }
            
            if(authentification.ExpireAt < DateTime.UtcNow){
                return BadRequest(new { message = "Pin expire", });
            }
            
            var utilisateurStatut = _dbContext.UtilisateurStatuts
                .Where(us => us.UtilisateurId == authentification.UtilisateurId)
                .OrderByDescending(us => us.DateCreation)
                .FirstOrDefault();

            if (utilisateurStatut != null && utilisateurStatut.StatutId == statutBloque.Id  )
            {
                return BadRequest(new { message = "votre compte est bloque" });
            }
            
            // Marquer le PIN comme utilisé
            authentification.Used = true;
            _dbContext.Authentifications.Update(authentification);
            _dbContext.SaveChanges();

            // Générer un token d'action (ici un exemple de token)
            var token = _sessionService.GenerateSession(authentification.UtilisateurId).Token;

            return Ok(new { message = "Connexion valide:vous etes maintenant connecte", token });
        }
        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { message = "Des données invalides ont été fournies." });
            }

            // Rechercher l'utilisateur par email
            var utilisateur = _dbContext.Utilisateurs.FirstOrDefault(u => u.Email == request.Email && u.DateCreation !=null);

            if (utilisateur == null)
            {
                
                return BadRequest(new { message = "Email non reconnu." });
            }

            // Vérifier si le compte est bloqué
            var statutBloque = _dbContext.Statuts
                        .FirstOrDefault(s => s.Nom == "bloque");
                    
            var utilisateurStatut = _dbContext.UtilisateurStatuts
            .Where(us => us.UtilisateurId == utilisateur.Id)
            .OrderByDescending(us => us.DateCreation)
            .FirstOrDefault();

            if (utilisateurStatut!=null && utilisateurStatut.StatutId==statutBloque.Id)
            {
                
                return BadRequest(new { message = "Compte bloqué. Veuillez contacter le support." });
            }

            // Vérifier le mot de passe
            if (!HashUtility.VerifyHash(request.MotDePasse, utilisateur.MotDePasse))
            {
                Statut statut = _tentaviveService.incrementTentative(request.Email);
                if (statut!=null)
                {
                    return BadRequest(new { message = "Mot de passe erroné.Votre compte a ete bloque", });    
                }
                return BadRequest(new { message = "Mot de passe erroné." });
            }

            // Générer un code PIN de vérification
            var pin = _authentificationService.GeneratePin();
            // Ajouter le PIN à la table authentification
            _authentificationService.InsertAuthentication(utilisateur.Id,pin);

            // Envoyer le PIN par email
            var serviceEmail = new ServiceEmail();
            serviceEmail.EnvoyerAsync(utilisateur.Email, "Code de vérification", $"Votre code PIN est : {pin}");

            return Ok(new { message = "Identité à confirmer. Un code PIN a été envoyé à votre email." });
        }
    }
    
    public class LoginRequest
    {
        [Required(ErrorMessage = "L'email est requis.")]
        [EmailAddress(ErrorMessage = "L'email n'est pas valide.")]
        public string? Email { get; set; }

        [Required(ErrorMessage = "Le mot de passe est requis.")]
        public string? MotDePasse { get; set; }
    }
}
