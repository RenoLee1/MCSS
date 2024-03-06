public class Specialist extends Thread{
    private Treatment treatment;
    public Specialist(Treatment treatment){
        this.treatment = treatment;
        this.treatment.addSpecialist(this);
    }

    public void run(){
        synchronized (this){
            while (treatment.readyToTreat != true){
                try {
                    wait();
                }catch (InterruptedException e){}
            }
            treatment.specialistGameTime();
            treatment.addSpecialist(this);

        }
    }

}
