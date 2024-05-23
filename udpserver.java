import java.net.*;
import java.io.*;
import java.util.*;
public class udpserver {
    public static void main(String[] args) {
        byte[] b=new byte[10];
        byte[] b1=new byte[10];
        byte[] b2=new byte[20];//bunlarin her yeni bir islemde sifirlamak gerekiyor mu onu kontrol et.
        byte[] b3 =new byte[40];//server icin dosya ismi
        byte[] b4 =new byte[400];
        byte[] islem_bilgisi =new byte[1024];
        byte[] parolab =new byte[100];
        byte[] server_mevcut_dosyalar = new byte[1000];
        byte[] dataOfFile = new byte[1000000000];
        byte[] dosya_mevcut_degil = new byte[1000];
        byte[] dosya_mevcut= new byte[1000];
        
        DatagramSocket dsoc;
        int paket_kontrol=0;
        Scanner inputKey= new Scanner(System.in);

        //3.islem icin degiskenler...
        DatagramPacket dp31,dp32;
        byte[] dosya_ismi3b = new byte[100];
        byte[] dosya_data3b = new byte[10000000];
        String dosya_ismi3="";
        String dosya_data3="";
        byte[] kaydedildib3 = new byte[100];
        System.out.println("Server dinliyor...");

        try {
            dsoc = new DatagramSocket(1000);
            int islem=0;
            DatagramPacket dp1;
           
            String baglandi,kaydedildi;
            DatagramPacket dp,dp2,dp3,dp4;
            
            while(true){//server devamli dinliyor.
                System.out.println("Server ile islem yapabilmeniz icin baglan yazmaniz gerekmektedir.");
                dp1=new DatagramPacket(b1,b1.length);
                dsoc.receive(dp1);//(2)
                baglandi=new String(dp1.getData(),0,dp1.getLength());
                if(baglandi.equals("baglan")){
                    //eger baglanti saglandiysa...
                     
                    String islemler="Yapilabilecek islemler icin:\n1)Parola istegi(Press1)\n2)Dosya istegi(Press2)\n3)Dosya gonderimi(Press3)\n";
                    islem_bilgisi=islemler.getBytes();
                    dsoc.send(new DatagramPacket(islem_bilgisi,islem_bilgisi.length,InetAddress.getLocalHost(),2000));//(3)
                    //hangi islem oldugunu ogrenen paket.(dp)
                    paket_kontrol++;
                    System.out.println("<"+paket_kontrol+".>"+" paket<islem menusu> yollandi!"+"("+islem_bilgisi.length+"Byte)");

                    //yapilacak islemi secmek icin clienttan cevap bekleniyor.
                    dp=new DatagramPacket(b,b.length);
                    dsoc.receive(dp);//(6)
                    String cevap=new String(dp.getData(),0,dp.getLength());
                    if(cevap.equals("1")){
                        System.out.println("Client ile baglanti yapabilmeniz icin parola girmeniz beklenmektedir.Lutfen giriniz:");
                        String parola=inputKey.next();
                        parolab=parola.getBytes();
                        dsoc.send(new DatagramPacket(parolab,parolab.length,InetAddress.getLocalHost(),2000));//(7)
                        paket_kontrol++;
                        System.out.println("<"+paket_kontrol+".>"+" paket<Parola> yollandi!"+"("+parolab.length+"Byte)");
                        dp2=new DatagramPacket(b2,b2.length);
                        dsoc.receive(dp2);
                        String dogruMu=new String(dp2.getData(),0,dp2.getLength());
                        if(dogruMu.equals("Yanlis parola")){
                            //dsoc.disconnect();
                            System.out.println("yanlis parola.Baglanti client tarafindan kesildi.");
                            paket_kontrol=0;
                            //O clienta gonderilen paket sifirlaniyor diger clientlari icin server yeniden dinlemeye devam ediyor.
                        }else{
                            System.out.println("Dogru parola yollandi islemlere devam ediliyor.");
                            continue;
                        }
                    
                    }else if(cevap.equals("2")){//2.islem karsi tarafa dosya gonderilecek.
                        //gelen cevabin yani islem seciminin 2 oldugunu alan server.
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
                        sendFiles.append(sb.toString());
                        server_mevcut_dosyalar=sendFiles.toString().getBytes();
                        dsoc.send(new DatagramPacket(server_mevcut_dosyalar,server_mevcut_dosyalar.length,InetAddress.getLocalHost(),2000));
                        paket_kontrol++;
                        System.out.println("<"+paket_kontrol+".>"+" paket<Mevcut dosya isimleri> yollandi!"+"("+server_mevcut_dosyalar.length+"Byte)");
                        
                        //clienttan hangi dosya oldugu icin cevap bekleniyor.
                        dp3=new DatagramPacket(b3,b3.length);
                        dsoc.receive(dp3);
                        String dosya_ismi=new String(dp3.getData(),0,dp3.getLength());//dosya ismini aldi..
                        System.out.println("Clienttan talep edilen dosya ismi:"+dosya_ismi);

                        //istenilen dosya ismini aldi.
                        //dosya mevcut ise dosyayi clienta gondericek ve client  dosya ismini ogrenciNo.xxx seklinde degistiricek.

                        
                        boolean flag = false;
                        int id= 0;
                        for(int i=0;i<files.length;i++) {
                            if(files[i].getName().toString().equalsIgnoreCase(dosya_ismi)) {
                                flag = true;
                                id = i;
                                break;
                            }
                        }//pdf dosyasi okumayi yapicak.

                        if(!flag) {

                            String mevcutDegil="Dosya mevcut degil";
                            dosya_mevcut_degil=mevcutDegil.getBytes();
                            dsoc.send(new DatagramPacket(dosya_mevcut_degil,dosya_mevcut_degil.length,InetAddress.getLocalHost(),2000));
                            paket_kontrol++;
                            System.out.println("<"+paket_kontrol+".>"+" paket<Dosya mevcut degil bilgisi> yollandi!"+"("+dosya_mevcut_degil.length+"Byte)");
                       
                            continue;
                        }else{

                            String mevcut="Dosya mevcut";
                            dosya_mevcut=mevcut.getBytes();
                            dsoc.send(new DatagramPacket(dosya_mevcut,dosya_mevcut.length,InetAddress.getLocalHost(),2000));
                            paket_kontrol++;
                            System.out.println("<"+paket_kontrol+".>"+" paket<Dosya mevcut bilgisi> yollandi!"+"("+dosya_mevcut.length+"Byte)");
                        
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

                            dataOfFile=sb1.toString().getBytes();
                            //System.out.println(sb1.toString());
                            dsoc.send(new DatagramPacket(dataOfFile,dataOfFile.length,InetAddress.getLocalHost(),2000));
                            paket_kontrol++;
                            System.out.println("<"+paket_kontrol+".>"+" paket<Dosyanin icindeki bilgiler> yollandi!"+"("+dataOfFile.length+"Byte)");
                            //dosya yolllandi.

                           
                            //kaydedildi bilgisi yani dosya transfer islemi bitti bilgisi.
                            dp4=new DatagramPacket(b4,b4.length);
                            dsoc.receive(dp4);
                            kaydedildi=new String(dp4.getData(),0,dp4.getLength());
                            System.out.println(kaydedildi);

                        }
            



                    }else if(cevap.equals("3")){
                        //System.out.println("3.islem");
                        dp31=new DatagramPacket(dosya_ismi3b,dosya_ismi3b.length);
                        dsoc.receive(dp31);//(2)
                        dosya_ismi3=new String(dp31.getData(),0,dp31.getLength());
                        System.out.println("Client tarafindan gonderilen dosya ismi:"+dosya_ismi3);



                        dp32=new DatagramPacket(dosya_data3b,dosya_data3b.length);
                        dsoc.receive(dp32);//(2)
                        dosya_data3=new String(dp32.getData(),0,dp32.getLength());
                        //gonderilen bilgiler alindi.


                        //dosyayi kaydetme islemi
                        
                        try {
                            File file =new File("dosya.java");
                            //System.out.println("gercek yol:"+file.getAbsolutePath());
                            String absolute = file.getAbsolutePath();
                            absolute = absolute.substring(0,absolute.lastIndexOf("\\"));
                            //System.out.println(absolute);
                            absolute = absolute+"\\server";//server adli klasore kaydediyor.
                            //System.out.println(absolute);
                            File file1 =new File(absolute);
                            file1.mkdir();
                            String dosya_tur=dosya_ismi3.substring(dosya_ismi3.lastIndexOf("."));
                            dosya_ismi3=dosya_ismi3.substring(0,dosya_ismi3.lastIndexOf("."));
                            dosya_ismi3=dosya_ismi3+"_"+"161101071"+dosya_tur;
                            FileWriter myWriter = new FileWriter(absolute+"\\"+dosya_ismi3);
                            myWriter.write(dosya_data3);
                            myWriter.close();
                            String kaydedildi3="Dosya basarili bir sekilde  server klasorune kaydedildi.Transfer tamamlanmistir.";
                            //kaydedildi bilgisi
                            kaydedildib3=kaydedildi3.getBytes();
                            dsoc.send(new DatagramPacket(kaydedildib3,kaydedildib3.length,InetAddress.getLocalHost(),2000));
                            paket_kontrol++;
                            System.out.println("<"+paket_kontrol+".>"+" paket<istenilen dosya kaydedildi> yollandi!"+kaydedildib3.length+"Byte)");

                        }catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }    

               


                    }else{
                        continue;
                    }
                                              
            
                }else{
                    System.out.println("baglanti saglanamadi.");
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                        
        
    }


}
