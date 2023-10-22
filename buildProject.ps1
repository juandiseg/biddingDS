rm */*.class

cd .\Server\
javac *.java
cp .\ICalc.class ..\Client\
cp .\AuctionItem.class ..\Client\

cd ..\Client
javac *.java
cd ..