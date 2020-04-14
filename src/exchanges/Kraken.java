package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.kraken.KrakenExchange;

import info.bitrich.xchangestream.kraken.KrakenStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Kraken extends GenericStreamingExchange {
	public Kraken() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(KrakenStreamingExchange.class.getName());
	}
}
