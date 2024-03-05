public class Nurse extends Thread{

    private int id;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;
    private volatile boolean isAvaliable;

    //Constructor
    public Nurse(int id, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.id = id;
        this.foyer=foyer;
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
        this.isAvaliable = true;
    }

    public void run() {

    }
}
