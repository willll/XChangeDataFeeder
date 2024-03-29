package exchanges;

import org.knowm.xchange.ExchangeFactory;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.gemini.GeminiStreamingExchange;

public class Gemini extends GenericStreamingExchange {
	public Gemini() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(GeminiStreamingExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(GeminiStreamingExchange.class.getName());
	}
}
