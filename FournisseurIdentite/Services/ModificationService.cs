using System.Threading.Tasks;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using Microsoft.EntityFrameworkCore;
using FournisseurIdentite.Utils;

namespace FournisseurIdentite.Services
{
    public class NewInfoUser
    {
        public string nom { get; set; } = string.Empty;
        public string motDePasse { get; set; } = string.Empty;
    }

    public class ModificationService
    {
        private readonly AppDbContext _context;

        public ModificationService(AppDbContext context)
        {
            _context = context;
        }

        public bool ModifierUtilisateur(string email, string motDePasse, NewInfoUser newInfoUser)
        {
            // Recherche de l'utilisateur existant par email
            var utilisateurExistant = _context.Utilisateurs
                .FirstOrDefault(u => u.Email == email);

            if (utilisateurExistant == null)
            {
                throw new InvalidOperationException("Utilisateur non trouvé.");
            }

            if (string.IsNullOrEmpty(utilisateurExistant.MotDePasse))
            {
                throw new InvalidOperationException("Le mot de passe stocké est manquant ou invalide.");
            }

            if (!HashUtility.VerifyHash(motDePasse, utilisateurExistant.MotDePasse))
            {
                throw new InvalidOperationException("Le mot de passe est incorrect.");
            }


            // Mise à jour des propriétés fournies
            if (!string.IsNullOrEmpty(newInfoUser.nom) && !string.IsNullOrEmpty(newInfoUser.motDePasse))
            {
                utilisateurExistant.Nom = newInfoUser.nom;
                utilisateurExistant.MotDePasse = HashUtility.GenerateSecureHash(newInfoUser.motDePasse);
            }

            // Sauvegarde des modifications

            _context.SaveChanges();

            return true;
        }

        public bool SupprimerUtilisateur(string email, string motDePasse)
        {
            // Recherche de l'utilisateur existant par email
            var utilisateurExistant = _context.Utilisateurs
                .FirstOrDefault(u => u.Email == email);

            if (utilisateurExistant == null)
            {
                throw new InvalidOperationException("Utilisateur non trouvé.");
            }

            if (string.IsNullOrEmpty(utilisateurExistant.MotDePasse))
            {
                throw new InvalidOperationException("Le mot de passe stocké est manquant ou invalide.");
            }

            if (!HashUtility.VerifyHash(motDePasse, utilisateurExistant.MotDePasse))
            {
                throw new InvalidOperationException("Le mot de passe est incorrect.");
            }

            // Suppression de l'utilisateur
            _context.Utilisateurs.Remove(utilisateurExistant);

            // Sauvegarde des modifications
            _context.SaveChanges();

            return true;
        }
    }
}
