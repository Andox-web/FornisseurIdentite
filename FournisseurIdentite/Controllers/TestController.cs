using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Utils;
using FournisseurIdentite.Services;
using FournisseurIdentite.Filters;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class TestController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly InscriptionService _inscriptionService;

        public TestController(AppDbContext context, InscriptionService inscriptionService)
        {
            _context = context;
            _inscriptionService = inscriptionService;
        
        [HttpPost("inscription")]
        public async Task<IActionResult> TestInscription([FromBody] UtilisateurModel utilisateur)
        {
            try
            {
                if (string.IsNullOrEmpty(utilisateur.Nom) || string.IsNullOrEmpty(utilisateur.Email) || string.IsNullOrEmpty(utilisateur.MotDePasse))
                {
                    return BadRequest("Tous les champs sont nécessaires.");
                }

                bool result = await _inscriptionService.InscrireUtilisateur(utilisateur.Nom, utilisateur.Email, utilisateur.MotDePasse);

                if (result)
                {
                    return Ok("Inscription réussie. Un email de validation a été envoyé.");
                }

                return BadRequest("Erreur lors de l'inscription.");
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { Message = "Erreur d'inscription", Error = ex.Message });
            }
        }

        // Action pour tester la validation de l'utilisateur
        [HttpPost("validation")]
        public async Task<IActionResult> TestValidation([FromBody] ValidationModel validationModel)
        {
            try
            {
                if (string.IsNullOrEmpty(validationModel.CodeCreation))
                {
                    return BadRequest("Le code de validation est nécessaire.");
                }

                bool result = await _inscriptionService.ValiderUtilisateur(validationModel.CodeCreation);

                if (result)
                {
                    return Ok("Validation réussie. Votre compte est maintenant actif.");
                }

                return BadRequest("Code de validation invalide.");
            }
            catch (Exception ex)
            {
                var innerException = ex.InnerException?.Message ?? "Aucune inner exception disponible.";
                return StatusCode(500, new { Message = "Erreur de validation", Error = ex.Message, InnerException = innerException });
            }
        }

        // Action pour tester la connexion à la base de données et insérer des valeurs
        [HttpGet("test-entity")]
        public IActionResult TestConnection()
        {
            try
            {
                var utilisateurs = _dbContext.Utilisateurs.ToList();
                var roles = _dbContext.Roles.ToList();
                var utilisateurRoles = _dbContext.UtilisateurRoles.ToList();
                var statuts = _dbContext.Statuts.ToList();
                var utilisateurStatuts = _dbContext.UtilisateurStatuts.ToList();
                var tentativesConnexion = _dbContext.TentativesConnexion.ToList();
                var typesSessions = _dbContext.TypesSessions.ToList();
                var sessions = _dbContext.Sessions.ToList();
                var authentifications = _dbContext.Authentifications.ToList();
                var reinitialisations = _dbContext.Reinitialisations.ToList();

                return Ok(new
                {
                    Utilisateurs = utilisateurs,
                    Roles = roles,
                    UtilisateurRoles = utilisateurRoles,
                    Statuts = statuts,
                    UtilisateurStatuts = utilisateurStatuts,
                    TentativesConnexion = tentativesConnexion,
                    TypesSessions = typesSessions,
                    Sessions = sessions,
                    Authentifications = authentifications,
                    Reinitialisations = reinitialisations
                });
                
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { Message = "Erreur de connexion ou d'insertion", Error = ex.Message });
            }
        }
        [HttpGet("test-token")]
        [RequiresSession]
        public IActionResult TestToken()
        {
            // Récupérer la session depuis le HttpContext
            var session = HttpContext.Items["UserSession"] as Session;

            if (session == null)
                return Unauthorized();

            return Ok(new
            {
                Message = "Token valide",
                SessionId = session.Id,
                UtilisateurId = session.UtilisateurId,
                ExpireAt = session.ExpireAt
            });
        }
        [HttpPost("test-generate-token")]
        public IActionResult GenerateToken(int UtilisateurId)
        {
            var utilisateur = _dbContext.Utilisateurs.Find(UtilisateurId);
            if (utilisateur == null)
                return NotFound("Utilisateur introuvable");

            var session = _sessionService.GenerateSession(UtilisateurId);

            return Ok(new
            {
                session.Id,
                session.Token,
                session.ExpireAt,
                session.UtilisateurId
            });
        }
        
    }
}
