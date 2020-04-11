package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.btcmarkets.BTCMarketsExchange;;

public class Btcmarkets extends GenericExchange {
	public Btcmarkets() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BTCMarketsExchange.class.getName());
	}
}