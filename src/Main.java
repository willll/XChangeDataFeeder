import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.knowm.xchange.currency.CurrencyPair;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import exchanges.factories.*;
import features.ILogger;
import utils.Config;
import utils.Constants;

public class Main implements ILogger {
	
	public static void main(String[] args) throws Exception {

		logger.warn("STARTUP");

		Options options = new Options();

        Option list = new Option("l", "list", false, "list currencies");
        list.setRequired(false);
        options.addOption(list);
		
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("XChange app ...", options);
            System.exit(1);
        }
        
        Boolean listCmd = cmd.hasOption("list");
        
		ArrayList<Thread> thds = new ArrayList<Thread>();

		// Entry point to the exchanges : XChange and XChange-stream
		EntryPoint ep = new EntryPoint();

		// Bus used to store share data : JeroMQ
		ZContext ctx = new ZContext();

		// bus configuration :
		String port = Config.getInstance().get(Constants.port);
		Socket clients = ctx.createSocket(SocketType.XPUB);
		clients.bind("tcp://0.0.0.0:" + port);
		Socket workers = ctx.createSocket(SocketType.XSUB);
		workers.bind("inproc://workers");

		String scp = Config.getInstance().get(Constants.currency_pairs);
		Set<CurrencyPair> cp = new HashSet<>();
		if (scp != null) {
			for(String pair : scp.split(",")) {
	            cp.add(new CurrencyPair(pair));
			}
		}
		// Fallback
		if(cp.size() == 0)
			cp.add(new CurrencyPair("BTC/USDT"));

		// ACX
		AcxFactory.acx(listCmd, ep, cp, thds, ctx);

		// ANX
		AnxFactory.anx(listCmd, ep, cp, thds, ctx);

		// BANKERA
		BankeraFactory.bankera(listCmd, ep, cp, thds, ctx);

		// BIBOX
		BiboxFactory.bibox(listCmd, ep, cp, thds, ctx);

		// BINANCE
		BinanceFactory.binance(listCmd, ep, cp, thds, ctx);

		// BITBAY
		BitbayFactory.bitbay(listCmd, ep, cp, thds, ctx);

		// BITCOINAVERAGE
		BitcoinaverageFactory.bitcoinaverage(listCmd, ep, cp, thds, ctx);

		// BITCOINCHARTS
		BitcoinchartsFactory.bitcoincharts(listCmd, ep, cp, thds, ctx);

		// BITCOINCORE
		BitcoincoreFactory.bitcoincore(listCmd, ep, cp, thds, ctx);

		// BITCOINDE
		BitcoindeFactory.bitcoinde(listCmd, ep, cp, thds, ctx);

		// BITCOINIUM
		BitcoiniumFactory.bitcoinium(listCmd, ep, cp, thds, ctx);

		// BITCOINTOYOU
		BitcointoyouFactory.bitcointoyou(listCmd, ep, cp, thds, ctx);

		// BITFINEX
		BitfinexFactory.bitfinex(listCmd, ep, cp, thds, ctx);

		// BITFLYER
		BitflyerFactory.bitflyer(listCmd, ep, cp, thds, ctx);

		// BITHUMB
		BithumbFactory.bithumb(listCmd, ep, cp, thds, ctx);

		// BITMEX
		BitmexFactory.bitmex(listCmd, ep, cp, thds, ctx);

		// BITSO
		BitsoFactory.bitso(listCmd, ep, cp, thds, ctx);

		// BITSTAMP
		BitstampFactory.bitstamp(listCmd, ep, cp, thds, ctx);

		// BITTREX
		BittrexFactory.bittrex(listCmd, ep, cp, thds, ctx);

		// BITY
		BityFactory.bity(listCmd, ep, cp, thds, ctx);

		// BITZ
		BitzFactory.bitZ(listCmd, ep, cp, thds, ctx);

		// BL3P
		Bl3pFactory.bl3p(listCmd, ep, cp, thds, ctx);

		// BLEUTRADE
		BleutradeFactory.bleutrade(listCmd, ep, cp, thds, ctx);

		// BLOCKCHAIN
		BlockchainFactory.blockchain(listCmd, ep, cp, thds, ctx);

		// BTCC
		BtccFactory.btcc(listCmd, ep, cp, thds, ctx);

		// BTCMARKETS
		BtcmarketsFactory.BTCMarkets(listCmd, ep, cp, thds, ctx);

		// BTCTRADE
		BtctradeFactory.BTCTrade(listCmd, ep, cp, thds, ctx);

		// BTCTURK
		BtcturkFactory.BTCTurk(listCmd, ep, cp, thds, ctx);

		// BX
		BxFactory.bx(listCmd, ep, cp, thds, ctx);

		// CAMPBX
		CampbxFactory.CampBX(listCmd, ep, cp, thds, ctx);

		// CCEX
		CcexFactory.CCEX(listCmd, ep, cp, thds, ctx);

		// CEXIO
		CexioFactory.Cexio(listCmd, ep, cp, thds, ctx);

		// COBINHOOD
		CobinhoodFactory.cobinhood(listCmd, ep, cp, thds, ctx);

		// COINBASE
		CoinbaseFactory.coinbase(listCmd, ep, cp, thds, ctx);

		// COINBASEPRO
		CoinbaseproFactory.coinbasepro(listCmd, ep, cp, thds, ctx);

		// COINBENE
		CoinbeneFactory.coinbene(listCmd, ep, cp, thds, ctx);

		// COINDEAL
		CoindealFactory.coindeal(listCmd, ep, cp, thds, ctx);

		// COINDIRECT
		CoindirectFactory.coindirect(listCmd, ep, cp, thds, ctx);

		// COINEGG
		CoineggFactory.CoinEgg(listCmd, ep, cp, thds, ctx);

		// COINEX
		CoinexFactory.coinex(listCmd, ep, cp, thds, ctx);

		// COINFLOOR
		CoinfloorFactory.coinfloor(listCmd, ep, cp, thds, ctx);

		// COINGI
		CoingiFactory.coingi(listCmd, ep, cp, thds, ctx);

		// CMC
		CoinmarketcapFactory.coinmarketcap(listCmd, ep, cp, thds, ctx);

		// COINMATE
		CoinmateFactory.coinmate(listCmd, ep, cp, thds, ctx);

		// COINONE
		CoinoneFactory.coinone(listCmd, ep, cp, thds, ctx);

		// COINSUPER
		CoinsuperFactory.coinsuper(listCmd, ep, cp, thds, ctx);

		// CRYPTOFACILITIES
		CryptofacilitiesFactory.CryptoFacilities(listCmd, ep, cp, thds, ctx);

		// CRYPTONIT
		CryptonitFactory.cryptonit(listCmd, ep, cp, thds, ctx);

		// CRYPTOPIA
		CryptopiaFactory.cryptopia(listCmd, ep, cp, thds, ctx);

		// CRYPTOWATCH
		CryptowatchFactory.cryptowatch(listCmd, ep, cp, thds, ctx);

		// DERIBIT
		DeribitFactory.deribit(listCmd, ep, cp, thds, ctx);

		// DRAGONEX
		DragonexFactory.dragonex(listCmd, ep, cp, thds, ctx);

		// DSX
		DsxFactory.DSX(listCmd, ep, cp, thds, ctx);

		// DVCHAIN
		DvchainFactory.DVChain(listCmd, ep, cp, thds, ctx);

		// ENIGMA
		EnigmaFactory.enigma(listCmd, ep, cp, thds, ctx);

		// EXMO
		ExmoFactory.exmo(listCmd, ep, cp, thds, ctx);

		// EXX
		ExxFactory.EXX(listCmd, ep, cp, thds, ctx);

		// FCOIN
		FcoinFactory.FCoin(listCmd, ep, cp, thds, ctx);

		// GATEIO
		GateioFactory.gateio(listCmd, ep, cp, thds, ctx);

		// GEMINI
		GeminiFactory.gemini(listCmd, ep, cp, thds, ctx);

		// GLOBITEX
		GlobitexFactory.globitex(listCmd, ep, cp, thds, ctx);

		// HITBTC
		HitbtcFactory.hitbtc(listCmd, ep, cp, thds, ctx);

		// HUOBI
		HuobiFactory.huobi(listCmd, ep, cp, thds, ctx);

		// IDEX
		IdexFactory.idex(listCmd, ep, cp, thds, ctx);

		// ITBIT
		ItbitFactory.ItBit(listCmd, ep, cp, thds, ctx);

		// KOINEKS
		KoineksFactory.koineks(listCmd, ep, cp, thds, ctx);

		// KOINIM
		KoinimFactory.koinim(listCmd, ep, cp, thds, ctx);

		// KRAKEN
		KrakenFactory.kraken(listCmd, ep, cp, thds, ctx);

		// KUCOIN
		KucoinFactory.kucoin(listCmd, ep, cp, thds, ctx);

		// KUNA
		KunaFactory.kuna(listCmd, ep, cp, thds, ctx);

		// LAKEBTC
		LakebtcFactory.LakeBTC(listCmd, ep, cp, thds, ctx);

		// LATOKEN
		LatokenFactory.latoken(listCmd, ep, cp, thds, ctx);

		// LGO
		LgoFactory.lgo(listCmd, ep, cp, thds, ctx);

		// LIQUI
		LiquiFactory.liqui(listCmd, ep, cp, thds, ctx);

		// LIVECOIN
		LivecoinFactory.livecoin(listCmd, ep, cp, thds, ctx);

		// LUNO
		LunoFactory.luno(listCmd, ep, cp, thds, ctx);

		// LYKKE
		LykkeFactory.lykke(listCmd, ep, cp, thds, ctx);

		// MERCADOBITCOIN
		MercadobitcoinFactory.MercadoBitcoin(listCmd, ep, cp, thds, ctx);

		// OKCOIN
		OkcoinFactory.OkCoin(listCmd, ep, cp, thds, ctx);

		// PARIBU
		ParibuFactory.paribu(listCmd, ep, cp, thds, ctx);

		// PAYMIUM
		PaymiumFactory.paymium(listCmd, ep, cp, thds, ctx);

		// POLONIEX
		PoloniexFactory.poloniex(listCmd, ep, cp, thds, ctx);

		// QUOINE
		QuoineFactory.quoine(listCmd, ep, cp, thds, ctx);

		// RIPPLE
		RippleFactory.ripple(listCmd, ep, cp, thds, ctx);

		// THEROCK
		TherockFactory.TheRock(listCmd, ep, cp, thds, ctx);

		// TRADEOGRE
		TradeogreFactory.TradeOgre(listCmd, ep, cp, thds, ctx);

		// TRUEFX
		TruefxFactory.TrueFx(listCmd, ep, cp, thds, ctx);

		// UPBIT
		UpbitFactory.upbit(listCmd, ep, cp, thds, ctx);

		// VAULTORO
		VaultoroFactory.vaultoro(listCmd, ep, cp, thds, ctx);

		// YOBIT
		YobitFactory.YoBit(listCmd, ep, cp, thds, ctx);

		// ZAIF
		ZaifFactory.zaif(listCmd, ep, cp, thds, ctx);


		if (thds.size() > 0) {
			// Connect work threads to client threads via a queue, this is a blocking operation
			ZMQ.proxy(clients, workers, null);
			
			// Infinite loop
			for (Thread thd : thds) {
				thd.join();
			}
		}
		
		workers.close();
		clients.close();
		ctx.destroy();

		logger.warn("EXIT");
	}
}
