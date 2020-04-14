package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcoincharts.BitcoinChartsExchange;

public class Bitcoincharts extends GenericExchange {
	public Bitcoincharts() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcoinChartsExchange.class.getName());
	}
}