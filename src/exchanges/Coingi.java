package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coingi.CoingiExchange;

public class Coingi extends GenericExchange {
	public Coingi() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoingiExchange.class.getName());
	}
}