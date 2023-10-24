rm */*.class

cd .\Server\
javac *.java
cp .\iSeller.class ..\SellerClient\
cp .\iBuyer.class ..\BuyerClient\
cp .\AuctionItem.class ..\SellerClient\
cp .\AuctionItem.class ..\BuyerClient\

cd ..\BuyerClient
javac *.java
cd ..\SellerClient
javac *.java
cd ..
