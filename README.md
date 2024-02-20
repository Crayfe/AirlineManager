# AirlineManager
App desarrollada en Java integrando tres capas distintas, desde una base de datos hasta una interfaz gráfica. Como ejemplo de caso real se propone una aplicación que permita la gestión de aeropuertos, vuelos y billetes de
avión.

Existen dos formas de correr la aplicación:
- Aplicación monolítica: La interfaz gráfica y la base de datos forman parte de la misma aplicación.
- Aplicación cliente-servidor: La interfaz gráfica y la base de datos están separados en dos programas diferentes: cliente y vervidor.

## Aplicación monolítica
Para la aplicación monolítica basta con ejecutar el proyecto ubicado en el directorio MonoliticAirlineManager.

## Aplicación cliente-servidor
Es necesario ejecutar dos programas diferentes y no necesariamente en la misma máquina. Cuando se inicie el cliente este hará las consultas a través de una api implementada en la parte del servidor.
