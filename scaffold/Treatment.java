import static java.lang.Thread.sleep;

public class Treatment {

    // record the patient in triage
    private Patient patient = null;

    // a flag indicating whether treatment room is available for patient or not
    protected volatile boolean available = true;

    // patient arrives treatment room
    public synchronized void arriveTreatment(Patient patient, Orderlies orderlies, Nurse nurse, Triage triage){
        while(available != true){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        this.patient = patient;
        available = false;

        try {
            sleep(Params.TRANSFER_TIME);
        }catch (InterruptedException e){}

//        orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;
        int freeOrderlies = orderlies.releaseOrderlies();

        System.out.println(patient.toString() + " enters treatment room");
        System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + freeOrderlies+ " free)");

        notifyAll();
    }

    // patient leaves treatment room
    public synchronized void leaveTreatment(Patient patient, Nurse nurse, Orderlies orderlies){
        while (patient.treated == false || orderlies.numberOfOrderlies < 3){
            try {
                System.out.println("Waiting!!!");
                wait();
            }catch (InterruptedException e) {}
        }

        System.out.println("Not waiting!!!!!!!!");
        int freeOrderlies = orderlies.recruitOrderlies();

        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + " (" + freeOrderlies+ " free)");
        System.out.println(patient.toString()+ " leaves treatment room");
    }

    // Specialist treat patient
    public synchronized void treatPatient(){
        System.out.println("Specialist enters treatment room");
        while (patient == null){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        System.out.println(patient.toString() + " treatment started");
        try {
            sleep(Params.TREATMENT_TIME);
        }catch (InterruptedException e){}

        patient.treated = true;
        System.out.println(patient.toString() + " treatment complete");
        notifyAll();
        patient = null;
    }

    // The Specialist leaves the Treatment location in between treating each Patient.
    public synchronized void specialistLeavesTreatment(){
        try {
            System.out.println("Specialist leaves treatment room");
            sleep(Params.SPECIALIST_AWAY_TIME);
            available = true;
            notifyAll();
        }catch (InterruptedException e){}
    }
}
