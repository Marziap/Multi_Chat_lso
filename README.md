# Multi_Chat_lso

## Note
Al momento il server si trova su un'istanza di AWS EC2 spenta (per rientrare nella free tier). L'istanza verrà accesa il giorno della demo ma se si vuole provare l'app mentre l'istanza è spenta basta seguire i seguenti passaggi:

### Server
1. Scaricare la libreria libpq-dev con il seguente comando "sudo apt-get install libpq-dev"
2. Compilare il file server.c con il seguente comando "gcc -o server server.c -lpq"
3. Avviare il server attraverso il comando "./server"

### Client
4. Nel client android studio, nella classe User (riga 13) cambiare il campo host della connessione dall'indirizzo dell'istanza EC2 a "localhost"
5. Fare build del progetto in android studio e far partire l'emulatore

### Figma
Clicca [qui]([https://www.figma.com/file/M0RoN6CjIb358u1pV8Ur6L/ChatLSO?type=design&node-id=0%3A1&mode=design&t=CjQleUoEpKAEMq9a-1](https://www.figma.com/proto/M0RoN6CjIb358u1pV8Ur6L/ChatLSO?type=design&node-id=1-15&t=Sk2jqIoSamdAoyh6-0&scaling=scale-down&page-id=0%3A1&starting-point-node-id=130%3A953)https://www.figma.com/proto/M0RoN6CjIb358u1pV8Ur6L/ChatLSO?type=design&node-id=1-15&t=Sk2jqIoSamdAoyh6-0&scaling=scale-down&page-id=0%3A1&starting-point-node-id=130%3A953) per vedere il prototipo in Figma

:)
