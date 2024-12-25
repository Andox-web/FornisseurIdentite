using Microsoft.AspNetCore.Mvc;
using FournisseurIdentite.Services;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class DemandeReinitialisationController : ControllerBase
    {
        private readonly DemandeReinitialisationService _demandeReinitialisationService;

        public DemandeReinitialisationController(DemandeReinitialisationService demandeReinitialisationService)
        {
            _demandeReinitialisationService = demandeReinitialisationService;
        }

        [HttpPost("demandeReinitialisation")]
        public async Task<IActionResult> DemandeReset([FromBody] string email)
        {
            if (string.IsNullOrWhiteSpace(email))
            {
                return BadRequest("L'email est requis.");
            }

            try
            {
                bool result = await _demandeReinitialisationService.DemandeReset(email);
                return Ok(new { success = result, message = "Demande de réinitialisation envoyée avec succès." });
            }
            catch (InvalidOperationException ex)
            {
                return NotFound(new { success = false, message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Une erreur est survenue.", details = ex.Message });
            }
        }
    }
}
