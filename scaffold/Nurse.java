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

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            synchronized (this) {

                currentPatient = foyer.getPatient(this);

                triage.arriveTriage(currentPatient, this.orderlies, this);

                try {
                    this.sleep(Params.TRIAGE_TIME);
                }catch (InterruptedException e){}


                if (currentPatient.Severe() == true) {

//                    triage.leaveTriage();
                    treatment.arriveTreatment(currentPatient, orderlies, this, triage);

//                    while (treatment.available == true){
//                        try {
//                            wait();
//                        }catch (InterruptedException e){}
//                        triage.leaveTriage();
//                        treatment.arriveTreatment(currentPatient);
//                    }

//                    while (currentPatient.treated != true){
//                        try {
//                            wait();
//                        }catch (InterruptedException e) {}
//                    }


                    treatment.leaveTreatment();

                    foyer.arriveAtFoyerWaitForDischarge(currentPatient, this, orderlies);

                }else{
                    triage.leaveTriage();
                    foyer.arriveAtFoyerWaitForDischarge(currentPatient, this, orderlies);
                }
            }

            // 处理病人
            // 这里应该包含处理病人的逻辑，例如分诊等

            synchronized (this) {
                currentPatient = null;
                isAvailable = true;
                System.out.println("Nurse "+this.id + " is free");
                foyer.addNurse(this);
            }
        }
    }

    public int getNurseId() {
        return this.id;
    }
}
