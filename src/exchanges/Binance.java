package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.binance.BinanceExchange;

import info.bitrich.xchangestream.binance.BinanceStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Binance extends GenericStreamingExchange {
	public Binance() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BinanceStreamingExchange.class.getName());
	}
}
