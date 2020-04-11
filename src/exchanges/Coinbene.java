package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinbene.CoinbeneExchange;

public class Coinbene extends GenericExchange {
	public Coinbene() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinbeneExchange.class.getName());
	}
}