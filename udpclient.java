import java.net.*;
import java.io.*;
import java.util.*;
public class udpclient{

    public static void main(String[] args) {
        byte[] islem_bilgisi=new byte[1024];
        byte[] b=new byte[20];
        byte[] b1=new byte[20];//bunlarin her yeni bir islemde sifirlamak gerekiyor mu onu kontrol et.
        byte[] b2=new byte[30];
        byte[] b3=new byte[30];
        byte[] b4=new byte[40];
        byte[] parola=new byte[100];
        byte[] mevcut_dosyalar = new byte[1000];
        byte[] mevcut_mu = new byte[100];
        byte[] kaydedildib = new byte[100];
        
        
        byte[] dataOfFile = new byte[1000000000]; 
        
        //FileInputStream f;
        Scanner inputKey= new Scanner(System.in);
        int paket=1;
        DatagramSocket dsoc;
        String code="262202072";//bunu manuel olarak kendim sectim.
        String cevap="";
        String secim="";
        String gelen_parola="";
        String yanlis_parola="Yanlis parola";
        String dogru_parola="Dogru parola";
        String dosya_isimleri="";
        String datafile="";
        String dosya_mevcut="";

        //........3. islem icin degiskenler.
        String dosya_ismi3="";
        byte[] dosya_ismi3b = new byte[100];
        byte[] dataOfFile3 = new byte[100000000];
        byte[] b13 = new byte[10000];
        
        try {
            boolean parolaDogruMu=true;
            dsoc = new DatagramSocket(2000);
            String baglan="";
            String dosya_istegi_ismi="";
            DatagramPacket dp,dp1,dp2,dp3,dp4,dp13;
            while(parolaDogruMu){
                //client cevap alma....
                System.out.println("Servera baglanmaniz icin \"baglan\" yazmaniz gerekmektedir.Eger cikmak istiyorsaniz \"q\" ya basiniz.");
                baglan=inputKey.next();
                if(baglan.equals("q")){
                    break;
                }
                else if(!baglan.equals("baglan")){
                    continue;
                }
                b1=baglan.getBytes();
                dsoc.send(new DatagramPacket(b1,b1.length,InetAddress.getLocalHost(),1000));//(1)
                System.out.println("<"+paket+".>"+" paket<baglanti istegi paketi> yollandi!"+"("+b1.length+"Byte)");
           
                dp=new DatagramPacket(islem_bilgisi,islem_bilgisi.length);//paketi aliyor..
                dsoc.receive(dp);//(4)
                cevap=new String(dp.getData(),0,dp.getLength());
                System.out.println("Gelen cevap:");
                System.out.println(cevap);   
                System.out.println("Lutfen yapmak istediginiz islemi secip cevabinizi yaziniz:");
                secim=inputKey.next();
                b=secim.getBytes();//secmek icin kullanicidan bilgi.
                dsoc.send(new DatagramPacket(b,b.length,InetAddress.getLocalHost(),1000));//(5)
                paket++;
                System.out.println("<"+paket+".>"+" paket<islem secimi> yollandi!"+"("+b.length+"Byte)");
                if(secim.equals("1")){
                    dp1=new DatagramPacket(parola,parola.length);//paketi aliyor..
                    dsoc.receive(dp1);//(8)
                    gelen_parola=new String(dp1.getData(),0,dp1.getLength());
                    if(gelen_parola.equals(code)){
                        System.out.println("Sunucu tarafindan dogru parola...islemlere devam ediliyor.");
                        b3=dogru_parola.getBytes();
                        dsoc.send(new DatagramPacket(b3,b3.length,InetAddress.getLocalHost(),1000));//(9)
                        paket++;
                        System.out.println("<"+paket+".>"+" paket<Dogru parola> yollandi!"+"("+b3.length+"Byte)");
                        continue;
                    }else{
                        System.out.println("Sunucu tarafindan yanlis parola...Sunucu tarafina bildirilip baglanti kesiliyor.");
                        b2=yanlis_parola.getBytes();
                        dsoc.send(new DatagramPacket(b2,b2.length,InetAddress.getLocalHost(),1000));
                        paket++;
                        System.out.println("<"+paket+".>"+" paket<yanlis parola> yollandi!"+"("+b2.length+"Byte)");
                        dsoc.disconnect();
                        parolaDogruMu=false;
                    }
                    
                }else if(secim.equals("2")){
                    //yapilan secim2
                    System.out.println("Serverin bulundugu mevcut dizindeki dosyalar:");
                    dp2=new DatagramPacket(mevcut_dosyalar,mevcut_dosyalar.length);//paketi aliyor..
                    dsoc.receive(dp2);
                    dosya_isimleri=new String(dp2.getData(),0,dp2.getLength());
                    System.out.println(dosya_isimleri);   
                
                
                    System.out.println("Sunucudan bir dosya istediniz.Lutfen istediginiz dosyanin ismini yaziniz.");
                    dosya_istegi_ismi=inputKey.next();//Scannerlari next kullandim bosluk girilmeyecegini varsaydigim icin(Tekrar duzeltilebilir.)
                    b4=dosya_istegi_ismi.getBytes();
                    dsoc.send(new DatagramPacket(b4,b4.length,InetAddress.getLocalHost(),1000));
                    paket++;
                    System.out.println("<"+paket+".>"+" paket<istenilen dosya ismi> yollandi!"+"("+b4.length+"Byte)");
                    
                    dp4=new DatagramPacket(mevcut_mu,mevcut_mu.length);//paketi aliyor..
                    dsoc.receive(dp4);
                    dosya_mevcut=new String(dp4.getData(),0,dp4.getLength());
                    if(dosya_mevcut.equalsIgnoreCase("Dosya mevcut degil")){
                        System.out.println("Aradiginiz dosya mevcut degildir.Lutfen tekrar deneyiniz.");
                        continue;

                    }else{ 
                        System.out.println("................");
                    

                        //gelen dosya kaydedilecek.
                        System.out.println(dosya_mevcut);

                        System.out.println("Dosya geldi");
                        dp3=new DatagramPacket(dataOfFile,dataOfFile.length);//paketi aliyor..
                        dsoc.receive(dp3);
                        datafile=new String(dp3.getData(),0,dp3.getLength());
                        //System.out.println(datafile);   
                        
                        //gelen dosyayi kaydetme islemi

                        try {
                            File file =new File("dosya.java");
                            //System.out.println("gercek yol:"+file.getAbsolutePath());
                            String absolute = file.getAbsolutePath();
                            absolute = absolute.substring(0,absolute.lastIndexOf("\\"));
                            //System.out.println(absolute);
                            absolute = absolute+"\\client";//client adli klasore kaydediyor.
                            //System.out.println(absolute);
                            File file1 =new File(absolute);
                            file1.mkdir();
                            String dosya_tur=dosya_istegi_ismi.substring(dosya_istegi_ismi.lastIndexOf("."));
                            dosya_istegi_ismi=dosya_istegi_ismi.substring(0,dosya_istegi_ismi.lastIndexOf("."));
                            dosya_istegi_ismi=dosya_istegi_ismi+"_"+"161101071"+dosya_tur;
                            FileWriter myWriter = new FileWriter(absolute+"\\"+dosya_istegi_ismi);
                            myWriter.write(datafile);
                            myWriter.close();
                            String kaydedildi="Dosya basarili bir sekilde  client klasorune kaydedildi.Transfer tamamlanmistir.";
                            
                            kaydedildib=kaydedildi.getBytes();
                            dsoc.send(new DatagramPacket(kaydedildib,kaydedildib.length,InetAddress.getLocalHost(),1000));
                            paket++;
                            System.out.println("<"+paket+".>"+" paket<istenilen dosya kaydedildi> yollandi!"+kaydedildib.length+"Byte)");
                            



                            
                        }catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                    }    
                }else if(secim.equals("3")){
                    System.out.println("3. islemi sectiniz.Lutfen gondermek istediginiz dosyanin ismini yaziniz.");
                    dosya_ismi3=inputKey.next();
                    dosya_ismi3b=dosya_ismi3.getBytes();
                    dsoc.send(new DatagramPacket(dosya_ismi3b,dosya_ismi3b.length,InetAddress.getLocalHost(),1000));
                    paket++;
                    System.out.println("<"+paket+".>"+" paket<Gonderilmek istenen dosya ismi> yollandi!"+dosya_ismi3b.length+"Byte)");


                    //dosyanin icindeki bilgiler gonderilecek.


                    File f = new File("asd");
                    String absolute = f.getAbsolutePath();
                    absolute=absolute.substring(0,absolute.lastIndexOf('\\'));
                    //serverin bulundugu dizindeki tum dosyalari bulmak icin (absolute)
                    File f1=new File(absolute);
                    File[] files=f1.listFiles();
                    StringBuilder sb=new StringBuilder("\n");
                    int x=0;
                    for(int i=0;i<files.length;i++){
                    
                        if(files[i].canRead() && !(files[i].getName().contains(".java") || files[i].getName().contains(".class")) ){//java dosyalarini ayri tutuyorum.
                                            
                            sb.append(files[i].getName()+" ,size:"+files[i].length()+"bytes\n");
                            x++;
                        }
                    }
                    String numbersOffiles=""+x+"File(s) found in current directory\n";
                    StringBuilder sendFiles = new StringBuilder(numbersOffiles);
                    System.out.println(numbersOffiles);
                    
                    //istenilen dosya ismini aldi.
                    //dosya mevcut ise dosyayi clienta gondericek ve client  dosya ismini ogrenciNo.xxx seklinde degistiricek.

                    
                    boolean flag = false;
                    int id= 0;
                    for(int i=0;i<files.length;i++) {
                        if(files[i].getName().toString().equalsIgnoreCase(dosya_ismi3)) {
                            flag = true;
                            id = i;
                            break;
                        }
                    }// dosyasi okumayi yapicak.

                    

                    
                    File filetocopy=new File(files[id].getAbsolutePath());
                    FileReader fileReader=new FileReader(filetocopy);
                    BufferedReader br=new BufferedReader(fileReader);
                    StringBuilder sb1=new StringBuilder();


                    String line;
                    while((line=br.readLine())!=null){
    
                        sb1.append(line);
                        sb1.append("\n");
                    }
                    //System.out.println(sb1.toString());

                    dataOfFile3=sb1.toString().getBytes();
                    //System.out.println(sb1.toString());
                    dsoc.send(new DatagramPacket(dataOfFile3,dataOfFile3.length,InetAddress.getLocalHost(),1000));
                    paket++;
                    System.out.println("<"+paket+".>"+" paket<Dosyanin icindeki bilgiler> yollandi!"+"("+dataOfFile3.length+"Byte)");
                    //dosya yolllandi.

                    String kaydedildi3;
                    //kaydedildi bilgisi yani dosya transfer islemi bitti bilgisi.
                    dp13=new DatagramPacket(b13,b13.length);
                    dsoc.receive(dp13);
                    kaydedildi3=new String(dp13.getData(),0,dp13.getLength());
                    System.out.println(kaydedildi3);
                

                }else{
                    System.out.println("Program kapatiliyor.");
                    break;
                }    
                    
                    
                    
            }    
                    
                                 
            
            
        } catch (Exception e) {//Burada ayri ayri exceptionlari yakalamasi lazim.
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        


    }





}