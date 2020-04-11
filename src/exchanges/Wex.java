package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.wex.v3.WexExchange;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.wex.WexStreamingExchange;

public class Wex extends GenericStreamingExchange {
	public Wex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(WexExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(WexStreamingExchange.class.getName());

		// Not supported : https://github.com/bitrich-info/xchange-stream
		this.tickerSupported = false;
	}
}
