using FournisseurIdentite.Database;
using FournisseurIdentite.Utils;
using FournisseurIdentite.Models;

namespace FournisseurIdentite.Services
{
    public class DemandeReinitialisationService
    {
        private readonly AppDbContext _context;

        public DemandeReinitialisationService(AppDbContext context)
        {
            _context = context;
        }

        public async Task<bool> DemandeReset(string email)
        {
            // Vérifier si l'email existe dans la table utilisateur
            var utilisateur = _context.Utilisateurs
                .FirstOrDefault(u => u.Email == email);

            if (utilisateur == null)
            {
                throw new InvalidOperationException("Utilisateur non trouvé.");
            }

            // Générer un code de réinitialisation unique
            string codeReinitialisation = Guid.NewGuid().ToString();

            // Créer un nouvel enregistrement dans la table réinitialisation
            if (string.IsNullOrEmpty(utilisateur.Email))
            {
                throw new InvalidOperationException("L'email de l'utilisateur est nul ou vide.");
            }

            var reinitialisation = new Reinitialisation
            {
                UtilisateurId = utilisateur.Id,
                Email = utilisateur.Email, // À ce stade, Email est garanti non-nul
                CodeReinitialisation = codeReinitialisation,
                ExpireAt = DateTime.UtcNow.AddHours(24),
                Used = false
            };

            _context.Reinitialisations.Add(reinitialisation);
            _context.SaveChanges();

            // Envoyer un email avec le lien de réinitialisation
            var serviceEmail = new ServiceEmail();
            await serviceEmail.EnvoyerAsync(
                utilisateur.Email,
                "Demande de réinitialisation de tentative",
                $"Bonjour {utilisateur.Nom},\n\n" +
                $"Veuillez cliquer sur le lien suivant pour valider votre demande de réinitialisation :\n" +
                $"http://localhost:5000/validation?email={email}&codereinitialisation={codeReinitialisation}\n\n" +
                "Ce lien expirera dans 24 heures."
            );

            return true;
        }
    }
}
