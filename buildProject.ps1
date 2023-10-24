rm */*.class

cd .\Server\
javac *.java
cp .\iUser.class ..\SellerClient\
cp .\iUser.class ..\BuyerClient\
cp .\AuctionItem.class ..\SellerClient\
cp .\AuctionItem.class ..\BuyerClient\

cd ..\BuyerClient
javac *.java
cd ..\SellerClient
javac *.java
cd ..
