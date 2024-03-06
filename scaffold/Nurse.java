public class Nurse extends Thread{

    private int id;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;

    private Patient currentPatient = null;
    private boolean isAvailable;

    //Constructor
    public Nurse(int id, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.id = id;
        this.foyer=foyer;
        this.foyer.addNurse(this);
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
        this.isAvailable = true;
    }

    public synchronized void assignPatient(Patient patient) {
        this.currentPatient = patient;
        this.isAvailable = false;
        notify();
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                while (currentPatient == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            synchronized (this) {
                triage.addNurse(this);
                
                triage.removeNurse();
            }

            // 处理病人
            // 这里应该包含处理病人的逻辑，例如分诊等

            synchronized (this) {
                currentPatient = null;
                isAvailable = true;
            }
            foyer.addNurse(this);
        }
    }

    public int getNurseId() {
        return this.id;
    }
}
