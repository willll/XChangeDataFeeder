package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.truefx.TrueFxExchange;

public class Truefx extends GenericExchange {
	public Truefx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(TrueFxExchange.class.getName());
	}
}