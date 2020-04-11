package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitflyer.BitflyerExchange;

import info.bitrich.xchangestream.bitflyer.BitflyerStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Bitflyer extends GenericStreamingExchange {
	public Bitflyer() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitflyerExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(BitflyerStreamingExchange.class.getName());
	}
}
