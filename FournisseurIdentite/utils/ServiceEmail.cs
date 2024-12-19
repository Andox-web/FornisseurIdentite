using MailKit.Net.Smtp;
using MimeKit;
using System.Threading.Tasks;
namespace FournisseurIdentite.Utils
{
    public class ServiceEmail
    {
        private readonly string _smtpServer = "smtp.gmail.com"; // Remplacez par votre serveur SMTP
        private readonly int _smtpPort = 587; // Port SMTP (souvent 587 ou 465)
        private readonly string _smtpUsername = "Zotahiana691@gmail.com"; // Remplacez par votre adresse email
        private readonly string _smtpPassword = "mnyb jket tdbp anvv"; // Remplacez par votre mot de passe

        public async Task EnvoyerAsync(string toEmail, string sujet, string message)
        {
            var emailMessage = new MimeMessage();
            emailMessage.From.Add(new MailboxAddress("Nom de votre entreprise", _smtpUsername));
            emailMessage.To.Add(new MailboxAddress("", toEmail));
            emailMessage.Subject = sujet;

            var body = new TextPart("plain")
            {
                Text = message
            };

            emailMessage.Body = body;

            using (var client = new SmtpClient())
            {
                try
                {
                    // Connexion au serveur SMTP
                    await client.ConnectAsync(_smtpServer, _smtpPort, false);

                    // Authentification avec le serveur SMTP
                    await client.AuthenticateAsync(_smtpUsername, _smtpPassword);

                    // Envoi de l'email
                    await client.SendAsync(emailMessage);
                }
                catch (Exception ex)
                {
                    // Gérer l'exception, loggez ou re-throw selon le cas
                    Console.WriteLine($"Erreur lors de l'envoi de l'email: {ex.Message}");
                    throw;
                }
                finally
                {
                    // Déconnexion propre du client
                    await client.DisconnectAsync(true);
                    client.Dispose();
                }
            }
        }
    }
}