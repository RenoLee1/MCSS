public class Triage {
    public volatile boolean available = true;
    private Patient patient = null;


    public synchronized void arriveTriage(Patient patient){
        while (available != true) {
            try {
                wait();
            }catch (InterruptedException e){}
        }
        System.out.println("welcome " + patient.getId() + " to Triage");
        this.patient=patient;
        available = false;
    }

    public synchronized void leaveTriage(){
        System.out.println("patient " + patient.getId()+ " left triage");
        patient = null;
        available = true;
        notifyAll();
    }
}
