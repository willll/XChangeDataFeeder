package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.therock.TheRockExchange;;

public class Therock extends GenericExchange {
	public Therock() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(TheRockExchange.class.getName());
	}
}