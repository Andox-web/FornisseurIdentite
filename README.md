# FornisseurIdentite
pour demarer , lancer dockercompose -d --build 

-### Inscription (zo)
    -url:http://localhost:5000/inscription
    -donnee : json de utilisateur
    -method : Post
    -responce: 
        -valider: message de confirmation avec id ,email,nom et demande de confirmer via email (mail contenant lien de Confirmation inscription avec ces donnee)
        -refuser: message d'erreur et renvoye le json utilisateur 
            -erreur possible:
                -email deja lier a un compte
                -donner insuffisant(nom,mdp ou email vide)
                -probleme lors de l'envoye de l'email

-### Confirmation inscription (zo)
    --url:http://localhost:5000/confirmerInscription
    -donnee : email,code_creation 
    -method : Get
    -responce: 
        -valider: message de confirmation d'insertion avec l'user et un token d'action  
        -refuser: message d'erreur et renvoye le json utilisateur 
            -erreur possible:
                -email deja lier a un compte
                -donner insuffisant(nom,mdp ou email vide)
                -probleme lors de l'envoye de l'email

-### Login
    -url:http://localhost:5000/login (manoa)
    -donnee : json de utilisateur (avec mdp et email)
    -method : Post
    -responce: 
        -valider: message de confirmation disons de confirmer l'identite grace au pin donner au email 
        -refuser: message d'erreur  
            -erreur possible:
                -mdp eronnee
                -email non reconnue 
                -compte bloque

-### Authentification
    -url:http://localhost:5000/Authentification (ando)
        -donnee : pin donnee dans email
        -method : Post
        -responce: 
            -valider: message de confirmation disons la connection est valide, il est connecte et un token d'action
            -refuser: message d'erreur  
                -erreur possible:
                    -pin invalide
                    -delai du pin depasse

-### Demande de reinitialisation 
    -url:http://localhost:5000/demandeReinitialisation (manoa)
        -donnee : email 
        -method : Get
        -responce: 
            -valider: message de confirmation disons que la demande de reintilatisation a ete envoyer a l'email (contenant lien de reinitialisisation avec ces donnee)
            -refuser: message d'erreur  
                -erreur possible:
                    -erreur envoye mail
                    -email non reconnue

-### Reinitialisation nd tentative
    -url:http://localhost:5000/reinitialisation (ando)
        -donnee : email , code_reinitialisation
        -method : Get
        -responce: 
            -valider: message de confirmation disons que le compte est reinitialiser 
            -refuser: message d'erreur  
                -erreur possible:
                    -code_reinitialisation invalide/expire
                    -email non reconnue

-### Modification des infos users (mikajy)
    -url:http://localhost:5000/reinitialisation
        -header : token
        -donnee: json utilisateur(avec seulement seulement les donnee a modifier) , email ,mot de pass
        -responce: 
            -valider: message de confirmation disons que l'utilisateur a ete modifier 
            -refuser: message d'erreur  
                -erreur possible:
                    -mot de pass erronne
                    -email non reconnue
                    -token invalide
                    -donnee a changer invalide(email)

-### Suppresion des infos users (mikajy)
    -url:http://localhost:5000/supprimerUtilisateur
        -header : token
        -donnee: email, mot de pass
        -responce: 
            -valider: message de confirmation disons que le compte a ete supprimer 
            -refuser: message d'erreur  
                -erreur possible:
                    -mot de pass erronne
                    -email non reconnue
                    -token invalide