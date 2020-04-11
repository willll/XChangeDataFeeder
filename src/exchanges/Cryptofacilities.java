package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cryptofacilities.CryptoFacilitiesExchange;

public class Cryptofacilities extends GenericExchange {
	public Cryptofacilities() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CryptoFacilitiesExchange.class.getName());
	}
}