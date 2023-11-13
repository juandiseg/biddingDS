rm */*.class

javac Server\*.java

cp .\Server\iSeller.class .\SellerClient
cp .\Server\iUser.class .\SellerClient
cp .\Server\User.class .\SellerClient
cp .\Server\iBuyer.class .\BuyerClient
cp .\Server\iUser.class .\BuyerClient
cp .\Server\User.class .\BuyerClient


cp .\Server\AuctionItem.class .\SellerClient
cp .\Server\AuctionItem.class .\BuyerClient

cd .\BuyerClient\
javac *.java
cd ..\SellerClient\
javac *.java
cd ..
