package exchanges;

import org.knowm.xchange.ExchangeFactory;
import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Hitbtc extends GenericStreamingExchange {
	public Hitbtc() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(HitbtcStreamingExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(HitbtcStreamingExchange.class.getName());
	}
}
