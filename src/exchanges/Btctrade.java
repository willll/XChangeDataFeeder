package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.btctrade.BTCTradeExchange;;

public class Btctrade extends GenericExchange {
	public Btctrade() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BTCTradeExchange.class.getName());
	}
}