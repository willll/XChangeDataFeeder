package exchanges.factories;

import exchanges.*;

/**
 * @author will
 *
 */
public class EntryPoint {

	private Acx acx = null;
	private Anx anx = null;
	private Bankera bankera = null;
	private Bibox bibox = null;
	private Binance binance = null;
	private Bitbay bitbay = null;
	private Bitcoinaverage bitcoinaverage = null;
	private Bitcoincharts bitcoincharts = null;
	private Bitcoinde bitcoinde = null;
	private Bitcoinium bitcoinium = null;
	private Bitcointoyou bitcointoyou = null;
	private Bitfinex bitfinex = null;
	private Bitflyer bitflyer = null;
	private Bithumb bithumb = null;
	private Bitmex bitmex = null;
	private Bitso bitso = null;
	private Bitstamp bitstamp = null;
	private Bittrex bittrex = null;
	private Bity bity = null;
	private Bitz bitz = null;
	private Bl3p bl3p = null;
	private Bleutrade bleutrade = null;
	private Blockchain blockchain = null;
	private Btcc btcc = null;
	private Btcmarkets btcmarkets = null;
	private Btctrade btctrade = null;
	private Btcturk btcturk = null;
	private Bx bx = null;
	private Campbx campbx = null;
	private Ccex ccex = null;
	private Cexio cexio = null;
	private Cobinhood cobinhood = null;
	private Coinbase coinbase = null;
	private Coinbasepro coinbasepro = null;
	private Coinbene coinbene = null;
	private Coindirect coindirect = null;
	private Coinegg coinegg = null;
	private Coinfloor coinfloor = null;
	private Coingi coingi = null;
	private Coinmarketcap coinmarketcap = null;
	private Coinmate coinmate = null;
	private Coinone coinone = null;
	private Coinsuper coinsuper = null;
	private Cryptofacilities cryptofacilities = null;
	private Cryptopia cryptopia = null;
	private Dragonex dragonex = null;
	private Dsx dsx = null;
	private Dvchain dvchain = null;
	private Exmo exmo = null;
	private Exx exx = null;
	private Fcoin fcoin = null;
	private Gateio gateio = null;
	private Gemini gemini = null;
	private Hitbtc hitbtc = null;
	private Huobi huobi = null;
	private Idex idex = null;
	private Itbit itbit = null;
	private Koineks koineks = null;
	private Koinim koinim = null;
	private Kraken kraken = null;
	private Kucoin kucoin = null;
	private Kuna kuna = null;
	private Lakebtc lakebtc = null;
	private Liqui liqui = null;
	private Livecoin livecoin = null;
	private Luno luno = null;
	private Mercadobitcoin mercadobitcoin = null;
	private Okcoin okcoin = null;
	private Paribu paribu = null;
	private Paymium paymium = null;
	private Poloniex poloniex = null;
	private Quoine quoine = null;
	private Ripple ripple = null;
	private Therock therock = null;
	private Truefx truefx = null;
	private Upbit upbit = null;
	private Vaultoro vaultoro = null;
	private Yobit yobit = null;
	private Zaif zaif = null;

	/**
	 * @author will
	 *
	 */
	public enum Exchanges {
		ABUCOINS("Abucoins"), ACX("Acx"), ANX("Anx"), BANKERA("Bankera"), BIBOX("Bibox"), BINANCE("Binance"),
		BITBAY("Bitbay"), BITCOINAVERAGE("Bitcoinaverage"), BITCOINCHARTS("Bitcoincharts"), BITCOINCORE("Bitcoincore"),
		BITCOINDE("Bitcoinde"), BITCOINIUM("Bitcoinium"), BITCOINTOYOU("Bitcointoyou"), BITFINEX("Bitfinex"),
		BITFLYER("Bitflyer"), BITHUMB("Bithumb"), BITMARKET("Bitmarket"), BITMEX("Bitmex"), BITSO("Bitso"),
		BITSTAMP("Bitstamp"), BITTREX("Bittrex"), BITY("Bity"), BITZ("Bitz"), BL3P("Bl3p"), BLEUTRADE("Bleutrade"),
		BLOCKCHAIN("Blockchain"), BTCC("Btcc"), BTCMARKETS("Btcmarkets"), BTCTRADE("Btctrade"), BTCTURK("Btcturk"),
		BX("Bx"), CAMPBX("Campbx"), CCEX("Ccex"), CEXIO("Cexio"), COBINHOOD("Cobinhood"), COINBASE("Coinbase"),
		COINBASEPRO("Coinbasepro"), COINBENE("Coinbene"), COINDEAL("Coindeal"), COINDIRECT("Coindirect"),
		COINEGG("Coinegg"), COINEX("Coinex"), COINFLOOR("Coinfloor"), COINGI("Coingi"), COINMARKETCAP("Coinmarketcap"),
		COINMATE("Coinmate"), COINONE("Coinone"), COINSUPER("Coinsuper"), CRYPTOFACILITIES("Cryptofacilities"),
		CRYPTONIT("Cryptonit"), CRYPTOPIA("Cryptopia"), DRAGONEX("Dragonex"), DSX("Dsx"), DVCHAIN("Dvchain"),
		EXMO("Exmo"), EXX("Exx"), FCOIN("Fcoin"), GATECOIN("Gatecoin"), GATEIO("Gateio"), GEMINI("Gemini"),
		GLOBITEX("Globitex"), HITBTC("Hitbtc"), HUOBI("Huobi"), IDEX("Idex"), ITBIT("Itbit"), KOINEKS("Koineks"),
		KOINIM("Koinim"), KRAKEN("Kraken"), KUCOIN("Kucoin"), KUNA("Kuna"), LAKEBTC("Lakebtc"), LIVECOIN("Livecoin"),
		LUNO("Luno"), LYKKE("Lykke"), MERCADOBITCOIN("Mercadobitcoin"), OKCOIN("Okcoin"),
		OPENEXCHANGERATES("Openexchangerates"), PARIBU("Paribu"), PAYMIUM("Paymium"), POLONIEX("Poloniex"),
		QUADRIGACX("Quadrigacx"), QUOINE("Quoine"), RIPPLE("Ripple"), THEROCK("Therock"), TRUEFX("Truefx"),
		UPBIT("Upbit"), VAULTORO("Vaultoro"), VIRCUREX("Vircurex"), WEX("Wex"), YOBIT("Yobit"), ZAIF("Zaif");

		private final String exchangeName;

		Exchanges(final String value) {
			exchangeName = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return exchangeName;
		}
	}

	/**
	 * 
	 */
	public EntryPoint() {
	}

	/**
	 * @param ex
	 * @return
	 */
	public GenericExchange getExchange(Exchanges ex) {
		switch (ex) {
		case ACX:
			if (this.acx == null)
				this.acx = new Acx();
			return this.acx;
		case ANX:
			if (this.anx == null)
				this.anx = new Anx();
			return this.anx;
		case BANKERA:
			if (this.bankera == null)
				this.bankera = new Bankera();
			return this.bankera;
		case BIBOX:
			if (this.bibox == null)
				this.bibox = new Bibox();
			return this.bibox;
		case BINANCE:
			if (this.binance == null)
				this.binance = new Binance();
			return this.binance;
		case BITBAY:
			if (this.bitbay == null)
				this.bitbay = new Bitbay();
			return this.bitbay;
		case BITCOINAVERAGE:
			if (this.bitcoinaverage == null)
				this.bitcoinaverage = new Bitcoinaverage();
			return this.bitcoinaverage;
		case BITCOINCHARTS:
			if (this.bitcoincharts == null)
				this.bitcoincharts = new Bitcoincharts();
			return this.bitcoincharts;
		case BITCOINDE:
			if (this.bitcoinde == null)
				this.bitcoinde = new Bitcoinde();
			return this.bitcoinde;
		case BITCOINIUM:
			if (this.bitcoinium == null)
				this.bitcoinium = new Bitcoinium();
			return this.bitcoinium;
		case BITCOINTOYOU:
			if (this.bitcointoyou == null)
				this.bitcointoyou = new Bitcointoyou();
			return this.bitcointoyou;
		case BITFINEX:
			if (this.bitfinex == null)
				this.bitfinex = new Bitfinex();
			return this.bitfinex;
		case BITFLYER:
			if (this.bitflyer == null)
				this.bitflyer = new Bitflyer();
			return this.bitflyer;
		case BITHUMB:
			if (this.bithumb == null)
				this.bithumb = new Bithumb();
			return this.bithumb;
		case BITMEX:
			if (this.bitmex == null)
				this.bitmex = new Bitmex();
			return this.bitmex;
		case BITSO:
			if (this.bitso == null)
				this.bitso = new Bitso();
			return this.bitso;
		case BITSTAMP:
			if (this.bitstamp == null)
				this.bitstamp = new Bitstamp();
			return this.bitstamp;
		case BITTREX:
			if (this.bittrex == null)
				this.bittrex = new Bittrex();
			return this.bittrex;
		case BITY:
			if (this.bity == null)
				this.bity = new Bity();
			return this.bity;
		case BITZ:
			if (this.bitz == null)
				this.bitz = new Bitz();
			return this.bitz;
		case BL3P:
			if (this.bl3p == null)
				this.bl3p = new Bl3p();
			return this.bl3p;
		case BLEUTRADE:
			if (this.bleutrade == null)
				this.bleutrade = new Bleutrade();
			return this.bleutrade;
		case BLOCKCHAIN:
			if (this.blockchain == null)
				this.blockchain = new Blockchain();
			return this.blockchain;
		case BTCC:
			if (this.btcc == null)
				this.btcc = new Btcc();
			return this.btcc;
		case BTCMARKETS:
			if (this.btcmarkets == null)
				this.btcmarkets = new Btcmarkets();
			return this.btcmarkets;
		case BTCTRADE:
			if (this.btctrade == null)
				this.btctrade = new Btctrade();
			return this.btctrade;
		case BTCTURK:
			if (this.btcturk == null)
				this.btcturk = new Btcturk();
			return this.btcturk;
		case BX:
			if (this.bx == null)
				this.bx = new Bx();
			return this.bx;
		case CAMPBX:
			if (this.campbx == null)
				this.campbx = new Campbx();
			return this.campbx;
		case CCEX:
			if (this.ccex == null)
				this.ccex = new Ccex();
			return this.ccex;
		case CEXIO:
			if (this.cexio == null)
				this.cexio = new Cexio();
			return this.cexio;
		case COBINHOOD:
			if (this.cobinhood == null)
				this.cobinhood = new Cobinhood();
			return this.cobinhood;
		case COINBASE:
			if (this.coinbase == null)
				this.coinbase = new Coinbase();
			return this.coinbase;
		case COINBASEPRO:
			if (this.coinbasepro == null)
				this.coinbasepro = new Coinbasepro();
			return this.coinbasepro;
		case COINBENE:
			if (this.coinbene == null)
				this.coinbene = new Coinbene();
			return this.coinbene;
		case COINDIRECT:
			if (this.coindirect == null)
				this.coindirect = new Coindirect();
			return this.coindirect;
		case COINEGG:
			if (this.coinegg == null)
				this.coinegg = new Coinegg();
			return this.coinegg;
		case COINFLOOR:
			if (this.coinfloor == null)
				this.coinfloor = new Coinfloor();
			return this.coinfloor;
		case COINGI:
			if (this.coingi == null)
				this.coingi = new Coingi();
			return this.coingi;
		case COINMARKETCAP:
			if (this.coinmarketcap == null)
				this.coinmarketcap = new Coinmarketcap();
			return this.coinmarketcap;
		case COINMATE:
			if (this.coinmate == null)
				this.coinmate = new Coinmate();
			return this.coinmate;
		case COINONE:
			if (this.coinone == null)
				this.coinone = new Coinone();
			return this.coinone;
		case COINSUPER:
			if (this.coinsuper == null)
				this.coinsuper = new Coinsuper();
			return this.coinsuper;
		case CRYPTOFACILITIES:
			if (this.cryptofacilities == null)
				this.cryptofacilities = new Cryptofacilities();
			return this.cryptofacilities;
		case CRYPTOPIA:
			if (this.cryptopia == null)
				this.cryptopia = new Cryptopia();
			return this.cryptopia;
		case DRAGONEX:
			if (this.dragonex == null)
				this.dragonex = new Dragonex();
			return this.dragonex;
		case DSX:
			if (this.dsx == null)
				this.dsx = new Dsx();
			return this.dsx;
		case DVCHAIN:
			if (this.dvchain == null)
				this.dvchain = new Dvchain();
			return this.dvchain;
		case EXMO:
			if (this.exmo == null)
				this.exmo = new Exmo();
			return this.exmo;
		case EXX:
			if (this.exx == null)
				this.exx = new Exx();
			return this.exx;
		case FCOIN:
			if (this.fcoin == null)
				this.fcoin = new Fcoin();
			return this.fcoin;
		case GATEIO:
			if (this.gateio == null)
				this.gateio = new Gateio();
			return this.gateio;
		case GEMINI:
			if (this.gemini == null)
				this.gemini = new Gemini();
			return this.gemini;
		case HITBTC:
			if (this.hitbtc == null)
				this.hitbtc = new Hitbtc();
			return this.hitbtc;
		case HUOBI:
			if (this.huobi == null)
				this.huobi = new Huobi();
			return this.huobi;
		case IDEX:
			if (this.idex == null)
				this.idex = new Idex();
			return this.idex;
		case ITBIT:
			if (this.itbit == null)
				this.itbit = new Itbit();
			return this.itbit;
		case KOINEKS:
			if (this.koineks == null)
				this.koineks = new Koineks();
			return this.koineks;
		case KOINIM:
			if (this.koinim == null)
				this.koinim = new Koinim();
			return this.koinim;
		case KRAKEN:
			if (this.kraken == null)
				this.kraken = new Kraken();
			return this.kraken;
		case KUCOIN:
			if (this.kucoin == null)
				this.kucoin = new Kucoin();
			return this.kucoin;
		case KUNA:
			if (this.kuna == null)
				this.kuna = new Kuna();
			return this.kuna;
		case LAKEBTC:
			if (this.lakebtc == null)
				this.lakebtc = new Lakebtc();
			return this.lakebtc;
		case LIVECOIN:
			if (this.livecoin == null)
				this.livecoin = new Livecoin();
			return this.livecoin;
		case LUNO:
			if (this.luno == null)
				this.luno = new Luno();
			return this.luno;
		case MERCADOBITCOIN:
			if (this.mercadobitcoin == null)
				this.mercadobitcoin = new Mercadobitcoin();
			return this.mercadobitcoin;
		case OKCOIN:
			if (this.okcoin == null)
				this.okcoin = new Okcoin();
			return this.okcoin;
		case PARIBU:
			if (this.paribu == null)
				this.paribu = new Paribu();
			return this.paribu;
		case PAYMIUM:
			if (this.paymium == null)
				this.paymium = new Paymium();
			return this.paymium;
		case POLONIEX:
			if (this.poloniex == null)
				this.poloniex = new Poloniex();
			return this.poloniex;
		case QUOINE:
			if (this.quoine == null)
				this.quoine = new Quoine();
			return this.quoine;
		case RIPPLE:
			if (this.ripple == null)
				this.ripple = new Ripple();
			return this.ripple;
		case THEROCK:
			if (this.therock == null)
				this.therock = new Therock();
			return this.therock;
		case TRUEFX:
			if (this.truefx == null)
				this.truefx = new Truefx();
			return this.truefx;
		case UPBIT:
			if (this.upbit == null)
				this.upbit = new Upbit();
			return this.upbit;
		case VAULTORO:
			if (this.vaultoro == null)
				this.vaultoro = new Vaultoro();
			return this.vaultoro;
		case YOBIT:
			if (this.yobit == null)
				this.yobit = new Yobit();
			return this.yobit;
		case ZAIF:
			if (this.zaif == null)
				this.zaif = new Zaif();
			return this.zaif;
		default:
			assert (false);
		}
		return null;
	}

	/**
	 * @param ex
	 * @return
	 */
	public GenericStreamingExchange getStreamingExchange(Exchanges ex) {
		switch (ex) {
		case BINANCE:
			if (this.binance == null)
				this.binance = new Binance();
			return this.binance;
		case BITFINEX:
			if (this.bitfinex == null)
				this.bitfinex = new Bitfinex();
			return this.bitfinex;
		case BITSTAMP:
			if (this.bitstamp == null)
				this.bitstamp = new Bitstamp();
			return this.bitstamp;
		case OKCOIN:
			if (this.okcoin == null)
				this.okcoin = new Okcoin();
			return this.okcoin;
		case POLONIEX:
			if (this.poloniex == null)
				this.poloniex = new Poloniex();
			return this.poloniex;
		default:
			assert (false);
		}
		return null;
	}
}
