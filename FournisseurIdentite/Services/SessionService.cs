using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using System;
using System.Linq;
using FournisseurIdentite.Utils;

namespace FournisseurIdentite.Services
{
    public class SessionService
    {
        private readonly AppDbContext _dbContext;

        public SessionService(AppDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public bool ValidateToken(string token, out Session session)
        {
            session = _dbContext.Sessions
                .FirstOrDefault(s => s.Token == token && s.ExpireAt > DateTime.UtcNow);

            return session != null;
        }
        public Session GenerateSession(int idUtilisateur)
        {
            var currentDateTime = DateTime.UtcNow.ToString("o"); // Format ISO 8601
            var randomString = Guid.NewGuid().ToString(); // Chaine aléatoire
            var token = HashUtility.GenerateSecureHash($"{currentDateTime}-{randomString}");

            var expireAt = DateTime.UtcNow.AddSeconds(EnvironmentVariable.GetEnvironmentVariable("SESSIONS_DURATION", 90));

            var newSession = new Session
            {
                UtilisateurId = idUtilisateur,
                Token = token,
                TypeSessionId = 1,
                ExpireAt = expireAt,
                DateCreation = DateTime.UtcNow
            };

            _dbContext.Sessions.Add(newSession);
            _dbContext.SaveChanges();

            return newSession;
        }
    }
}
