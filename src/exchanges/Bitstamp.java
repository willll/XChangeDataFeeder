package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;

import info.bitrich.xchangestream.bitstamp.BitstampStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bitstamp extends GenericStreamingExchange {
	public Bitstamp() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BitstampStreamingExchange.class.getName());
	}
}
