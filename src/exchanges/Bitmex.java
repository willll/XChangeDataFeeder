package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitmex.BitmexExchange;

import info.bitrich.xchangestream.bitmex.BitmexStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bitmex extends GenericStreamingExchange {
	public Bitmex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitmexExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BitmexStreamingExchange.class.getName());
	}
}
