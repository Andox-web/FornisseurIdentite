using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Utils;
using FournisseurIdentite.Services;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class ReinitialisationController : ControllerBase
    {
        private readonly AppDbContext _dbContext;
        private readonly TentativeService _tentaviveService; 

        public ReinitialisationController(AppDbContext dbContext,TentativeService tentativeService)
        {
            _dbContext = dbContext;
            _tentaviveService =  tentativeService;
        }

        [HttpGet("reinitialisation")]
        public IActionResult ReinitialiserCompte(string email,string codeReinitialisation)
        {
            try
            {
                var reinitialisation = _dbContext.Reinitialisations
                    .Where(r => r.Email == email)
                    .OrderByDescending(r => r.DateCreation)
                    .Where(r => r.CodeReinitialisation==codeReinitialisation)
                    .FirstOrDefault();

                if (reinitialisation == null)
                {
                    return BadRequest(new { message = "Code de réinitialisation invalide ou email non reconnu." });
                }

                // // Vérification de l'expiration
                // if (reinitialisation.ExpireAt < DateTime.UtcNow)
                // {
                //     return BadRequest(new { message = "Le code de réinitialisation a expiré." });
                // }

                // Vérification si déjà utilisé
                if (reinitialisation.Used)
                {
                    return BadRequest(new { message = "Le code de réinitialisation a déjà été utilisé." });
                }

                var utilisateur = _dbContext.Utilisateurs.FirstOrDefault(u => u.Email == email);
                if (utilisateur != null)
                {
                    _tentaviveService.reinitialisation(email);
                }

                // Mise à jour de l'état de la réinitialisation
                reinitialisation.Used = true;
                _dbContext.Reinitialisations.Update(reinitialisation);

                // Réinitialisation des tentatives (exemple)
                
                _dbContext.SaveChanges();

                return Ok(new { message = "Le compte a été réinitialisé avec succès." });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Une erreur est survenue.", details = ex.Message });
            }
        }
    }
}
