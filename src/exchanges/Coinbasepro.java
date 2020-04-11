package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinbasepro.CoinbaseProExchange;

public class Coinbasepro extends GenericStreamingExchange {
	public Coinbasepro() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinbaseProExchange.class.getName());
	}
}
