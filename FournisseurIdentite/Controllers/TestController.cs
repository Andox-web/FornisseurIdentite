using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System;
using FournisseurIdentite.Database;

namespace FournisseurIdentite.Controllers
{
    [ApiController]
    public class TestController : ControllerBase
    {
        // Action pour tester la connexion à la base de données et insérer des valeurs
        [HttpGet("test")]
        public IActionResult TestConnection()
        {
            try
            {
                // Créer une connexion à la base de données
                using (var connection = DatabaseConnection.GetConnection())
                {
                    // Ouvrir la connexion
                    connection.Open();

                    // Commande SQL pour sélectionner toutes les lignes de la table "statuts"
                    var query = "SELECT * FROM statuts";

                    // Exécuter la commande SQL
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        // Lire les résultats de la requête
                        using (var reader = command.ExecuteReader())
                        {
                            var results = new List<object>();

                            while (reader.Read())
                            {
                                // Assumer que la table "statuts" a des colonnes "id", "nom", et "description"
                                results.Add(new
                                {
                                    Id = reader["id"],
                                    Nom = reader["nom"],
                                    Description = reader["description"]
                                });
                            }

                            return Ok(new { Message = "Données récupérées", Data = results });
                        }
                    }
                }

            }
            catch (Exception ex)
            {
                // En cas d'erreur, retourner une erreur
                return StatusCode(500, new { Message = "Erreur de connexion ou d'insertion", Error = ex.Message });
            }
        }
    }
}
