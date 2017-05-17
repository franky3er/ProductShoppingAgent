service ShopService {
	void ping();
	i64 fetchProductPrice(1: string productName, 2: string productAmount),
	bool buyProduct(1: string productName, 2: string productAmount, 3: string deliveryAddress)
}
