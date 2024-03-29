package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.lgo.LgoExchange;

import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.lgo.LgoStreamingExchange;

public class Lgo extends GenericStreamingExchange {
	public Lgo() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LgoExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(LgoStreamingExchange.class.getName());
	}
}
