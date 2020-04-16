package exchanges;

import org.knowm.xchange.ExchangeFactory;

import info.bitrich.xchangestream.cexio.CexioStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;

public class Cexio extends GenericStreamingExchange {
	public Cexio() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CexioStreamingExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(CexioStreamingExchange.class.getName());
	}
}
