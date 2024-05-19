# Multi_Chat_lso

## Note
At the moment the server is on an anstance of AWS EC2 whicj is turned off to stay into the free tier. For the demo i will turn on the instance but if you want to try hosting the server follow these steps:

### Server
1. Download the libpq-dev with "sudo apt-get install libpq-dev"
2. Compile the file server.c with "gcc -o server server.c -lpq"
3. start the server with "./server"

### Client
4. In the android studio client, in the class User at row 13 change the host from the EC2 one to "localhost"
5. Build the project in android studio and start the emulator

### Figma
Click [here](https://www.figma.com/proto/M0RoN6CjIb358u1pV8Ur6L?page-id=0%3A1&type=design&node-id=0-1&t=CjQleUoEpKAEMq9a-0&scaling=scale-down&starting-point-node-id=130%3A953) the see the Figma prototype

:)
