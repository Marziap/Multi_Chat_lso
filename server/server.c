#include "mylibrary.h"


int enableKeepAlive(int socket);
PGresult *executeQuery(const char *query, PGconn *conn, int clientSocket);
void printQueryResults(PGresult *result, int clientSocket);
void login(PGconn *conn, int clientSocket, char * username);
void isInRoom(PGconn *conn, int clientSocket, char * user_id, char* room_id);
void getId(PGconn *conn, int clientSocket, char * username);
void getRoomId(PGconn *conn, int clientSocket, char * name);
void registra(PGconn *conn, int clientSocket, char * username, char *password);
void declineRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id);
void acceptRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id);
void sendRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id);
void creaStanza(PGconn *conn, int clientSocket, char * nome, char * ammi_id);
void sendMessages(PGconn *conn, int clientSocket, char * user_id, char * room_id, char* message);
void addToStanza(PGconn *conn, int clientSocket, char* user_id, char* room_id);
void getAllStanze(PGconn *conn, int clientSocket);
void getMessages(PGconn *conn, int clientSocket, char* room_id);
void getWaitlist(PGconn *conn, int clientSocket, char* ammi_id);
void getUsers(PGconn *conn, int clientSocket);
void getMieStanze(PGconn *conn, int clientSocket);
void separaStringhe(const char *inputString, char ***outputArray, int *arraySize);
void switchCase(PGconn *conn, int clientSocket, char **words);
void handleClient(int clientSocket, struct sockaddr_in clientAddr, PGconn *conn);

int main()
{
    //connection string
    char connectionString[SIZE];
    snprintf(connectionString, SIZE, "host=%s port=%s dbname=%s user=%s password=%s sslmode=disable",
             DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASS);

    // Connessione al database
    PGconn *conn = PQconnectdb(connectionString);
    if (PQstatus(conn) != CONNECTION_OK)
    {
        fprintf(stderr, "Connessione al database fallita: %s\n", PQerrorMessage(conn));
        PQfinish(conn);
        exit(1);
    }else{
        printf("\nConnessione al db riuscita\n");
        }

    int serverSocket, clientSocket;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t addrLen = sizeof(clientAddr);

    // Creazione del socket del server
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1)
    {
        perror("Errore nella creazione del socket");
        exit(1);
    }

    // Configurazione dell'indirizzo del server
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY); // Ascolta su tutte le interfacce
    serverAddr.sin_port = htons(PORT);

    // Binding del socket del server all'indirizzo specificato
    if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == -1)
    {
        perror("Errore nel binding del socket");
        exit(1);
    }

        enableKeepAlive(serverSocket);


    // Inizio ad ascoltare sul socket del server
    if (listen(serverSocket, BACKLOG) == -1)
    {
        perror("Errore nell'ascolto sul socket");
        exit(1);
    }

    printf("Server in ascolto sulla porta %d...\n", PORT);

    // Accetta le connessioni in entrata
    while (1)
    {
        // Accetta la connessione
        clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &addrLen);
        if (clientSocket == -1)
        {
            perror("Errore nell'accettazione della connessione");
            exit(1);
        }

        // Fork del processo per gestire la connessione con il client
        pid_t pid = fork();
        if (pid == -1)
        {
            perror("Errore nella creazione del processo figlio");
            exit(1);
        }
        else if (pid == 0)
        {//processo figlio
            close(serverSocket);
            handleClient(clientSocket, clientAddr, conn);
            PQfinish(conn);
            exit(0);
        }
        else
        {
            // Processo padre
            close(clientSocket);
        }
    }

    // Chiusura del socket del server
    close(serverSocket);

    // Chiusura della connessione con il database
    PQfinish(conn);

    return 0;

}