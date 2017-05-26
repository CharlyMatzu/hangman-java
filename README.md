# AhorcadoGame-Sockets

Juego del 'ahorcado' en socket basado en la arquitectura Bus de eventos (EventBus) el cual se compone de:
-Servidor logico del juego: encargado de la logica misma del juego
-Cliente MVC: Los jugadores quienes interactuan con el servidor 
-Broker de mensajes: encargado de comunicar a todos los elementos entre si. El servidor y los jugadores no se comunican directamente, toda la información pasa por el Broker el cual tiene la lista y las direcciones fisicas de cada socket correspondiente a cada elemento.
