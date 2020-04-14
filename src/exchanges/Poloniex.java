package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.poloniex.PoloniexExchange;

import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Poloniex extends GenericStreamingExchange {
	public Poloniex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(PoloniexStreamingExchange.class.getName());
	}
}
