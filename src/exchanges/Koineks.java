package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.koineks.KoineksExchange;

public class Koineks extends GenericExchange {
	public Koineks() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KoineksExchange.class.getName());
	}
}