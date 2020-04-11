package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.hitbtc.HitbtcStreamingExchange;

public class Hitbtc extends GenericStreamingExchange {
	public Hitbtc() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(HitbtcExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(HitbtcStreamingExchange.class.getName());
	}
}
