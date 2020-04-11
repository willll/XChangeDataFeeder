package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.quadrigacx.QuadrigaCxExchange;

public class Quadrigacx extends GenericExchange {
	public Quadrigacx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(QuadrigaCxExchange.class.getName());
	}
}
