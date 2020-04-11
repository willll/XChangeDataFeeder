package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinmate.CoinmateExchange;

import info.bitrich.xchange.coinmate.CoinmateStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Coinmate extends GenericStreamingExchange {
	public Coinmate() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinmateExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(CoinmateStreamingExchange.class.getName());

		// Not supported : https://github.com/bitrich-info/xchange-stream
		this.tickerSupported = false;
	}
}
