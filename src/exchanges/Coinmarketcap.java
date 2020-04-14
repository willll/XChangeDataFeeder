package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinmarketcap.deprecated.v2.CoinMarketCapExchange;

public class Coinmarketcap extends GenericExchange {
	public Coinmarketcap() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinMarketCapExchange.class.getName());
	}
}