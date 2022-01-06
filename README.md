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





