import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class Foyer {

    private Queue<Nurse> availableNurses = new LinkedList<>();
    private Queue<Patient> arrivedPatients = new LinkedList<>();
    private Queue<Patient> leftPatient = new LinkedList<>();

    // collect nurses that are available for allocating
    public synchronized void addNurse(Nurse nurse){
        availableNurses.add(nurse);
        notifyAll();
    }

    // patient left ED
    public synchronized void departFromED(){
        while (leftPatient.isEmpty()){
            try {
                wait();
            }catch (InterruptedException e){}
        }
        Patient patient = leftPatient.poll();
        notifyAll();

    }

    // The patient return to the foyer and is preparing to be discharged.
    public synchronized void arriveAtFoyerWaitForDischarge(Patient patient, Nurse nurse, Orderlies orderlies){


        try {
            sleep(Params.TRANSFER_TIME);

        }catch (InterruptedException e){}

        leftPatient.add(patient);

//        orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;
        int freeOrderlies =  orderlies.releaseOrderlies();

        System.out.println("Patient " + patient.getId()+" enters Foyer.");
        System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + freeOrderlies+ " free)");
        notifyAll();

        System.out.println(patient.toString()+" discharged from ED");

    }

    // patient arrived ED before admitted
    public synchronized void arriveAtED(Patient patient){
        System.out.println("Patient "+patient.getId()+" arrived ED");
        arrivedPatients.add(patient);
        notifyAll();
    }

    // Allocated a nurse to a patient
    public synchronized Patient getPatient(Nurse nurse, Orderlies orderlies){

        // if no patient, just wait
        while (arrivedPatients.isEmpty()){
            try {
                wait();
            }catch (InterruptedException e){}
            availableNurses.remove(nurse);
        }

        Patient pickedUpPatient = arrivedPatients.poll();
        nurse.getPatient(pickedUpPatient);
        System.out.println(pickedUpPatient.toString() + " admitted to ED");
        System.out.println(pickedUpPatient.toString() + " allocated to Nurse " + nurse.getNurseId());

        // recruit orderlies
        while (orderlies.numberOfOrderlies < Params.TRANSFER_ORDERLIES){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        int freeOrderlies = orderlies.recruitOrderlies();

        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + " (" + freeOrderlies + " free)");
        System.out.println(pickedUpPatient.toString()+ " leaves Foyer");
        return pickedUpPatient;
    }

}
