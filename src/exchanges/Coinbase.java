package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinbase.CoinbaseExchange;

public class Coinbase extends GenericExchange {
	public Coinbase() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinbaseExchange.class.getName());
	}
}