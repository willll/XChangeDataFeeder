package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bankera.BankeraExchange;

import info.bitrich.xchangestream.bankera.BankeraStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bankera extends GenericStreamingExchange {
	public Bankera() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BankeraExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BankeraStreamingExchange.class.getName());
	}
}
