using System;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Utils;

namespace FournisseurIdentite.Services
{
    public class AuthentificationService
    {
        private readonly AppDbContext _dbContext;

        public AuthentificationService(AppDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        // Méthode pour générer un code PIN

        public string GeneratePin()
        {
            var random = new Random();
            return random.Next(100000, 999999).ToString(); // Générer un PIN à 6 chiffres
        }
        public Authentification InsertAuthentication(int utilisateurId, string pin)
        {
            // Générer un hash sécurisé pour le PIN
            var hashedPin = HashUtility.GenerateSecureHash(pin);
            var utilisateur = _dbContext.Utilisateurs.Find(utilisateurId);
            if (utilisateur == null)
                throw new InvalidOperationException("Utilisateur introuvable.");

            // Créer un nouvel objet Authentification
            var authentification = new Authentification
            {
                UtilisateurId = utilisateur.Id,
                Email = utilisateur.Email,
                Pin = hashedPin,
                DateCreation = DateTime.UtcNow,
                ExpireAt = DateTime.UtcNow.AddSeconds(EnvironmentVariable.GetEnvironmentVariable("PIN_DURATION", 90)), 
                Used = false
            };

            // Ajouter à la base de données
            _dbContext.Authentifications.Add(authentification);

            // Sauvegarder les modifications
            _dbContext.SaveChanges();

            // Retourner l'objet Authentification inséré
            return authentification;
        }
        
    }

}
