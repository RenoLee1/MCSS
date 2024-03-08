public class Triage {
    public volatile boolean available = true;
    private Patient patient = null;


    public synchronized void arriveTriage(Patient patient, Orderlies orderlies, Nurse nurse){
        while (available != true) {

            try {
                wait();
            }catch (InterruptedException e){}
        }

        orderlies.numberOfOrderlies -= Params.TRANSFER_ORDERLIES;

        System.out.println(patient.toString()+ " leaves Foyer");

        try {
            Thread.sleep(Params.TRANSFER_TIME);
        }catch (InterruptedException e){}

        orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;

        System.out.println(patient.toString() + " enters Triage");
        System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + orderlies.numberOfOrderlies+ " free)");

        this.patient=patient;
        available = false;

    }

    public synchronized void leaveTriage(){
        if (patient.Severe() == true){
            patient = null;
        }else {
            patient = null;
            available = true;
        }
    }
}
