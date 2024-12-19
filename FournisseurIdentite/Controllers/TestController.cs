using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Utils;

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
        public async Task<IActionResult> TestValidation([FromQuery] string code)
        {
            try
            {
                if (string.IsNullOrEmpty(code))
                {
                    return BadRequest("Le code de validation est nécessaire.");
                }

                bool result = await _inscriptionService.ValiderUtilisateur(code);

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
        [HttpGet("test")]
        public IActionResult TestConnection()
        {
            try
            {
                var utilisateurs = _context.Utilisateurs.ToList();
                var roles = _context.Roles.ToList();
                var utilisateurRoles = _context.UtilisateurRoles.ToList();
                var statuts = _context.Statuts.ToList();
                var utilisateurStatuts = _context.UtilisateurStatuts.ToList();
                var tentativesConnexion = _context.TentativesConnexion.ToList();
                var typesSessions = _context.TypesSessions.ToList();
                var sessions = _context.Sessions.ToList();
                var authentifications = _context.Authentifications.ToList();
                var reinitialisations = _context.Reinitialisations.ToList();

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
    }
}
