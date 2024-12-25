using Microsoft.AspNetCore.Mvc;
using FournisseurIdentite.Models;
using FournisseurIdentite.Services;
using System.Threading.Tasks;
using FournisseurIdentite.Filters;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class ModificationController : ControllerBase
    {
        private readonly ModificationService _modificationService;

        public ModificationController(ModificationService modificationService)
        {
            _modificationService = modificationService;
        }
        
        [HttpPut("modification")]
        [RequiresSession]
        public IActionResult ModifierUtilisateur([FromQuery] string email, [FromQuery] string motDePasse, [FromBody] NewInfoUser utilisateurMisAJour)
        {
            try
            {
                bool resultat = _modificationService.ModifierUtilisateur(email, motDePasse, utilisateurMisAJour);
                if (resultat)
                {
                    return Ok("Utilisateur mis à jour avec succès.");
                }
                else
                {
                    return BadRequest("Échec de la mise à jour de l'utilisateur.");
                }
            }
            catch (InvalidOperationException ex)
            {
                return Unauthorized(ex.Message);
            }
        }

        [HttpDelete("supprimerUtilisateur")]
        [RequiresSession]
        public IActionResult SupprimerUtilisateur([FromQuery] string email, [FromQuery] string motDePasse)
        {
            try
            {

                bool resultat = _modificationService.SupprimerUtilisateur(email, motDePasse);
                if (resultat)
                {
                    return Ok("Utilisateur supprimé avec succès.");
                }
                else
                {
                    return BadRequest("Échec de la suppression de l'utilisateur.");
                }
            }
            catch (InvalidOperationException ex)
            {
                return Unauthorized(ex.Message);
            }
        }
    }
}
