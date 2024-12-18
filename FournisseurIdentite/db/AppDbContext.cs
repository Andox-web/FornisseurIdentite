using System;
using Npgsql;
using FournisseurIdentite.Models;
using Microsoft.EntityFrameworkCore;

namespace FournisseurIdentite.Database
{
    public class AppDbContext : DbContext
    {
        public DbSet<Utilisateur> Utilisateurs { get; set; }
        public DbSet<Role> Roles { get; set; }
        public DbSet<UtilisateurRole> UtilisateurRoles { get; set; }
        public DbSet<Statut> Statuts { get; set; }
        public DbSet<UtilisateurStatut> UtilisateurStatuts { get; set; }
        public DbSet<TentativeConnexion> TentativesConnexion { get; set; }
        public DbSet<TypeSession> TypesSessions { get; set; }
        public DbSet<Session> Sessions { get; set; }
        public DbSet<Authentification> Authentifications { get; set; }
        public DbSet<Reinitialisation> Reinitialisations { get; set; }

        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Utilisateur>().ToTable("utilisateur");
            modelBuilder.Entity<Role>().ToTable("role");
            modelBuilder.Entity<UtilisateurRole>().ToTable("utilisateurrole");
            modelBuilder.Entity<Statut>().ToTable("statut");
            modelBuilder.Entity<UtilisateurStatut>().ToTable("utilisateurstatut");
            modelBuilder.Entity<TentativeConnexion>().ToTable("tentativesconnexion");
            modelBuilder.Entity<TypeSession>().ToTable("typesession");
            modelBuilder.Entity<Session>().ToTable("session");
            modelBuilder.Entity<Authentification>().ToTable("authentification");
            modelBuilder.Entity<Reinitialisation>().ToTable("reinitialisation");
        }
    }

   
}
 