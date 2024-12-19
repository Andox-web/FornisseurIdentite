using Microsoft.AspNetCore.Mvc.Filters;

namespace FournisseurIdentite.Filters
{
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method)]
    public class RequiresSessionAttribute : Attribute, IFilterMetadata { }
}
