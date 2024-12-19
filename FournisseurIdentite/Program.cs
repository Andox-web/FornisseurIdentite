using FournisseurIdentite.Database;
using Npgsql.EntityFrameworkCore.PostgreSQL;
using Microsoft.EntityFrameworkCore;
using FournisseurIdentite.Utils;
using FournisseurIdentite.Filters;
using FournisseurIdentite.Services;
using System.Reflection;
using Npgsql;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddControllers(options =>
{
    options.Filters.Add<SessionFilter>();
});
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseLowerCaseNamingConvention()
            .UseNpgsql(Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection")));

builder.Services.RegisterServices(Assembly.GetExecutingAssembly(), "FournisseurIdentite.Services");

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();