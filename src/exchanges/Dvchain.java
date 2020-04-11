package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.dvchain.DVChainExchange;

public class Dvchain extends GenericExchange {
	public Dvchain() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(DVChainExchange.class.getName());
	}
}