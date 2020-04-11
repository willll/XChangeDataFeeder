package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CexioFactory extends GenericStreamingFactory {

	CexioFactory() {
		exchange = Exchanges.CEXIO;
		ticker_pub = "CEXIO_TICKER_PUB";
		orderbook_pub = "CEXIO_ORDERBOOK_PUB";
	}
}