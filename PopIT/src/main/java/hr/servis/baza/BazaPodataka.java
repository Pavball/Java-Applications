package hr.servis.baza;


import hr.servis.entiteti.*;
import hr.servis.iznimke.BazaPodatakaException;

import hr.servis.kontroleri.AdminNarudzbaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.MessageDigest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.stream.Collectors;

import static hr.servis.kontroleri.AdminMainPageController.adminId;
import static hr.servis.kontroleri.UserMainPageController.userId;

public class BazaPodataka {
    private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);
    public static final Integer BROJ_ZAPISA_LOGIN = 5;
    public static final Integer BROJ_ZAPISA_ADMIN = 3;
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Connection spojiNaBazu() throws Exception{

        Properties konfiguracijaBaza = new Properties();
        konfiguracijaBaza.load(new FileInputStream("dat/bazaPodataka.properties"));

        Connection con = DriverManager.getConnection(
                konfiguracijaBaza.getProperty("bazaPodatakaURL"),
                konfiguracijaBaza.getProperty("korisnickoIme"),
                konfiguracijaBaza.getProperty("lozinka"));

        return con;
    }

    public static List<User> dohvatiSveUsere(){
        List<User> listUsera = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih User-a!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM KLIJENT");

            while(rs.next()){
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                Integer csv = rs.getInt("csv");
                LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();

                User noviUser = new User(id, username, password, csv, datumRodjenja);
                listUsera.add(noviUser);
            }

            //Serijaliziraj sve user-e
            try {
               ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("dat/user.dat")));
                oos.writeObject(listUsera);
                oos.close();
            }catch (IOException ex){
                ex.printStackTrace();
                logger.error("Neuspijeh u serijaliziranju user-a u BazaPodataka klasi, dohvatiSveUsere() metodi.", ex);
            }

            ArrayList<User> procitaniUseri = new ArrayList<>();

            try {
                ObjectInputStream ins = new ObjectInputStream(new BufferedInputStream(new FileInputStream("dat/user.dat")));
                procitaniUseri = (ArrayList<User>) ins.readObject();
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Neuspijeh u citanju serijalizacije user-a u BazaPodataka klasi, dohvatiSveUsere() metodi.", e);
            }

            for (int i = 0; i < procitaniUseri.size(); i++) {
                System.out.println(procitaniUseri.get(i).getUsername());
            }
            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka za dohvacanje User-a u dohvatiSveUsere() metodi!");
            logger.error("Došlo je do pogreške kod dohvaćanja user-a iz baze podataka u dohvatiSveUsere() metodi.", e);
            e.printStackTrace();
        }

        return listUsera;
    }

    public static List<Admin> dohvatiSveAdmine(){
        List<Admin> listAdmina = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih Admin-a!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM ADMIN");

            while(rs.next()){
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Admin noviAdmin = new Admin(id, username, password);
                listAdmina.add(noviAdmin);
            }

            //Serijaliziraj sve admin-e
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("dat/admin.dat")));
                oos.writeObject(listAdmina);
                oos.close();
            }catch (IOException ex){
                ex.printStackTrace();
                logger.error("Neuspijeh u serijaliziranju admin-a u BazaPodataka klasi, dohvatiSveAdmine() metodi.", ex);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka za dohvacanje admin-a!");
            logger.error("Došlo je do pogreške kod dohvaćanja admina-a iz baze podataka u metodi dohvatiSveAdmine()", e);
            e.printStackTrace();
        }

        return listAdmina;
    }

    public static List<PromjeneUser> dohvatiSvePromjeneUser(){
        List<PromjeneUser> listUsera = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih promjena user-a!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM PROMJENAUSER");

            while(rs.next()){
                Integer id = rs.getInt("id");
                String username = rs.getString("stara");
                String password = rs.getString("nova");
                String adminIme = rs.getString("adminime");
                Timestamp datum_i_vrijeme = rs.getTimestamp("datum_i_vrijeme");

                PromjeneUser noviUser = new PromjeneUser(id, username, password, adminIme, datum_i_vrijeme);
                listUsera.add(noviUser);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka za dohvacivanje promjena user-a!");
            logger.error("Došlo je do pogreške kod dohvaćanja promjena user-a u bazi u metodi dohvatiSvePromjeneUser()", e);
            e.printStackTrace();
        }

        return listUsera;
    }
    public static List<PromjeneAdmin> dohvatiSvePromjeneAdmin(){
        List<PromjeneAdmin> listAdmina = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih promjena admin-a!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM PROMJENAADMIN");

            while(rs.next()){
                Integer id = rs.getInt("id");
                String username = rs.getString("stara");
                String password = rs.getString("nova");
                String adminIme = rs.getString("adminime");
                Timestamp datum_i_vrijeme = rs.getTimestamp("datum_i_vrijeme");

                PromjeneAdmin noviAdmin = new PromjeneAdmin(id, username, password, adminIme, datum_i_vrijeme);
                listAdmina.add(noviAdmin);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze promjena admin-a u metodi dohvatiSvePromjeneAdmin()", e);
            e.printStackTrace();
        }

        return listAdmina;
    }
    public static List<Pitanja> dohvatiSvaPitanja(){
        List<Pitanja> listPitanja = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih pitanja-a!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM PITANJA");

            while(rs.next()){
                Integer id = rs.getInt("id");
                String subject = rs.getString("subject");
                String pitanje = rs.getString("pitanje");
                Integer userId = rs.getInt("userid");

                Pitanja novoPitanje = new Pitanja(id, subject, pitanje, userId);
                listPitanja.add(novoPitanje);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka za pitanja!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze pitanja u metodi dohvatiSvaPitanja()", e);
            e.printStackTrace();
        }

        return listPitanja;
    }
    public static List<Odgovor> dohvatiOdredeneOdgovore(){
        List<Odgovor> listOdgovora = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju odredenih odgovora!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM ODGOVOR WHERE userid = " + userId);

            while(rs.next()){
                Integer id = rs.getInt("id");
                String subject = rs.getString("subject");
                String pitanje = rs.getString("pitanje");
                String odgovor = rs.getString("odgovor");
                Integer userId = rs.getInt("userid");
                Odgovor noviOdgovor = new Odgovor(id, subject, pitanje, odgovor, userId);
                listOdgovora.add(noviOdgovor);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze odredenih odgovora u metodi dohvatiOdredeneOdgovore()", e);
            e.printStackTrace();
        }

        return listOdgovora;
    }
    public static List<Narudzbe> dohvatiSveNarudzbe(){
        List<Narudzbe> listaNarudzbi = new ArrayList<>();
        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih narudzbi!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM NARUDZBA");

            while(rs.next()){
                Integer id = rs.getInt("id");
                Narudzbe novaNarudzba = (Narudzbe) rs.getObject("narudzba");

                AdminNarudzbaController.narudzbeIdList.add(id);
                listaNarudzbi.add(novaNarudzba);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze narudzbi u metodi dohvatiSveNarudzbe()", e);
            e.printStackTrace();
        }

        return listaNarudzbi;
    }
    public static List<Popravci> dohvatiSvePopravke(){
        List<Popravci> listaPopravci = new ArrayList<>();
        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju svih popravaka!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM POPRAVCI");

            while(rs.next()){
                Integer id = rs.getInt("id");
                Popravci noviPopravak = (Popravci) rs.getObject("popravak");

                listaPopravci.add(noviPopravak);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze popravaka u metodi dohvatiSvePopravke()", e);
            e.printStackTrace();
        }

        return listaPopravci;
    }
    public static List<PopravciOdgovor> dohvatiOdredenePopravke(){
        List<PopravciOdgovor> listOdgovora = new ArrayList<>();

        try{
            Connection con = spojiNaBazu();

            if(con != null){
                System.out.println("Uspijeh u dohvacanju odredenih popravaka!");
            }

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM ODGOVORPOPRAVCI WHERE userid = " + userId);

            while(rs.next()){
                Integer id = rs.getInt("id");
                String subject = rs.getString("tippopravka");
                String pitanje = rs.getString("opiskvara");
                String odgovor = rs.getString("odgovor");
                Integer userId = rs.getInt("userid");
                PopravciOdgovor noviOdgovor = new PopravciOdgovor(subject, pitanje, odgovor, userId);
                listOdgovora.add(noviOdgovor);
            }

            con.close();

        }catch (Exception e){
            System.out.println("Doslo je do pogreske kod spajanja na bazu podataka!");
            logger.error("Došlo je do pogreške kod dohvaćanja baze odredeni popravci u metodi dohvatiOdredenePopravke()", e);
            e.printStackTrace();
        }

        return listOdgovora;
    }

    public static void unesiUsera(String username, String password, Integer csv, LocalDate datumRodenja) throws BazaPodatakaException {
        String hashedPass = hashPass(password);
        Integer maxId = 0;
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO klijent (username, password,csv,datum_rodjenja) values (?,?,?,?)");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, password);

            preparedStatement.setInt(3,csv);
            preparedStatement.setDate(4, Date.valueOf(datumRodenja));
            preparedStatement.executeUpdate();

            List<User> userList;
            userList = dohvatiSveUsere();

            for (int i = 0; i < userList.size(); i++) {
                maxId = userList.get(i).getId();
            }

            //Spremi u datoteku
            try(FileWriter fw = new FileWriter("dat/login.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(""+ maxId);
                out.println(""+ username);
                out.println(""+ hashedPass);
                out.println(""+ csv);
                out.println(""+ datumRodenja);

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                logger.error("Došlo je do pogreške kod unosenja user-a u datoteku u metodi unesiUsera()", e);
            }


        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja user-a u bazu podataka u metodi unesiUsera() u klasi BazaPodataka.", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajUsera(Integer id, String username, String password, LocalDate datumRodenja) throws BazaPodatakaException {
        String hashedPass = hashPass(password);
        //Prvo procita serijalizaciju za usporedbu
        ArrayList<User> procitaniUseri = new ArrayList<>();

        try {
            ObjectInputStream ins = new ObjectInputStream(new BufferedInputStream(new FileInputStream("dat/user.dat")));

            procitaniUseri = (ArrayList<User>) ins.readObject();
            ins.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("Došlo je do pogreške kod citanja serijalizacije Usera u metodi azurirajUsera() u klasi BazaPodataka", e);
        }

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String idDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String userDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String passDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                listaAdmin.add(new Admin(Integer.valueOf(idDat), userDat, passDat));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju Admin datoteke u metodi azurirajUsere() u klasi BazaPodataka!", e);
            System.err.println(e);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<User> listaUser = new ArrayList<>();
        String FILE_NAME2 = "dat/login.txt";
        try (BufferedReader user = new BufferedReader(new FileReader(FILE_NAME2))) {

            List<String> datotekaUsera = user.lines().collect(Collectors.toList());


            for (int i = 0; i < datotekaUsera.size() / BROJ_ZAPISA_LOGIN; i++) {
                String idUser = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN);
                String usernameDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +1);
                String passwordDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +2);
                Integer csvDatoteka = Integer.valueOf(datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +3));

                String datumRodjenjaString = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +4);
                LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);

                listaUser.add(new User(Integer.valueOf(idUser), usernameDatoteka, passwordDatoteka, csvDatoteka, datumRodjenja));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju User datoteke u metodi azurirajUsera() u klasi BazaPodataka!", e);
            System.err.println(e);
        }

        for (int i = 0; i < listaUser.size(); i++) {
            if(listaUser.get(i).getId().equals(id)){
                listaUser.get(i).setUsername(username);
                listaUser.get(i).setPassword(hashedPass);
                listaUser.get(i).setDatumRodjenja(datumRodenja);
            }
        }

        File myObj = new File("dat/login.txt");
        myObj.delete();
        //Spremi u datoteku

        for (int i = 0; i < listaUser.size(); i++) {
            try(FileWriter fw = new FileWriter("dat/login.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(""+ (listaUser.get(i).getId()));
                out.println(""+ listaUser.get(i).getUsername());
                out.println(""+ listaUser.get(i).getPassword());
                out.println(""+ listaUser.get(i).getCSV());
                out.println(""+ listaUser.get(i).getDatumRodjenja());

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                logger.error("Došlo je do pogreške kod upisivanja user-a u datoteku u metodi azurirajUsera()", e);
            }
        }

        try (Connection veza = spojiNaBazu()) {

            PreparedStatement preparedStatement = veza.prepareStatement("UPDATE klijent SET username=?, password=?, datum_rodjenja=? where id=" + id);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setDate(3, Date.valueOf(datumRodenja));
            preparedStatement.executeUpdate();

            //Onda usporedi sa idom da se dobije prava osoba, te upise nova osoba
            PreparedStatement preparedSerialize = veza.prepareStatement("INSERT INTO promjenauser (stara, nova, adminIme, datum_i_vrijeme) values (?,?,?,?)");
                for (int i = 0; i < procitaniUseri.size(); i++) {
                    if (procitaniUseri.get(i).getId().equals(id)) {
                        //USERNAME SERIALIZATION
                        if (procitaniUseri.get(i).getUsername().equals(username)) {

                        } else {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            String adminIme = "";
                            //Namjesti Username Admina u tablicu
                            for(int k = 0; k< listaAdmin.size(); k++){
                                if(listaAdmin.get(k).getId().equals(adminId)){
                                    adminIme = listaAdmin.get(k).getUsername();
                                    break;
                                }
                            }

                            String stariUser = procitaniUseri.get(i).getUsername();
                            procitaniUseri.get(i).setUsername(username);

                            //Sprema promjenu u tablicu
                            preparedSerialize.setString(1,stariUser);
                            preparedSerialize.setString(2, username);
                            preparedSerialize.setString(3, adminIme);
                            preparedSerialize.setTimestamp(4, Timestamp.valueOf(sdf3.format(timestamp)));
                            preparedSerialize.executeUpdate();
                        }

                        //PASSWORD SERIALIZATION
                        if (procitaniUseri.get(i).getPassword().equals(password)) {

                        } else {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            String adminIme = "";
                            //Namjesti Username Admina u tablicu
                            for(int k = 0; k< listaAdmin.size(); k++){
                                if(listaAdmin.get(k).getId().equals(adminId)){
                                    adminIme = listaAdmin.get(k).getUsername();
                                    break;
                                }
                            }


                            String stariPass = procitaniUseri.get(i).getPassword();
                            procitaniUseri.get(i).setPassword(password);

                            //Sprema promjenu u tablicu
                            preparedSerialize.setString(1,stariPass);
                            preparedSerialize.setString(2, password);
                            preparedSerialize.setString(3, adminIme);
                            preparedSerialize.setTimestamp(4, Timestamp.valueOf(sdf3.format(timestamp)));
                            preparedSerialize.executeUpdate();
                        }

                        //DATUM RODJENJA SERIALIZATION
                        if (procitaniUseri.get(i).getDatumRodjenja().equals(datumRodenja)) {

                        } else {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());


                            String adminIme = "";
                            //Namjesti Username Admina u tablicu
                            for(int k = 0; k < listaAdmin.size(); k++){
                                if(listaAdmin.get(k).getId().equals(adminId)){
                                    adminIme = listaAdmin.get(k).getUsername();
                                    break;
                                }
                            }

                            LocalDate stariDate = procitaniUseri.get(i).getDatumRodjenja();
                            procitaniUseri.get(i).setDatumRodjenja(datumRodenja);

                            //Sprema promjenu u tablicu
                            preparedSerialize.setString(1, String.valueOf(stariDate));
                            preparedSerialize.setString(2, String.valueOf(datumRodenja));
                            preparedSerialize.setString(3, adminIme);
                            preparedSerialize.setTimestamp(4, Timestamp.valueOf(sdf3.format(timestamp)));
                            preparedSerialize.executeUpdate();
                        }

                    }
                }

                //Serijaliziraj nove user-e
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("dat/user.dat")));
                    oos.writeObject(procitaniUseri);
                    oos.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                    logger.error("Došlo je do pogreške kod serijaliziranja novih user-a u metodi azurirajUsera() u klasi BazaPodataka.", ex);
                }

            } catch (Exception ex) {
                String poruka = "Došlo je do pogreške u radu s bazom podataka";
                logger.error("Došlo je do pogreške kod ažuriranja user-a u metodi azurirajUsera() u klasi BazaPodataka!", ex);
                throw new BazaPodatakaException(poruka, ex);
            }
    }
    public static void izbrisiUsera(Integer id, String username, String password) throws BazaPodatakaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<User> listaUser = new ArrayList<>();
        String FILE_NAME2 = "dat/login.txt";
        try (BufferedReader user = new BufferedReader(new FileReader(FILE_NAME2))) {

            List<String> datotekaUsera = user.lines().collect(Collectors.toList());
            for (int i = 0; i < datotekaUsera.size() / BROJ_ZAPISA_LOGIN; i++) {
                String idUser = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN);
                String usernameDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +1);
                String passwordDatoteka = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +2);
                Integer csvDatoteka = Integer.valueOf(datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +3));

                String datumRodjenjaString = datotekaUsera.get(i * BROJ_ZAPISA_LOGIN +4);
                LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);

                listaUser.add(new User(Integer.valueOf(idUser), usernameDatoteka, passwordDatoteka, csvDatoteka, datumRodjenja));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju User datoteke u metodi izbrisiUsera()!", e);
            System.err.println(e);
        }

        for (int i = 0; i < listaUser.size(); i++) {
            if(listaUser.get(i).getId().equals(id)){
                listaUser.remove(i);
            }
        }

        File myObj = new File("dat/login.txt");
        myObj.delete();
        //Spremi u datoteku

        for (int i = 0; i < listaUser.size(); i++) {
            try(FileWriter fw = new FileWriter("dat/login.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(""+ (listaUser.get(i).getId()));
                out.println(""+ listaUser.get(i).getUsername());
                out.println(""+ listaUser.get(i).getPassword());
                out.println(""+ listaUser.get(i).getCSV());
                out.println(""+ listaUser.get(i).getDatumRodjenja());

            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Došlo je do pogreške unosenja user-a u datoteku u metodi izbrisiUsera().", e);
            }
        }


        try (Connection veza = spojiNaBazu()){
            PreparedStatement ps = veza.prepareStatement("DELETE FROM KLIJENT WHERE (username, password) = (?,?)");
            ps.setString(1,username);
            ps.setString(2,password);
            ps.executeUpdate();
        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod brisanja user-a iz baze u metodi izbrisiUsera()!", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void unesiAdmina(String username, String password) throws BazaPodatakaException {
        Integer maxID = 0;
        String hashedPass = hashPass(password);
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO admin (username, password) values (?,?)");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            logger.info("Dohvaćanje Admin datoteke...");
            List<Admin> listaAdmin = new ArrayList<>();

            String FILE_NAME = "dat/loginAdmin.txt";
            try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

                List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());
                logger.info("Dohvaćena datoteka: " + FILE_NAME);

                for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                    String id = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                    String usernameDatoteka = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                    String passwordDatoteka = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                    listaAdmin.add(new Admin(Integer.valueOf(id), usernameDatoteka, passwordDatoteka));
                }
                logger.info("Upisani admini u listu... ");
            } catch (IOException e) {
                logger.error("Pogreška u čitanju Admin datoteke u metodi unesiAdmina()!", e);
                System.err.println(e);
            }

            List<Admin> adminList;
            adminList = dohvatiSveAdmine();

            for (int i = 0; i < adminList.size(); i++) {
                maxID = adminList.get(i).getId();
            }

            //Spremi u datoteku
            try(FileWriter fw = new FileWriter("dat/loginAdmin.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(""+ maxID);
                out.println(""+ username);
                out.println(""+ hashedPass);

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                logger.error("Došlo je do pogreške kod serijaliziranja admina u datoteku u metodi unesiAdmina().", e);
            }

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja admin-a u bazu u metodi unesiAdmina()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void azurirajAdmina(Integer id, String username, String password) throws BazaPodatakaException {
        String hashedPass = hashPass(password);
        //Prvo procita serijalizaciju za usporedbu
        ArrayList<Admin> procitaniAdmini = new ArrayList<>();

        try {
            ObjectInputStream ins = new ObjectInputStream(new BufferedInputStream(new FileInputStream("dat/admin.dat")));

            procitaniAdmini = (ArrayList<Admin>) ins.readObject();
            ins.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("Došlo je do pogreške kod deserijaliziranja admina iz datoteke u metodi azurirajAdmina().", e);
        }

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String idDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String userDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String passDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                listaAdmin.add(new Admin(Integer.valueOf(idDat), userDat, passDat));
            }
        } catch (IOException e) {
            logger.error("Pogreška u čitanju datoteke!", e);
            System.err.println(e);
        }

        for (int i = 0; i < listaAdmin.size(); i++) {
            if(listaAdmin.get(i).getId().equals(id)){
                listaAdmin.get(i).setUsername(username);
                listaAdmin.get(i).setPassword(hashedPass);
            }
        }

        File myObj = new File("dat/loginAdmin.txt");
        myObj.delete();
        //Spremi u datoteku

        for (int i = 0; i < listaAdmin.size(); i++) {
            try(FileWriter fw = new FileWriter("dat/loginAdmin.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(""+ (listaAdmin.get(i).getId()));
                out.println(""+ listaAdmin.get(i).getUsername());
                out.println(""+ listaAdmin.get(i).getPassword());

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }


        try (Connection veza = spojiNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("UPDATE admin SET username=?, password=? where id=" + id);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.executeUpdate();

            //Onda usporedi sa idom da se dobije prava osoba, te upise nova osoba
            PreparedStatement preparedSerialize = veza.prepareStatement("INSERT INTO promjenaadmin (stara, nova, adminIme, datum_i_vrijeme) values (?,?,?,?)");
            for (int i = 0; i < procitaniAdmini.size(); i++) {
                if (procitaniAdmini.get(i).getId().equals(id)) {
                    //USERNAME SERIALIZATION
                    if (procitaniAdmini.get(i).getUsername().equals(username)) {

                    } else {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        String adminIme = "";
                        //Namjesti Username Admina u tablicu
                        for(int k = 0; k< listaAdmin.size(); k++){
                            if(listaAdmin.get(k).getId().equals(adminId)){
                                adminIme = listaAdmin.get(k).getUsername();
                                break;
                            }
                        }

                        String stariUser = procitaniAdmini.get(i).getUsername();
                        procitaniAdmini.get(i).setUsername(username);

                        //Sprema promjenu u tablicu
                        preparedSerialize.setString(1,stariUser);
                        preparedSerialize.setString(2, username);
                        preparedSerialize.setString(3, adminIme);
                        preparedSerialize.setTimestamp(4, Timestamp.valueOf(sdf3.format(timestamp)));
                        preparedSerialize.executeUpdate();
                    }

                    //PASSWORD SERIALIZATION
                    if (procitaniAdmini.get(i).getPassword().equals(password)) {

                    } else {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        String adminIme = "";
                        //Namjesti Username Admina u tablicu
                        for(int k = 0; k< listaAdmin.size(); k++){
                            if(listaAdmin.get(k).getId().equals(adminId)){
                                adminIme = listaAdmin.get(k).getUsername();
                                break;
                            }
                        }
                        String stariPass = procitaniAdmini.get(i).getPassword();
                        procitaniAdmini.get(i).setPassword(password);

                        //Sprema promjenu u tablicu
                        preparedSerialize.setString(1,stariPass);
                        preparedSerialize.setString(2, password);
                        preparedSerialize.setString(3, adminIme);
                        preparedSerialize.setTimestamp(4, Timestamp.valueOf(sdf3.format(timestamp)));
                        preparedSerialize.executeUpdate();
                    }


                }
            }

            //Serijaliziraj nove admine-e
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("dat/admin.dat")));
                oos.writeObject(procitaniAdmini);
                oos.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod ažuriranja user-a!", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void izbrisiAdmina(Integer id, String username, String password) throws BazaPodatakaException {

        //Dohvacanje Admin datoteke u listu
        List<Admin> listaAdmin = new ArrayList<>();
        String FILE_NAME = "dat/loginAdmin.txt";
        try (BufferedReader admin = new BufferedReader(new FileReader(FILE_NAME))) {

            List<String> datotekaAdmina = admin.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaAdmina.size() / BROJ_ZAPISA_ADMIN; i++) {
                String idDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN);
                String userDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +1);
                String passDat = datotekaAdmina.get(i * BROJ_ZAPISA_ADMIN +2);


                listaAdmin.add(new Admin(Integer.valueOf(idDat), userDat, passDat));
            }

            for (int i = 0; i < listaAdmin.size(); i++) {
                if(listaAdmin.get(i).getId().equals(id)){
                    listaAdmin.remove(i);
                }
            }

        } catch (IOException e) {
            logger.error("Pogreška u čitanju Admin datoteke u metodi izbrisiAdmina()!", e);
            System.err.println(e);
        }

            File adminDatoteka = new File("dat/loginAdmin.txt");
            adminDatoteka.delete();
            //Spremi u datoteku

            for (int i = 0; i < listaAdmin.size(); i++) {
                try(FileWriter fw = new FileWriter("dat/loginAdmin.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                {
                    out.println(""+ (listaAdmin.get(i).getId()));
                    out.println(""+ listaAdmin.get(i).getUsername());
                    out.println(""+ listaAdmin.get(i).getPassword());

                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    logger.error("Došlo je do pogreške kod serijaliziranja admina u metodi izbrisiAdmina().", e);
                }
            }


        try (Connection veza = spojiNaBazu()){
            PreparedStatement ps = veza.prepareStatement("DELETE FROM ADMIN WHERE (username, password) = (?,?)");
            ps.setString(1,username);
            ps.setString(2,password);
            ps.executeUpdate();
        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod brisanja admin-a u metodi izbrisiAdmina()!", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void posaljiPitanje(String subject, String pitanje, Integer userId) throws BazaPodatakaException {
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO pitanja (subject, pitanje, userid) values (?,?,?)");
            preparedStatement.setString(1,subject);
            preparedStatement.setString(2, pitanje);
            preparedStatement.setInt(3,userId);
            preparedStatement.executeUpdate();

            System.out.println("Pitanje poslano!");

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja pitanja u metodi posaljiPitanje()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void posaljiOdgovor(String subject, String pitanje, String odgovor, Integer userId) throws BazaPodatakaException {
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO odgovor (subject, pitanje, odgovor, userid) values (?,?,?,?)");
            preparedStatement.setString(1,subject);
            preparedStatement.setString(2, pitanje);
            preparedStatement.setString(3, odgovor);
            preparedStatement.setInt(4,userId);
            preparedStatement.executeUpdate();

            System.out.println("Odgovor poslan!");

            PreparedStatement ps = veza.prepareStatement("DELETE FROM PITANJA WHERE (subject, pitanje) = (?,?)");
            ps.setString(1,subject);
            ps.setString(2,pitanje);
            ps.executeUpdate();

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja odgovora ili brisanja pitanja u metodi posaljiOdgovor()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }
    public static void posaljiPopravakOdgovor(Integer userId, String subject, String pitanje, String odgovor, Popravci popravak) throws BazaPodatakaException {
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO odgovorpopravci (tippopravka, opiskvara, odgovor, userid) values (?,?,?,?)");
            preparedStatement.setString(1,subject);
            preparedStatement.setString(2, pitanje);
            preparedStatement.setString(3, odgovor);
            preparedStatement.setInt(4,userId);
            preparedStatement.executeUpdate();

            System.out.println("Odgovor poslan!");

            PreparedStatement ps = veza.prepareStatement("DELETE FROM POPRAVCI WHERE (popravak) = (?)");
            ps.setObject(1,popravak);
            ps.executeUpdate();

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja ili brisanja popravka u metodi posaljiPopravakOdgovor()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static void posaljiNarudzbu(Narudzbe narudzba) throws BazaPodatakaException {
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO narudzba (narudzba) values (?)");
            preparedStatement.setObject(1, narudzba);
            preparedStatement.executeUpdate();

            System.out.println("Narudzba poslana!");

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja narudzbe u bazu podataka u metodi posaljiNarudzbu()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }
    public static void posaljiPopravak(Popravci popravak) throws BazaPodatakaException {
        try (Connection veza = spojiNaBazu()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO popravci (popravak) values (?)");
            preparedStatement.setObject(1, popravak);
            preparedStatement.executeUpdate();

            System.out.println("Popravak poslan!");

        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod unošenja popravka u bazu podataka u metodi posaljiPopravak()", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }
    public static void kupiProduct(Integer id, Narudzbe narudzba) throws BazaPodatakaException {


        try (Connection veza = spojiNaBazu()){

            PreparedStatement preparedStatement = veza.prepareStatement("UPDATE narudzba SET narudzba=? where id=" + id);
            preparedStatement.setObject(1,narudzba);
            preparedStatement.executeUpdate();


        }catch (Exception ex){
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error("Došlo je do pogreške kod ažuriranja narudzbe u metodi kupiProduct()!", ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }



    public static String hashPass(String pass){
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            logger.error("Došlo je do pogreške kod hashiranja passworda.", ex);
            throw new RuntimeException(ex);
        }
    }

}
