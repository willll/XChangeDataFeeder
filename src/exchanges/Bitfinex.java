package exchanges;

import org.knowm.xchange.ExchangeFactory;
import info.bitrich.xchangestream.bitfinex.BitfinexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bitfinex extends GenericStreamingExchange {
	public Bitfinex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexStreamingExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BitfinexStreamingExchange.class.getName());
	}
}
