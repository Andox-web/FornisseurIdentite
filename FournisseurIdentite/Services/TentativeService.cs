using System;
using FournisseurIdentite.Database;
using FournisseurIdentite.Models;
using FournisseurIdentite.Utils;

namespace FournisseurIdentite.Services
{
    public class TentativeService
    {
        private readonly AppDbContext _dbContext;

        public TentativeService(AppDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public Statut incrementTentative(string email){
            var tentative = _dbContext.TentativesConnexion
                    .FirstOrDefault(t => t.Email == email);
            
            var utilisateur = _dbContext.Utilisateurs.FirstOrDefault(u => u.Email == email);
            if (utilisateur == null)
                throw new Exception("Utilisateur non trouvé.");

            if (tentative != null)
            {
                tentative.CompteurTentative++;
                tentative.DateTentative = DateTime.UtcNow;
                if(tentative.CompteurTentative >= EnvironmentVariable.GetEnvironmentVariable("NB_TENTATIVE_MAX",3)){

                    var statutBloque = _dbContext.Statuts
                        .FirstOrDefault(s => s.Nom == "bloque");
                    
                    var utilisateurStatut = _dbContext.UtilisateurStatuts
                    .Where(us => us.UtilisateurId == tentative.UtilisateurId)
                    .OrderByDescending(us => us.DateCreation)
                    .FirstOrDefault();
                    
                    if (utilisateurStatut == null)
                    {
                        utilisateurStatut = new UtilisateurStatut
                        {
                            UtilisateurId = tentative.UtilisateurId,
                            StatutId = statutBloque.Id,
                            DateCreation = DateTime.UtcNow
                        };
                        
                        _dbContext.UtilisateurStatuts.Add(utilisateurStatut);
                        _dbContext.SaveChanges();
                    }

                    return statutBloque;
                }
            }
            else
            {
                _dbContext.TentativesConnexion.Add(new TentativeConnexion
                {
                    UtilisateurId = utilisateur.Id,
                    Email = email,
                    CompteurTentative = 1,
                    DateTentative = DateTime.UtcNow
                });
            }

            // Enregistrer les modifications dans la base de données
            _dbContext.SaveChanges();
            return null;
        }
    }
}