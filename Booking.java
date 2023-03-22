import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Taxi
{
     static int taxicount = 0; 
     int id;
     boolean booked;
     char currentSpot;
     int freeTime; 
     int totalEarnings;
     List<String> trips;
     
    
    public Taxi()
    {
        booked = false;
        currentSpot = 'A';
        freeTime = 6;
        totalEarnings = 0;
        trips = new ArrayList<String>();
        taxicount = taxicount + 1;
        id = taxicount;
    }

    public void setDetails(boolean booked,char currentSpot,int freeTime,int totalEarnings,String tripDetail) throws IOException
    {
            this.booked = booked;
            this.currentSpot = currentSpot;
            this.freeTime = freeTime;
            this.totalEarnings = totalEarnings;
            this.trips.add(tripDetail);
           
    }

    public void addToFile() throws IOException
    {
    	FileWriter file=new FileWriter("F:\\ZOHO TRAINING\\Evaluation 6\\TaxiApplication\\Taxi\\taxi.txt",true);
		BufferedWriter bfile=new BufferedWriter(file);
        String str="\nTaxi - "+ this.id + " Total Earnings - " + this.totalEarnings;
        bfile.write(str);
        String str2="\nTaxiID    BookingID    CustomerID    From    To    PickupTime    DropTime    Amount";
        bfile.write(str2);
        
        for(String trip : trips)
        {
           String str3="\n"+id + "          " + trip;
           bfile.write(str3);
        }
        String line="\n--------------------------------------------------------------------------------------";
        bfile.write(line);
        bfile.flush();
    }

    
    
}

public class Booking
{
    public static void bookTaxi(int customerID,char pickupPoint,char dropPoint,int pickupTime,List<Taxi> freeTaxis) throws IOException
    {
        int min = 1000;
        int distanceBetweenpickUpandDrop = 0;
        int earning = 0;
        int nextfreeTime = 0;
        char nextSpot = 'Z';
        Taxi bookedTaxi = null;
        String tripDetail = "";
        
        for(Taxi t : freeTaxis)
        {
            int distanceBetweenCustomerAndTaxi = Math.abs((t.currentSpot - '0') - (pickupPoint - '0')) * 15;
            if(distanceBetweenCustomerAndTaxi < min)
            {
                bookedTaxi = t;
                distanceBetweenpickUpandDrop = Math.abs((dropPoint - '0') - (pickupPoint - '0')) * 15;
                earning = (distanceBetweenpickUpandDrop-5) * 10 + 100;                
                int dropTime  = pickupTime + distanceBetweenpickUpandDrop/15;
                if(dropTime>24) {
                	dropTime=dropTime-24;
                }
                else {
                	dropTime=dropTime;
                }
                nextfreeTime = dropTime;
                nextSpot = dropPoint;
                tripDetail = customerID + "               " + customerID + "          " + pickupPoint +  "      " + dropPoint + "       " + pickupTime + "          " +dropTime + "           " + earning;
                min = distanceBetweenCustomerAndTaxi;
            }
            
        }

        bookedTaxi.setDetails(true,nextSpot,nextfreeTime,bookedTaxi.totalEarnings + earning,tripDetail);
        System.out.println();
        System.out.println("---------Taxi " + bookedTaxi.id + " booked---------");
        System.out.println();
    }

    public static List<Taxi> createTaxis(int n)
    {
        List<Taxi> taxis = new ArrayList<Taxi>();
        for(int i=1 ;i <=n;i++)
        {
            Taxi t = new Taxi();
            taxis.add(t);
        }
        return taxis;
    }

    public static List<Taxi> getFreeTaxis(List<Taxi> taxis,int pickupTime,char pickupPoint)
    {
        List<Taxi> freeTaxis = new ArrayList<Taxi>();
        for(Taxi t:taxis)
        {   
            if(t.freeTime <= pickupTime && (Math.abs((t.currentSpot - '0') - (pickupPoint - '0')) <= pickupTime - t.freeTime))
            freeTaxis.add(t);

        }
        return freeTaxis;
    }


    public static void main(String[] args) throws IOException
    {
    	Scanner s = new Scanner(System.in);

        List<Taxi> taxis = createTaxis(4);
        Taxi t1=taxis.get(0);
        Taxi t2=taxis.get(0);
        Taxi t3=taxis.get(0);
        Taxi t4=taxis.get(0);
      
        int id = 1;
        
        System.out.println("1.Book Taxi\n2.Print Taxi Details\n3.Exit");
        int choice = s.nextInt();

        while(choice<3)
        {
        if(choice==1)
        {
        
        int customerID = id;
        System.out.print("Enter Pickup point:");
        char pickupPoint = s.next().charAt(0);
        System.out.print("Enter Drop point:");
        char dropPoint = s.next().charAt(0);
        System.out.print("Enter Pickup time:");
        int pickupTime = s.nextInt();
        if(pickupTime>24) {
        	System.out.println();
        	System.out.println("You are Enterd InValid Time!!!");
        	System.out.println();
        	System.out.print("Enter Pickup time:");
        	pickupTime = s.nextInt();
        }

        if(pickupPoint < 'A' || dropPoint > 'F'|| pickupPoint > 'F' || dropPoint < 'A')
        {
            System.out.println("Valid pickup and drop are A, B, C, D, E, F. Exitting");
            return;
        }

        List<Taxi> freeTaxis = getFreeTaxis(taxis,pickupTime,pickupPoint);

        if(freeTaxis.size() == 0)
        {
            System.out.println("No Taxi can be alloted. Exitting");
            return;
        }    

        Collections.sort(freeTaxis,(a,b)->a.totalEarnings - b.totalEarnings); 
        
        bookTaxi(id,pickupPoint,dropPoint,pickupTime,freeTaxis);
        id++;
        }
        else if(choice == 2)
        {
             for(Taxi t : taxis)
             t.addToFile();
             
             FileReader file=new FileReader("F:\\ZOHO TRAINING\\Evaluation 6\\TaxiApplication\\Taxi\\taxi.txt");
			 BufferedReader bfile=new BufferedReader(file);
			 String line=bfile.readLine();
		     while(line!=null) {
			   System.out.println(line);
		       line=bfile.readLine();
			 }
           
        }
        System.out.println("1.Book Taxi\n2.Print Taxi Details\n3.Exit");
        choice = s.nextInt();
        
        }      
        }
    }
