package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cexio.CexIOExchange;

import info.bitrich.xchangestream.cexio.CexioStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;;

public class Cexio extends GenericStreamingExchange {
	public Cexio() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CexIOExchange.class.getName());
		this.streamingExchange = StreamingExchangeFactory.INSTANCE
				.createExchange(CexioStreamingExchange.class.getName());
	}
}
