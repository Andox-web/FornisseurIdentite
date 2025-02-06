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

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowLocalhost", builder =>
    {
        builder.WithOrigins("http://localhost:8082")  // Allow frontend origin
               .AllowAnyMethod()
               .AllowAnyHeader();
    });
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

app.UseCors("AllowLocalhost"); 

// app.UseCors(
//         options => options.WithOrigins("http://localhost:8082").AllowAnyMethod()
// );

app.UseAuthorization();

app.MapControllers();

app.Run();