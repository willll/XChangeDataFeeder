package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.campbx.CampBXExchange;

public class Campbx extends GenericExchange {
	public Campbx() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CampBXExchange.class.getName());
	}
}