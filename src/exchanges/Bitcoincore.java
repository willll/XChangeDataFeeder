package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcoincore.BitcoinCore;

public class Bitcoincore extends GenericExchange {
	public Bitcoincore() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcoinCore.class.getName());
	}
}