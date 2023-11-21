TO COMPILE THE PROJECT IN WINDOWS USE: buildProject.ps1
TO COMPILE THE PROJECT IN LINUX USE: buildProject.ps1
You might also extract the script from these and run it from the same directory where they're stored.

Then:

1. Run $ rmiregistry from /Server
2. Run $ java Server from /Server
3. Run $ java BuyerClient from /BuyerClient to access a buyer client instance.
4. Run $ java SellerClient from /SellerClient to access a seller client instance.

You might create new instances by running those commands in new terminals.

Type HELP to get a overview of the available commands.

From that point onwards, every command walks you through the necessary inputs.

For the signature, a client sends a dummy text file to the server, which then returns it signed. Then, the client imports the Certificate from the Server and verifies the signature. The signature can be tampered using the commented code in the UtilityHandlers.
