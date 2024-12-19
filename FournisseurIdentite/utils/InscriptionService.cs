using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using Microsoft.EntityFrameworkCore;

namespace FournisseurIdentite.Utils
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
                MotDePasse = HashUtility.GenerateSecureHash(motDePasse), 
                CodeCreation = codeCreation
            };

            _context.Utilisateurs.Add(utilisateur);
            await _context.SaveChangesAsync();

            // Associer le statut "inactif"
            var statutInactif = await _context.Statuts.FirstOrDefaultAsync(s => s.Nom == "inactif");
            if (statutInactif == null)
            {
                throw new Exception("Statut 'inactif' introuvable.");
            }

            var utilisateurStatut = new UtilisateurStatut
            {
                UtilisateurId = utilisateur.Id,
                StatutId = statutInactif.Id
            };

            _context.UtilisateurStatuts.Add(utilisateurStatut);
            await _context.SaveChangesAsync();

            // Envoi de l'email de validation
            var serviceEmail = new ServiceEmail();
            await serviceEmail.EnvoyerAsync(utilisateur.Email, "Validation de votre compte", 
                $"Bonjour {utilisateur.Nom},\n\nMerci de vous être inscrit. Veuillez cliquer sur le lien suivant pour valider votre compte : http://localhost:5000/validation?code={codeCreation}");

            return true;
        }

        public async Task<bool> ValiderUtilisateur(string codeCreation)
        {
            // Rechercher l'utilisateur avec le code de création
            var utilisateur = await _context.Utilisateurs
                .FirstOrDefaultAsync(u => u.CodeCreation == codeCreation);

            if (utilisateur == null)
            {
                throw new Exception("Code de validation invalide ou expiré.");
            }

            // Rechercher le statut "actif"
            var statutActif = await _context.Statuts
                .FirstOrDefaultAsync(s => s.Nom == "actif");

            if (statutActif == null)
            {
                throw new Exception("Statut 'actif' introuvable.");
            }

            // Mettre à jour le statut de l'utilisateur en "actif"
            var utilisateurStatut = await _context.UtilisateurStatuts
                .FirstOrDefaultAsync(us => us.UtilisateurId == utilisateur.Id);

            DateTime utcNow = DateTime.UtcNow;  // Obtenez l'heure actuelle en UTC

            if (utilisateurStatut != null)
            {
                utilisateurStatut.StatutId = statutActif.Id;
                utilisateurStatut.DateCreation = utcNow; // Mise à jour de la date en UTC
                utilisateur.DateCreation = utcNow;
            }
            else
            {
                _context.UtilisateurStatuts.Add(new UtilisateurStatut
                {
                    UtilisateurId = utilisateur.Id,
                    StatutId = statutActif.Id,
                    DateCreation = utcNow // Date en UTC
                });
            }

            await _context.SaveChangesAsync();
            return true;
        }


    }
}