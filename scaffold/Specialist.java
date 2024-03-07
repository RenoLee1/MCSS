public class Specialist extends Thread{
    private Treatment treatment;
    public Specialist(Treatment treatment){
        this.treatment = treatment;
        this.treatment.addSpecialist(this);
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            synchronized (this){
                treatment.treatPatient();
                treatment.specialistGameTime();
            }
        }
    }

}
