using System;
using System.Security.Cryptography;
using System.Text;

namespace FournisseurIdentite.Utils
{
    public class HashUtility
    {
         private static readonly int SaltSize = EnvironmentVariable.GetEnvironmentVariable("HASHING_SALT_SIZE", 16);
        private static readonly int HashSize = EnvironmentVariable.GetEnvironmentVariable("HASHING_HASH_SIZE", 32);
        private static readonly int Iterations = EnvironmentVariable.GetEnvironmentVariable("HASHING_ITERATIONS", 100000);
        /// Génère un hash sécurisé pour une chaîne donnée.
        public static string GenerateSecureHash(string input)
        {
            if (string.IsNullOrEmpty(input))
                throw new ArgumentException("La chaîne d'entrée ne peut pas être vide ou nulle.", nameof(input));

            // Générer un sel aléatoire
            byte[] salt = GenerateSalt();

            // Générer le hachage avec PBKDF2
            using (var pbkdf2 = new Rfc2898DeriveBytes(input, salt, Iterations, HashAlgorithmName.SHA256))
            {
                byte[] hash = pbkdf2.GetBytes(HashSize);

                // Combiner le sel et le hachage
                byte[] hashBytes = new byte[SaltSize + HashSize];
                Array.Copy(salt, 0, hashBytes, 0, SaltSize);
                Array.Copy(hash, 0, hashBytes, SaltSize, HashSize);

                // Retourner sous forme de chaîne Base64
                return Convert.ToBase64String(hashBytes);
            }
        }

        /// Vérifie si une chaîne correspond au hash stocké.
        public static bool VerifyHash(string input, string storedHash)
        {
            if (string.IsNullOrEmpty(input) || string.IsNullOrEmpty(storedHash))
                throw new ArgumentException("Les chaînes d'entrée et de hashage ne peuvent pas être nulles ou vides.");

            byte[] hashBytes = Convert.FromBase64String(storedHash);

            // Extraire le sel et le hachage
            byte[] salt = new byte[SaltSize];
            byte[] storedHashBytes = new byte[HashSize];
            Array.Copy(hashBytes, 0, salt, 0, SaltSize);
            Array.Copy(hashBytes, SaltSize, storedHashBytes, 0, HashSize);

            // Générer un hachage pour l'entrée avec le même sel
            using (var pbkdf2 = new Rfc2898DeriveBytes(input, salt, Iterations, HashAlgorithmName.SHA256))
            {
                byte[] computedHash = pbkdf2.GetBytes(HashSize);

                // Comparer les hachages
                return CryptographicEquals(storedHashBytes, computedHash);
            }
        }

        /// Génère un sel aléatoire pour sécuriser les mots de passe.\
        private static byte[] GenerateSalt()
        {
            byte[] salt = new byte[SaltSize];
            using (var rng = RandomNumberGenerator.Create())
            {
                rng.GetBytes(salt);
            }
            return salt;
        }

        /// Compare deux tableaux d'octets de manière sécurisée.
        private static bool CryptographicEquals(byte[] a, byte[] b)
        {
            if (a.Length != b.Length) return false;

            bool areEqual = true;
            for (int i = 0; i < a.Length; i++)
            {
                areEqual &= (a[i] == b[i]);
            }
            return areEqual;
        }
    }
}