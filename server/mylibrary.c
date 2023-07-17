#include "/usr/include/postgresql/libpq-fe.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/wait.h>

#define PORT 5555
#define BACKLOG 100
#define SIZE 5000
#define DB_HOST "ls-0c0324022fc57bdfe683e66d46fc414415d15cb0.cngbiakr7x0v.eu-central-1.rds.amazonaws.com"
#define DB_PORT "5432"
#define DB_NAME "dbchat"
#define DB_USER "postgres"
#define DB_PASS "*;:N;l^g9YU[u2ablnxdt.|A.-L(giRr"


int enableKeepAlive(int socket) {
    int optval = 1;
    int optlen = sizeof(optval);
    return setsockopt(socket, SOL_SOCKET, SO_KEEPALIVE, &optval, optlen);
}

PGresult *executeQuery(const char *query, PGconn *conn, int clientSocket)
{
    PGresult *result = PQexec(conn, query);

    if (PQresultStatus(result) != PGRES_COMMAND_OK && PQresultStatus(result) != PGRES_TUPLES_OK)
    {
        char errorMessage[SIZE];
        snprintf(errorMessage, SIZE, "Errore nell'esecuzione della query: %s", PQerrorMessage(conn));
        send(clientSocket, errorMessage, strlen(errorMessage), 0);
        PQclear(result);
        return NULL;
    }
    else
    {
        return result;
    }
}

void printQueryResults(PGresult *result, int clientSocket)
{
    int rows = PQntuples(result);
    int columns = PQnfields(result);

    char buffer[SIZE];
    memset(buffer, 0, SIZE);

    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < columns; j++)
        {
            char *value = PQgetvalue(result, i, j);
            strcat(buffer, value);
            if (j < columns - 1) {
                strcat(buffer, "&");
            }
        }
        if (i < rows - 1) {
            strcat(buffer, "@");
        }
    }

    fflush(stdout);
    strcat(buffer, "\n");
//    printf("\nDa inviare al client: %s", buffer);
    send(clientSocket, buffer, strlen(buffer), 0);
}



void login(PGconn *conn, int clientSocket, char * username)
{
char query[SIZE];
 snprintf(query, SIZE, "SELECT password FROM users WHERE username = '%s';", username);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void isInRoom(PGconn *conn, int clientSocket, char * user_id, char* room_id)
{
char query[SIZE];
 snprintf(query, SIZE, "SELECT * FROM users_in_rooms WHERE user_id = %s AND room_id = %s;", user_id, room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getId(PGconn *conn, int clientSocket, char * username)
{
char query[SIZE];
 snprintf(query, SIZE, "SELECT id FROM users WHERE username = '%s';", username);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getRoomId(PGconn *conn, int clientSocket, char * name)
{
char query[SIZE];
//printf("room_name: |%s|", name);
 snprintf(query, SIZE, "SELECT id FROM rooms WHERE name = '%s';", name);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void registra(PGconn *conn, int clientSocket, char * username, char *password)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO users (username, password) VALUES ('%s', '%s');", username, password);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    { //  printf("\nregistrazione eseguita");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}


void declineRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id)
{
char query[SIZE];
 snprintf(query, SIZE, "DELETE FROM waitlist WHERE user_id = %s AND room_id = %s;", user_id, room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nrequest deleted");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}

void acceptRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO users_in_rooms (user_id, room_id) VALUES (%s, %s);", user_id, room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nrequest accepted");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}

void sendRequest(PGconn *conn, int clientSocket, char * user_id, char *room_id)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO waitlist (user_id, room_id) VALUES (%s, %s);", user_id, room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nrichiesta inviata");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}


void creaStanza(PGconn *conn, int clientSocket, char * nome, char * ammi_id)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO rooms (name, ammi_id) VALUES ('%s', %s);", nome, ammi_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nstanza creata");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}

void sendMessages(PGconn *conn, int clientSocket, char * user_id, char * room_id, char* message)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO messages (user_id, room_id, message) VALUES (%s, %s, '%s');", user_id, room_id, message);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nmessaggio inviato");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}

void addToStanza(PGconn *conn, int clientSocket, char* user_id, char* room_id)
{
char query[SIZE];
 snprintf(query, SIZE, "INSERT INTO users_in_rooms (user_id, room_id) VALUES (%s, %s);", user_id, room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {  // printf("\nutente inserito");
        send(clientSocket, "ok\n", strlen("ok\n"), 0);
        PQclear(result);
    }
}

void getAllStanze(PGconn *conn, int clientSocket)
{
char query[SIZE];
 strcpy(query, "SELECT name FROM rooms;");

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getMessages(PGconn *conn, int clientSocket, char* room_id)
{
char query[SIZE];
 sprintf(query, "SELECT username, message FROM messages JOIN users ON messages.user_id = users.id WHERE room_id = %s ORDER BY timestamp ASC;", room_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getWaitlist(PGconn *conn, int clientSocket, char* ammi_id)
{
char query[SIZE];
 sprintf(query, "SELECT username, name FROM waitlist JOIN rooms ON waitlist.room_id = rooms.id JOIN users ON waitlist.user_id = users.id WHERE ammi_id = %s;", ammi_id);

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getUsers(PGconn *conn, int clientSocket)
{
char query[SIZE];
 strcpy(query, "SELECT username FROM users;");

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}


void getMieStanze(PGconn *conn, int clientSocket)
{
char query[SIZE];
 strcpy(query, "SELECT name FROM rooms;");

    PGresult *result = executeQuery(query, conn, clientSocket);
    if (result)
    {
        printQueryResults(result, clientSocket);
        PQclear(result);
    }
}

void separaStringhe(const char *inputString, char ***outputArray, int *arraySize)
{
    // Conteggio delle parole nella stringa
    int wordCount = 1;
    for (int i = 0; inputString[i] != '\0'; i++)
    {
        if (inputString[i] == '%')
        {
            wordCount++;
        }
    }

    // Allocazione dell'array di stringhe
    char **words = (char **)malloc(wordCount * sizeof(char *));

    // Split della stringa
    char *token = strtok((char *)inputString, "%");
    int index = 0;
    while (token != NULL)
    {
        int wordSize = strlen(token) + 1;
        words[index] = (char *)malloc(wordSize);
        strncpy(words[index], token, wordSize);

        token = strtok(NULL, "%");
        index++;
    }

    *outputArray = words;
    *arraySize = wordCount;
}


void switchCase(PGconn *conn, int clientSocket, char **words){

int opzione = atoi(words[0]);

switch(opzione){
case 0: {
getId(conn, clientSocket, words[1]);
break;
}
case 1:{

login(conn, clientSocket, words[1]);
break;
}

case 2:{
registra(conn, clientSocket, words[1], words[2]);
break;
}

case 3:{
getMieStanze(conn, clientSocket);
break;
}

case 4:{
getAllStanze(conn, clientSocket);
break;
}

case 5:{
sendMessages(conn, clientSocket, words[1], words[2], words[3]);
break;
}

case 6:{
sendRequest(conn, clientSocket, words[1], words[2]);
break;
}

case 7:{
creaStanza(conn, clientSocket, words[1], words[2]);
break;
}

case 8:{
getWaitlist(conn, clientSocket, words[1]);
break;
}

case 9:{
getMessages(conn, clientSocket, words[1]);
break;
}

case 10:{
getUsers(conn, clientSocket);
break;
}

case 11:{
addToStanza(conn, clientSocket, words[1], words[2]);
break;
}

case 12:{
getRoomId(conn, clientSocket, words[1]);
break;
}

case 13:{
isInRoom(conn, clientSocket, words[1], words[2]);
break;
}

case 14:{
declineRequest(conn, clientSocket, words[1], words[2]);
break;
}

case 15:{
acceptRequest(conn, clientSocket, words[1], words[2]);
break;
}

default:{

send(clientSocket, "opzione non valida", strlen("opzione non valida"), 0);
break;

}

}

}


void handleClient(int clientSocket, struct sockaddr_in clientAddr, PGconn *conn)
{
    char buffer[SIZE];
    int bytesRead;

    // Ottieni l'indirizzo IP del client
    char clientIP[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &(clientAddr.sin_addr), clientIP, INET_ADDRSTRLEN);
   // printf("\nClient connected: %s\n", clientIP);

    while ((bytesRead = recv(clientSocket, buffer, SIZE, 0)) > 0)
    {
        char **words;
        int wordCount;

        separaStringhe(buffer, &words, &wordCount);

        switchCase(conn, clientSocket, words);

        for (int i = 0; i < wordCount; i++)
        {
                free(words[i]);
        }
                free(words);

        // Pulisci il buffer
        memset(buffer, 0, sizeof(buffer));
    }

    if (bytesRead == 0)
    {
     //   printf("\nClient disconnected: %s\n", clientIP);
    }
    else if (bytesRead == -1)
    {
        perror("Errore nella lettura dal socket");
    }

    close(clientSocket);
    exit(0);
}
