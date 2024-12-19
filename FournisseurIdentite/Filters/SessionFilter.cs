using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using FournisseurIdentite.Services;

namespace FournisseurIdentite.Filters
{
    public class SessionFilter : IActionFilter
    {
        private readonly SessionService _sessionService;

        public SessionFilter(SessionService sessionService)
        {
            _sessionService = sessionService;
        }

        public void OnActionExecuting(ActionExecutingContext context)
        {
            // Vérifier si l'attribut est présent
            var requiresSession = context.ActionDescriptor.EndpointMetadata
                .OfType<RequiresSessionAttribute>()
                .Any();

            if (!requiresSession)
                return;

            var authorizationHeader = context.HttpContext.Request.Headers["Authorization"].FirstOrDefault();

            if (string.IsNullOrEmpty(authorizationHeader) || !authorizationHeader.StartsWith("Bearer "))
            {
                context.Result = new UnauthorizedObjectResult("aucune session trouve");
                return;
            }

            var token = authorizationHeader.Substring("Bearer ".Length).Trim();

            if (!_sessionService.ValidateToken(token, out var session))
            {
                context.Result = new UnauthorizedObjectResult("session invalide");
                return;
            }


            // Ajouter la session valide au contexte
            context.HttpContext.Items["UserSession"] = session;
        }

        public void OnActionExecuted(ActionExecutedContext context) { }
    }
}
