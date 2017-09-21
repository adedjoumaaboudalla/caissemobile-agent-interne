package caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.DatePicker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;


public class PrinterUtils {

    private final String bluetoothConfig = "bluetoothConfig";
    private final String serverIp = "DP-HT201";
    private SharedPreferences preferences;
    private String matriculeCollecteur = null;
    private String msgFin = null;
    private String agence = null;
    private String societeAdresse = null;
    // android built in classes for bluetooth operations
			BluetoothAdapter mBluetoothAdapter;
			BluetoothSocket mmSocket;
			BluetoothDevice mmDevice;
            public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy H:m:s") ;
			
			OutputStream mmOutputStream;
			InputStream mmInputStream;
			Thread workerThread;
			
			byte[] readBuffer;
			int readBufferPosition;
			int counter;
			volatile boolean stopWorker;
			
			String msg;
			String nomImprimante;
            OperationDAO operationDAO = null ;
			CaissierDAO caissierDAO = null ;
			CompteDAO compteDAO = null ;

	Context context = null ;
			
			public PrinterUtils(Context context){
                preferences =  PreferenceManager.getDefaultSharedPreferences(context);
                nomImprimante = preferences.getString(bluetoothConfig, serverIp) ;
                operationDAO = new OperationDAO(context) ;
				caissierDAO = new CaissierDAO(context) ;
				compteDAO = new CompteDAO(context) ;
				AgenceDAO agenceDAO = new AgenceDAO(context) ;
				agence = agenceDAO.getOne(caissierDAO.getLast().getAgence_id()).getNomagence() ;
                societeAdresse = preferences.getString("adresse", "21 38 22 24 / 61 23 92 92") ;
				msgFin = preferences.getString("messagefinal", "Copyright Media Soft") ;
				this.context = context ;
            }
			
			public PrinterUtils(String msg){
				this.msg=msg;
			}
			
			public PrinterUtils(String msg, String nomImprimante){
				this.msg = msg;
				if(nomImprimante.equals("")){
					this.nomImprimante = "BlueTooth Printer";
				}else{
				this.nomImprimante = nomImprimante;
				}
				Log.d("PRinter ", "Imprimante :" + nomImprimante);
			}

	/*
	 * This will find a bluetooth printer device
	 */
	void findBT() {

		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				//myLabel.setText("No bluetooth adapter available");
			}

			if (!mBluetoothAdapter.isEnabled()) {
//				Intent enableBluetooth = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
					.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					
					// MP300 is the name of the bluetooth printer device
//					if (device.getName().equals("BlueTooth Printer")) {
					if (device.getName().equals(nomImprimante)) {
						mmDevice = device;
						Log.d("Printer","Bluetooth Device Found "+ device.getName());

						break;
					}
				}
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Tries to open a connection to the bluetooth printer device
	 */
	void openBT() throws IOException {
		try {
			// Standard SerialPortService ID
			UUID uuid = UUID.fromString("00001105-0000-mille-8000-00805F9B34FB");
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
			mmInputStream = mmSocket.getInputStream();

			beginListenForData();
			Log.d("Printer", "Bluetooth Device opened");
			//myLabel.setText("Bluetooth Opened");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private boolean _Open() {
					
		

		boolean valid = false;

		Method m;
		try {
			m = mmDevice.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
			try {
				mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
				mBluetoothAdapter.cancelDiscovery();
				
				try {
					mmSocket.connect();
					mmOutputStream = new DataOutputStream(mmSocket.getOutputStream());
					mmInputStream = new DataInputStream(mmSocket.getInputStream());
					valid = true;
					beginListenForData();
					Log.d("BTRWThread Open", "Connected to " + mmDevice.getName());
					valid = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return valid;
	}

	
	/*
	 * After opening a connection to bluetooth printer device, 
	 * we have to listen and check if a data were sent to be printed.
	 */
	void beginListenForData() {
		try {
			final Handler handler = new Handler();
			
			// This is the ASCII code for a newline character
			final byte delimiter = 10;

			stopWorker = false;
			readBufferPosition = 0;
			readBuffer = new byte[1024];
			
			workerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()
							&& !stopWorker) {
						
						try {
							
							int bytesAvailable = mmInputStream.available();
							if (bytesAvailable > 0) {
								byte[] packetBytes = new byte[bytesAvailable];
								mmInputStream.read(packetBytes);
								for (int i = 0; i < bytesAvailable; i++) {
									byte b = packetBytes[i];
									if (b == delimiter) {
										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(readBuffer, 0,
												encodedBytes, 0,
												encodedBytes.length);
										final String data = new String(
//												encodedBytes, "US-ASCII");
												encodedBytes, "UTF-8");
										readBufferPosition = 0;

										handler.post(new Runnable() {
											@Override
											public void run() {
												//myLabel.setText(data);
											}
										});
									} else {
										readBuffer[readBufferPosition++] = b;
									}
								}
							}
							
						} catch (IOException ex) {
							stopWorker = true;
						}
						
					}
				}
			});

			workerThread.start();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This will send data to be printed by the bluetooth printer
	 */
	void sendData() throws IOException {
		try {
			mmOutputStream.write(msg.getBytes());

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Close the connection to bluetooth printer.
	 */
	void closeBT() throws IOException {
		try {
			stopWorker = true;
			mmOutputStream.close();
			mmInputStream.close();
			mmSocket.close();
			//myLabel.setText("Bluetooth Closed");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Imprimer
	 */
	
	public void print(){
		
		try {
			findBT();
			_Open();	
			sendData();
			closeBT();
		} catch (IOException ex) {
		}
		
	}


    public void printTicket(){
        msg = "Test" ;
        print();
    }

    public void printTicket(Operation operation,String caise){

        String op = "" ;
        Compte compte = compteDAO.getOne(operation.getNumcompte()) ;
        if(operation.getTypeoperation()==0) op ="Dépot";
        else if(operation.getTypeoperation()==4) op ="Remboursement";
        else op = "Retrait";
        Calendar cal = Calendar.getInstance() ;
        msg = "################################";
        msg+= "\n";
        msg +=  caise ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "No piece      : "+ operation.getNumpicedef();
        msg+= "\n";
        msg+= "No piece temp : "+ operation.getNumpiece();
        msg+= "\n";
        msg+= "Date          : "+ DAOBase.formatterj.format(operation.getDateoperation());
        msg+= "\n";
        msg+= "Date Sys      : "+ DAOBase.formatterj.format(new Date());
        msg+= "\n";
        if (operation.getNumcompte().contains("/G/"))msg+= "Groupe        : "+ operation.getNom() ;
        else msg+= "Client        : "+ operation.getNom() + " " + operation.getPrenom() ;
        msg+= "\n";
        msg += "Agence       : "+  agence ;
        msg+= "\n";
        msg += "Guichet      : "+  caissierDAO.getLast().getCodeguichet() ;
        msg+= "\n";
        msg+= "--------------------------------";
        msg+= "\n";
        msg+= "Operation     :  " + op ;
        msg+= "\n";
        if (operation.getTypeoperation()!=4) msg+= "Compte        :  " + operation.getNumcompte() ;
        else msg+= "Crédit   :  " + operation.getNumcompte() ;
        msg+= "\n";
        msg+= "Montant       : " + Utiles.formatMtn(operation.getMontant()) + " F" ;
        //msg+= "\n";
        //msg+= "Nouveau solde :  " + Utiles.formatMtn(compte.getSolde()) + " F" ;
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";

        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            print();
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();
    }


    public void printTicket(String dateDebut,String dateFin){


        DatePicker debut = new DatePicker(context) ;
        DatePicker fin = new DatePicker(context) ;
        if (dateDebut==null)dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
        if (dateFin==null)dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());

        String op = "" ;
        Calendar cal = Calendar.getInstance() ;
        msg = "################################";
        msg+= "\n";
        msg +=  "RECAPITULATIF" ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "Operation    | Nbre | Montant";
        msg+= "\n";

        ArrayList<Operation> operations = operationDAO.getAll(2,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));

        float mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }


        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.depo) + "       |  " + operations.size() + " |  " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(1,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }

        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.retrait) + "      |  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(3,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }

        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.adhesion) + "    |  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(4,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }


        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.rembr) + "|  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";
        msg+= "Date      : "+ preferences.getString("dateouvert","");
        msg+= "\n";
        msg+= "Date Sys  : "+ DAOBase.formatterj.format(new Date());
        msg+= "\n";
        msg+= "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";


        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            print();
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();
    }


/*
    public void printTicket(ArrayList<Depense> depenses){

        Calendar cal = Calendar.getInstance() ;
        msg = "##############################";
        msg+= "\n";
        msg +=  societeNom ;
        msg+= "\n";
        msg +=  societeAdresse ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "DU : "+ formatter.format(depenses.get(0)) +  " AU : "+ formatter.format(depenses.get(depenses.size()-1));
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg+= "DESIGNATION                 Mte";
        msg += "\n";
        msg += "--------------------------------";
        msg+= "\n";
        int n = depenses.size() ;
        int total = 0 ;
        for (int i = 0; i < n; i++){
            Depense pv = depenses.get(i) ;
            total += pv.getMontant() ;
            msg+= depenselibelle(pv.getDescription()) + " " + montant(String.valueOf(pv.getMontant())) ;
            msg+= "\n";
        }
        msg += "--------------------------------";
        msg+= "\n";
        msg+= "TOTAL     ";
        msg+= totaux(String.valueOf(total))  + " FCFA" ;
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";


        try {
            print();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
*/

    public String name(String name){
        int n = 11 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>11)?(name.substring(0,10)):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }


    public String depenselibelle(String name){
        int n = 17 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>17)?(name.substring(0,16)):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }

    public String prix(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String montant(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String quantite(String prix){
        int n = 5 ;
        int siz = 0 ;
        prix = (prix.length()>5)?(prix.substring(0,4)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String totaux(String totaux){
        int n = 9 ;
        int siz = 0 ;
        totaux = (totaux.length()>9)?(totaux.substring(0,8)):(totaux) ;
        siz = n-totaux.length() ;
        for (int i =0 ; i < siz; ++i){
            totaux = " " + totaux ;
        }
        return totaux ;
    }


    public void print(String msg) {
        this.msg = msg ;
        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            print();
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();
    }



		public void printTicket(Categorie categorie, String lib, Membre membre, String numproduit) {
			String op = "" ;
			Calendar cal = Calendar.getInstance() ;
            CategorieDAO categorieDAO = new CategorieDAO(context);;


            msg = "################################";
            msg+= "\n";
            msg +=  "     "+lib ;
            msg+= "\n";
            msg += "################################";
            msg+= "\n";
            msg+= "Date             : "+ preferences.getString("dateouvert","");
            msg+= "\n";
            msg+= "Membre           : "+ membre.getNom() + " " + membre.getPrenom() ;
            msg+= "\n";
            msg += "Num membre Temp  : "+  membre.getNummembre() + "E01" ;
            msg+= "\n";
            msg+= "Date Sys         : "+ DAOBase.formatterj.format(new Date());
            msg+= "\n";
            msg += "Agence           : "+  agence ;
            msg+= "\n";
            msg += "Guichet          : "+  caissierDAO.getLast().getCodeguichet() ;
            msg+= "\n";
            msg+= "--------------------------------";
            msg+= "\n";
            ArrayList<Categorie> categories = categorieDAO.getAll(categorie.getNumCategorie(),numproduit) ;

            double frais = 0 ;
            for (int i = 0; i < categories.size(); i++) {
                msg += categories.get(i).getLibelleFrais() + " : " + String.format("%.2f", categories.get(i).getValeurFrais()) + "\n";
                frais += categories.get(i).getValeurFrais() ;
            }
            msg+= "Dépot Initial     : "+ String.valueOf(membre.getMontant() - frais);
            msg+= "\n";
            msg += "--------------------------------";
            msg+= "\n";
            msg+= "Montant          :  " + Utiles.formatMtn(membre.getMontant()) + " F" ;
            msg += "\n";
            msg += msgFin;
            msg += "\n";
            msg += "################################";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";
            msg += "\n";



            try {
				print(msg);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
}
