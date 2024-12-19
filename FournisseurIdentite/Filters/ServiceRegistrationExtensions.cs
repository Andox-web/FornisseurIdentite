using Microsoft.Extensions.DependencyInjection;
using System.Reflection;

namespace FournisseurIdentite.Filters
{
    public static class ServiceRegistrationExtensions
    {
        public static void RegisterServices(this IServiceCollection services, Assembly assembly, string @namespace)
        {
            var typesToRegister = assembly.GetTypes()
                .Where(type => type.Namespace != null
                               && type.Namespace.StartsWith(@namespace)
                               && type.IsClass
                               && !type.IsAbstract);

            foreach (var type in typesToRegister)
            {
                services.AddScoped(type); 
            }
        }
    }
}
