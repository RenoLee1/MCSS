import java.util.LinkedList;
import java.util.Queue;

public class Foyer {

    private Queue<Nurse> availableNurses = new LinkedList<>();

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

    public synchronized void departFromED(){ notifyAll(); }

    public synchronized void arriveAtED(Patient patient) {
        while (patient.allocated != true) {
            try {
                Nurse nurse = getAvailableNurse();
                nurse.assignPatient(patient);
                patient.allocated = true;
                System.out.println(patient.getId() + "has been assigned to " + nurse.getNurseId());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
