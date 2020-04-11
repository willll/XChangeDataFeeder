package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.kuna.KunaExchange;

public class Kuna extends GenericExchange {
	public Kuna() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KunaExchange.class.getName());
	}
}