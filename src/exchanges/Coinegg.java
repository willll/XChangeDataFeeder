package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinegg.CoinEggExchange;;

public class Coinegg extends GenericExchange {
	public Coinegg() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinEggExchange.class.getName());
	}
}