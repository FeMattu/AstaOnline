package test;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import classi.Asta;
import classi.Cliente;
import server.Resources;

class tt {

	private static Resources resources =null;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		String ip = null;
		try {
			ip = InetAddress.getByName("astadb.ddns.net").getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String databaseURL = "jdbc:mysql://" + ip + ":3306";
		resources = new Resources(databaseURL, "gambit", "test", "trK5iuHPLQNLDZ9J");
	}

	@Test
	void testGetIndirizziMulticast() {
		List<String> indirizziMulticast = resources.getIndirizziMulticast();
		String indirizzo = "224.0.0.";
		int i = 3;
		boolean ok = false;
		
		for (String string : indirizziMulticast) {
			if(string.equals(indirizzo+i))
				ok = true;
			else fail("Test getIndirizziMulticastFallisto");
			i++;
		}
		
		assert(ok);
	}

	@Test
	void testGetCategorie() {
		String[] categorie = resources.getCategorie();
		for (String string : categorie) {
			System.out.println(string);
		}
	}

	@Test
	void testGetCurrentGambits() {
	}

	@Test
	void testGetAllGambitsToNow() {
		fail("Not yet implemented");
	}

	@Test
	void testAddAstaIntoDB() {
		
		//resources.addAstaIntoDB(new Asta(123, new Date(12344555), new Date(12345666), 
		//		"192.168.1.1", new Cliente("Amber77", "Ike", null, null, null, null, null), null));
	}

	@Test
	void testRemoveAstaFromDB() {
		fail("Not yet implemented");
	}

	@Test
	void testGetIdUltimaAsta() {
		fail("Not yet implemented");
	}

	@Test
	void testGetClienti() {
		LinkedList<Cliente> clienti = resources.getClienti();
		
		if(clienti.size() == 100) {
			boolean ok = false;
			Cliente cliente1 = clienti.get(0);
			
			if(cliente1.getUSERNAME().equals("Amber77")
					&& cliente1.getNome().equals("Ike")
					&& cliente1.getCognome().equals("Petterson")
					&& cliente1.getData_nascita().toString().equals("1995-04-05")
					&& cliente1.getResidenza().equals("35 Palos Verdes Mall"))
				ok = true;
			
			assert(ok);
		}
	}

	@Test
	void testGetVenditori() {
		fail("Not yet implemented");
	}

	@Test
	void testAddCliente() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveCliente() {
		fail("Not yet implemented");
	}

	@Test
	void testGetProdotti() {
		fail("Not yet implemented");
	}

	@Test
	void testAddProdotto() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveProdotto() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOfferte() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOfferteByCliente() {
		fail("Not yet implemented");
	}

	@Test
	void testGetOfferteByAsta() {
		fail("Not yet implemented");
	}

	@Test
	void testAddOfferta() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveOfferta() {
		fail("Not yet implemented");
	}

}
