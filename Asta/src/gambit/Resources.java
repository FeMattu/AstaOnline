package gambit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Classe thread-safe in cui vengono gestite tutte le risorse del server, inoltre si interfaccia con il DBMS.
 */
public class Resources {
    //variabili Connessione al database
    private Connection con;
	private String databaseURL;
	private String user;
	
	//liste thread-safe
	private List<Asta> aste;
	private List<String> indirizziMulticast;

	/**
	 * Costruisce un monitor delle risorse, e funziona da classe CRUD per il database, 
	 * instaurando la connessione con il database dato;
	 * <strong>ATTENZIONE.</strong> i driver caricati per il database sono i driver per MySQL
	 * @param databaseURL URL del database a cui connettersi
	 * @param database Databse da utilizzare
	 * @param user UserName del database
	 * @param psw Password del databse
	 */
	public Resources(String databaseURL, String database, String user, String psw) {	
		this.databaseURL = databaseURL;
		this.user = user;
		
		try {
			System.out.println("Connessione con il database...");
			con = DriverManager.getConnection(databaseURL+"/"+database, user, psw);
			System.out.println("Connessione effettuata");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		aste = Collections.synchronizedList(new LinkedList<Asta>());
		indirizziMulticast = Collections.synchronizedList(new ArrayList<String>());
		
		System.out.println("Crezione degli indirizzi IP multicast");
        for(int i = 0+3; i < 30+3; i++) {
        	String indirizzo = "224.0.0."+i;
        	indirizziMulticast.add(indirizzo);
        }
        System.out.println("Indirizzi creati: "+indirizziMulticast.size());	
	}
	
	//DATI DATABASE
	/**
	 * restituisce l'URL del database
	 * @return URL Database
	 */
	public String getDatabaseURL() {
		return databaseURL;
	}

	/**
	 * restiuisce l'user connesso al database
	 * @return User connesso al database
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * metodo per avere una query custom al database
	 * <strong>non so se va perchè ho fatto na roba molto ambigua</strong>
	 * @param query Query da efettuare
	 * @return ResultSet restituito
	 */
	public ResultSet customQuery(String query) {
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		synchronized (lockAste) {
			synchronized (lockClienti) {
				synchronized (lockOfferte) {
					synchronized (lockProdotti) {
						try {
							return sta.executeQuery(query);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

	//INDIRIZZI MULTICAST
	/**
	 * Ritorna una lista thread-safae degli inidrizzi multicast disponibili
	 * @return lista thread-safe degli indirizzi multicast disponibili
	 */
	public List<String> getIndirizziMulticast() {
		return indirizziMulticast;
	}
	
	//GESTIONE CATEGORIE
	public String[] getCategorie() {
		String [] categorie = new String[9];
		int i = 0;
		try {
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("SELECT categoria FROM Categorie;");
			while(rs.next()) {
				categorie[i] = rs.getString("categoria");
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return categorie;
	}
	
	/**
	 * Recupera l'id di una categoria
	 * @param categoria Categoria di cui prendere l'id, il metodo non è case-sensitive
	 * @return id della categoria, altrimenti -1
	 */
	public int getIdCategoria(String categoria) {
		
		String[] categorie = getCategorie();
		String query = "SELECT id_categoria FROM Categorie WHERE categoria=";
		for (String string : categorie) {
			if(string.equalsIgnoreCase(categoria)) {
				query = query+string;
			}
		}
		
		try {
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(query);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return -1;
	}
	
	//GESTIONE DELLE ASTE
	private Object lockAste = new Object();
	
	/**
	 * Restitutisce tutte le aste che sono attive, quello a cui è quindi possibile partecipare.
	 * la loista è <strong>Thread-safe</strong>.
	 * @return linked list (thread-safe) interna delle aste attive.
	 */
	public List<Asta> getCurrentGambits() {
		return aste;
	}
	
	/**
	 * Aggiunge un asta attiva
	 * @param asta Oggetto rappresentate l'asta attiva
	 * @return strong>true</strong> se l'asta è stata aggiunta con successo <strong>false</strong> altrimenti
	 */
	public boolean addActiveAsta(Asta asta) {
		return aste.add(asta);
	}
	
	/**
	 * rimuove un'asta attiva
	 * @param asta Asta da rimuovere dall lista
	 * @return Asta rimossa
	 */
	public Asta removeActiveAsta(Asta asta) {
		return aste.remove(getIdUltimaAsta());
	}
	
	/**
	 * prende una singola asta
	 * @param id_asta Id dell asta da prendere
	 * @return la rappresentazone dell'asta
	 */
	public Asta getAsta(int id_asta) {
		LinkedList<Prodotto> prodotti = getProdotti();
		LinkedList<Cliente> vincitori = getClienti();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockAste) {	
			Prodotto prodotto = null;
			Cliente vincitore = null;
			try {
				ResultSet rs = sta.executeQuery("SELECT * FROM Aste WHERE id_asta="+id_asta);
				rs.next();
				for (Cliente cliente : vincitori) {
					if(cliente.getUSERNAME().equals(rs.getString("vincitore"))) {
						vincitore = cliente;
						vincitori.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				for (Prodotto prod : prodotti) {
					if(prod.getID_PRODOTTO() == rs.getInt("id_prodotto")) {
						prodotto = prod;
						prodotti.remove(prodotto);
						break;
					}
				}
				return new Asta(
						rs.getInt("id_asta"),
						rs.getTimestamp("dataOra_inzio"),
						rs.getTimestamp("dataOra_fine"),
						rs.getString("indirizzo_ip"),
						vincitore,
						prodotto);
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	/**
	 * restituisce tutte le aste che ci sono all'interno del database, 
	 * quindi tutte le aste che ci sono state e che sono concluse
	 * @return ritorna le aste all'interno del database
	 */
	public LinkedList<Asta> getAllGambitsToNow() {
		LinkedList<Asta> asteDatabase = new LinkedList<Asta>();
		LinkedList<Prodotto> prodotti = getProdotti();
		LinkedList<Cliente> vincitori = getClienti();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockAste) {	
			Prodotto prodotto = null;
			Cliente vincitore = null;
			try {
				ResultSet rs = sta.executeQuery("SELECT * FROM Aste");
				while(rs.next()) {
					for (Cliente cliente : vincitori) {
						if(cliente.getUSERNAME().equals(rs.getString("vincitore"))) {
							vincitore = cliente;
							vincitori.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					for (Prodotto prod : prodotti) {
						if(prod.getID_PRODOTTO() == rs.getInt("id_prodotto")) {
							prodotto = prod;
							prodotti.remove(prodotto);
							break;
						}
					}
					asteDatabase.add(new Asta(
							rs.getInt("id_asta"),
							rs.getTimestamp("dataOra_inzio"),
							rs.getTimestamp("dataOra_fine"),
							rs.getString("indirizzo_ip"),
							vincitore,
							prodotto));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return asteDatabase;
		}
	}
	
	/**
	 * Inserisce l'asta terminata all'interno del db
	 * @param asta da aggiungere
	 * @return <strong>true</strong> se l'asta è stata aggiunta con successo <strong>false</strong> altrimenti
	 */
	public boolean addAstaIntoDB(Asta asta) {
		String query = "INSERT INTO Aste VALUES ('"
				+ asta.getId_asta() +"','"
				+ asta.getDataOra_inizio() +"','"
				+ asta.getDataOra_fine() +"','"
				+ asta.getVincitore().getUSERNAME() +"','"
				+ asta.getProdotto().getID_PRODOTTO() +"','"
				+ asta.getIp() + "')";
		
		synchronized (lockAste) {	
			try {
				Statement sta = con.createStatement();
				sta.executeUpdate(query);
				return true;
			} catch(Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	
	/**
	 * Rimuove un'asta all'interno del database
	 * @param id_asta id dell'asta da rimuovere
	 * @return <strong>true</strong> se l'asta è stata rimossa <strong>false</strong> altrimenti
	 */
	public Asta removeAstaFromDB(int id_asta) {
		String queryDati = "SELECT * FROM Aste WHERE id_asta="+id_asta;
		String queryRimozione = "DELETE FROM Aste WHERE id_asta="+id_asta;
		Asta asta = null;
		
		LinkedList<Prodotto> prodotti = getProdotti();
		LinkedList<Cliente> vincitori = getClienti();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockAste) {
			try {
				ResultSet rs = sta.executeQuery(queryDati);
				rs.next();
				Cliente vincitore = null;
				Prodotto prodotto = null;
				for (Cliente cliente : vincitori) {
					if(cliente.getUSERNAME().equals(rs.getString("vincitore"))) {
						vincitore = cliente;
						vincitori.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				for (Prodotto prod : prodotti) {
					if(prod.getID_PRODOTTO() == rs.getInt("id_prodotto")) {
						prodotto = prod;
						prodotti.remove(prodotto);
						break;
					}
				}
				asta = new Asta(
						rs.getInt("id_asta"),
						rs.getTimestamp("dataOra_inzio"),
						rs.getTimestamp("dataOra_fine"),
						rs.getString("indirizzo_ip"),
						vincitore,
						prodotto);
				if(sta.executeUpdate(queryRimozione) > 0)
					return asta;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	/**
	  * Il seguente metodo ritorna l'id dell'ultima asta effettuata e, in caso di errore, ritorna -1
	  * 
	  * @return id ultima asta
	  */
	 public int getIdUltimaAsta() {
		 int idUltimaAsta = 0;
		 String query = "SELECT MAX(id_asta) FROM Aste";
		 try {
			 Statement sta = con.createStatement();
			 ResultSet rs = sta.executeQuery(query);
			 rs.next();
			 idUltimaAsta = rs.getInt(1);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return idUltimaAsta;
	 }
	 	
	//GESTIONE DEI CLIENTI
	//lock per il synchronized
	private Object lockClienti = new Object();
	
	/**
	 * Recupera tutti i clienti dal database, non vengono distini i clienti che hanno effettuate offerte
	 * o che hanno messo in vendita dei prodotti, ma solo tutti i clienti che sono registrati
	 * @return clienti registrati
	 */
	public LinkedList<Cliente> getClienti(){	
		LinkedList<Cliente> clienti = new LinkedList<Cliente>();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockClienti) {
			try {
				ResultSet rs = sta.executeQuery("SELECT * FROM Clienti;");
				while(rs.next()) {
					clienti.add(new Cliente(
							rs.getString("UserName"),
							rs.getString("nome"),
							rs.getString("cognome"),
							rs.getDate("data_nascita"),
							rs.getString("residenza"),
							rs.getString("password"),
							rs.getString("email")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return clienti;
		}
	}
	
	/**
	 * Restituisce tutti i clienti (venditori) che hanno messo all'asta un prodotto
	 * @return clienti che hanno messo all'asta un prodotto
	 */
	public LinkedList<Cliente> getVenditori() {
		LinkedList<Cliente> venditori = new LinkedList<Cliente>();
		String query = "SELECT UserName,Clienti.nome,cognome,data_nascita,residenza,password,email "
				+ "FROM Prodotti, Clienti "
				+ "WHERE Prodotti.venditore = Clienti.UserName "
				+ "GROUP BY UserName;";
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockClienti) {
			try {
				ResultSet rs = sta.executeQuery(query);
				while(rs.next()) {
					venditori.add(new Cliente(
							rs.getString("UserName"),
							rs.getString("nome"),
							rs.getString("cognome"),
							rs.getDate("data_nascita"),
							rs.getString("residenza"),
							rs.getString("password"),
							rs.getString("email")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return venditori;
		}
	}
	
	/**
	 * recupera un cliente dal database
	 * @param UserName UserName del cliente
	 * @return l'ogggetto rappresentate il cliente
	 */
	public Cliente getCliente(String UserName) {
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockClienti) {
			try {
				ResultSet rs = sta.executeQuery("SELECT * FROM Clienti WHERE UserName='"+UserName+"';");
				rs.next();
				return new Cliente(
							rs.getString("UserName"),
							rs.getString("nome"),
							rs.getString("cognome"),
							rs.getDate("data_nascita"),
							rs.getString("residenza"),
							rs.getString("password"),
							rs.getString("email"));
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	/**
	 * aggiunge un cliente nel database
	 * @param cliente cliente da aggiungere
	 * @return <strong>true</strong> se il cliente è stato aggiunto, <strong>false</strong> altrimenti
	 */
	public boolean addCliente(Cliente cliente) {
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String query = "INSERT INTO Clienti "
				+ "(UserName,nome,cognome,data_nascita,residenza,password,email) "
				+ "VALUES ('"+cliente.getUSERNAME()+"','"
				+ cliente.getNome()+"','"
				+ cliente.getCognome()+"','"
				+ cliente.getData_nascita()+"','"
				+ cliente.getResidenza()+"','"
				+ cliente.getPassword()+"','"
				+ cliente.getEmail()+"');";
		
		synchronized (lockClienti) {
			try {
				int rs = sta.executeUpdate(query);
				if( rs != 0) return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * Rimuove un cliente dal database
	 * @param userName UserName del cliente da rimuovere
	 * @return Cliente rimosso
	 */
	public Cliente removeCliente(String userName) {
		Cliente cliente = null;
		String queryDati = "SELECT * FROM Clienti WHERE userName="+userName;
		String queryRimozione = "DELETE FROM Clienti WHERE userName="+userName;
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		synchronized (lockClienti) {
			try {
				ResultSet rs = sta.executeQuery(queryDati);
				rs.next();
				cliente = new Cliente(
							rs.getString("UserName"),
							rs.getString("nome"),
							rs.getString("cognome"),
							rs.getDate("data_nascita"),
							rs.getString("residenza"),
							rs.getString("password"),
							rs.getString("email"));	
				if(sta.executeUpdate(queryRimozione) > 0)
					return cliente;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	
	//GESTIONE DEI PRODOTTI
	//lock per il synchronized
	private Object lockProdotti = new Object();
	
	/**
	 * Recupera tutti i prodotti all'interno del database.
	 * Dopo aver effettuato la query, i prodotti vengono presi se l'id e' maggiore dell'id dell'ultimo prodotto aggiunto,
	 * quidi vengo aggiuti tutti i prodotti che non sono presenti all'interno della lista.
	 * <strong>Notare</strong>, questo metodo oltre a recuperare tutti i prodotti all'interno del database recupera
	 * anche tutti i clienti associati ai prodotti, quindi i venditori; il metodo effettua quindi un'invocazione
	 * implicita del metodo {@link getVenditori()}, ma non restituisce la lista dei vendiori
	 * <strong>metodo synchrinizzato</strong>, con il metodo {@link addProdotto()}.
	 * @return
	 */
	public LinkedList<Prodotto> getProdotti() {
		LinkedList<Prodotto> prodotti = new LinkedList<Prodotto>();
		LinkedList<Cliente> venditori = getVenditori();
		String query = "SELECT * "
				+ "FROM Prodotti,Categorie "
				+ "WHERE Prodotti.id_categoria = Categorie.id_categoria "
				+ "ORDER BY Prodotti.id_prodotto;";
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockProdotti) {
			try {
				ResultSet rs = sta.executeQuery(query);
				while (rs.next()) {			
					Cliente venditore = null;
					for (Cliente cliente : venditori) {
						if(cliente.getUSERNAME().equals(rs.getString("venditore"))) {
							venditore = cliente;
							venditori.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					prodotti.add(new Prodotto(
							rs.getInt("id_prodotto"), 
							rs.getString("nome"), 
							rs.getString("descrizione"), 
							rs.getFloat("prezzoDiBase"), 
							rs.getBoolean("venduto"), 
							venditore,
							rs.getTimestamp("dataOra_aggiunta"),
							rs.getString("categoria")));	
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return prodotti;	
		}
	}
	
	public Prodotto getProdotto(int id_prodotto) {
		LinkedList<Cliente> venditori = getVenditori();
		String query = "SELECT * "
				+ "FROM Prodotti,Categorie "
				+ "WHERE Prodotti.id_categoria = Categorie.id_categoria "
				+ "WHERE id_prodotto="+id_prodotto;
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockProdotti) {
			try {
				ResultSet rs = sta.executeQuery(query);		
				Cliente venditore = null;
				rs.next();
				for (Cliente cliente : venditori) {
					if(cliente.getUSERNAME().equals(rs.getString("venditore"))) {
						venditore = cliente;
						break;
					}
				}
				return new Prodotto(
						rs.getInt("id_prodotto"), 
						rs.getString("nome"), 
						rs.getString("descrizione"), 
						rs.getFloat("prezzoDiBase"), 
						rs.getBoolean("venduto"), 
						venditore,
						rs.getTimestamp("dataOra_aggiunta"),
						rs.getString("categoria"));	
			
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			return null;	
		}
	}
	
	/**
	 * Aggiunge un prodotto all'interno del database, questo è un methodo thrad-safe.
	 * E' synchrinizzato con il metodo {@link getProdotti}.
	 * @param prodotto Prodotto che si vuole aggiungere all'interno del database
	 * @return <strong>true</strong> il prodotto è stato aggiunto correttamente all'interno del database,
	 * <strong>false</strong> altrimenti.
	 */
	public boolean addProdotto(Prodotto prodotto) {
		String query = "INSERT INTO Prodotti"
				+ "(id_prodotto,nome,descrizione,prezzoDiBase,venduto,venditore,id_categoria,dataOra_aggiunta)"
				+ "VALUES ('"+prodotto.getID_PRODOTTO()+"','"
				+ prodotto.getNome()+"','"
				+ prodotto.getDescrizione()+"','"
				+ prodotto.getPrezzoDiBase()+"','"
				+ prodotto.isVenduto()+"','"
				+ prodotto.getVenditore().getUSERNAME()+"','"
				+ prodotto.getCategoria()+"','"
				+ prodotto.getDataOra_aggiunta()+"')";
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockProdotti) {
			try {
				int rs = sta.executeUpdate(query);
				if( rs != 0) return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * Rimuove un prodotto all'interno del database
	 * @param id del prodotto da rimuovere
	 * @return il prodotto rimosso
	 */
	public Prodotto removeProdotto(int id_prodotto) {
		Prodotto prodotto = null;
		String queryDati = "SELECT * FROM Prodotti WHERE id_prodotto="+id_prodotto;
		String queryRimozione = "DELETE FROM Prodotti WHERE id_prodotto="+id_prodotto;
		LinkedList<Cliente> venditori = getVenditori();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockClienti) {
			try {
				Cliente venditore = null;
				ResultSet rs = sta.executeQuery(queryDati);
				rs.next();
				for (Cliente cliente : venditori) {
					if(cliente.getUSERNAME().equals(rs.getString("venditore"))) {
						venditore = cliente;
						venditori.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				prodotto = new Prodotto(
						rs.getInt("id_prodotto"), 
						rs.getString("nome"), 
						rs.getString("descrizione"), 
						rs.getFloat("prezzoDiBase"), 
						rs.getBoolean("venduto"), 
						venditore,
						rs.getTimestamp("dataOra_aggiunta"),
						rs.getString("categoria"));	
				
				if(sta.executeUpdate(queryRimozione) > 0)
					return prodotto;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAttributesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	//GESTIONE DELLE OFFERTE
	private Object lockOfferte = new Object();
	
	/**
	 * Restituisce tutte le offerte che ci sono all'interno del database
	 * @return
	 */
	 public LinkedList<Offerta> getOfferte(){
		 LinkedList<Offerta> offerte = new LinkedList<Offerta>();
		 LinkedList<Cliente> offerenti = getClienti();
		 LinkedList<Asta> aste = getAllGambitsToNow();
		 String query = "SELECT * FROM Offerte";
		 
		 Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 synchronized (lockOfferte) {
			 try {
				ResultSet rs = sta.executeQuery(query);
				Cliente offerente = null;
				Asta asta = null;
				while(rs.next()) {
					for (Cliente cliente : offerenti) {
						if(cliente.getUSERNAME().equals(rs.getString("userName"))) {
							offerente = cliente;
							offerenti.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					for (Asta a : aste) {
						if(a.getId_asta() == rs.getInt("id_asta")) {
							asta = a;
							aste.remove(a); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					offerte.add(new Offerta(
							rs.getInt("id_offerta"),
							rs.getTimestamp("DataOra_offerta"),
							rs.getFloat("offerta"),
							offerente,
							asta));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return offerte;
		}
	 }
	 
	 /**
	  * restituisce tutte le offerte fatta da un cliente
	  * @param cliente Cliente di cui recpuerare tutte le offerte
	  * @return Offerte fatte dal cliente
	  */
	 public LinkedList<Offerta> getOfferteByCliente(Cliente cliente){
		LinkedList<Offerta> offerte = new LinkedList<Offerta>();
		LinkedList<Asta> aste = getAllGambitsToNow();
		String query = "SELECT * FROM Offerte WHERE UserName="+cliente.getUSERNAME();	
		 
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockOfferte) {
			try {
				ResultSet rs = sta.executeQuery(query);
				Asta asta = null;
				while(rs.next()) {
					for (Asta a : aste) {
						if(a.getId_asta() == rs.getInt("id_asta")) {
							asta = a;
							aste.remove(a); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					offerte.add(new Offerta(
							rs.getInt("id_offerta"),
							rs.getTimestamp("DataOra_offerta"),
							rs.getFloat("offerta"),
							cliente,
							asta));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return offerte;
		}
	 }
	 
	 /**
	  * recupera un offerta dal database
	  * @param id_offerta Id dell'offerta
	  * @return L'oggetto rappresentante l'asta
	  */
	 public Offerta getOfferta(int id_offerta) {
		 LinkedList<Cliente> offerenti = getClienti();
		 LinkedList<Asta> aste = getAllGambitsToNow();
		 String query = "SELECT * FROM Offerte WHERE id_offerta="+id_offerta;
		 
		 Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 synchronized (lockOfferte) {
			 try {
				ResultSet rs = sta.executeQuery(query);
				Cliente offerente = null;
				Asta asta = null;
				rs.next();
				for (Cliente cliente : offerenti) {
					if(cliente.getUSERNAME().equals(rs.getString("userName"))) {
						offerente = cliente;
						offerenti.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				for (Asta a : aste) {
					if(a.getId_asta() == rs.getInt("id_asta")) {
						asta = a;
						aste.remove(a); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				
				return new Offerta(
						rs.getInt("id_offerta"),
						rs.getTimestamp("DataOra_offerta"),
						rs.getFloat("offerta"),
						offerente,
						asta);		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	 }
	 
	 /**
	  * restiutisce tutte le offerte effettuate ad un'asta
	  * @param asta Asta di cui recuperare tutte le offerte che sono state fatte
	  * @return Offerta fatte all'asta
	  */
	 public LinkedList<Offerta> getOfferteByAsta (Asta asta){
		LinkedList<Offerta> offerte = new LinkedList<Offerta>();
		LinkedList<Cliente> offerenti = getClienti();
		String query = "SELECT * FROM Offerte WHERE ID_Asta="+asta.getId_asta();
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		synchronized (lockOfferte) {
			 try {	
				ResultSet rs = sta.executeQuery(query);
				Cliente offerente = null;
				while(rs.next()) {
					for (Cliente cliente : offerenti) {
						if(cliente.getUSERNAME().equals(rs.getString("userName"))) {
							offerente = cliente;
							offerenti.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
							break;
						}
					}
					offerte.add(new Offerta(
							rs.getInt("id_offerta"),
							rs.getTimestamp("DataOra_offerta"),
							rs.getFloat("offerta"),
							offerente,
							asta));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return offerte;
		}
	 }
	 
	 public boolean addOfferta(Offerta offerta) {
		 String query = "INSERT INTO Prodotti "
				+ "(id_prodotto,nome,descrizione,prezzoDiBase,venduto,venditore,id_categoria,dataOra_aggiunta) "
				+ "VALUES ('"+offerta.getId_offerta()+"','"
				+ offerta.getDataOraOfferta()+"','"
				+ offerta.getOfferta()+"','"
				+ offerta.getOfferente().getUSERNAME()+"','"
				+ offerta.getAsta().getId_asta()+"');";
		 
		LinkedList<Cliente> offerenti = getClienti();
		LinkedList<Asta> aste = getAllGambitsToNow();
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockOfferte) {
			try {
				int rs = sta.executeUpdate(query);
				if( rs != 0) return true;	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	 }
	 
	 public Offerta removeOfferta(int id_offerta) {
		String queryDati = "SELECT * FROM Offerta WHERE id_offerta="+id_offerta;
		String queryRimozione = "DELETE FROM Offerte WHERE id_offerta="+id_offerta;
		LinkedList<Cliente> offerenti = getClienti();
		LinkedList<Asta> aste = getAllGambitsToNow();
		Offerta offerta = null;
		
		Statement sta = null;
		try {
			sta = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		synchronized (lockOfferte) {
			try {
				Cliente venditore = null;
				ResultSet rs = sta.executeQuery(queryDati);
				Cliente offerente = null;
				Asta asta = null;
				rs.next();
				for (Cliente cliente : offerenti) {
					if(cliente.getUSERNAME().equals(rs.getString("userName"))) {
						offerente = cliente;
						offerenti.remove(cliente); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				for (Asta a : aste) {
					if(a.getId_asta() == rs.getInt("id_asta")) {
						asta = a;
						aste.remove(a); //rimozione del cliente dalla lista per velocizzare le preossime ricerche
						break;
					}
				}
				offerta = new Offerta(
						rs.getInt("id_offerta"),
						rs.getTimestamp("dataOra_offerta"),
						rs.getFloat("offerta"),
						offerente,
						asta);
				
				if(sta.executeUpdate(queryRimozione) > 0)
					return offerta;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	 }	 
}
