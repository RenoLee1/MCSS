import static java.lang.Thread.sleep;

public class Triage {
    // a flag indicating whether triage is available for patient or not
    public volatile boolean available = true;

    // record the patient in triage
    private volatile Patient patient = null;

    // patient arrives triage
    public synchronized void arriveTriage(Patient patient, Orderlies orderlies, Nurse nurse){
        while (available != true) {

            try {
                wait();
            }catch (InterruptedException e){}
        }

        try {
            sleep(Params.TRANSFER_TIME);

        }catch (InterruptedException e){}


//        orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;
        int freeOrderlies = orderlies.releaseOrderlies();

        System.out.println(patient.toString() + " enters Triage");
        System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + freeOrderlies + " free)");

        try {
            sleep(Params.TRIAGE_TIME);
        }catch (InterruptedException e){}

        this.patient=patient;
        available = false;

        notifyAll();

    }

    // patient leaves triage
    public synchronized void leaveTriage(Orderlies orderlies, Nurse nurse){

        while (orderlies.numberOfOrderlies < Params.TRANSFER_ORDERLIES){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        int freeOrderlies = orderlies.recruitOrderlies();

        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + " (" + freeOrderlies + " free)");
        System.out.println(patient.toString()+ " leaves Triage");

        patient = null;
        available = true;
        notifyAll();
    }
}
