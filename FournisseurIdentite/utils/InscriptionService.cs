using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using Microsoft.EntityFrameworkCore;
using FournisseurIdentite.Utils;

namespace FournisseurIdentite.Services
{
    public class InscriptionService
    {
        private readonly AppDbContext _context;

        public InscriptionService(AppDbContext context){
            _context = context;
        }

        public async Task<bool> InscrireUtilisateur(string nom, string email, string motDePasse)
        {
            // Vérifier si l'email existe déjà
            if (await _context.Utilisateurs.AnyAsync(u => u.Email == email))
            {
                throw new Exception("Un utilisateur avec cet email existe déjà.");
            }

            // Générer un code de validation unique
            string codeCreation = Guid.NewGuid().ToString();

            // Créer l'utilisateur
            var utilisateur = new Utilisateur
            {
                Nom = nom,
                Email = email,
                MotDePasse = HashUtility.GenerateSecureHash(motDePasse), // Hachage du mot de passe
                CodeCreation = codeCreation
            };
            var serviceEmail = new ServiceEmail();
            await serviceEmail.EnvoyerAsync(utilisateur.Email, "Validation de votre compte", 
                $"Bonjour {utilisateur.Nom},\n\nMerci de vous être inscrit. Veuillez cliquer sur le lien suivant pour valider votre compte : http://localhost:5000/validation?email={email}&&code={utilisateur.CodeCreation}");

            _context.Utilisateurs.Add(utilisateur);
            await _context.SaveChangesAsync();
            // Envoi de l'email de validation
            
            return true;
        }

        public async Task<bool> ValiderUtilisateur(string email,string codeCreation)
        {
            // Rechercher l'utilisateur avec le code de création
            var utilisateur = await _context.Utilisateurs
                .FirstOrDefaultAsync(u => u.CodeCreation == codeCreation && u.Email == email);

            if (utilisateur == null)
            {
                throw new Exception("Code de validation invalide ou expiré.");
            }

            // Mettre à jour le statut de l'utilisateur en "actif"
            DateTime utcNow = DateTime.UtcNow;  // Obtenez l'heure actuelle en UTC
            utilisateur.DateCreation = utcNow;

            await _context.SaveChangesAsync();
            return true;
        }


    }
}