public class Foyer {
    public synchronized void departFromED(){

    }
    public synchronized void arriveAtED(Patient patient){
        while (patient.allocated != true){
            try {
                notifyAll();
                wait();
            }catch (InterruptedException e){}
        }
    }
}
