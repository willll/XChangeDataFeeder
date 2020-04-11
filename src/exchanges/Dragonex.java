package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.dragonex.DragonexExchange;

public class Dragonex extends GenericExchange {
	public Dragonex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(DragonexExchange.class.getName());
	}
}