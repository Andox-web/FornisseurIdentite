using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Threading.Tasks;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Services;
using FournisseurIdentite.Utils;

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

            if (authentification == null)
            {
                Statut statut = _tentaviveService.incrementTentative(email);
                if (statut!=null)
                {
                    return BadRequest(new { message = "PIN invalide ou déjà utilisé. Votre compte a ete bloque", });    
                }
                return BadRequest(new { message = "PIN invalide ou déjà utilisé." });
            }

            if (authentification.ExpireAt < DateTime.Now)
            {
                return BadRequest(new { message = "Le délai du PIN a été dépassé." });
            }
            
            // Marquer le PIN comme utilisé
            authentification.Used = true;
            _dbContext.Authentifications.Update(authentification);
            _dbContext.SaveChanges();

            // Générer un token d'action (ici un exemple de token)
            var token = _sessionService.GenerateSession(authentification.UtilisateurId).Token;

            return Ok(new { message = "Connexion valide:vous etes maintenant connecte", token });
        }
    }
}
