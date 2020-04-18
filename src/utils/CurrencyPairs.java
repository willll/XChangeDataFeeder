package utils;

import java.util.Set;

import org.knowm.xchange.currency.CurrencyPair;

import exchanges.factories.EntryPoint.Exchanges;

public class CurrencyPairs {
	public static void displayCurrencyPairs(Exchanges xch, Set<CurrencyPair> cp) {
		System.out.println (xch + " :\n");
		for (CurrencyPair currencyPair : cp) {
			System.out.print(currencyPair + ", ");
		}
		System.out.println ("\n");
	}
}
