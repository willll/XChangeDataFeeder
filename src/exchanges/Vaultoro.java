package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.vaultoro.VaultoroExchange;

public class Vaultoro extends GenericExchange {
	public Vaultoro() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(VaultoroExchange.class.getName());
	}
}