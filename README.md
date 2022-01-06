# Challenge-Alkemy
API REST con Spring boot.

## Permite dar de alta,modificar,obtener y eliminar personajes,peliculas y sus géneros del mundo de Disney.
Para acceder a los endpoints el usuario debe registrarse en la API.
Ejemplo en Postman:

![login](https://user-images.githubusercontent.com/50058025/148308138-dc8ef87c-d619-41ed-8531-f459db1db548.png)

. Ejemplo en Swagger2:
![crearUsuarioswagger](https://user-images.githubusercontent.com/50058025/148323718-ff09404f-9bed-44da-b88e-1c8cc0be9eab.png)

Una vez registrado puede hacer login con userName y password.
![nuevologin](https://user-images.githubusercontent.com/50058025/148308834-5fe9a2c5-c580-4031-8b94-a08490e9da61.png)

. En Swagger2:
![loginswagger](https://user-images.githubusercontent.com/50058025/148323872-46c1b850-5daa-49e1-83aa-d64a082148fd.png)

Una vez generado el token debe enviarlo en el header de las peticiones.
Ejemplo en Swagger2:
![botonauthorize](https://user-images.githubusercontent.com/50058025/148324043-84995b9c-41f1-4e21-bf46-900e8bbb1988.png)
Seleccione el botón Authorize. Agregue la palabra Bearer y separado por un espacion el token generado.
![ingresartokenSwagger](https://user-images.githubusercontent.com/50058025/148324222-36473675-0dc2-4624-9d16-33833cb2db6e.png)

. Enviar token en Postman. Agregar Authorization en el header de la petición.
Como valor enviar Bearer seguido de espacio y el token generado.
![envioPostman](https://user-images.githubusercontent.com/50058025/148325101-68b64dc6-3f2c-485e-ad34-ef8e0dec1fec.png)

### Una vez ingresado nuestro token podemos agregar,modificar filtrar y eliminar nuestras películas y personajes.
Ejemplo --> obtener todas las películas:
## En Postman:
![allpostman](https://user-images.githubusercontent.com/50058025/148326522-82cadec6-5024-4e8e-bfaf-401159d4fe14.png)
## En Swagger2:
![allswagger2](https://user-images.githubusercontent.com/50058025/148326636-b7ba27ac-0580-4d12-8db1-a0fb20a32384.png)

## Alta de una pelicula:
 ![Altapeliculaswagger](https://user-images.githubusercontent.com/50058025/148327880-e7e5daa5-8069-458c-a544-8d192053092f.png)
 
 Podemos omitir los personjes y el genero y enviar la película con esta estructura:
 ![altapeliculasimple](https://user-images.githubusercontent.com/50058025/148327991-615c8316-ec6e-4cc1-93a7-d34da63300a1.png)
 
 ### Asignar una imagen para un personaje:
 ![enviarfotoperso](https://user-images.githubusercontent.com/50058025/148328468-da72a787-8db0-44be-84bc-a0aa81a09c2b.png)

### Podemos filtrar nuestras películas(solo aplica un parámetro de búsqueda):
![filtrarpostman](https://user-images.githubusercontent.com/50058025/148329143-815d29e2-3753-4b62-bf4e-271d0463d385.png)

También nuestros personajes(también aplica un parámetro de búsqueda):
![filtrarswagger](https://user-images.githubusercontent.com/50058025/148329261-18dd57e8-6091-4455-8b8e-343d1c0ef35e.png)






