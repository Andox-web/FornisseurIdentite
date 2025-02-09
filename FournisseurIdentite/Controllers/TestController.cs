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
        private readonly AppDbContext _dbContext;
        private readonly InscriptionService _inscriptionService;
        private readonly SessionService _sessionService;

        public TestController(AppDbContext context, InscriptionService inscriptionService,SessionService sessionService)
        {
            _dbContext = context;
            _inscriptionService = inscriptionService;
            _sessionService = sessionService;
        }
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
       [HttpGet("validation")]
        public async Task<IActionResult> TestValidation(string email, string code)
        {
            try
            {
                if (string.IsNullOrEmpty(code))
                {
                    return BadRequest("Le code de validation est nécessaire.");
                }

                bool result = await _inscriptionService.ValiderUtilisateur(email,code);

                if (result)
                {
                    return Ok("Validation réussie. Votre compte est maintenant utilisable.");
                }

                return BadRequest("Code de validation invalide.");
            }
            catch (Exception ex)
            {
                var innerException = ex.InnerException?.Message ?? "Aucune inner exception disponible.";
                return StatusCode(500, new { Message = "Erreur de validation", Error = ex.Message, InnerException = innerException });
            }
        }
       
        [HttpGet("test-connection")]
        public IActionResult TestToken(string token)
        {
            if (!_sessionService.ValidateToken(token, out var session))
            {
                return new UnauthorizedObjectResult(new {message = "session invalide",IsValid = false} );
            }
            return Ok(new
            {
                Message = "Token valide",
                IsValid = true,
                SessionId = session.Id,
                UtilisateurId = session.UtilisateurId,
                ExpireAt = session.ExpireAt
            });
        }
        [HttpGet("refresh-connection")]
        public IActionResult refreshToken(string token)
        {
            if (!_sessionService.ValidateToken(token, out var session))
            {
                return new UnauthorizedObjectResult(new {message = "session invalide,veuillez vous reconnecter.",IsValid = false} );
            }
            var newSession =  _sessionService.GenerateSession(session.UtilisateurId);
            return Ok(new
            {
                Message = "Token valide",
                IsValid = true,
                SessionId = newSession.Id,
                token = newSession.Token
                UtilisateurId = newSession.UtilisateurId,
                ExpireAt = newSession.ExpireAt
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