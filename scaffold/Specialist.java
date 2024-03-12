public class Specialist extends Thread{

    private Treatment treatment;

    // constructor
    public Specialist(Treatment treatment){
        this.treatment = treatment;
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            synchronized (this){
                treatment.treatPatient();
                treatment.specialistLeavesTreatment();
            }
        }
    }

}
