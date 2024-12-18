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

        public TestController(AppDbContext context)
        {
            _context = context;
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
