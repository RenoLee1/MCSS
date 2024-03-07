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

        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + "(" + orderlies.numberOfOrderlies+ " free)");
        System.out.println(patient.toString()+ " leaves triage");

        this.patient = patient;
        available = false;
        triage.available = true;
        try {
            Thread.sleep(Params.TRANSFER_TIME);

            orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;

            System.out.println(patient.toString() + " enters treatment room");
            System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + "(" + orderlies.numberOfOrderlies+ " free)");
        }catch (InterruptedException e){}

        notifyAll();

    }

    public synchronized void leaveTreatment(){
        while (patient == null){
            try {
                wait();
            }catch (InterruptedException e) {}
        }

        notifyAll();
//        System.out.println("Patient "+patient.getId()+ " left treatment since he has been treated" );
    }

    public synchronized void addSpecialist(Specialist specialist){
        this.specialist = specialist;
    }

    public synchronized void treatPatient(){
        while (patient == null){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        System.out.println("Specialist enters treatment room");
        System.out.println(patient.toString() + " treatment started");
        try {
            specialist.sleep(Params.TREATMENT_TIME);
        }catch (InterruptedException e){}

        patient.treated = true;
        System.out.println(patient.toString() + " treatment complete");
        patient = null;
    }

    public synchronized void specialistGameTime(){
        try {
            System.out.println("Specialist leaves treatment room");
            specialist.sleep(Params.SPECIALIST_AWAY_TIME);
            available =true;

        }catch (InterruptedException e){}
    }
}
