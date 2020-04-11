package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v2.BitfinexExchange;

import info.bitrich.xchangestream.bitfinex.BitfinexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bitfinex extends GenericStreamingExchange {
	public Bitfinex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BitfinexStreamingExchange.class.getName());
	}
}
