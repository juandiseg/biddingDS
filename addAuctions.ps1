cd .\SellerClient\
java SellerClient ADD 1 1 title-item-1 description-test 2 29.99 39.99
java SellerClient ADD 1 2 title-item-2 description-test 2 29.99 39.99
java SellerClient ADD 2 1 title-item-1 seconDescription-test 0 4.99 14.99

cd ..\BuyerClient\

java BuyerClient GET AUCTIONS
java BuyerClient GET ITEMS
java BuyerClient GET ITEM AUCTIONS 1
java BuyerClient BID 1 username address@email.com 30.99
java BuyerClient BID 1 usernameTwo address@email.com 31.99
java BuyerClient BID 1 username address@email.com 32.99
java BuyerClient BID 2 usernameThree address@email.com 19.99

cd ..
