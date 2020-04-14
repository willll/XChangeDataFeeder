package exchanges;

import org.knowm.xchange.ExchangeFactory;
import info.bitrich.xchangestream.coinbasepro.CoinbaseProStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Coinbasepro extends GenericStreamingExchange {
	public Coinbasepro() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinbaseProStreamingExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(CoinbaseProStreamingExchange.class.getName());
	}
}
