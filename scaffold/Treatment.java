import static java.lang.Thread.sleep;

public class Treatment {
    private Patient patient = null;
    public volatile boolean available = true;

    private Specialist specialist;

    public synchronized void arriveTreatment(Patient patient, Orderlies orderlies, Nurse nurse, Triage triage){
        while(available != true || orderlies.numberOfOrderlies < 3){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        orderlies.numberOfOrderlies -= Params.TRANSFER_ORDERLIES;

        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + " (" + orderlies.numberOfOrderlies+ " free)");
        System.out.println(patient.toString()+ " leaves triage");

        this.patient = patient;
        available = false;

        try {

            sleep(Params.TRANSFER_TIME);

            orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;

            System.out.println(patient.toString() + " enters treatment room");
            System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + orderlies.numberOfOrderlies+ " free)");
        }catch (InterruptedException e){}

        triage.available = true;
        notifyAll();

        System.out.println(triage.available + "?????");

    }

    public synchronized void leaveTreatment(){
        while (patient != null){
            try {
                wait();
            }catch (InterruptedException e) {}
        }

//        System.out.println("Patient "+patient.getId()+ " left treatment since he has been treated" );
    }

    public synchronized void addSpecialist(Specialist specialist){
        this.specialist = specialist;
    }

    // Specialist treat patient
    public synchronized void treatPatient(){
        while (patient == null){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        System.out.println("Specialist enters treatment room");
        System.out.println(patient.toString() + " treatment started");
        try {
            sleep(Params.TREATMENT_TIME);
        }catch (InterruptedException e){}

        patient.treated = true;
        System.out.println(patient.toString() + " treatment complete");
        patient = null;
        notifyAll();
    }

    public synchronized void specialistGameTime(){
        try {
            System.out.println("Specialist leaves treatment room");
            sleep(Params.SPECIALIST_AWAY_TIME);
            available = true;
            notifyAll();
        }catch (InterruptedException e){}
    }
}
