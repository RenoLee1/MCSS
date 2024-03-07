import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class Foyer {

    private Queue<Nurse> availableNurses = new LinkedList<>();
    private Queue<Patient> arrivedPatients = new LinkedList<>();
    private Queue<Patient> leftPatient = new LinkedList<>();

    public synchronized void addNurse(Nurse nurse){
        availableNurses.add(nurse);
        notifyAll();
    }

    public synchronized Nurse getAvailableNurse() throws InterruptedException {
        while (availableNurses.isEmpty()) {
            wait();
        }
        return availableNurses.poll();
    }

    public synchronized void departFromED(){
        while (leftPatient.isEmpty()){
            try {
                wait();
            }catch (InterruptedException e){}
        }
        Patient patient = leftPatient.poll();
        notifyAll();

    }

    public synchronized void arriveAtFoyerWaitForDischarge(Patient patient, Nurse nurse, Orderlies orderlies){

        while (orderlies.numberOfOrderlies < 3){
            try {
                wait();
            }catch (InterruptedException e){}
        }

        orderlies.numberOfOrderlies -= Params.TRANSFER_ORDERLIES;
        System.out.println("Nurse "+nurse.getNurseId()+" recruits 3 orderlies" + " (" + orderlies.numberOfOrderlies+ " free)");
        if (patient.Severe() == true){
            System.out.println(patient.toString()+ " leaves treatment room");
        }else {
            System.out.println(patient.toString()+ " leaves triage");
        }

        try {
            sleep(Params.TRANSFER_TIME);
            leftPatient.add(patient);

            orderlies.numberOfOrderlies += Params.TRANSFER_ORDERLIES;
            System.out.println("Patient " + patient.getId()+" enters Foyer.");
            System.out.println("Nurse "+nurse.getNurseId()+" releases 3 orderlies" + " (" + orderlies.numberOfOrderlies+ " free)");

        }catch (InterruptedException e){}

        System.out.println(patient.toString()+" discharged from ED");
        while (leftPatient.contains(patient)){
            try {
                wait();
            }catch (InterruptedException e){}
        }

    }

    public synchronized void arriveAtED(Patient patient){
        System.out.println("Patient "+patient.getId()+" arrived ED");
        arrivedPatients.add(patient);
        notifyAll();
    }

    public synchronized Patient getPatient(Nurse nurse){

        while (arrivedPatients.isEmpty()){
            try {
                wait();
            }catch (InterruptedException e){}
            availableNurses.remove(nurse);
        }

        Patient pickedUpPatient = arrivedPatients.poll();
        System.out.println(pickedUpPatient.toString() + " admitted to ED");
        System.out.println(pickedUpPatient.toString() + " allocated to Nurse " + nurse.getNurseId());
        return pickedUpPatient;
    }

}
