using System;
using System.Security.Cryptography;
using System.Text;
using Microsoft.EntityFrameworkCore;

namespace FournisseurIdentite.Utils
{
    public class EnvironmentVariable
    {
        public static int GetEnvironmentVariable(string key, int defaultValue)
        {
            string? value = Environment.GetEnvironmentVariable(key);
            return int.TryParse(value, out int result) ? result : defaultValue;
        }
    }
}