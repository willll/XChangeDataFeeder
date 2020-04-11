package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.paribu.ParibuExchange;

public class Paribu extends GenericExchange {
	public Paribu() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(ParibuExchange.class.getName());
	}
}