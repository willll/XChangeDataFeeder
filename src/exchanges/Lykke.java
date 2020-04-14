package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.lykke.LykkeExchange;

public class Lykke extends GenericExchange {
	public Lykke() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(LykkeExchange.class.getName());
	}
}