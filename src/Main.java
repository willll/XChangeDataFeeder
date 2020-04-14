import java.util.ArrayList;
import java.util.Iterator;

import org.knowm.xchange.currency.CurrencyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import org.apache.commons.cli.*;

import exchanges.factories.EntryPoint;
import exchanges.factories.EntryPoint.Exchanges;
import exchanges.factories.ExchangesFactory;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import info.bitrich.xchangestream.core.StreamingExchange;
import tasks.OrderBookPublisherStreamingTask;
import tasks.TickerPublisherStreamingTask;
import utils.Config;
import utils.Constants;


public class Main {

	private static Logger logger = LoggerFactory.getLogger("Console");

	static void displayCurrencyPairs(Exchanges xch, ArrayList<CurrencyPair> cp) {
		System.out.println (xch + " ;\n");
		for (CurrencyPair currencyPair : cp) {
			System.out.print(currencyPair + ", ");
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		logger.trace("STARTUP");

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
		Socket clients = ctx.createSocket(ZMQ.XPUB);
		clients.bind("tcp://*:" + port);
		Socket workers = ctx.createSocket(ZMQ.XSUB);
		workers.bind("inproc://workers");

		String scp = Config.getInstance().get(Constants.currency_pairs);
		ArrayList<CurrencyPair> cp = new ArrayList<>();
		if (scp != null) {
			for(String pair : scp.split(",")) {
	            cp.add(new CurrencyPair(pair));
			}
		}
		if(cp.size() == 0)
			cp.add(new CurrencyPair("BTC/USDT"));

				/*
		 * ACX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.ACX, ep.getExchange(Exchanges.ACX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Acx_cp = cp;
                String bscp = Config.getInstance().get(Constants.acx_currency_pairs);
                if(bscp != null) {
                        Acx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Acx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Acx_cp = ep.getExchange(Exchanges.ACX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Acx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Acx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getAcxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Acx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.acx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getAcxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * ANX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.anx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.ANX, ep.getExchange(Exchanges.ANX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Anx_cp = cp;
                String bscp = Config.getInstance().get(Constants.anx_currency_pairs);
                if(bscp != null) {
                        Anx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Anx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Anx_cp = ep.getExchange(Exchanges.ANX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Anx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Anx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.anx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getAnxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Anx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.anx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getAnxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BANKERA
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BANKERA, ep.getExchange(Exchanges.BANKERA).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bankera_cp = cp;
                String bscp = Config.getInstance().get(Constants.bankera_currency_pairs);
                if(bscp != null) {
                        Bankera_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bankera_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bankera_cp = ep.getExchange(Exchanges.BANKERA).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bankera_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bankera
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBankeraFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bankera
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bankera_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBankeraFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BIBOX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BIBOX, ep.getExchange(Exchanges.BIBOX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bibox_cp = cp;
                String bscp = Config.getInstance().get(Constants.bibox_currency_pairs);
                if(bscp != null) {
                        Bibox_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bibox_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bibox_cp = ep.getExchange(Exchanges.BIBOX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bibox_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bibox
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBiboxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bibox
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bibox_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBiboxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BINANCE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BINANCE, ep.getExchange(Exchanges.BINANCE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Binance_cp = cp;
                String bscp = Config.getInstance().get(Constants.binance_currency_pairs);
                if(bscp != null) {
                        Binance_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Binance_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Binance_cp = ep.getExchange(Exchanges.BINANCE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Binance_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from binance
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBinanceFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from binance
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.binance_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBinanceFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITBAY
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITBAY, ep.getExchange(Exchanges.BITBAY).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitbay_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitbay_currency_pairs);
                if(bscp != null) {
                        Bitbay_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitbay_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitbay_cp = ep.getExchange(Exchanges.BITBAY).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitbay_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitbay
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitbayFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitbay
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitbayFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITBAY
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITBAY, ep.getExchange(Exchanges.BITBAY).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitbay_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitbay_currency_pairs);
                if(bscp != null) {
                        Bitbay_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitbay_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitbay_cp = ep.getExchange(Exchanges.BITBAY).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitbay_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitbay
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitbayFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitbay
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitbay_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitbayFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINAVERAGE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINAVERAGE, ep.getExchange(Exchanges.BITCOINAVERAGE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcoinaverage_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcoinaverage_currency_pairs);
                if(bscp != null) {
                        Bitcoinaverage_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcoinaverage_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcoinaverage_cp = ep.getExchange(Exchanges.BITCOINAVERAGE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcoinaverage_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcoinaverage
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoinaverageFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcoinaverage
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinaverage_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoinaverageFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINCHARTS
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINCHARTS, ep.getExchange(Exchanges.BITCOINCHARTS).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcoincharts_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcoincharts_currency_pairs);
                if(bscp != null) {
                        Bitcoincharts_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcoincharts_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcoincharts_cp = ep.getExchange(Exchanges.BITCOINCHARTS).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcoincharts_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcoincharts
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoinchartsFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcoincharts
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincharts_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoinchartsFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINCORE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINCORE, ep.getExchange(Exchanges.BITCOINCORE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcoincore_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcoincore_currency_pairs);
                if(bscp != null) {
                        Bitcoincore_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcoincore_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcoincore_cp = ep.getExchange(Exchanges.BITCOINCORE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcoincore_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcoincore
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoincoreFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcoincore
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoincore_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoincoreFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINDE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINDE, ep.getExchange(Exchanges.BITCOINDE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcoinde_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcoinde_currency_pairs);
                if(bscp != null) {
                        Bitcoinde_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcoinde_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcoinde_cp = ep.getExchange(Exchanges.BITCOINDE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcoinde_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcoinde
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoindeFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcoinde
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinde_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoindeFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINIUM
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINIUM, ep.getExchange(Exchanges.BITCOINIUM).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcoinium_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcoinium_currency_pairs);
                if(bscp != null) {
                        Bitcoinium_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcoinium_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcoinium_cp = ep.getExchange(Exchanges.BITCOINIUM).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcoinium_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcoinium
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoiniumFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcoinium
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcoinium_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcoiniumFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITCOINTOYOU
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITCOINTOYOU, ep.getExchange(Exchanges.BITCOINTOYOU).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitcointoyou_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitcointoyou_currency_pairs);
                if(bscp != null) {
                        Bitcointoyou_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitcointoyou_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitcointoyou_cp = ep.getExchange(Exchanges.BITCOINTOYOU).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitcointoyou_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitcointoyou
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcointoyouFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitcointoyou
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitcointoyou_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitcointoyouFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITFINEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITFINEX, ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitfinex_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitfinex_currency_pairs);
                if(bscp != null) {
                        Bitfinex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitfinex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitfinex_cp = ep.getExchange(Exchanges.BITFINEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitfinex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bitfinex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitfinexFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bitfinex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitfinex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitfinexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITFLYER
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITFLYER, ep.getExchange(Exchanges.BITFLYER).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitflyer_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitflyer_currency_pairs);
                if(bscp != null) {
                        Bitflyer_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitflyer_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitflyer_cp = ep.getExchange(Exchanges.BITFLYER).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitflyer_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bitflyer
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitflyerFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bitflyer
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitflyer_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitflyerFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITHUMB
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITHUMB, ep.getExchange(Exchanges.BITHUMB).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bithumb_cp = cp;
                String bscp = Config.getInstance().get(Constants.bithumb_currency_pairs);
                if(bscp != null) {
                        Bithumb_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bithumb_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bithumb_cp = ep.getExchange(Exchanges.BITHUMB).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bithumb_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bithumb
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBithumbFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bithumb
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bithumb_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBithumbFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITMEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITMEX, ep.getExchange(Exchanges.BITMEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitmex_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitmex_currency_pairs);
                if(bscp != null) {
                        Bitmex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitmex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitmex_cp = ep.getExchange(Exchanges.BITMEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitmex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bitmex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitmexFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bitmex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitmex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitmexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITSO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITSO, ep.getExchange(Exchanges.BITSO).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitso_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitso_currency_pairs);
                if(bscp != null) {
                        Bitso_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitso_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitso_cp = ep.getExchange(Exchanges.BITSO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitso_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitso
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitsoFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitso
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitso_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitsoFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITSTAMP
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITSTAMP, ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Bitstamp_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitstamp_currency_pairs);
                if(bscp != null) {
                        Bitstamp_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitstamp_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitstamp_cp = ep.getExchange(Exchanges.BITSTAMP).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitstamp_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from bitstamp
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitstampFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from bitstamp
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitstamp_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitstampFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITTREX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITTREX, ep.getExchange(Exchanges.BITTREX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bittrex_cp = cp;
                String bscp = Config.getInstance().get(Constants.bittrex_currency_pairs);
                if(bscp != null) {
                        Bittrex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bittrex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bittrex_cp = ep.getExchange(Exchanges.BITTREX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bittrex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bittrex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBittrexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bittrex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bittrex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBittrexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITY
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITY, ep.getExchange(Exchanges.BITY).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bity_cp = cp;
                String bscp = Config.getInstance().get(Constants.bity_currency_pairs);
                if(bscp != null) {
                        Bity_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bity_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bity_cp = ep.getExchange(Exchanges.BITY).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bity_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bity
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBityFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bity
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bity_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBityFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BITZ
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitz_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BITZ, ep.getExchange(Exchanges.BITZ).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bitz_cp = cp;
                String bscp = Config.getInstance().get(Constants.bitz_currency_pairs);
                if(bscp != null) {
                        Bitz_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bitz_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bitz_cp = ep.getExchange(Exchanges.BITZ).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bitz_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bitz
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitz_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBitzFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bitz
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bitz_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBitzFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BL3P
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BL3P, ep.getExchange(Exchanges.BL3P).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bl3p_cp = cp;
                String bscp = Config.getInstance().get(Constants.bl3p_currency_pairs);
                if(bscp != null) {
                        Bl3p_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bl3p_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bl3p_cp = ep.getExchange(Exchanges.BL3P).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bl3p_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bl3p
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBl3pFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bl3p
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bl3p_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBl3pFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BLEUTRADE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BLEUTRADE, ep.getExchange(Exchanges.BLEUTRADE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bleutrade_cp = cp;
                String bscp = Config.getInstance().get(Constants.bleutrade_currency_pairs);
                if(bscp != null) {
                        Bleutrade_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bleutrade_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bleutrade_cp = ep.getExchange(Exchanges.BLEUTRADE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bleutrade_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bleutrade
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBleutradeFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bleutrade
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bleutrade_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBleutradeFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BLOCKCHAIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.blockchain_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BLOCKCHAIN, ep.getExchange(Exchanges.BLOCKCHAIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Blockchain_cp = cp;
                String bscp = Config.getInstance().get(Constants.blockchain_currency_pairs);
                if(bscp != null) {
                        Blockchain_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Blockchain_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Blockchain_cp = ep.getExchange(Exchanges.BLOCKCHAIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Blockchain_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Blockchain
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.blockchain_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBlockchainFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Blockchain
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.blockchain_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBlockchainFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BTCC
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BTCC, ep.getExchange(Exchanges.BTCC).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Btcc_cp = cp;
                String bscp = Config.getInstance().get(Constants.btcc_currency_pairs);
                if(bscp != null) {
                        Btcc_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Btcc_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Btcc_cp = ep.getExchange(Exchanges.BTCC).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Btcc_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Btcc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBtccFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Btcc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcc_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBtccFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BTCMARKETS
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcmarkets_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BTCMARKETS, ep.getExchange(Exchanges.BTCMARKETS).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Btcmarkets_cp = cp;
                String bscp = Config.getInstance().get(Constants.btcmarkets_currency_pairs);
                if(bscp != null) {
                        Btcmarkets_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Btcmarkets_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Btcmarkets_cp = ep.getExchange(Exchanges.BTCMARKETS).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Btcmarkets_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Btcmarkets
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcmarkets_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBtcmarketsFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Btcmarkets
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcmarkets_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBtcmarketsFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BTCTRADE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.btctrade_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BTCTRADE, ep.getExchange(Exchanges.BTCTRADE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Btctrade_cp = cp;
                String bscp = Config.getInstance().get(Constants.btctrade_currency_pairs);
                if(bscp != null) {
                        Btctrade_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Btctrade_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Btctrade_cp = ep.getExchange(Exchanges.BTCTRADE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Btctrade_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Btctrade
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btctrade_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBtctradeFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Btctrade
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btctrade_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBtctradeFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BTCTURK
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcturk_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BTCTURK, ep.getExchange(Exchanges.BTCTURK).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Btcturk_cp = cp;
                String bscp = Config.getInstance().get(Constants.btcturk_currency_pairs);
                if(bscp != null) {
                        Btcturk_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Btcturk_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Btcturk_cp = ep.getExchange(Exchanges.BTCTURK).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Btcturk_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Btcturk
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcturk_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBtcturkFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Btcturk
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.btcturk_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBtcturkFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * BX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.BX, ep.getExchange(Exchanges.BX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Bx_cp = cp;
                String bscp = Config.getInstance().get(Constants.bx_currency_pairs);
                if(bscp != null) {
                        Bx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Bx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Bx_cp = ep.getExchange(Exchanges.BX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Bx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Bx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getBxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Bx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.bx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getBxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CAMPBX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.campbx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CAMPBX, ep.getExchange(Exchanges.CAMPBX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Campbx_cp = cp;
                String bscp = Config.getInstance().get(Constants.campbx_currency_pairs);
                if(bscp != null) {
                        Campbx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Campbx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Campbx_cp = ep.getExchange(Exchanges.CAMPBX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Campbx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Campbx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.campbx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCampbxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Campbx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.campbx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCampbxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CCEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CCEX, ep.getExchange(Exchanges.CCEX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Ccex_cp = cp;
                String bscp = Config.getInstance().get(Constants.ccex_currency_pairs);
                if(bscp != null) {
                        Ccex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Ccex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Ccex_cp = ep.getExchange(Exchanges.CCEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Ccex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Ccex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCcexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Ccex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ccex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCcexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CEXIO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cexio_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CEXIO, ep.getExchange(Exchanges.CEXIO).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Cexio_cp = cp;
                String bscp = Config.getInstance().get(Constants.cexio_currency_pairs);
                if(bscp != null) {
                        Cexio_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cexio_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cexio_cp = ep.getExchange(Exchanges.CEXIO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cexio_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from cexio
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cexio_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCexioFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from cexio
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cexio_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCexioFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COBINHOOD
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COBINHOOD, ep.getExchange(Exchanges.COBINHOOD).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Cobinhood_cp = cp;
                String bscp = Config.getInstance().get(Constants.cobinhood_currency_pairs);
                if(bscp != null) {
                        Cobinhood_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cobinhood_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cobinhood_cp = ep.getExchange(Exchanges.COBINHOOD).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cobinhood_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Cobinhood
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCobinhoodFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Cobinhood
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cobinhood_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCobinhoodFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINBASE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINBASE, ep.getExchange(Exchanges.COINBASE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinbase_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinbase_currency_pairs);
                if(bscp != null) {
                        Coinbase_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinbase_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinbase_cp = ep.getExchange(Exchanges.COINBASE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinbase_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinbase
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbaseFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinbase
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbase_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbaseFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINBASEPRO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINBASEPRO, ep.getExchange(Exchanges.COINBASEPRO).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Coinbasepro_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinbasepro_currency_pairs);
                if(bscp != null) {
                        Coinbasepro_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinbasepro_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinbasepro_cp = ep.getExchange(Exchanges.COINBASEPRO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinbasepro_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from coinbasepro
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbaseproFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from coinbasepro
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbasepro_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbaseproFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINBENE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbene_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINBENE, ep.getExchange(Exchanges.COINBENE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinbene_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinbene_currency_pairs);
                if(bscp != null) {
                        Coinbene_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinbene_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinbene_cp = ep.getExchange(Exchanges.COINBENE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinbene_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinbene
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbene_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbeneFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinbene
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinbene_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinbeneFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINDEAL
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINDEAL, ep.getExchange(Exchanges.COINDEAL).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coindeal_cp = cp;
                String bscp = Config.getInstance().get(Constants.coindeal_currency_pairs);
                if(bscp != null) {
                        Coindeal_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coindeal_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coindeal_cp = ep.getExchange(Exchanges.COINDEAL).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coindeal_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coindeal
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoindealFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coindeal
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindeal_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoindealFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINDIRECT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINDIRECT, ep.getExchange(Exchanges.COINDIRECT).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coindirect_cp = cp;
                String bscp = Config.getInstance().get(Constants.coindirect_currency_pairs);
                if(bscp != null) {
                        Coindirect_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coindirect_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coindirect_cp = ep.getExchange(Exchanges.COINDIRECT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coindirect_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coindirect
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoindirectFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coindirect
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coindirect_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoindirectFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINEGG
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinegg_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINEGG, ep.getExchange(Exchanges.COINEGG).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinegg_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinegg_currency_pairs);
                if(bscp != null) {
                        Coinegg_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinegg_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinegg_cp = ep.getExchange(Exchanges.COINEGG).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinegg_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinegg
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinegg_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoineggFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinegg
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinegg_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoineggFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINEX, ep.getExchange(Exchanges.COINEX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinex_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinex_currency_pairs);
                if(bscp != null) {
                        Coinex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinex_cp = ep.getExchange(Exchanges.COINEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINFLOOR
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINFLOOR, ep.getExchange(Exchanges.COINFLOOR).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinfloor_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinfloor_currency_pairs);
                if(bscp != null) {
                        Coinfloor_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinfloor_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinfloor_cp = ep.getExchange(Exchanges.COINFLOOR).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinfloor_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinfloor
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinfloorFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinfloor
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinfloor_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinfloorFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINGI
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coingi_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINGI, ep.getExchange(Exchanges.COINGI).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coingi_cp = cp;
                String bscp = Config.getInstance().get(Constants.coingi_currency_pairs);
                if(bscp != null) {
                        Coingi_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coingi_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coingi_cp = ep.getExchange(Exchanges.COINGI).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coingi_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coingi
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coingi_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoingiFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coingi
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coingi_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoingiFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINMARKETCAP
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINMARKETCAP, ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinmarketcap_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinmarketcap_currency_pairs);
                if(bscp != null) {
                        Coinmarketcap_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinmarketcap_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinmarketcap_cp = ep.getExchange(Exchanges.COINMARKETCAP).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinmarketcap_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinmarketcap
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinmarketcap
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmarketcap_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmarketcapFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINMATE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINMATE, ep.getExchange(Exchanges.COINMATE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Coinmate_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinmate_currency_pairs);
                if(bscp != null) {
                        Coinmate_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinmate_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinmate_cp = ep.getExchange(Exchanges.COINMATE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinmate_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from coinmate
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmateFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from coinmate
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinmate_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinmateFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINONE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINONE, ep.getExchange(Exchanges.COINONE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinone_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinone_currency_pairs);
                if(bscp != null) {
                        Coinone_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinone_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinone_cp = ep.getExchange(Exchanges.COINONE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinone_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinone
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinoneFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinone
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinone_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinoneFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * COINSUPER
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.COINSUPER, ep.getExchange(Exchanges.COINSUPER).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Coinsuper_cp = cp;
                String bscp = Config.getInstance().get(Constants.coinsuper_currency_pairs);
                if(bscp != null) {
                        Coinsuper_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Coinsuper_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Coinsuper_cp = ep.getExchange(Exchanges.COINSUPER).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Coinsuper_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Coinsuper
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinsuperFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Coinsuper
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.coinsuper_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCoinsuperFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CRYPTOFACILITIES
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptofacilities_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CRYPTOFACILITIES, ep.getExchange(Exchanges.CRYPTOFACILITIES).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Cryptofacilities_cp = cp;
                String bscp = Config.getInstance().get(Constants.cryptofacilities_currency_pairs);
                if(bscp != null) {
                        Cryptofacilities_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cryptofacilities_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cryptofacilities_cp = ep.getExchange(Exchanges.CRYPTOFACILITIES).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cryptofacilities_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Cryptofacilities
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptofacilities_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptofacilitiesFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Cryptofacilities
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptofacilities_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptofacilitiesFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CRYPTONIT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CRYPTONIT, ep.getExchange(Exchanges.CRYPTONIT).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Cryptonit_cp = cp;
                String bscp = Config.getInstance().get(Constants.cryptonit_currency_pairs);
                if(bscp != null) {
                        Cryptonit_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cryptonit_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cryptonit_cp = ep.getExchange(Exchanges.CRYPTONIT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cryptonit_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Cryptonit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptonitFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Cryptonit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptonit_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptonitFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CRYPTOPIA
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CRYPTOPIA, ep.getExchange(Exchanges.CRYPTOPIA).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Cryptopia_cp = cp;
                String bscp = Config.getInstance().get(Constants.cryptopia_currency_pairs);
                if(bscp != null) {
                        Cryptopia_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cryptopia_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cryptopia_cp = ep.getExchange(Exchanges.CRYPTOPIA).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cryptopia_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Cryptopia
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptopiaFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Cryptopia
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptopia_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptopiaFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * CRYPTOWATCH
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.CRYPTOWATCH, ep.getExchange(Exchanges.CRYPTOWATCH).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Cryptowatch_cp = cp;
                String bscp = Config.getInstance().get(Constants.cryptowatch_currency_pairs);
                if(bscp != null) {
                        Cryptowatch_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Cryptowatch_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Cryptowatch_cp = ep.getExchange(Exchanges.CRYPTOWATCH).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Cryptowatch_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Cryptowatch
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptowatchFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Cryptowatch
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.cryptowatch_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getCryptowatchFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * DERIBIT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.DERIBIT, ep.getExchange(Exchanges.DERIBIT).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Deribit_cp = cp;
                String bscp = Config.getInstance().get(Constants.deribit_currency_pairs);
                if(bscp != null) {
                        Deribit_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Deribit_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Deribit_cp = ep.getExchange(Exchanges.DERIBIT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Deribit_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Deribit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getDeribitFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Deribit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.deribit_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getDeribitFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * DRAGONEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.DRAGONEX, ep.getExchange(Exchanges.DRAGONEX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Dragonex_cp = cp;
                String bscp = Config.getInstance().get(Constants.dragonex_currency_pairs);
                if(bscp != null) {
                        Dragonex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Dragonex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Dragonex_cp = ep.getExchange(Exchanges.DRAGONEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Dragonex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Dragonex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getDragonexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Dragonex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dragonex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getDragonexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * DSX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.dsx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.DSX, ep.getExchange(Exchanges.DSX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Dsx_cp = cp;
                String bscp = Config.getInstance().get(Constants.dsx_currency_pairs);
                if(bscp != null) {
                        Dsx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Dsx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Dsx_cp = ep.getExchange(Exchanges.DSX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Dsx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Dsx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dsx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getDsxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Dsx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dsx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getDsxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * DVCHAIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.dvchain_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.DVCHAIN, ep.getExchange(Exchanges.DVCHAIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Dvchain_cp = cp;
                String bscp = Config.getInstance().get(Constants.dvchain_currency_pairs);
                if(bscp != null) {
                        Dvchain_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Dvchain_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Dvchain_cp = ep.getExchange(Exchanges.DVCHAIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Dvchain_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Dvchain
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dvchain_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getDvchainFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Dvchain
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.dvchain_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getDvchainFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * ENIGMA
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.ENIGMA, ep.getExchange(Exchanges.ENIGMA).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Enigma_cp = cp;
                String bscp = Config.getInstance().get(Constants.enigma_currency_pairs);
                if(bscp != null) {
                        Enigma_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Enigma_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Enigma_cp = ep.getExchange(Exchanges.ENIGMA).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Enigma_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Enigma
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getEnigmaFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Enigma
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.enigma_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getEnigmaFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * EXMO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.EXMO, ep.getExchange(Exchanges.EXMO).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Exmo_cp = cp;
                String bscp = Config.getInstance().get(Constants.exmo_currency_pairs);
                if(bscp != null) {
                        Exmo_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Exmo_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Exmo_cp = ep.getExchange(Exchanges.EXMO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Exmo_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Exmo
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getExmoFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Exmo
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.exmo_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getExmoFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * EXX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.exx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.EXX, ep.getExchange(Exchanges.EXX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Exx_cp = cp;
                String bscp = Config.getInstance().get(Constants.exx_currency_pairs);
                if(bscp != null) {
                        Exx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Exx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Exx_cp = ep.getExchange(Exchanges.EXX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Exx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Exx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.exx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getExxFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Exx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.exx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getExxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * FCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.fcoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.FCOIN, ep.getExchange(Exchanges.FCOIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Fcoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.fcoin_currency_pairs);
                if(bscp != null) {
                        Fcoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Fcoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Fcoin_cp = ep.getExchange(Exchanges.FCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Fcoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Fcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.fcoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getFcoinFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Fcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.fcoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getFcoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * GATEIO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.GATEIO, ep.getExchange(Exchanges.GATEIO).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Gateio_cp = cp;
                String bscp = Config.getInstance().get(Constants.gateio_currency_pairs);
                if(bscp != null) {
                        Gateio_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Gateio_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Gateio_cp = ep.getExchange(Exchanges.GATEIO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Gateio_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Gateio
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getGateioFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Gateio
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.gateio_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getGateioFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * GEMINI
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.GEMINI, ep.getExchange(Exchanges.GEMINI).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Gemini_cp = cp;
                String bscp = Config.getInstance().get(Constants.gemini_currency_pairs);
                if(bscp != null) {
                        Gemini_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Gemini_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Gemini_cp = ep.getExchange(Exchanges.GEMINI).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Gemini_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from gemini
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getGeminiFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from gemini
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.gemini_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getGeminiFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * GLOBITEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.GLOBITEX, ep.getExchange(Exchanges.GLOBITEX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Globitex_cp = cp;
                String bscp = Config.getInstance().get(Constants.globitex_currency_pairs);
                if(bscp != null) {
                        Globitex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Globitex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Globitex_cp = ep.getExchange(Exchanges.GLOBITEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Globitex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Globitex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getGlobitexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Globitex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.globitex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getGlobitexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * HITBTC
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.HITBTC, ep.getExchange(Exchanges.HITBTC).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Hitbtc_cp = cp;
                String bscp = Config.getInstance().get(Constants.hitbtc_currency_pairs);
                if(bscp != null) {
                        Hitbtc_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Hitbtc_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Hitbtc_cp = ep.getExchange(Exchanges.HITBTC).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Hitbtc_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from hitbtc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getHitbtcFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from hitbtc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.hitbtc_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getHitbtcFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * HUOBI
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.HUOBI, ep.getExchange(Exchanges.HUOBI).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Huobi_cp = cp;
                String bscp = Config.getInstance().get(Constants.huobi_currency_pairs);
                if(bscp != null) {
                        Huobi_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Huobi_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Huobi_cp = ep.getExchange(Exchanges.HUOBI).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Huobi_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Huobi
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getHuobiFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Huobi
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.huobi_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getHuobiFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * IDEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.IDEX, ep.getExchange(Exchanges.IDEX).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Idex_cp = cp;
                String bscp = Config.getInstance().get(Constants.idex_currency_pairs);
                if(bscp != null) {
                        Idex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Idex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Idex_cp = ep.getExchange(Exchanges.IDEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Idex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Idex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getIdexFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Idex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.idex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getIdexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * ITBIT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.itbit_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.ITBIT, ep.getExchange(Exchanges.ITBIT).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Itbit_cp = cp;
                String bscp = Config.getInstance().get(Constants.itbit_currency_pairs);
                if(bscp != null) {
                        Itbit_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Itbit_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Itbit_cp = ep.getExchange(Exchanges.ITBIT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Itbit_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Itbit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.itbit_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getItbitFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Itbit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.itbit_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getItbitFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KOINEKS
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KOINEKS, ep.getExchange(Exchanges.KOINEKS).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Koineks_cp = cp;
                String bscp = Config.getInstance().get(Constants.koineks_currency_pairs);
                if(bscp != null) {
                        Koineks_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Koineks_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Koineks_cp = ep.getExchange(Exchanges.KOINEKS).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Koineks_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Koineks
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKoineksFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Koineks
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.koineks_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKoineksFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KOINIM
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KOINIM, ep.getExchange(Exchanges.KOINIM).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Koinim_cp = cp;
                String bscp = Config.getInstance().get(Constants.koinim_currency_pairs);
                if(bscp != null) {
                        Koinim_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Koinim_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Koinim_cp = ep.getExchange(Exchanges.KOINIM).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Koinim_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Koinim
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKoinimFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Koinim
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.koinim_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKoinimFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KRAKEN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KRAKEN, ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Kraken_cp = cp;
                String bscp = Config.getInstance().get(Constants.kraken_currency_pairs);
                if(bscp != null) {
                        Kraken_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Kraken_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Kraken_cp = ep.getExchange(Exchanges.KRAKEN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Kraken_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from kraken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKrakenFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from kraken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kraken_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKrakenFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KUCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KUCOIN, ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Kucoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.kucoin_currency_pairs);
                if(bscp != null) {
                        Kucoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Kucoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Kucoin_cp = ep.getExchange(Exchanges.KUCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Kucoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Kucoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKucoinFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Kucoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kucoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKucoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * KUNA
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.KUNA, ep.getExchange(Exchanges.KUNA).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Kuna_cp = cp;
                String bscp = Config.getInstance().get(Constants.kuna_currency_pairs);
                if(bscp != null) {
                        Kuna_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Kuna_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Kuna_cp = ep.getExchange(Exchanges.KUNA).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Kuna_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Kuna
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getKunaFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Kuna
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.kuna_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getKunaFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LAKEBTC
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.lakebtc_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LAKEBTC, ep.getExchange(Exchanges.LAKEBTC).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Lakebtc_cp = cp;
                String bscp = Config.getInstance().get(Constants.lakebtc_currency_pairs);
                if(bscp != null) {
                        Lakebtc_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Lakebtc_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Lakebtc_cp = ep.getExchange(Exchanges.LAKEBTC).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Lakebtc_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Lakebtc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lakebtc_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLakebtcFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Lakebtc
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lakebtc_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLakebtcFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LATOKEN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LATOKEN, ep.getExchange(Exchanges.LATOKEN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Latoken_cp = cp;
                String bscp = Config.getInstance().get(Constants.latoken_currency_pairs);
                if(bscp != null) {
                        Latoken_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Latoken_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Latoken_cp = ep.getExchange(Exchanges.LATOKEN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Latoken_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Latoken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLatokenFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Latoken
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.latoken_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLatokenFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LGO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LGO, ep.getExchange(Exchanges.LGO).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Lgo_cp = cp;
                String bscp = Config.getInstance().get(Constants.lgo_currency_pairs);
                if(bscp != null) {
                        Lgo_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Lgo_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Lgo_cp = ep.getExchange(Exchanges.LGO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Lgo_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from lgo
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLgoFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from lgo
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lgo_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLgoFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LIQUI
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LIQUI, ep.getExchange(Exchanges.LIQUI).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Liqui_cp = cp;
                String bscp = Config.getInstance().get(Constants.liqui_currency_pairs);
                if(bscp != null) {
                        Liqui_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Liqui_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Liqui_cp = ep.getExchange(Exchanges.LIQUI).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Liqui_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Liqui
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLiquiFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Liqui
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.liqui_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLiquiFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LIVECOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LIVECOIN, ep.getExchange(Exchanges.LIVECOIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Livecoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.livecoin_currency_pairs);
                if(bscp != null) {
                        Livecoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Livecoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Livecoin_cp = ep.getExchange(Exchanges.LIVECOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Livecoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Livecoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLivecoinFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Livecoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.livecoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLivecoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LUNO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LUNO, ep.getExchange(Exchanges.LUNO).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Luno_cp = cp;
                String bscp = Config.getInstance().get(Constants.luno_currency_pairs);
                if(bscp != null) {
                        Luno_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Luno_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Luno_cp = ep.getExchange(Exchanges.LUNO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Luno_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Luno
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLunoFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Luno
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.luno_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLunoFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * LYKKE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.LYKKE, ep.getExchange(Exchanges.LYKKE).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Lykke_cp = cp;
                String bscp = Config.getInstance().get(Constants.lykke_currency_pairs);
                if(bscp != null) {
                        Lykke_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Lykke_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Lykke_cp = ep.getExchange(Exchanges.LYKKE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Lykke_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Lykke
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getLykkeFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Lykke
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.lykke_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getLykkeFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * MERCADOBITCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.mercadobitcoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.MERCADOBITCOIN, ep.getExchange(Exchanges.MERCADOBITCOIN).getCurrencyPairs());
			} else {
                ArrayList<CurrencyPair> Mercadobitcoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.mercadobitcoin_currency_pairs);
                if(bscp != null) {
                        Mercadobitcoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Mercadobitcoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Mercadobitcoin_cp = ep.getExchange(Exchanges.MERCADOBITCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Mercadobitcoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from Mercadobitcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.mercadobitcoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getMercadobitcoinFactory().create_ticker_feeders(ep, ctx, cp));
                }

                //Create an orderbook from Mercadobitcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.mercadobitcoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getMercadobitcoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * OKCOIN
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.OKCOIN, ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Okcoin_cp = cp;
                String bscp = Config.getInstance().get(Constants.okcoin_currency_pairs);
                if(bscp != null) {
                        Okcoin_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Okcoin_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Okcoin_cp = ep.getExchange(Exchanges.OKCOIN).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Okcoin_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from okcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getOkcoinFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from okcoin
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.okcoin_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getOkcoinFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * PARIBU
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.PARIBU, ep.getExchange(Exchanges.PARIBU).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Paribu_cp = cp;
                String bscp = Config.getInstance().get(Constants.paribu_currency_pairs);
                if(bscp != null) {
                        Paribu_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Paribu_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Paribu_cp = ep.getExchange(Exchanges.PARIBU).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Paribu_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from paribu
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getParibuFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from paribu
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.paribu_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getParibuFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * PAYMIUM
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.PAYMIUM, ep.getExchange(Exchanges.PAYMIUM).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Paymium_cp = cp;
                String bscp = Config.getInstance().get(Constants.paymium_currency_pairs);
                if(bscp != null) {
                        Paymium_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Paymium_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Paymium_cp = ep.getExchange(Exchanges.PAYMIUM).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Paymium_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from paymium
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getPaymiumFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from paymium
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.paymium_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getPaymiumFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * POLONIEX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.POLONIEX, ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Poloniex_cp = cp;
                String bscp = Config.getInstance().get(Constants.poloniex_currency_pairs);
                if(bscp != null) {
                        Poloniex_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Poloniex_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Poloniex_cp = ep.getExchange(Exchanges.POLONIEX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Poloniex_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from poloniex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getPoloniexFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from poloniex
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.poloniex_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getPoloniexFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * QUOINE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.QUOINE, ep.getExchange(Exchanges.QUOINE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Quoine_cp = cp;
                String bscp = Config.getInstance().get(Constants.quoine_currency_pairs);
                if(bscp != null) {
                        Quoine_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Quoine_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Quoine_cp = ep.getExchange(Exchanges.QUOINE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Quoine_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from quoine
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getQuoineFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from quoine
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.quoine_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getQuoineFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * RIPPLE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.RIPPLE, ep.getExchange(Exchanges.RIPPLE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Ripple_cp = cp;
                String bscp = Config.getInstance().get(Constants.ripple_currency_pairs);
                if(bscp != null) {
                        Ripple_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Ripple_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Ripple_cp = ep.getExchange(Exchanges.RIPPLE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Ripple_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from ripple
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getRippleFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from ripple
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.ripple_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getRippleFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * THEROCK
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.therock_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.THEROCK, ep.getExchange(Exchanges.THEROCK).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Therock_cp = cp;
                String bscp = Config.getInstance().get(Constants.therock_currency_pairs);
                if(bscp != null) {
                        Therock_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Therock_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Therock_cp = ep.getExchange(Exchanges.THEROCK).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Therock_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from therock
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.therock_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getTherockFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from therock
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.therock_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getTherockFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * TRADEOGRE
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.tradeogre_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.TRADEOGRE, ep.getExchange(Exchanges.TRADEOGRE).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Tradeogre_cp = cp;
                String bscp = Config.getInstance().get(Constants.tradeogre_currency_pairs);
                if(bscp != null) {
                        Tradeogre_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Tradeogre_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Tradeogre_cp = ep.getExchange(Exchanges.TRADEOGRE).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Tradeogre_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from tradeogre
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.tradeogre_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getTradeogreFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from tradeogre
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.tradeogre_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getTradeogreFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * TRUEFX
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.truefx_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.TRUEFX, ep.getExchange(Exchanges.TRUEFX).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Truefx_cp = cp;
                String bscp = Config.getInstance().get(Constants.truefx_currency_pairs);
                if(bscp != null) {
                        Truefx_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Truefx_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Truefx_cp = ep.getExchange(Exchanges.TRUEFX).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Truefx_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from truefx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.truefx_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getTruefxFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from truefx
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.truefx_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getTruefxFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * UPBIT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.UPBIT, ep.getExchange(Exchanges.UPBIT).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Upbit_cp = cp;
                String bscp = Config.getInstance().get(Constants.upbit_currency_pairs);
                if(bscp != null) {
                        Upbit_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Upbit_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Upbit_cp = ep.getExchange(Exchanges.UPBIT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Upbit_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from upbit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getUpbitFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from upbit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.upbit_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getUpbitFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * VAULTORO
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.VAULTORO, ep.getExchange(Exchanges.VAULTORO).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Vaultoro_cp = cp;
                String bscp = Config.getInstance().get(Constants.vaultoro_currency_pairs);
                if(bscp != null) {
                        Vaultoro_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Vaultoro_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Vaultoro_cp = ep.getExchange(Exchanges.VAULTORO).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Vaultoro_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from vaultoro
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getVaultoroFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from vaultoro
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.vaultoro_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getVaultoroFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * YOBIT
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.yobit_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.YOBIT, ep.getExchange(Exchanges.YOBIT).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Yobit_cp = cp;
                String bscp = Config.getInstance().get(Constants.yobit_currency_pairs);
                if(bscp != null) {
                        Yobit_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Yobit_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Yobit_cp = ep.getExchange(Exchanges.YOBIT).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Yobit_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from yobit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.yobit_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getYobitFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from yobit
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.yobit_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getYobitFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}

		/*
		 * ZAIF
		 */
		if (Boolean.parseBoolean(Config.getInstance().get(Constants.zaif_enabled))) {
		    if (listCmd) {
				displayCurrencyPairs(Exchanges.ZAIF, ep.getExchange(Exchanges.ZAIF).getCurrencyPairs());
			} else {

                ArrayList<CurrencyPair> Zaif_cp = cp;
                String bscp = Config.getInstance().get(Constants.zaif_currency_pairs);
                if(bscp != null) {
                        Zaif_cp = new ArrayList<>();
                        for(String pair : bscp.split(",")) {
                            Zaif_cp.add(new CurrencyPair(pair));
                        }
                } else {
                    Zaif_cp = ep.getExchange(Exchanges.ZAIF).getCurrencyPairs();
                    Iterator<CurrencyPair> pair = cp.iterator();
                    while(pair.hasNext()){
                        CurrencyPair p = pair.next();
                        if(!Zaif_cp.contains(p)) {
                            pair.remove();
                        }
                    }
                }

                //Create a ticker from zaif
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.zaif_ticker_enabled))) {
                    thds.addAll(ExchangesFactory.getZaifFactory().create_ticker_feeders(ep, ctx, cp));
                }
                // BUG : orderbook and tickers should be requested on the same thread or they are mutually exclusive
                //Create an orderbook from zaif
                if (Boolean.parseBoolean(Config.getInstance().get(Constants.zaif_orderbook_enabled))) {
                    thds.addAll(ExchangesFactory.getZaifFactory().create_orderbook_feeders(ep, ctx, cp));
                }
			}
		}


		// Connect work threads to client threads via a queue
		ZMQ.proxy(clients, workers, null);

		// Infinite loop
		for (Thread thd : thds) {
			thd.join();
		}

		workers.close();
		clients.close();
		ctx.destroy();

		logger.trace("EXIT");
	}
}
