public class Treatment {
    private Patient patient = null;
    public volatile boolean available = true;
    public volatile boolean readyToTreat = false;
    private Specialist specialist;

    public synchronized void arriveTreatment(Patient patient){
        while(available != true){
            try {
                wait();
            }catch (InterruptedException e){}
        }
        this.patient = patient;
        available = false;
        this.readyToTreat = true;
        System.out.println("Patient "+patient.getId()+ "has severe: "+ patient.Severe() + " enter treatment");
    }

    public synchronized void leaveTreatment(){
        System.out.println("Patient "+patient.getId()+ " left treatment since he has been treated" );
        patient = null;
        notifyAll();
    }

    public synchronized void addSpecialist(Specialist specialist){
        this.specialist = specialist;
    }

    public synchronized void specialistGameTime(){
        try {
            System.out.println("Specialist is sleeping");
            specialist.sleep(Params.SPECIALIST_AWAY_TIME);
            available =true;
            System.out.println("Specialist wakes up");
            readyToTreat = false;
        }catch (InterruptedException e){}
    }
}
