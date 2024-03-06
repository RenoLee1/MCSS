public class Triage {
    private boolean available = true;
    private Nurse nurse;

    public synchronized void addNurse(Nurse nurse){
        while (nurse != null) {
            try {
                wait();
            }catch (InterruptedException e) {}
        }
        this.nurse = nurse;
    }

    public synchronized void removeNurse(){
        this.nurse = null;
        notifyAll();
    }
}
