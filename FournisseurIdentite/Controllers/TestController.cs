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
        private readonly SessionService _sessionService;
        private readonly AppDbContext _dbContext;

        public TestController(SessionService sessionService, AppDbContext dbContext)
        {
            _sessionService = sessionService;
            _dbContext = dbContext;
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