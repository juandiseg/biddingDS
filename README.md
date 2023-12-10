This coursework consists of the implementation of an auctioning system using java RMI. The system supports two types of users, buyers and sellers, and provides access to different functionality to each of them. The auctioning system relies on publishing items matching a reference, i.e.: sellers can only publish items which are registered in the system. The system provides the logic for creating, browsing and closing basic/forward and double auctions. It also provides digital signature, server replication and fault tolerance mechanisms so the system becomes more secure, available and reliable.

Prerequisites:

1. Compile the code, run $ ./buildProject.sh
2. Have an RMI registry instance running, run $ ./runRegistry.sh
3. Start the server by running $ ./runServer.sh
   -> You can run this script multiple times to create more replicas of the server.
   -> New instances should print "Data Synch'ed", if not terminate them. This error is caused by new replicas having the same name as previous ones.

Once the server is running you may choose one of two options:

· Access the SELLER interface running $ ./runSeller
· Access the BUYER interface running $ ./runBuyer

In those interfaces enter "HELP" to get a list of the available commands. From that point onwards, every command walks you through the necessary inputs.

To verify that we are contacting the right server, you may run CHECK SIGNATURE to verify the server's identity. The client sends a dummy text file to the server, which then returns it signed. Then, the client imports the Certificate from the Server and verifies the signature.

To verify the signature check would tell you if the data has been tampered uncomment the code in the UtilityHandlers.

At this point, you might have as many instance of buyer/sellers and servers as you desire as long as there is one server instance running.
