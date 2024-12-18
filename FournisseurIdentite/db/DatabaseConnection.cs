using System;
using Npgsql;

namespace FournisseurIdentite.Database
{
    public class DatabaseConnection
    {
        // Méthode pour obtenir la chaîne de connexion depuis les variables d'environnement
        public static string GetConnectionString()
        {
            var host = Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection");
            return host;
        }

        // Méthode pour créer une connexion Npgsql
        public static NpgsqlConnection GetConnection()
        {
            var connectionString = GetConnectionString();

            var connection = new NpgsqlConnection(connectionString);
            if(connection==null){
                throw new Exception("Connection is null");
            }
            return connection;
        }
    }
}
