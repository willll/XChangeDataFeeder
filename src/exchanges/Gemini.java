package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.gemini.v1.GeminiExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.gemini.GeminiStreamingExchange;

public class Gemini extends GenericStreamingExchange {
	public Gemini() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(GeminiExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(GeminiStreamingExchange.class.getName());

		// Not supported : https://github.com/bitrich-info/xchange-stream
		this.tickerSupported = false;
	}
}
