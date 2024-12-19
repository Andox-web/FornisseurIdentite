using Microsoft.AspNetCore.Mvc;
using FournisseurIdentite.Models;
using FournisseurIdentite.Services;
using System.Threading.Tasks;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ModificationController : ControllerBase
    {
        private readonly ModificationService _modificationService;

        public ModificationController(ModificationService modificationService)
        {
            _modificationService = modificationService;
        }
        
        [HttpPut("modifier")]
        public IActionResult ModifierUtilisateur([FromQuery] string email, [FromQuery] string motDePasse, [FromBody] NewInfoUser utilisateurMisAJour)
        {
            try
            {
                Console.WriteLine(email);
                Console.WriteLine(motDePasse);
                Console.WriteLine(utilisateurMisAJour.nom, utilisateurMisAJour.motDePasse);
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

        [HttpDelete("supprimer")]
        public IActionResult SupprimerUtilisateur([FromQuery] string email, [FromQuery] string motDePasse)
        {
            try
            {
                Console.WriteLine(email);
                Console.WriteLine(motDePasse);

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
