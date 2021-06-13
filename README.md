# Blockchain Real-estate App

A proof-of-concept real-estate management application secured with a Proof by Authority Blockchain. Demonstrates a
possible use-case of blockchain to secure the full real-estate lifecycle, from seller to buyer, to bank.

This application was built as an extension project for the class `Security in Computing & IT (COSC2536)` offered by RMIT
in Semester 1, 2021.

This project will be open-sourced on the 15th of May 2021.

## How to start

1. Import the project into your favorite IDE.
2. Run Maven install to download all dependancies.
3. Create a profile to run the main method in App.java.
4. Provide the following console arguments depending on your needs:
    - `--port=<port>` - port number this application will run with.
    - `--admin` - if this flag is set, the instance will start with the ability to sign new blocks onto the network.

You can also do `mvn javafx:run` to run the app.

## Limitations:

- No secure way to distribute keys (possible with a conventional DB and login system)
- Limited P2P network protocol (max 10 clients, with more leading to slow connection speeds)
- P2P network is limited to localhost. Can be fixed by replacing with a reputable P2P networking framework.
- PDF files are not distributed on the network (too large). Could be signed and distributed through conventional means.

