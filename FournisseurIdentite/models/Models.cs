using System;
using System.ComponentModel.DataAnnotations;

namespace FournisseurIdentite.Models
{
    public class Utilisateur
    {
        [Key]
        public int Id { get; set; }

        public string? Nom { get; set; }

        public string? Email { get; set; }

        public string? MotDePasse { get; set; }

        public DateTime? DateCreation { get; set; }

        public string? CodeCreation { get; set; }
    }

    public class Role
    {
        [Key]
        public int Id { get; set; }

        public string Nom { get; set; } = string.Empty;
    }

    public class UtilisateurRole
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public int RoleId { get; set; }
        public Role Role { get; set; } = null!;
    }

    public class Statut
    {
        [Key]
        public int Id { get; set; }

        public string Nom { get; set; } = string.Empty;

        public string? Description { get; set; }
    }

    public class UtilisateurStatut
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public int StatutId { get; set; }
        public Statut Statut { get; set; } = null!;

        public DateTime DateCreation { get; set; } = DateTime.UtcNow;
    }

    public class TentativeConnexion
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public string Email { get; set; } = string.Empty;

        public int CompteurTentative { get; set; }

        public DateTime DateTentative { get; set; } = DateTime.UtcNow;
    }

    public class TypeSession
    {
        [Key]
        public int Id { get; set; }

        public string Nom { get; set; } = string.Empty;

        public string? Description { get; set; }
    }

    public class Session
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public int TypeSessionId { get; set; }
        public TypeSession TypeSession { get; set; } = null!;

        public string Token { get; set; } = string.Empty;

        public DateTime ExpireAt { get; set; }

        public DateTime DateCreation { get; set; } = DateTime.UtcNow;
    }

    public class Authentification
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public string Email { get; set; } = string.Empty;

        public string Pin { get; set; } = string.Empty;

        public bool Used { get; set; } = false;

        public DateTime ExpireAt { get; set; }

        public DateTime DateCreation { get; set; } = DateTime.UtcNow;
    }

    public class Reinitialisation
    {
        [Key]
        public int Id { get; set; }

        public int UtilisateurId { get; set; }
        public Utilisateur Utilisateur { get; set; } = null!;

        public string Email { get; set; } = string.Empty;

        public string CodeReinitialisation { get; set; } = string.Empty;

        public DateTime ExpireAt { get; set; }

        public bool Used { get; set; } = false;

        public DateTime DateCreation { get; set; } = DateTime.UtcNow;
    }

    public class UtilisateurModel
    {
        public string Nom { get; set; }  = string.Empty;
        public string Email { get; set; }  = string.Empty;
        public string MotDePasse { get; set; }  = string.Empty;
    }

    // Modèle pour la validation
    public class ValidationModel
    {
        public string CodeCreation { get; set; }  = string.Empty;
    }
}
