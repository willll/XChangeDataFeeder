package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.okcoin.OkCoinExchange;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.okcoin.OkCoinStreamingExchange;

public class Okcoin extends GenericStreamingExchange {
	public Okcoin() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(OkCoinExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(OkCoinStreamingExchange.class.getName());
	}
}
