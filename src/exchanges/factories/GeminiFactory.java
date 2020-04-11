package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class GeminiFactory extends GenericStreamingFactory {

	GeminiFactory() {
		exchange = Exchanges.GEMINI;
		ticker_pub = "GEMINI_TICKER_PUB";
		orderbook_pub = "GEMINI_ORDERBOOK_PUB";
	}
}