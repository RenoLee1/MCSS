public class Nurse extends Thread{

    private int id;
    private Foyer foyer;
    private Triage triage;
    private Orderlies orderlies;
    private Treatment treatment;

    private Patient currentPatient = null;

    //Constructor
    public Nurse(int id, Foyer foyer, Triage triage, Orderlies orderlies, Treatment treatment) {
        this.id = id;
        this.foyer=foyer;
        this.foyer.addNurse(this);
        this.triage = triage;
        this.orderlies = orderlies;
        this.treatment = treatment;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            synchronized (this) {

                currentPatient = foyer.getPatient(this, orderlies);

                triage.arriveTriage(currentPatient, this.orderlies, this);


                if (currentPatient.Severe() == true) {

                    triage.leaveTriage(orderlies, this);
                    treatment.arriveTreatment(currentPatient, orderlies, this, triage);

                    treatment.leaveTreatment(currentPatient, this, orderlies);

                    foyer.arriveAtFoyerWaitForDischarge(currentPatient, this, orderlies);

                }else{
                    triage.leaveTriage(orderlies, this);
                    foyer.arriveAtFoyerWaitForDischarge(currentPatient, this, orderlies);
                }
            }

            // patient has left, waiting for the next patient
            synchronized (this) {
                currentPatient = null;
                System.out.println("Nurse "+this.id + " is free");
                foyer.addNurse(this);
            }
        }
    }

    public int getNurseId() {
        return this.id;
    }

    public void getPatient(Patient patient){
        this.currentPatient = patient;
    }
}
